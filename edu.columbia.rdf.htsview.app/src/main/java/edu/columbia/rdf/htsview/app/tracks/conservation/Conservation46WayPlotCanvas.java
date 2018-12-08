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
package edu.columbia.rdf.htsview.app.tracks.conservation;

import java.awt.Color;
import java.io.IOException;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.app.tracks.dna.DnaSubFigure;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;

/**
 * The Class Conservation46WayPlotCanvas.
 */
public class Conservation46WayPlotCanvas extends DnaSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m layer. */
  private Conservation46WayCanvasLayer mLayer;

  /**
   * Instantiates a new conservation 46 way plot canvas.
   *
   * @param conservationAssembly the conservation assembly
   * @param titlePosition the title position
   */
  public Conservation46WayPlotCanvas(ConservationAssembly conservationAssembly,
      TitleProperties titlePosition) {
    mLayer = new Conservation46WayCanvasLayer(conservationAssembly);

    currentAxes().addChild(mLayer);

    Track.setTitle(Conservation46WayPlotTrack.TITLE,
        titlePosition,
        currentAxes());
  }

  /**
   * Creates the.
   *
   * @param assembly the assembly
   * @param titlePosition the title position
   * @return the conservation 46 way plot canvas
   */
  public static Conservation46WayPlotCanvas create(
      ConservationAssembly assembly,
      TitleProperties titlePosition) {

    Conservation46WayPlotCanvas canvas = new Conservation46WayPlotCanvas(
        assembly, titlePosition);

    // set the graph limits
    canvas.currentAxes().getX1Axis().getTitle().setText(null);
    canvas.currentAxes().getX1Axis().startEndTicksOnly();

    canvas.currentAxes().getY1Axis().getTitle().setText(null);
    canvas.currentAxes().getY1Axis().startEndTicksOnly();

    canvas.currentAxes().setInternalSize(Track.SMALL_TRACK_SIZE);
    // canvas.getGraphSpace().getLayoutProperties().setMargins(MARGINS);

    return canvas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.htsview.tracks.dna.DnaSubFigure#update(org.jebtk.bioinformatics.genome.
   * GenomicRegion, int, double, int, int, int, java.awt.Color, java.awt.Color,
   * org.graphplot.figure.PlotStyle)
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
      PlotStyle style) throws IOException {
    super.update(displayRegion,
        resolution,
        yMax,
        width,
        height,
        margin,
        lineColor,
        fillColor,
        style);

    mLayer.update(displayRegion);
  }
}
