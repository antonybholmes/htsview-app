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
import java.awt.Graphics2D;
import java.awt.Point;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.geom.Point2DDouble;
import org.jebtk.core.text.Formatter;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotSeriesLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.UniqueXY;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.graphics.CanvasMouseEvent;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ModernCanvasMouseListener;
import org.jebtk.modern.status.StatusService;
import org.jebtk.modern.theme.ModernTheme;


// TODO: Auto-generated Javadoc
/**
 * The Class MouseHighlightPeakPlotLayer.
 */
public class MouseHighlightPeakPlotLayer extends PlotSeriesLayer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant COLOR. */
	private static final Color COLOR = 
			ColorUtils.getTransparentColor50(Color.BLACK);
	
	/** The Constant SNAP_SIZE. */
	private static final int SNAP_SIZE = 5;
	
	/**
	 * The Class CanvasEvents.
	 */
	private class CanvasEvents implements ModernCanvasMouseListener {
		
		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseClicked(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMouseClicked(CanvasMouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseEntered(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMouseEntered(CanvasMouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseExited(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMouseExited(CanvasMouseEvent e) {
			reset();
		}

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMousePressed(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMousePressed(CanvasMouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseReleased(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMouseReleased(CanvasMouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseDragged(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMouseDragged(CanvasMouseEvent e) {
			reset();
		}

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseMoved(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMouseMoved(CanvasMouseEvent e) {
			if (mXy == null) {
				return;
			}
			
			int x = e.getScaledPos().getX();
			
			Point p = mXy.closestX(x);
			
			// In cases where there is no data in the region, skip trying
			// to draw the crosshairs
			if (p != null) {
				if (Math.abs(x - p.x) <= SNAP_SIZE) {
					mPoint = p;
				
					fireCanvasRedraw();
				}
			}
		}
		
	}

	/** The m xy. */
	private UniqueXY mXy;
	
	/** The m M. */
	private AnnotationMatrix mM;
	
	/** The m point. */
	private Point mPoint = null;
	
	/**
	 * Instantiates a new mouse highlight peak plot layer.
	 *
	 * @param series the series
	 */
	public MouseHighlightPeakPlotLayer(String series) {
		super("Mouse Highlight Peaks", series);
		
		addCanvasMouseListener(new CanvasEvents());
	}

	/* (non-Javadoc)
	 * @see org.graphplot.figure.PlotSeriesLayer#plotClipped(java.awt.Graphics2D, org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure, org.graphplot.figure.Axes, org.graphplot.figure.Plot, org.abh.common.math.matrix.AnnotationMatrix, org.graphplot.figure.series.XYSeries)
	 */
	@Override
	public void plotClipped(Graphics2D g2,
			DrawingContext context,
			SubFigure figure,
			Axes axes,
			Plot plot,
			AnnotationMatrix m,
			XYSeries series) {
		if (context == DrawingContext.PRINT) {
			return;
		}		

		//System.err.println("mouse m " + m.getRowCount() + " " + m.getColumnNames() + " " + series.getName() + series.iterator().next());
		
		if (mXy == null || !mM.equals(m)) {
			mM = m;
			
			mXy = new UniqueXY(m, series, axes);
		}

		if (mPoint != null) {
			g2.setColor(COLOR);
			g2.setStroke(ModernTheme.DOTTED_LINE_STROKE);
			
			g2.drawLine(0, 
					mPoint.y, 
					axes.getInternalPlotSize().getW(), 
					mPoint.y);
			
			g2.drawLine(mPoint.x,
					0, 
					mPoint.x, 
					axes.getInternalPlotSize().getH());
			
			Point2DDouble p = mXy.original(mPoint.x);
			
			if (p != null) {
				//String s = "(" + p.getX() + "," + p.getY() + ")";
			
				//g2.drawString(s, mPoint.x - g2.getFontMetrics().stringWidth(s), mPoint.y);
				
				StatusService.getInstance().setStatus("x:" + Formatter.number().format((int)p.getX()) + ", y:" + Formatter.number().format(p.getY()));
			}
		}
	}
	
	/**
	 * Reset.
	 */
	private void reset() {
		if (mPoint != null) {
			StatusService.getInstance().setReady();
		
			mPoint = null;
		
			fireCanvasRedraw();
		}
	}
}
