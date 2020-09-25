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
import java.io.IOException;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.tracks.FixedYSubFigure;
import edu.columbia.rdf.htsview.tracks.Track;

/**
 * The Class DnaSubFigure.
 */
public abstract class DnaSubFigure extends FixedYSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

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

    super.update(genome,
        displayRegion,
        resolution,
        yMax,
        width,
        Track.SMALL_TRACK_SIZE.height,
        margin,
        lineColor,
        fillColor,
        style);
  }
}
