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
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;

import org.jebtk.bioinformatics.genomic.DNA;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.RepeatMaskType;
import org.jebtk.bioinformatics.genomic.SequenceReader;
import org.jebtk.bioinformatics.genomic.SequenceRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.font.FontService;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The Class DnaBasesCanvasLayer.
 */
public class DnaBasesCanvasLayer extends AxesClippedLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  private static final Font DNA_FONT = FontService.getInstance()
      .loadFont("Courier New", 16);

  /** The m display region. */
  private GenomicRegion mDisplayRegion;

  /** The m assembly. */
  private SequenceReader mAssembly;

  private boolean mColorMode = false;

  /**
   * Instantiates a new dna bases canvas layer.
   *
   * @param genome the genome
   * @param assembly the assembly
   */
  public DnaBasesCanvasLayer(SequenceReader assembly, boolean colorMode) {
    mAssembly = assembly;
    mColorMode = colorMode;
  }

  /**
   * Update.
   *
   * @param displayRegion the display region
   */
  public void update(GenomicRegion displayRegion) {
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
    if (mDisplayRegion.getLength() <= DnaPlotTrack.MAX_DISPLAY_BASES) {
      plotBases(g2, context, figure, subFigure, axes);
    } else {
      plotColors(g2, context, figure, subFigure, axes);
    }
  }

  private void plotBases(Graphics2D g2,
      DrawingContext context,
      Figure figure,
      SubFigure subFigure,
      Axes axes) {

    g2.setFont(DNA_FONT);

    int y = g2.getFontMetrics().getAscent() + g2.getFontMetrics().getDescent();

    g2.setColor(Color.BLACK);

    int bw = g2.getFontMetrics().stringWidth("G");
    int hbw = bw / 2;

    try {
      SequenceRegion sequence = mAssembly.getSequence(mDisplayRegion,
          RepeatMaskType.N);

      int start = mDisplayRegion.getStart();

      int x1 = axes.toPlotX1(mDisplayRegion.getStart());
      // int w;

      for (char c : sequence.getSequence().toArray()) {

        if (mColorMode) {
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
          }
        }

        String s = Character.toString(c);

        x1 = axes.toPlotX1(start) - hbw; // (axes.toPlotX1(start) +
                                         // axes.toPlotX1(start + 1)) / 2 - hbw;
        // w = axes.toPlotX1(start + 1) - x1;
        // x1 += (w - bw) / 2;

        g2.drawString(s, x1, y);

        ++start;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void plotColors(Graphics2D g2,
      DrawingContext context,
      Figure figure,
      SubFigure subFigure,
      Axes axes) {

    if (mDisplayRegion.getLength() > DnaPlotTrack.MAX_DISPLAY_COLOR_BASES) {
      return;
    }

    int x1;
    int y = 0;
    int h = axes.getInternalSize().getH();

    try {
      SequenceRegion sequence = mAssembly.getSequence(mDisplayRegion,
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
        w = Math.max(1, axes.toPlotX1(start + 1) - x1);

        g2.fillRect(x1, y, w, h);

        ++start;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
