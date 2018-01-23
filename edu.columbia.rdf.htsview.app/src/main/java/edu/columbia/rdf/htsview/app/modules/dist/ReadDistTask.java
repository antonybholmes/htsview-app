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
package edu.columbia.rdf.htsview.app.modules.dist;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.DefaultTreeMapCreator;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.io.BufferedTableWriter;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.Temp;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.modern.status.StatusService;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.app.modules.heatmap.HeatMapIdLocation;
import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;
import edu.columbia.rdf.matcalc.MainMatCalc;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.OpenFile;
import edu.columbia.rdf.matcalc.bio.BioModuleLoader;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadDistTask.
 */
public class ReadDistTask extends SwingWorker<Void, Void> {

  /** The m locations. */
  private List<HeatMapIdLocation> mLocations;

  /** The m padding. */
  private int mPadding;

  /** The m window. */
  private int mWindow;

  /** The m parent. */
  private ModernRibbonWindow mParent;

  /** The m tracks. */
  private List<SamplePlotTrack> mTracks;

  /** The m file. */
  private Path mFile;

  /** The m name. */
  private String mName;

  /** The m average. */
  private boolean mAverage;

  /**
   * Instantiates a new read dist task.
   *
   * @param parent the parent
   * @param name the name
   * @param tracks the tracks
   * @param regions the regions
   * @param padding the padding
   * @param window the window
   * @param average the average
   */
  public ReadDistTask(ModernRibbonWindow parent, String name,
      List<SamplePlotTrack> tracks, List<HeatMapIdLocation> regions,
      int padding, int window, boolean average) {
    mParent = parent;
    mName = name;
    mTracks = tracks;
    mLocations = regions;
    mPadding = padding;
    mWindow = window;
    mAverage = average;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.SwingWorker#doInBackground()
   */
  @Override
  public Void doInBackground() {
    StatusService.getInstance().setStatus("Compiling peaks...");

    MainMatCalcWindow window;

    try {
      mFile = Temp.generateTempFile("txt");

      createCountsFile();

      window = MainMatCalc.main(mParent.getAppInfo(), new BioModuleLoader());

      new OpenFile(window, mFile).autoOpen();

      window.runModule("Line Graph", "--switch-tab");

      Graph2dWindow plotWindow = window.getCurrentPlotWindow();

      Axes axes = plotWindow.getFigure().currentSubFigure().currentAxes();

      axes.getLegend().setVisible(true);
      axes.getX1Axis().setLimits(-mPadding, mPadding);
      axes.getX1Axis().getTitle().setText("Distance to " + mName);
      axes.getY1Axis().getTitle().setText("Normalized ChIP-Seq reads");
      axes.setMargins(100);

      // Update all of the tracks

      int i = 0;

      for (Plot plot : axes.getPlots()) {
        plot.getCurrentSeries().getStyle().getLineStyle()
            .setColor(mTracks.get(i).getLineColor());
        plot.getCurrentSeries().getStyle().getFillStyle()
            .setColor(mTracks.get(i).getFillColor());
        plot.getCurrentSeries().getMarker().setVisible(false);

        ++i;
      }
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
    StatusService.getInstance().setReady();

    /*
     * SwingUtilities.invokeLater(new Runnable() {
     * 
     * @Override public void run() { try { MainMatCalcWindow window =
     * MainBioMatCalc.main(mFile, true, 0);
     * 
     * window.runModule("Line Graph", "--switch-tab", "--plot",
     * "--x-axis-name=Sample");
     * 
     * } catch (Exception e) { e.printStackTrace(); } } });
     */
  }

  /**
   * Creates the counts file.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void createCountsFile() throws IOException {

    // There are always an odd number of bins centered about zero
    int bins = 2 * (mPadding / mWindow) + 1;

    Map<Sample, IterMap<Integer, Double>> mBinCountMap = DefaultTreeMap
        .create(new DefaultTreeMapCreator<Integer, Double>(0.0)); // new
                                                                  // TreeMap<Sample,
                                                                  // Map<Integer,
                                                                  // Double>>();

    // int c = 1;

    List<Sample> samples = new ArrayList<Sample>(mTracks.size());

    for (SamplePlotTrack track : mTracks) {
      Sample sample = track.getSample();

      samples.add(sample);

      for (HeatMapIdLocation location : mLocations) {
        if (location.getRegion() == null) {
          continue;
        }

        // Extend around a point since we are binning around TSS or
        // genomic points. Either the point will be a TSS in which case
        // the mid region will be the same as the TSS since it is a
        // single location, or else use the mid point of the region
        // since we must have uniform binning around each location
        GenomicRegion ext = GenomicRegion.extend(GenomicRegion
            .midRegion(location.getRegion()), mPadding, mPadding);

        // +- 2kb

        List<Double> counts = getCounts(track, ext, mWindow);

        for (int i = 0; i < bins; ++i) {
          mBinCountMap.get(sample).put(i,
              mBinCountMap.get(sample).get(i) + counts.get(i));
        }
      }
    }

    if (mAverage) {
      System.err.println("Averaging " + mFile);

      for (SamplePlotTrack track : mTracks) {
        Sample sample = track.getSample();

        for (int i : mBinCountMap.get(sample).keySet()) {
          mBinCountMap.get(sample).put(i,
              mBinCountMap.get(sample).get(i) / mLocations.size());
        }
      }
    }

    // This is file of all the counts

    System.err.println("Writing to " + mFile);

    BufferedTableWriter writer = FileUtils.newBufferedTableWriter(mFile);

    try {
      for (int i = 0; i < mTracks.size(); ++i) {
        writer.write(mTracks.get(i).getName() + " x");
        writer.sep();
        writer.write(mTracks.get(i).getName() + " y");

        if (i < mTracks.size() - 1) {
          writer.sep();
        }
      }

      writer.newLine();

      for (int i = 0; i < bins; ++i) {
        String bin = Integer.toString((i - bins / 2) * mWindow);

        for (int j = 0; j < mTracks.size(); ++j) {
          writer.write(bin);
          writer.sep();
          writer
              .write(Double.toString(mBinCountMap.get(samples.get(j)).get(i)));

          if (j < mTracks.size() - 1) {
            writer.sep();
          }
        }

        writer.newLine();
      }
    } finally {
      writer.close();
    }
  }

  /**
   * Get the counts and subtract the input if necessary.
   *
   * @param track the track
   * @param ext the ext
   * @param mWindow the m window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private List<Double> getCounts(SamplePlotTrack track,
      GenomicRegion ext,
      int mWindow) throws IOException {
    List<Double> counts = track.getAssembly()
        .getRPM(track.getSample(), ext, mWindow);

    /*
     * if (mInput != null) { List<Double> inputCounts =
     * mAssembly.getNormalizedCounts(mInput, ext, mWindow);
     * 
     * for (int i = 0; i < counts.size(); ++i) { counts.set(i, Math.max(0,
     * counts.get(i) - inputCounts.get(i))); } }
     */

    return counts;
  }
}
