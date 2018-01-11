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
package edu.columbia.rdf.htsview.app.tracks.igv.seg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraphGroupModel;
import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.IterMap;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.widget.ModernWidget;

import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedPlotTrack;

// TODO: Auto-generated Javadoc
/**
 * The Class SegmentsPlotLayer.
 */
public class SegmentsPlotLayer extends AxesLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  // private static final int BAR_HEIGHT = 20;

  /** The m bed graph group. */
  protected BedGraphGroupModel mBedGraphGroup;

  /** The m regions. */
  private IterMap<String, List<Segment>> mRegions;

  /** The m color map. */
  private ColorMap mColorMap;

  /**
   * Instantiates a new segments plot layer.
   *
   * @param colorMap the color map
   */
  public SegmentsPlotLayer(ColorMap colorMap) {
    mColorMap = colorMap; // ColorMap.createBlueWhiteRedMap();
  }

  /**
   * Update.
   *
   * @param regions the regions
   * @param colorMap the color map
   */
  public void update(IterMap<String, List<Segment>> regions,
      ColorMap colorMap) {
    mRegions = regions;

    setColorMap(colorMap);
  }

  /**
   * Sets the color map.
   *
   * @param colorMap the new color map
   */
  public void setColorMap(ColorMap colorMap) {
    mColorMap = colorMap;
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

    if (CollectionUtils.isNullOrEmpty(mRegions)) {
      return;
    }

    int x1;
    int x2;

    int textX = -20;
    int textY;

    // int h = BAR_HEIGHT; //space.getPlotSize().getH();
    int w;
    int y = 0;

    g2.setColor(Color.LIGHT_GRAY);
    g2.fillRect(0,
        y,
        axes.toPlotX1(axes.getX1Axis().getMax()),
        BedPlotTrack.BAR_HEIGHT * mRegions.size());

    for (String name : mRegions) {

      g2.setColor(Color.BLACK);
      textY = y + ModernWidget.getTextYPosCenter(g2, BedPlotTrack.BAR_HEIGHT);
      g2.drawString(name, textX - g2.getFontMetrics().stringWidth(name), textY);

      List<Segment> segments = mRegions.get(name);

      for (Segment segment : segments) {

        x1 = axes.toPlotX1(segment.getStart());
        x2 = axes.toPlotX1(segment.getEnd());

        // figure.get

        // Lets use what IGV does
        double v = Mathematics.bound(segment.getMean(), -1.5, 1.5);

        // scale between 0 and 1
        v = (v + 1.5) / 3;

        g2.setColor(mColorMap.getColor(v));

        // if (segment.getMean() > 0) {
        // g2.setColor(Color.RED);
        // } else {
        // g2.setColor(Color.BLUE);
        // }

        // Default mode when there are no blocks is to draw a block
        // spanning the whole region

        // Must be a minimum of 1 pixel wide
        w = Math.max(1, x2 - x1 + 1);

        if (w > 1) {
          g2.fillRect(x1, y, w, SegPlotTrack.BAR_HEIGHT);
        } else {
          // If the bar is one pixel wide, draw it as a line
          // rather than rectangle.
          g2.drawLine(x1, y, x1, y + SegPlotTrack.BAR_HEIGHT);
        }
      }

      y += SegPlotTrack.BAR_HEIGHT;
    }
  }
}
