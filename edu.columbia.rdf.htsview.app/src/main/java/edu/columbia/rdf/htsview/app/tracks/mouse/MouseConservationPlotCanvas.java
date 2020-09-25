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
package edu.columbia.rdf.htsview.app.tracks.mouse;

import java.awt.Color;
import java.io.IOException;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.app.tracks.dna.DnaSubFigure;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;

/**
 * The Class MouseConservationPlotCanvas.
 */
public class MouseConservationPlotCanvas extends DnaSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m layer. */
  private MouseConservationCanvasLayer mLayer;

  /**
   * Instantiates a new mouse conservation plot canvas.
   *
   * @param conservationAssembly the conservation assembly
   * @param titlePosition the title position
   */
  public MouseConservationPlotCanvas(ConservationAssembly conservationAssembly,
      TitleProperties titlePosition) {
    mLayer = new MouseConservationCanvasLayer(conservationAssembly);

    currentAxes().addChild(mLayer);

    Track.setTitle(MouseConservationPlotTrack.TITLE,
        titlePosition,
        currentAxes());
  }

  /**
   * Creates the.
   *
   * @param assembly the assembly
   * @param titlePosition the title position
   * @return the mouse conservation plot canvas
   */
  public static MouseConservationPlotCanvas create(
      ConservationAssembly assembly,
      TitleProperties titlePosition) {

    MouseConservationPlotCanvas canvas = new MouseConservationPlotCanvas(
        assembly, titlePosition);

    Axes axes = canvas.currentAxes();

    // set the graph limits
    axes.getX1Axis().getTitle().setText(null);
    axes.getX1Axis().startEndTicksOnly();

    axes.getY1Axis().getTitle().setText(null);
    axes.getY1Axis().startEndTicksOnly();

    axes.setInternalSize(Track.SMALL_TRACK_SIZE);
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
    super.update(genome,
        displayRegion,
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
