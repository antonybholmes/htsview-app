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

import java.io.IOException;

import org.jebtk.bioinformatics.genomic.GenomeAssembly;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;
import org.jebtk.graphplot.figure.Axes;

// TODO: Auto-generated Javadoc
/**
 * The Class DnaBasesPlotTrack.
 */
public class DnaBasesPlotTrack extends DnaPlotTrack {


	/**
	 * Instantiates a new dna bases plot track.
	 *
	 * @param genomeAssembly the genome assembly
	 */
	public DnaBasesPlotTrack(GenomeAssembly genomeAssembly) {
		super("DNA Bases", genomeAssembly);
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String, edu.columbia.rdf.htsview.tracks.TitleProperties)
	 */
	@Override
	public TrackSubFigure createGraph(String genome,
			TitleProperties titlePosition) throws IOException {
		
		//
		// Display some genes
		//
		
		mSubFigure = DnaBasesPlotCanvas.create(genome,
				mGenomeAssembly,
				titlePosition);
		
		setMargins(getName(), titlePosition, mSubFigure);
		
		Axes.disableAllFeatures(mSubFigure.currentAxes());

		return mSubFigure;
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.Track#updateGraph(org.jebtk.bioinformatics.genome.GenomicRegion, int, int, int, int)
	 */
	@Override
	public TrackSubFigure updateGraph(GenomicRegion displayRegion, 
			int resolution,
			int width,
			int height,
			int margin) throws IOException {
		mSubFigure.update(displayRegion, resolution, width, height, margin);
		
		return mSubFigure;
	}
}
