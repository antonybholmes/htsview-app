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
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;

import edu.columbia.rdf.htsview.app.tracks.dna.DnaPlotTrack;

// TODO: Auto-generated Javadoc
/**
 * The Class Conservation46WayGraphCanvasLayer.
 */
public class Conservation46WayGraphCanvasLayer extends AxesClippedLayer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The m display region. */
	private GenomicRegion mDisplayRegion;

	/** The m assembly. */
	private ConservationAssembly mAssembly;
	
	/**
	 * Instantiates a new conservation 46 way graph canvas layer.
	 *
	 * @param assembly the assembly
	 */
	public Conservation46WayGraphCanvasLayer(ConservationAssembly assembly) {
		mAssembly = assembly;
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
	 * @see org.graphplot.figure.AxesClippedLayer#plotLayer(java.awt.Graphics2D, org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure, org.graphplot.figure.Axes)
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

		
		int h = axes.getInternalSize().getH();
		int y1 = h;
		
		try {
			List<Double> scores = mAssembly.getScores(mDisplayRegion);

			int start = mDisplayRegion.getStart();
			int x1 = 0;
			int w;
			int y;

			g2.setColor(Color.BLACK);

			for (double score : scores)	{
				y = (int)(h * score / 100.0);
				
				x1 = axes.toPlotX1(start);
				w = Math.max(1, axes.toPlotX1(start + 1) - x1);
				
				g2.fillRect(x1, y1 - y, w, y);

				++start;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
