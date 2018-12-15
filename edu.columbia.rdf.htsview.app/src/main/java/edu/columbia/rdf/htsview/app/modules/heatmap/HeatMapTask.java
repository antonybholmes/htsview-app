/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.htsview.app.modules.heatmap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.SwingWorker;

import org.jebtk.bioinformatics.genomic.Gene;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.bioinformatics.genomic.GenomicEntity;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.core.Properties;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.TreeSetCreator;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.TmpService;
import org.jebtk.graphplot.AspectRatio;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;
import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;
import edu.columbia.rdf.matcalc.MainMatCalc;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.bio.BioModuleLoader;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.HeatMapProperties;

/**
 * The Class HeatMapTask.
 */
public class HeatMapTask extends SwingWorker<Void, Void> {

  /** The m matrices. */
  private List<DataFrame> mMatrices;

  /** The m regions. */
  private List<HeatMapIdLocation> mRegions;

  /** The m padding. */
  private int mPadding;

  /** The m window. */
  private int mWindow;

  /** The m heat map sort. */
  private HeatMapSort mHeatMapSort;

  /** The m input. */
  private Sample mInput;

  /** The m parent. */
  private ModernRibbonWindow mParent;

  /** The m genome model. */
  private GenomeModel mGenomeModel;

  /** The m samples. */
  // private SampleAssembly mAssembly;
  private List<SamplePlotTrack> mSamples;

  /**
   * Instantiates a new heat map task.
   *
   * @param parent the parent
   * @param samples the samples
   * @param input the input
   * @param regions the regions
   * @param padding the padding
   * @param window the window
   * @param heatMapSort the heat map sort
   * @param genomeModel the genome model
   */
  public HeatMapTask(ModernRibbonWindow parent, List<SamplePlotTrack> samples,
      Sample input, List<HeatMapIdLocation> regions, int padding, int window,
      HeatMapSort heatMapSort, GenomeModel genomeModel) {
    mParent = parent;
    mSamples = samples;
    mInput = input;
    mRegions = regions;
    mPadding = padding;
    mWindow = window;
    mHeatMapSort = heatMapSort;
    mGenomeModel = genomeModel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.SwingWorker#doInBackground()
   */
  @Override
  public Void doInBackground() {
    try {
      mMatrices = createHeatMapMatrices();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.SwingWorker#done()
   */
  @Override
  public void done() {
    Properties properties = new HeatMapProperties();

    properties.set("plot.colormap", ColorMap.createWhiteRedMap());
    properties.set("plot.aspect-ratio", new AspectRatio(0.1));
    properties.set("plot.show-grid-color", false);
    properties.set("plot.show-outline-color", false);
    properties.set("plot.show-row-labels", false);
    properties.set("plot.color.standardization",
        ColorNormalizationType.ZSCORE_MATRIX);

    properties.set("plot.color.intensity", 2);

    try {
      MainMatCalcWindow window = MainMatCalc.main(mParent.getAppInfo(),
          new BioModuleLoader());

      window.openMatrices().open(mMatrices);

      window.runModule("Heat Map", "--plot");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates the heat map matrices.
   *
   * @return the list
   * @throws Exception the exception
   */
  private List<DataFrame> createHeatMapMatrices() throws Exception {
    // int window = 100;
    int bins = 2 * (mPadding / mWindow) + 1;

    List<DataFrame> ret = new ArrayList<DataFrame>();

    for (SamplePlotTrack track : mSamples) {
      Sample sample = track.getSample();

      DataFrame m = DataFrame.createNumericalMatrix(mRegions.size(), bins);

      m.setName(sample.getName());

      for (int i = 0; i < bins; ++i) {
        m.setColumnName(i, Integer.toString((i - bins / 2) * mWindow));
      }

      Path temp = TmpService.getInstance().newTmpFile("txt");

      BufferedWriter writer = FileUtils.newBufferedWriter(temp);

      try {
        switch (mHeatMapSort) {
        case TSS_DISTANCE:
          sortTssDist(track, bins, m);
          break;
        case INTENSITY:
          sortIntensity(track, bins, m);
          break;
        default:
          sortNone(track, bins, m);
          break;
        }
      } finally {
        writer.close();
      }

      ret.add(m);
    }

    return ret;
  }

  /**
   * Sort none.
   *
   * @param sample the sample
   * @param bins the bins
   * @param m the m
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private void sortNone(SamplePlotTrack sample, int bins, DataFrame m)
      throws IOException, ParseException {
    // for (HeatMapIdLocation region : mRegions) {
    for (int i = 0; i < mRegions.size(); ++i) {
      HeatMapIdLocation region = mRegions.get(i);

      if (region.getRegion() == null) {
        continue;
      }

      GenomicRegion ext = GenomicRegion
          .extend(region.getRegion(), mPadding, mPadding);

      // +- 2kb

      double[] counts = getCounts(sample, ext, mWindow);

      // writer.write(gene.getRefSeq());
      // writer.write(TextUtils.TAB_DELIMITER);
      // writer.write(gene.getEntrez());
      // writer.write(TextUtils.TAB_DELIMITER);
      // writer.write(gene.getSymbol());

      m.setRowAnnotation("Id", i, region.getId());
      m.setRowAnnotation("Location", i, region.getRegion().toString());

      for (int j = 0; j < counts.length; ++j) {
        // System.err.println("heat " + i + " " + j + " " + counts.get(j) + " "
        // +
        // region.getRegion().toString());
        m.set(i, j, counts[j]);
      }

      if (i % 1000 == 0) {
        System.err.println("Processed " + i + " locations...");
      }
    }
  }

  /**
   * Sort tss dist.
   *
   * @param sample the sample
   * @param bins the bins
   * @param m the m
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private void sortTssDist(SamplePlotTrack sample, int bins, DataFrame m)
      throws IOException {
    Genome genome = mGenomeModel.get();

    Map<Integer, Set<String>> tssMap = new TreeMap<Integer, Set<String>>();

    Set<String> used = new HashSet<String>();

    Genome g = GenesService.getInstance().getFirstGeneDb(genome.getAssembly());

    for (int i = 0; i < mRegions.size(); ++i) {
      HeatMapIdLocation region = mRegions.get(i);

      if (region.getRegion() == null) {
        continue;
      }

      Iterable<GenomicElement> closestGenes = GenesService.getInstance()
          .getGenes(g)
          .closestByTss(g, region.getRegion(), GenomicEntity.TRANSCRIPT);

      // System.err.println("sym " + closestGenes.get(0).getSymbol());

      int tssDistance = Gene.tssDist5p(closestGenes.iterator().next(),
          region.getRegion()); // GenomicRegion.midDist(region.getRegion(),
                               // Gene.tssRegion(closestGenes.get(0)));

      // System.err.println("tss " + tssDistance + " " +
      // closestGenes.get(0).getLocation() + " " + region.getRegion());

      if (!tssMap.containsKey(tssDistance)) {
        tssMap.put(tssDistance, new TreeSet<String>());
      }

      for (GenomicElement gene : closestGenes) {
        String symbol = gene.getProp(GenomicEntity.GENE_NAME_TYPE);

        if (used.contains(symbol)) {
          continue;
        }

        tssMap.get(tssDistance).add(gene.getProp(GenomicEntity.REFSEQ_TYPE));
        used.add(symbol);
      }
    }

    int i = 0;

    for (int tssDist : tssMap.keySet()) {
      for (String refseq : tssMap.get(tssDist)) {
        GenomicElement gene = GenesService.getInstance().getGenes(g)
            .getElement(g, refseq, GenomicEntity.TRANSCRIPT);

        GenomicRegion tssRegion = Gene.tssRegion(gene);

        GenomicRegion ext = GenomicRegion.extend(tssRegion, mPadding, mPadding);

        // +- 2kb

        double[] counts = getCounts(sample, ext, mWindow);

        m.setRowAnnotation("Id", i, refseq);
        m.setRowAnnotation("Location", i, ext.getLocation());

        for (int j = 0; j < counts.length; ++j) {
          m.set(i, j, counts[j]);
        }

        ++i;
      }
    }
  }

  /**
   * Sort intensity.
   *
   * @param sample the sample
   * @param bins the bins
   * @param m the m
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private void sortIntensity(SamplePlotTrack sample, int bins, DataFrame m)
      throws IOException, ParseException {
    Map<Double, Set<GenomicRegion>> tssMap = DefaultTreeMap
        .create(new TreeSetCreator<GenomicRegion>()); // new
                                                      // TreeMap<Double,
                                                      // Set<GenomicRegion>>();

    Map<GenomicRegion, double[]> countMap = new HashMap<GenomicRegion, double[]>();

    Map<GenomicRegion, GenomicRegion> extMap = new HashMap<GenomicRegion, GenomicRegion>();

    for (int i = 0; i < mRegions.size(); ++i) {
      HeatMapIdLocation region = mRegions.get(i);

      if (region.getRegion() == null) {
        continue;
      }

      GenomicRegion r = region.getRegion();

      // List<Gene> closestGenes =
      // mGeneMap.get(mGenomeModel.get()).findClosestGenesByTss(region);

      // The region is already a midpoint, so we just extend it
      GenomicRegion ext = GenomicRegion.extend(r, mPadding, mPadding);

      // System.err.println("closest " + r.toString());

      // Find closest tss to peak and center on that
      // GenomicRegion ext =
      // GenomicRegion.extend(Gene.tssRegion(closestGenes.get(0)),
      // mPadding,
      // mPadding);

      double[] counts = getCounts(sample, ext, mWindow);

      double sum = 0;

      for (double count : counts) {
        sum += count;
      }

      tssMap.get(sum).add(r);

      countMap.put(r, counts);

      extMap.put(r, ext);
    }

    int i = 0;

    for (double sum : CollectionUtils
        .reverse(CollectionUtils.sort(tssMap.keySet()))) {
      for (GenomicRegion region : tssMap.get(sum)) {
        GenomicRegion ext = extMap.get(region); // GenomicRegion.extend(GenomicRegion.midRegion(region),
                                                // mPadding,
                                                // mPadding);

        // +- 2kb

        double[] counts = countMap.get(region); // getCounts(ext, mWindow);

        m.setRowAnnotation("Id", i, region.getLocation());
        m.setRowAnnotation("Location", i, ext.getLocation());

        for (int j = 0; j < counts.length; ++j) {
          m.set(i, j, counts[j]);
        }

        ++i;
      }
    }
  }

  /**
   * Get the counts and subtract the input if necessary.
   *
   * @param sample the sample
   * @param ext the ext
   * @param mWindow the m window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private double[] getCounts(SamplePlotTrack sample,
      GenomicRegion ext,
      int mWindow) throws IOException {
    SampleAssembly assembly = sample.getAssembly();

    double[] counts = assembly.getRPM(sample.getSample(), ext, mWindow);

    if (mInput != null) {
      double[] inputCounts = assembly.getRPM(mInput, ext, mWindow);

      for (int i = 0; i < counts.length; ++i) {
        counts[i] = Math.max(0, counts[i] - inputCounts[i]);
      }
    }

    return counts;
  }
}
