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

import java.awt.Graphics2D;
import java.io.IOException;

import org.jebtk.bioinformatics.genomic.DNA;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.RepeatMaskType;
import org.jebtk.bioinformatics.genomic.SequenceReader;
import org.jebtk.bioinformatics.genomic.SequenceRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The Class DnaColorCanvasLayer.
 */
public class DnaColorCanvasLayer extends AxesClippedLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m display region. */
  private GenomicRegion mDisplayRegion;

  /** The m assembly. */
  private SequenceReader mAssembly;

  private Genome mGenome;

  /**
   * Instantiates a new dna color canvas layer.
   *
   * @param genome the genome
   * @param assembly the assembly
   */
  public DnaColorCanvasLayer(SequenceReader assembly) {
    mAssembly = assembly;
  }

  /**
   * Update.
   *
   * @param displayRegion the display region
   */
  public void update(Genome genome, GenomicRegion displayRegion) {
    mGenome = genome;
    mDisplayRegion = displayRegion;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.AxesClippedLayer#plotLayer(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure,
   * org.graphplot.figure.Axes)
   */
  @Override
  public void plotLayer(Graphics2D g2,
      DrawingContext context,
      Figure figure,
      SubFigure subFigure,
      Axes axes) {

    // So that we don't attempt to pull a whole chromosome
    if (mDisplayRegion.getLength() > DnaPlotTrack.MAX_DISPLAY_BASES) {
      return;
    }

    int x1;
    int y = 0;
    int h = axes.getInternalSize().getH();
    int bw = g2.getFontMetrics().stringWidth("G");
    int hbw = bw / 2;

    try {
      SequenceRegion sequence = mAssembly.getSequence(mGenome, mDisplayRegion,
          RepeatMaskType.N);

      int start = mDisplayRegion.getStart();
      int w;

      // System.err.println(w + " " +
      // space.getLayoutProperties().getPlotSize().getW()
      // + " " + mDisplayRegion.getLength());

      for (char c : sequence.getSequence().toArray()) {
        switch (c) {
        case 'a':
          // fullHeight = false;
        case 'A':
          g2.setColor(DNA.BASE_A_COLOR);
          break;
        case 'c':
          // fullHeight = false;
        case 'C':
          g2.setColor(DNA.BASE_C_COLOR);
          break;
        case 'g':
          // fullHeight = false;
        case 'G':
          g2.setColor(DNA.BASE_G_COLOR);
          break;
        case 't':
          // fullHeight = false;
        case 'T':
          g2.setColor(DNA.BASE_T_COLOR);
          break;
        default:
          g2.setColor(DNA.BASE_N_COLOR);
          break;
        }

        x1 = axes.toPlotX1(start);
        w = axes.toPlotX1(start + 1) - x1;

        g2.fillRect(x1, y, w, h);

        ++start;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
