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
import java.awt.Graphics2D;
import java.io.IOException;

import org.jebtk.bioinformatics.genomic.GenomeAssembly;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.RepeatMaskType;
import org.jebtk.bioinformatics.genomic.SequenceRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;

// TODO: Auto-generated Javadoc
/**
 * The Class DnaBasesCanvasLayer.
 */
public class DnaBasesCanvasLayer extends AxesClippedLayer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	
	/** The m display region. */
	private GenomicRegion mDisplayRegion;

	/** The m assembly. */
	private GenomeAssembly mAssembly;



	/** The m genome. */
	private String mGenome;

	/**
	 * Instantiates a new dna bases canvas layer.
	 *
	 * @param genome the genome
	 * @param assembly the assembly
	 */
	public DnaBasesCanvasLayer(String genome, GenomeAssembly assembly) {
		super("DNA Bases");

		mGenome = genome;
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


		int y = g2.getFontMetrics().getAscent() + 
				g2.getFontMetrics().getDescent();
	
		g2.setColor(Color.BLACK);
		
		try {
			SequenceRegion sequence =
					mAssembly.getSequence(mGenome, mDisplayRegion, RepeatMaskType.N);

			int start = mDisplayRegion.getStart();
	
			int x1 = axes.toPlotX1(mDisplayRegion.getStart());
			int w;
			
			for (char c : sequence.getSequence().toArray())	{
				String s = Character.toString(c);
				
				x1 = axes.toPlotX1(start);
				w = axes.toPlotX1(start + 1) - x1;
				x1 += (w - g2.getFontMetrics().stringWidth(s)) / 2;
				
				g2.drawString(s, x1, y);
				
				++start;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
