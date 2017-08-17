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
import java.awt.Graphics2D;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.colormap.ColorMap;

import edu.columbia.rdf.htsview.app.tracks.dna.DnaPlotTrack;

// TODO: Auto-generated Javadoc
/**
 * The Class ConservationCanvasLayer.
 */
public class ConservationCanvasLayer extends AxesClippedLayer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The m display region. */
	private GenomicRegion mDisplayRegion;

	/** The m assembly. */
	private ConservationAssembly mAssembly;

	/** The m max. */
	private double mMax;
	
	/** The Constant COLOR_MAP. */
	private static final ColorMap COLOR_MAP = 
			ColorMap.createGrayMap();

	/**
	 * Instantiates a new conservation canvas layer.
	 *
	 * @param title the title
	 * @param assembly the assembly
	 * @param max the max
	 */
	public ConservationCanvasLayer(String title,
			ConservationAssembly assembly,
			double max) {
		super(title);

		mAssembly = assembly;
		mMax = max;
	}
	
	/**
	 * Update.
	 *
	 * @param displayRegion the display region
	 */
	public void update(GenomicRegion displayRegion) {
		mDisplayRegion = displayRegion;
	}

	/* (non-Javadoc)
	 * @see org.graphplot.figure.AxesClippedLayer#plotClipped(java.awt.Graphics2D, org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure, org.graphplot.figure.Axes)
	 */
	@Override
	public void plotClipped(Graphics2D g2,
			DrawingContext context,
			SubFigure figure,
			Axes axes) {
		
		// So that we don't attempt to pull a whole chromosome
		if (mDisplayRegion.getLength() > DnaPlotTrack.MAX_DISPLAY_BASES) {
			return;
		}

		int y = 0;
		int h = axes.getInternalPlotSize().getH();

		try {
			List<Double> scores = mAssembly.getScores(mDisplayRegion);

			int start = mDisplayRegion.getStart();
			int x1 = 0;
			int w;

			double s;
			
			g2.setColor(Color.BLACK);

			for (double score : scores)	{
				
				s = 100 * score / mMax;
				
				g2.setColor(COLOR_MAP.getColorByIndex((int)s));
				
				x1 = axes.toPlotX1(start);
				w = axes.toPlotX1(start + 1) - x1;
				
				g2.fillRect(x1, y, w, h);

				++start;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
