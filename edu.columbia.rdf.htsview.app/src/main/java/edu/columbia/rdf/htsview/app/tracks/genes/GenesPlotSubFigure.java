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
import java.util.Collection;
import java.util.Set;

import org.jebtk.bioinformatics.genomic.Gene;
import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.collections.TreeSetCreator;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.tracks.FixedSubFigure;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;

// TODO: Auto-generated Javadoc
/**
 * The Class GenesPlotSubFigure.
 */
public class GenesPlotSubFigure extends FixedSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m genes layer. */
  private GenesPlotLayer mGenesLayer;

  /** The m properties. */
  private GenesProperties mProperties;

  /** The m genome. */
  private String mGenome;

  /** The m genes id. */
  private String mGenesId;

  /**
   * Instantiates a new genes plot sub figure.
   *
   * @param name
   *          the name
   * @param properties
   *          the properties
   * @param genome
   *          the genome
   * @param id
   *          the id
   * @param titlePosition
   *          the title position
   */
  public GenesPlotSubFigure(String name, GenesProperties properties, String genome, String id,
      TitleProperties titlePosition) {
    mProperties = properties;

    mGenome = genome;
    mGenesId = id;

    mGenesLayer = new GenesPlotLayer(properties);

    currentAxes().addChild(mGenesLayer);

    Track.setTitle(name, titlePosition, currentAxes());
  }

  /**
   * Creates the.
   *
   * @param name
   *          the name
   * @param genesProperties
   *          the genes properties
   * @param genome
   *          the genome
   * @param genesId
   *          the genes id
   * @param titlePosition
   *          the title position
   * @return the genes plot sub figure
   */
  public static GenesPlotSubFigure create(String name, GenesProperties genesProperties, String genome, String genesId,
      TitleProperties titlePosition) {

    // Now lets create a plot
    GenesPlotSubFigure canvas = new GenesPlotSubFigure(name, genesProperties, genome, genesId, titlePosition);

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
  public void update(GenomicRegion displayRegion, int resolution, double yMax, int width, int height, int margin,
      Color lineColor, Color fillColor, PlotStyle style) {
    Collection<Gene> genes = GenesService.getInstance().getGenes(mGenome, mGenesId).findGenes(displayRegion);

    IterMap<String, Set<Gene>> geneMap = DefaultTreeMap.create(new TreeSetCreator<Gene>());

    GenesView view = mProperties.getView();

    switch (view) {
    case COMPACT:
    case DENSE:
      for (Gene g : genes) {
        geneMap.get(g.getSymbol()).add(g);
      }

      break;
    default:
      // full
      for (Gene g : genes) {
        String id = g.getSymbol();

        if (!g.getRefSeq().equals(TextUtils.NA)) {
          id += " (" + g.getRefSeq() + ")";
        }

        if (!g.getTranscriptId().equals(TextUtils.NA)) {
          id += " (" + g.getTranscriptId() + ")";
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

    super.update(displayRegion, resolution, genes.size(), width, height, margin, lineColor, fillColor, style);
  }
}
