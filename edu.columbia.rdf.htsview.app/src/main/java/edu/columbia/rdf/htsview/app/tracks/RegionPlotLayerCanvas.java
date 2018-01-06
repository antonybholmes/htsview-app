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
package edu.columbia.rdf.htsview.app.tracks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraphGroupModel;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;

// TODO: Auto-generated Javadoc
/**
 * The Class RegionPlotLayerCanvas.
 *
 * @param <T>
 *          the generic type
 */
public class RegionPlotLayerCanvas<T extends GenomicRegion> extends AxesClippedLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  // private static final int BAR_HEIGHT = 20;

  /** The m bed graph group. */
  protected BedGraphGroupModel mBedGraphGroup;

  /** The m regions. */
  private List<T> mRegions;

  /** The m color. */
  private Color mColor;

  // private GenomicRegionsModel mGenomicModel;

  // private GenomicRegion mDisplayRegion;

  /**
   * Instantiates a new region plot layer canvas.
   *
   * @param color
   *          the color
   */
  public RegionPlotLayerCanvas(Color color) {
    mColor = color;
  }

  /**
   * Update.
   *
   * @param regions
   *          the regions
   */
  public void update(List<T> regions) {
    mRegions = regions;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.AxesClippedLayer#plotLayer(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure,
   * org.graphplot.figure.Axes)
   */
  @Override
  public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {

    if (CollectionUtils.isNullOrEmpty(mRegions)) {
      return;
    }

    // int minX =
    // space.toPlotX(space.getGraphProperties().getXAxisProperties().getMin());
    // int maxX =
    // space.toPlotX(space.getGraphProperties().getXAxisProperties().getMax());

    int x1;
    int x2;
    // int px1;
    // int px2;
    int y1 = 0;
    int h = axes.getInternalSize().getH();
    int w;

    g2.setColor(mColor);

    for (GenomicRegion region : mRegions) {
      // if
      // (!space.getGraphProperties().getXAxisProperties().withinBounds(region.getStart())
      // &&
      // !space.getGraphProperties().getXAxisProperties().withinBounds(region.getEnd()))
      // {
      // continue;
      // }

      x1 = axes.toPlotX1(region.getStart());
      x2 = axes.toPlotX1(region.getEnd());

      // px1 = Mathematics.bound(x1, minX, maxX);
      // px2 = Mathematics.bound(x2, minX, maxX);

      // Every feature must be one pixel wide so it can be seen
      w = Math.max(1, x2 - x1);

      g2.fillRect(x1, y1, w, h);
    }
  }
}
