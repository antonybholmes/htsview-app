/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.htsview.app.modules.counts;

import java.util.ArrayList;
import java.util.List;

import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.tooltip.ModernToolTip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.htsview.app.MainHtsViewWindow;
import edu.columbia.rdf.htsview.app.modules.HTSViewModule;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;

/**
 * Merges designated segments together using the merge column. Consecutive rows
 * with the same merge id will be merged together. Coordinates and copy number
 * will be adjusted but genes, cytobands etc are not.
 *
 * @author Antony Holmes
 *
 */
public class CountsModule extends HTSViewModule implements ModernClickListener {
  private static final Logger LOG = LoggerFactory.getLogger(CountsModule.class);

  /**
   * The member convert button.
   */
  private ModernButton mCountsButton = new RibbonLargeButton("Counts",
      AssetService.getInstance().loadIcon("read_dist", 32),
      AssetService.getInstance().loadIcon("read_dist", 24));

  /**
   * The member window.
   */
  private MainHtsViewWindow mWindow;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Counts";
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainHtsViewWindow window) {
    mWindow = window;

    // home
    mCountsButton.setToolTip(
        new ModernToolTip("Fill Gaps", "Fill gaps using reference."));
    mCountsButton.setClickMessage("Fill Gaps");
    mWindow.getRibbon().getToolbar("Tools").getSection("Fill Gaps")
        .add(mCountsButton);

    mCountsButton.addClickListener(this);
  }

  @Override
  public void clicked(ModernClickEvent e) {
    counts();
  }

  private void counts() {
    List<SamplePlotTrack> sampleTracks = new ArrayList<SamplePlotTrack>();

    for (Track track : mWindow.getTracksPanel().getSelectedTracks()) {
      if (track instanceof SamplePlotTrack) {
        sampleTracks.add((SamplePlotTrack) track);
      }
    }

    if (sampleTracks.size() == 0) {
      ModernMessageDialog.createWarningDialog(mWindow,
          "You must select a sample.");
      return;
    }

    CountsDialog dialog = new CountsDialog(mWindow, mWindow.getGenomeModel(),
        sampleTracks);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    ModernDialogStatus status = ModernMessageDialog.createOkCancelInfoDialog(
        mWindow,
        "Generating counts may take several minutes.");

    if (status == ModernDialogStatus.CANCEL) {
      return;
    }

    CountTask task = new CountTask(sampleTracks, 
        mWindow.getGenomeModel().get(), 
        dialog.getRegions(),
        dialog.getNorm());

    task.doInBackground();
  }
}
