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
package edu.columbia.rdf.htsview.app.tracks.dna;

import java.awt.Color;

import org.jebtk.bioinformatics.ext.ucsc.Cytobands;
import org.jebtk.bioinformatics.genomic.ChromosomeSizes;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.ui.external.ucsc.CytobandsRegionLayer;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.measurement.MeasurementSubFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class CytobandsSubFigure.
 */
public class CytobandsSubFigure extends MeasurementSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m sizes. */
  private static ChromosomeSizes mSizes;

  /** The m layer. */
  private CytobandsRegionLayer mLayer = null;

  /** The m name. */
  private String mName;

  /**
   * Instantiates a new cytobands sub figure.
   *
   * @param name the name
   * @param cytobands the cytobands
   * @param titlePosition the title position
   */
  public CytobandsSubFigure(String name, Cytobands cytobands,
      TitleProperties titlePosition) {
    mName = name;
    mLayer = new CytobandsRegionLayer(cytobands);

    currentAxes().addChild(mLayer);

    Track.setTitle(name, titlePosition, currentAxes());
  }

  /**
   * Creates the.
   *
   * @param name the name
   * @param sizes the sizes
   * @param cytobands the cytobands
   * @param titlePosition the title position
   * @return the cytobands sub figure
   */
  public static CytobandsSubFigure create(String name,
      ChromosomeSizes sizes,
      Cytobands cytobands,
      TitleProperties titlePosition) {

    mSizes = sizes;

    CytobandsSubFigure canvas = new CytobandsSubFigure(name, cytobands,
        titlePosition);

    canvas.currentAxes().setInternalSize(Track.MEDIUM_TRACK_SIZE);

    return canvas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.measurement.MeasurementSubFigure#update(
   * org. jebtk.bioinformatics.genome.GenomicRegion, int, double, int, int, int,
   * java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
   */
  @Override
  public void update(GenomicRegion displayRegion,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin,
      Color lineColor,
      Color fillColor,
      PlotStyle style) {

    super.update(displayRegion,
        resolution,
        yMax,
        width,
        height,
        margin,
        lineColor,
        fillColor,
        style);

    mLayer.setRegion(displayRegion);

    // Update the title to reflect changes
    currentAxes().getTitle()
        .setText(mName + " - " + displayRegion.getChr().toString());

    // set the graph limits
    currentAxes().getX1Axis().setLimits(1,
        mSizes.getSize(displayRegion.getChr()));
  }
}
