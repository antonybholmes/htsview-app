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
package edu.columbia.rdf.htsview.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;

import org.jebtk.core.settings.SettingsService;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.text.ModernDialogHeadingLabel;
import org.jebtk.modern.widget.ModernTwoStateWidget;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.app.tracks.peaks.PeakAssembly;
import edu.columbia.rdf.htsview.app.tracks.peaks.PeakSet;

// TODO: Auto-generated Javadoc
/**
 * The class BRTDialog.
 */
public class SampleDialog extends ModernDialogTaskWindow implements ModernClickListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The m check sample.
	 */
	private CheckBox mCheckSample = new ModernCheckSwitch("Sample", true);

	/**
	 * The m check reads.
	 */
	private CheckBox mCheckReads = new ModernCheckSwitch("Reads");

	/** The m read support. */
	private boolean mReadSupport;

	/** The m peak assembly. */
	private PeakAssembly mPeakAssembly;

	/** The m sample. */
	private Sample mSample;

	/** The m peaks map. */
	private Map<String, ModernTwoStateWidget> mPeaksMap =
			new HashMap<String, ModernTwoStateWidget>();

	/**
	 * Instantiates a new BRT dialog.
	 *
	 * @param parent 	The parent.
	 * @param sample The sample.
	 * @param isBrt 	Contains both read and signal data.
	 * @param peakAssembly the peak assembly
	 */
	public SampleDialog(ModernWindow parent, 
			final Sample sample,	
			boolean isBrt,
			PeakAssembly peakAssembly) {
		super(parent);

		mSample = sample;
		mReadSupport = isBrt;
		mPeakAssembly = peakAssembly; //WebAssemblyService.getInstance().getPeakAssembly();

		setTitle(sample.getName() + " Display Options");

		try {
			createUi();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mCheckSample.setSelected(SettingsService.getInstance().getAsBool("edb.reads.tracks.sample-plot.brt.show-sample"));
		mCheckReads.setSelected(SettingsService.getInstance().getAsBool("edb.reads.tracks.sample-plot.brt.show-reads"));

		

		UI.centerWindowToScreen(this);
	}

	/**
	 * Creates the ui.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final void createUi() throws IOException {

		ModernComponent content = new ModernComponent();
		
		

		if (mReadSupport) {
			Box box = VBox.create();
			
			//sectionHeader("Data", box);

			box.add(mCheckSample);
			box.add(UI.createVGap(5));
			box.add(mCheckReads);
			box.add(UI.createVGap(20));
			
			content.setHeader(box);
		}
		
		setSize(500, 200);

		if (mPeakAssembly != null) {
			List<PeakSet> samplePeaks = mPeakAssembly.getJsonPeaks(mSample);

			if (samplePeaks.size() > 0) {

				ModernComponent c2 = new ModernComponent();
				
				c2.setHeader(new ModernDialogHeadingLabel("Peaks", 
						BorderService.getInstance().createBottomBorder(5)));
				
				Box box = Box.createVerticalBox();
				
				// List all of the available peaks
				for (PeakSet peaks : samplePeaks) {
					ModernTwoStateWidget checkPeaks = new ModernCheckSwitch(peaks.getName());

					mPeaksMap.put(peaks.getName(), checkPeaks);

					box.add(checkPeaks);
					box.add(UI.createVGap(5));
				}
				
				c2.setBody(new ModernScrollPane(box).setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER));
				
				content.setBody(c2);
				
				setSize(500, 300);
			}
		}

		setDialogCardContent(content);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.ui.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		if (e.getSource().equals(mOkButton)) {

			SettingsService.getInstance().update("edb.reads.tracks.sample-plot.brt.show-sample", 
					mCheckSample.isSelected());
			SettingsService.getInstance().update("edb.reads.tracks.sample-plot.brt.show-reads", 
					mCheckReads.isSelected());

			setStatus(ModernDialogStatus.OK);
		}

		super.clicked(e);
	}

	/**
	 * Gets the show sample.
	 *
	 * @return the show sample
	 */
	public boolean getShowSample() {
		return mCheckSample.isSelected();
	}

	/**
	 * Gets the show reads.
	 *
	 * @return the show reads
	 */
	public boolean getShowReads() {
		return mCheckReads.isSelected();
	}

	/**
	 * Return the list of selected peaks.
	 *
	 * @return the show peaks
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<PeakSet> getShowPeaks() throws IOException {
		List<PeakSet> ret = new ArrayList<PeakSet>();

		if (mPeakAssembly != null) {
			List<PeakSet> samplePeaks = mPeakAssembly.getJsonPeaks(mSample);

			for (PeakSet peaks : samplePeaks) {
				if (mPeaksMap.get(peaks.getName()).isSelected()) {
					ret.add(peaks);
				}
			}
		}

		return ret;
	}
}
