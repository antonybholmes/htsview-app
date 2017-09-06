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

import org.jebtk.bioinformatics.genomic.GenomeAssembly;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import org.jebtk.graphplot.figure.PlotStyle;

// TODO: Auto-generated Javadoc
/**
 * The Class DnaRepeatMaskPlotCanvas.
 */
public class DnaRepeatMaskPlotCanvas extends DnaSubFigure {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The m layer. */
	private DnaRepeatMaskCanvasLayer mLayer;
	
	/**
	 * Instantiates a new dna repeat mask plot canvas.
	 *
	 * @param genome the genome
	 * @param genomeAssembly the genome assembly
	 * @param titlePosition the title position
	 */
	public DnaRepeatMaskPlotCanvas(String genome,
			GenomeAssembly genomeAssembly,
			TitleProperties titlePosition) {
		mLayer = new DnaRepeatMaskCanvasLayer(genome, genomeAssembly);
		
		currentAxes().putZ(mLayer);

		Track.setTitle(DnaRepeatMaskPlotTrack.TITLE, titlePosition, currentAxes());
	}

	/**
	 * Creates the.
	 *
	 * @param genome the genome
	 * @param genomeAssembly the genome assembly
	 * @param titlePosition the title position
	 * @return the dna repeat mask plot canvas
	 */
	public static DnaRepeatMaskPlotCanvas create(String genome,
			GenomeAssembly genomeAssembly,
			TitleProperties titlePosition) {

		DnaRepeatMaskPlotCanvas canvas = new DnaRepeatMaskPlotCanvas(genome,
				genomeAssembly,
				titlePosition);

		// set the graph limits
		canvas.currentAxes().getX1Axis().getTitle().setText(null);
		canvas.currentAxes().getX1Axis().startEndTicksOnly();

		canvas.currentAxes().getY1Axis().getTitle().setText(null);
		canvas.currentAxes().getY1Axis().startEndTicksOnly();

		canvas.currentAxes().setInternalSize(Track.SMALL_TRACK_SIZE);
		//canvas.getGraphSpace().getLayoutProperties().setMargins(MARGINS);

		
		return canvas;
	}
	
	/* (non-Javadoc)
	 * @see org.htsview.tracks.dna.DnaSubFigure#update(org.jebtk.bioinformatics.genome.GenomicRegion, int, double, int, int, int, java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
	 */
	@Override
	public void update(GenomicRegion displayRegion, 
			int resolution,
			double yMax,
			int width,
			int height,
			int margin,
			Color lineColor,
			Color fillColor,
			PlotStyle style) {
		super.update(displayRegion, resolution, yMax, width, height, margin, lineColor, fillColor, style);

		mLayer.update(displayRegion);
	}
}
