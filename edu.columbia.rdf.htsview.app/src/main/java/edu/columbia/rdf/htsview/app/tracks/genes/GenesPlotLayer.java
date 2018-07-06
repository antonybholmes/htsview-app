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
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.Set;

import org.jebtk.bioinformatics.genomic.Gene;
import org.jebtk.bioinformatics.genomic.GenomicEntity;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.bioinformatics.genomic.Strand;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.IterMap;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.widget.ModernWidget;

/**
 * The Class GenesPlotLayer.
 */
public class GenesPlotLayer extends AxesLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant BAR_HEIGHT. */
  public static final int BAR_HEIGHT = 24;

  /** The Constant HALF_BAR_HEIGHT. */
  public static final int HALF_BAR_HEIGHT = BAR_HEIGHT / 2;

  /** The Constant QUARTER_BAR_HEIGHT. */
  public static final int QUARTER_BAR_HEIGHT = BAR_HEIGHT / 4;

  /** The Constant TSS_ARROW_HEIGHT. */
  public static final int TSS_ARROW_HEIGHT = 16;

  /** The Constant TSS_HALF_ARROW_HEIGHT. */
  public static final int TSS_HALF_ARROW_HEIGHT = TSS_ARROW_HEIGHT / 2;

  /** The Constant ARROW_GAP. */
  public static final int ARROW_GAP = BAR_HEIGHT * 2;

  /** The Constant ARROW_HEIGHT. */
  public static final int ARROW_HEIGHT = BAR_HEIGHT / 4;

  /** The Constant DOUBLE_ARROW_HEIGHT. */
  public static final int DOUBLE_ARROW_HEIGHT = ARROW_HEIGHT * 2;

  /** The Constant BLOCK_HEIGHT. */
  public static final int BLOCK_HEIGHT = 2 * BAR_HEIGHT;

  /** The Constant GAP. */
  public static final int GAP = BLOCK_HEIGHT + ModernWidget.PADDING;

  /** The m gene properties. */
  private GenesProperties mGeneProperties;

  /** The m gene cache. */
  private IterMap<String, Set<GenomicEntity>> mGeneCache;

  /** The Constant FORWARD_TSS_ARROW. */
  private static final GeneralPath FORWARD_TSS_ARROW = new GeneralPath();

  /** The Constant REVERSE_TSS_ARROW. */
  private static final GeneralPath REVERSE_TSS_ARROW = new GeneralPath();

  /** The Constant FORWARD_ARROW. */
  private static final GeneralPath FORWARD_ARROW = new GeneralPath();

  /** The Constant REVERSE_ARROW. */
  private static final GeneralPath REVERSE_ARROW = new GeneralPath();

  static {
    FORWARD_TSS_ARROW.moveTo(0, -TSS_HALF_ARROW_HEIGHT);
    FORWARD_TSS_ARROW.lineTo(TSS_HALF_ARROW_HEIGHT, 0);
    FORWARD_TSS_ARROW.lineTo(0, TSS_HALF_ARROW_HEIGHT);
    FORWARD_TSS_ARROW.closePath();

    REVERSE_TSS_ARROW.moveTo(0, -TSS_HALF_ARROW_HEIGHT);
    REVERSE_TSS_ARROW.lineTo(-TSS_HALF_ARROW_HEIGHT, 0);
    REVERSE_TSS_ARROW.lineTo(0, TSS_HALF_ARROW_HEIGHT);
    REVERSE_TSS_ARROW.closePath();

    FORWARD_ARROW.moveTo(0, 0);
    FORWARD_ARROW.lineTo(ARROW_HEIGHT, ARROW_HEIGHT);
    FORWARD_ARROW.lineTo(0, DOUBLE_ARROW_HEIGHT);
    // FORWARD_ARROW.closePath();

    REVERSE_ARROW.moveTo(ARROW_HEIGHT, 0);
    REVERSE_ARROW.lineTo(0, ARROW_HEIGHT);
    REVERSE_ARROW.lineTo(ARROW_HEIGHT, DOUBLE_ARROW_HEIGHT);
    // REVERSE_ARROW.closePath();
  }

  /**
   * Instantiates a new genes plot layer.
   *
   * @param genesProperties the genes properties
   */
  public GenesPlotLayer(GenesProperties genesProperties) {
    mGeneProperties = genesProperties;
  }

  /**
   * Update.
   *
   * @param genes the genes
   * @param displayRegion the display region
   */
  public void update(IterMap<String, Set<GenomicEntity>> genes,
      GenomicRegion displayRegion) {
    mGeneCache = genes;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.AxesLayer#plot(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure,
   * org.graphplot.figure.Axes)
   */
  @Override
  public void drawPlot(Graphics2D g2,
      DrawingContext context,
      Figure figure,
      SubFigure subFigure,
      Axes axes) {

    aaPlot(g2, context, figure, subFigure, axes);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.AxesLayer#drawPlot(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure,
   * org.graphplot.figure.Axes)
   */
  @Override
  public void aaPlot(Graphics2D g2,
      DrawingContext context,
      Figure figure,
      SubFigure subFigure,
      Axes axes) {

    if (CollectionUtils.isNullOrEmpty(mGeneCache)) {
      return;
    }

    // Use the graph properties and subplot layout to
    // create the graph space mapper

    int x1;
    int x2;
    // int px1;
    // int px2;
    int y = BAR_HEIGHT + TSS_HALF_ARROW_HEIGHT;
    int y1;
    int textY;
    int textX = -20;

    int minX = axes.toPlotX1(axes.getX1Axis().getMin());
    int maxX = axes.toPlotX1(axes.getX1Axis().getMax());

    int c = 0;

    Strand strand;

    GenesView view = mGeneProperties.getView();

    int maxGenes = mGeneProperties.getMaxGenes();

    g2.setStroke(mGeneProperties.getStyle().getStroke());

    for (String symbol : mGeneCache) {
      if (c == maxGenes) {
        break;
      }

      // isMainVariant = mGeneCache.size() == 1 ||
      // (mGenes.findMainVariant(g.getSymbol()) != null &&
      // mGenes.findMainVariant(g.getSymbol()).equals(g));

      // g2.setFont(isMainVariant ?
      // mGeneProperties.getVariantGene().getFont().getFont() :
      // mGeneProperties.getOtherGene().getFont().getFont());

      // g2.setColor(isMainVariant ?
      // mGeneProperties.getVariantGene().getFont().getColor() :
      // mGeneProperties.getOtherGene().getFont().getColor());

      if (view != GenesView.COMPACT) {
        // g2.setFont(mGeneProperties.getVariantGene().getFont().getFont());
        g2.setColor(mGeneProperties.getVariantGene().getFont().getColor());
        textY = y + (g2.getFontMetrics().getAscent()
            - g2.getFontMetrics().getDescent()) / 2;
        g2.drawString(symbol,
            textX - g2.getFontMetrics().stringWidth(symbol),
            textY);
      }

      // Set the strand to be that of the first gene we encounter
      strand = mGeneCache.get(symbol).iterator().next().getStrand();

      //
      // The line
      //

      int lx1 = Integer.MAX_VALUE;
      int lx2 = Integer.MIN_VALUE;

      for (GenomicEntity g : mGeneCache.get(symbol)) {
        lx1 = Math.min(lx1, g.mStart);
        lx2 = Math.max(lx2, g.mEnd);
      }

      lx1 = axes.toPlotX1(lx1);
      lx2 = axes.toPlotX1(lx2);

      // g2.setColor(isMainVariant ?
      // mGeneProperties.getVariantGene().getLineStyle().getColor() :
      // mGeneProperties.getOtherGene().getLineStyle().getColor());

      if (mGeneProperties.getShowArrows()) {
        drawArrows(g2, strand, lx1, lx2, y, mGeneProperties.getArrowColor());
      }

      g2.setColor(mGeneProperties.getVariantGene().getLineStyle().getColor());

      // The gene line
      g2.drawLine(lx1, y, lx2, y);

      for (GenomicEntity g : mGeneCache.get(symbol)) {
        //
        // Draw the utr
        //

        for (GenomicEntity exon : ((Gene)g).get5pUtrs()) {
          x1 = axes.toPlotX1(exon.mStart);
          x2 = axes.toPlotX1(exon.mEnd);

          // If exons are outside the view window, simply don't draw
          // them rather than using the min/max x. This stops the
          // exons bunching up at the end of the gene as we scroll
          if (x1 < minX || x2 > maxX) {
            continue;
          }

          // if (!exon.getType().contains("utr")) {
          // continue;
          // }

          // Draw the UTR regions with a narrow band, as with the
          // UCSC
          y1 = y - QUARTER_BAR_HEIGHT;

          g2.setColor(mGeneProperties.getUTR().getFillStyle().getColor());
          g2.fillRect(x1, y1, x2 - x1, HALF_BAR_HEIGHT);

          g2.setColor(mGeneProperties.getUTR().getLineStyle().getColor());
          g2.drawRect(x1, y1, x2 - x1, HALF_BAR_HEIGHT);
        }

        for (GenomicEntity exon : g.getEntities(GenomicType.UTR_3P)) {
          x1 = axes.toPlotX1(exon.mStart);
          x2 = axes.toPlotX1(exon.mEnd);

          // If exons are outside the view window, simply don't draw
          // them rather than using the min/max x. This stops the
          // exons bunching up at the end of the gene as we scroll
          if (x1 < minX || x2 > maxX) {
            continue;
          }

          // if (!exon.getType().contains("utr")) {
          // continue;
          // }

          // Draw the UTR regions with a narrow band, as with the
          // UCSC
          y1 = y - QUARTER_BAR_HEIGHT;

          g2.setColor(mGeneProperties.getUTR().getFillStyle().getColor());
          g2.fillRect(x1, y1, x2 - x1, HALF_BAR_HEIGHT);

          g2.setColor(mGeneProperties.getUTR().getLineStyle().getColor());
          g2.drawRect(x1, y1, x2 - x1, HALF_BAR_HEIGHT);
        }

        //
        // Draw the exons last so they overwrite the UTR.
        //

        ExonProperties p = mGeneProperties.getVariantGene().getExons();

        for (GenomicEntity exon : g.getEntities(GenomicType.EXON)) {
          x1 = axes.toPlotX1(exon.mStart);
          x2 = axes.toPlotX1(exon.mEnd);

          // If exons are outside the view window, simply don't draw
          // them rather than using the min/max x. This stops the
          // exons bunching up at the end of the gene as we scroll
          if (x1 < minX || x2 > maxX) {
            continue;
          }

          // if (!exon.getType().equals("exon")) {
          // continue;
          // }

          y1 = y - HALF_BAR_HEIGHT;

          // g2.setColor(isMainVariant ?
          // mGeneProperties.getVariantGene().getLineStyle().getColor() :
          // mGeneProperties.getOtherGene().getLineStyle().getColor());

          g2.setColor(p.getLineStyle().getColor());
          g2.fillRect(x1, y1, x2 - x1, BAR_HEIGHT);

          // g2.setColor(p.getLineStyle().getColor());
          // g2.drawRect(x1, y1, x2 - x1, BAR_HEIGHT);

          if (mGeneProperties.getShowExonArrows()) {
            if (Strand.isSense(strand)) {
              drawForwardArrow(g2, x1, y1);
            } else {
              drawReverseArrow(g2, x2, y1);
            }
          }
        }
      }

      //
      // The end lines
      //

      // g2.setColor(isMainVariant ?
      // mGeneProperties.getVariantGene().getLineStyle().getColor() :
      // mGeneProperties.getOtherGene().getLineStyle().getColor());

      g2.setColor(mGeneProperties.getVariantGene().getLineStyle().getColor());

      if (lx1 >= minX && lx1 <= maxX) {
        g2.drawLine(lx1, y - BAR_HEIGHT, lx1, y + BAR_HEIGHT);
      }

      if (lx2 >= minX && lx2 <= maxX) {
        g2.drawLine(lx2, y - BAR_HEIGHT, lx2, y + BAR_HEIGHT);
      }

      //
      // Draw tss arrows
      //

      y1 = y - HALF_BAR_HEIGHT;

      if (mGeneProperties.getDrawTssArrows()) {
        if (strand == Strand.SENSE) {
          if (lx1 >= minX && lx1 <= maxX) {
            drawForwardArrow(g2, lx1, y1);
          }
        } else {
          if (lx2 >= minX && lx2 <= maxX) {
            drawReverseArrow(g2, lx2, y1);
          }
        }
      }

      ++c;

      if (view != GenesView.COMPACT) {
        y += GAP;
      }
    }
  }

  /**
   * Draw forward arrow.
   *
   * @param g2 the g 2
   * @param x the x
   * @param y the y
   */
  public static final void drawForwardArrow(Graphics2D g2, int x, int y) {
    Graphics2D g2Temp = (Graphics2D) g2.create();

    g2Temp.translate(x + HALF_BAR_HEIGHT, y - HALF_BAR_HEIGHT);
    g2Temp.drawLine(-HALF_BAR_HEIGHT, 0, -HALF_BAR_HEIGHT, HALF_BAR_HEIGHT);
    g2Temp.drawLine(-HALF_BAR_HEIGHT, 0, 0, 0);
    g2Temp.fill(FORWARD_TSS_ARROW);

    g2Temp.dispose();
  }

  /**
   * Draw reverse arrow.
   *
   * @param g2 the g 2
   * @param x the x
   * @param y the y
   */
  public final static void drawReverseArrow(Graphics2D g2, int x, int y) {
    Graphics2D g2Temp = (Graphics2D) g2.create();

    g2Temp.translate(x - HALF_BAR_HEIGHT, y - HALF_BAR_HEIGHT);
    g2Temp.drawLine(HALF_BAR_HEIGHT, 0, HALF_BAR_HEIGHT, HALF_BAR_HEIGHT);
    g2Temp.drawLine(0, 0, HALF_BAR_HEIGHT, 0);
    g2Temp.fill(REVERSE_TSS_ARROW);

    g2Temp.dispose();
  }

  /**
   * Draw arrows.
   *
   * @param g2 the g 2
   * @param strand the strand
   * @param x1 the x 1
   * @param x2 the x 2
   * @param y1 the y 1
   * @param color the color
   */
  public final static void drawArrows(Graphics2D g2,
      Strand strand,
      int x1,
      int x2,
      int y1,
      Color color) {
    Graphics2D g2Temp = ImageUtils.clone(g2);

    // Stop arrows being drawn at the gene starts or ends since this looks
    // visually messy.
    x1 += ARROW_GAP;
    x2 -= ARROW_GAP;
    
    int x;
    int y = y1 - ARROW_HEIGHT;

    try {
      g2Temp.setColor(color);

      if (Strand.isSense(strand)) {
        x = x1; // + ARROW_GAP;

        g2Temp.translate(x, y);

        while (x < x2) {
          g2Temp.draw(FORWARD_ARROW);

          g2Temp.translate(ARROW_GAP, 0);

          x += ARROW_GAP;
        }
      } else {
        x = x2; // - ARROW_HEIGHT - ARROW_GAP;

        g2Temp.translate(x, y);

        while (x > x1) {
          g2Temp.draw(REVERSE_ARROW);

          g2Temp.translate(-ARROW_GAP, 0);

          x -= ARROW_GAP;
        }
      }
    } finally {
      g2Temp.dispose();
    }
  }
}
