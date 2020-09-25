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
package edu.columbia.rdf.htsview.app.tracks.genes;

import java.awt.Color;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.bioinformatics.genomic.GenomicEntity;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.collections.TreeSetCreator;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.tracks.FixedYSubFigure;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;

/**
 * The Class GenesPlotSubFigure.
 */
public class GenesPlotSubFigure extends FixedYSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m genes layer. */
  private GenesPlotLayer mGenesLayer;

  /** The m properties. */
  private GenesProperties mProperties;

  private String mTrack;

  private Genome mGenome;

  /**
   * Instantiates a new genes plot sub figure.
   *
   * @param name the name
   * @param properties the properties
   * @param genome the genome
   * @param db the id
   * @param titlePosition the title position
   */
  public GenesPlotSubFigure(String name, String track,
      GenesProperties properties, TitleProperties titlePosition) {
    mTrack = track;

    mProperties = properties;

    mGenesLayer = new GenesPlotLayer(properties);

    currentAxes().addChild(mGenesLayer);

    Track.setTitle(name, titlePosition, currentAxes());
  }

  /**
   * Creates the.
   *
   * @param name the name
   * @param genesProperties the genes properties
   * @param genome the genome
   * @param genesId the genes id
   * @param titlePosition the title position
   * @return the genes plot sub figure
   */
  public static GenesPlotSubFigure create(String name,
      String track,
      GenesProperties genesProperties,
      TitleProperties titlePosition) {

    // Now lets create a plot
    GenesPlotSubFigure canvas = new GenesPlotSubFigure(name, track,
        genesProperties, titlePosition);

    return canvas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.FixedSubFigure#update(org.jebtk.
   * bioinformatics.genome.GenomicRegion, int, double, int, int, int,
   * java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
   */
  @Override
  public void update(Genome genome,
      GenomicRegion displayRegion,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin,
      Color lineColor,
      Color fillColor,
      PlotStyle style) throws IOException {

    if (mGenome == null
        || !genome.getAssembly().equals(mGenome.getAssembly())) {
      // Cache the genome unless the assembly name ch
      mGenome = Genome.changeTrack(genome, mTrack);
    }

    Collection<GenomicElement> genes = null;

    genes = GenesService.getInstance().getGenes(mGenome)
        .find(mGenome, displayRegion, GenomicType.TRANSCRIPT, 1);
    

    if (genes == null) {
      genes = Collections.emptyList();
    }

    IterMap<String, Set<GenomicElement>> geneMap = DefaultTreeMap
        .create(new TreeSetCreator<GenomicElement>());

    GenesView view = mProperties.getView();

    switch (view) {
    case COMPACT:
    case DENSE:
      for (GenomicElement g : genes) {
        geneMap.get(g.getProperty(GenomicEntity.GENE_NAME)).add(g);
      }

      break;
    default:
      // full
      for (GenomicElement g : genes) {
        String id = g.getProperty(GenomicEntity.GENE_NAME);

        String v = g.getProperty(GenomicEntity.REFSEQ_ID);

        if (!v.equals(TextUtils.NA)) {
          id += " (" + v + ")";
        }

        v = g.getProperty(GenomicEntity.TRANSCRIPT_ID);

        if (!v.equals(TextUtils.NA)) {
          id += " (" + v + ")";
        }

        geneMap.get(id).add(g);
      }

      break;
    }

    // Include extra space to allow the title to be draw in the margin,
    // otherwise there is overwriting

    int s;

    if (view == GenesView.COMPACT) {
      s = 1;
    } else {
      s = geneMap.size();
    }

    height = GenesPlotLayer.GAP * s;

    mGenesLayer.update(geneMap, displayRegion);

    super.update(genome,
        displayRegion,
        resolution,
        genes.size(),
        width,
        height,
        margin,
        lineColor,
        fillColor,
        style);
  }
}
