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
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.io.Temp;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.math.matrix.AnnotatableMatrix;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.status.StatusService;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.xml.sax.SAXException;

import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;
import edu.columbia.rdf.matcalc.MainMatCalc;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.OpenMode;
import edu.columbia.rdf.matcalc.bio.BioModuleLoader;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadDistTask.
 */
public class CountTask extends SwingWorker<Void, Void> {

	/** The m locations. */
	private List<GenomicRegion> mLocations;
	
	/** The m parent. */
	private ModernRibbonWindow mParent;

	/** The m tracks. */
	private List<SamplePlotTrack> mTracks;
	
	/** The m file. */
	private Path mFile;
	
	/** The m name. */
	private String mName;

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
	public CountTask(ModernRibbonWindow parent,
			String name,
			List<SamplePlotTrack> tracks,
			List<GenomicRegion> regions) {
		mParent = parent;
		mName = name;
		mTracks = tracks;
		mLocations = regions;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	public Void doInBackground() {
		StatusService.getInstance().setStatus("Compiling peaks...");

		MainMatCalcWindow window;

		try {
			mFile = Temp.generateTempFile("txt");

			createCountsFile();

			window = MainMatCalc.main(new BioModuleLoader()); 

			window.openFile(mFile).rowAnnotations(1).autoOpen();
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
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void createCountsFile() throws IOException {
		AnnotationMatrix matrix = AnnotatableMatrix.createNumericalMatrix(mLocations.size(), mTracks.size());
		
		for (int i = 0; i < mTracks.size(); ++i) {
			matrix.setColumnName(i, mTracks.get(i).getName());
		}
		
		for (int i = 0; i < mLocations.size(); ++i) {
			matrix.setRowName(i, mLocations.get(i).getLocation());
		}
		
		for (int i = 0; i < mTracks.size(); ++i) {
			SamplePlotTrack track = mTracks.get(i);
			
			for (int j = 0; j < mLocations.size(); ++j) {
				GenomicRegion r = mLocations.get(j);
				
				int counts = getCounts(track, r);

				matrix.set(i, j, counts);
			}
		}

		System.err.println("Writing to " + mFile);
		
		AnnotationMatrix.writeAnnotationMatrix(matrix, mFile);
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
