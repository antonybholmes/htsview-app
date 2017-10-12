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
package edu.columbia.rdf.htsview.app.modules.counts;

import java.awt.FontFormatException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.UnsupportedLookAndFeelException;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.MatrixNormalization;
import org.jebtk.core.io.Temp;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.status.StatusService;

import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;
import edu.columbia.rdf.matcalc.MainMatCalc;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.bio.BioModuleLoader;

/**
 * The Class ReadDistTask.
 */
public class CountTask extends SwingWorker<Void, Void> {

	/** The m locations. */
	private List<GenomicRegion> mLocations;

	/** The m tracks. */
	private List<SamplePlotTrack> mTracks;

	private NormalizationMethod mNorm = NormalizationMethod.TPM;

	/**
	 * Instantiates a new read dist task.
	 *
	 * @param parent the parent
	 * @param name the name
	 * @param tracks the tracks
	 * @param regions the regions
	 * @param padding the padding
	 * @param window the window
	 * @param average the average
	 */
	public CountTask(List<SamplePlotTrack> tracks,
			List<GenomicRegion> regions,
			NormalizationMethod norm) {
		mTracks = tracks;
		mLocations = regions;
		mNorm = norm;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	public Void doInBackground() {
		StatusService.getInstance().setStatus("Compiling peaks...");

		MainMatCalcWindow window;

		try {
			Path countFile = Temp.generateTempFile("txt");
			//Path normFile = Temp.generateTempFile("txt");

			DataFrame normMatrix = createCountsFile(countFile);

			window = MainMatCalc.main(new BioModuleLoader()); 

			window.openMatrix(normMatrix); //.openFile(normFile).rowAnnotations(1).autoOpen();
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | FontFormatException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	public void done() {

	}

	/**
	 * Creates the counts file.
	 * @return 
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private DataFrame createCountsFile(Path countsFile) throws IOException {
		DataFrame matrix = DataFrame.createNumericalMatrix(mLocations.size(), mTracks.size());

		for (int i = 0; i < mTracks.size(); ++i) {
			matrix.setColumnName(i, mTracks.get(i).getName());
		}

		for (int i = 0; i < mLocations.size(); ++i) {
			matrix.setRowName(i, mLocations.get(i).getLocation());
		}

		for (int i = 0; i < mLocations.size(); ++i) {
			GenomicRegion r = mLocations.get(i);

			for (int j = 0; j < mTracks.size(); ++j) {
				SamplePlotTrack track = mTracks.get(j);

				int counts = getCounts(track, r);

				matrix.set(i, j, counts);
			}
		}

		//System.err.println("Writing to " + countsFile);
		//DataFrame.writeDataFrame(matrix, countsFile);

		

		// Add the totals to matrix
		for (int i = 0; i < mTracks.size(); ++i) {
			SamplePlotTrack track = mTracks.get(i);

			int total = track.getAssembly().getMappedReads(track.getSample());
			
			matrix.setColumnAnnotation("total-reads", i, total);
		}

		matrix.setName("Counts");

		DataFrame normMatrix;

		// Normalize
		switch (mNorm) {
		case RPM:
			normMatrix = MatrixNormalization.rpm(matrix);
			break;
		case TPM:
			normMatrix = MatrixNormalization.tpm(matrix, mLocations);
			break;
		case RPKM:
			normMatrix = MatrixNormalization.rpkm(matrix, mLocations);
			break;
		case MEDIAN_RATIO:
			normMatrix = MatrixOperations.medianRatio(matrix);
			break;
		default:
			// No transformation
			normMatrix = matrix;
			break;
		}

		// Add annotations

		if (mNorm != NormalizationMethod.NONE) {
			for (int i = 0; i < mTracks.size(); ++i) {
				String name = mTracks.get(i).getName();

				if (mNorm != NormalizationMethod.NONE) {
					name += " " + mNorm;
				}

				normMatrix.setColumnName(i, name);
			}

			for (int i = 0; i < mLocations.size(); ++i) {
				normMatrix.setRowName(i, mLocations.get(i).getLocation());
			}
		}

		//System.err.println("Writing to " + normFile);

		//DataFrame.writeDataFrame(normMatrix, normFile);

		return normMatrix;
	}


	/**
	 * Get the counts and subtract the input if necessary.
	 *
	 * @param track the track
	 * @param ext the ext
	 * @param mWindow the m window
	 * @return the counts
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private int getCounts(SamplePlotTrack track,
			GenomicRegion r) throws IOException {
		return track.getAssembly().getStarts(track.getSample(), r, -1).size();
	}
}
