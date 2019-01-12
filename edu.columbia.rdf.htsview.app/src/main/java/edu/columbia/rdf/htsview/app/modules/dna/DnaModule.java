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
package edu.columbia.rdf.htsview.app.modules.dna;

import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.tooltip.ModernToolTip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.htsview.app.MainHtsViewWindow;
import edu.columbia.rdf.htsview.app.modules.HTSViewModule;

/**
 * Merges designated segments together using the merge column. Consecutive rows
 * with the same merge id will be merged together. Coordinates and copy number
 * will be adjusted but genes, cytobands etc are not.
 *
 * @author Antony Holmes
 *
 */
public class DnaModule extends HTSViewModule implements ModernClickListener {
  private static final Logger LOG = LoggerFactory.getLogger(DnaModule.class);

  /**
   * The member convert button.
   */
  private ModernButton mDnaButton = new RibbonLargeButton("DNA",
      AssetService.getInstance().loadIcon("dna", 24));

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
    return "DNA";
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
    mDnaButton
        .setToolTip(new ModernToolTip("DNA", "Get DNA sequence for region."));
    mDnaButton.setClickMessage("DNA");
    mWindow.getRibbon().getToolbar("Tools").getSection("DNA").add(mDnaButton);

    mDnaButton.addClickListener(this);
  }

  @Override
  public void clicked(ModernClickEvent e) {
    dna();
  }

  private void dna() {
    DnaTask task = new DnaTask(mWindow, mWindow.getGenomeModel(),
        mWindow.getGenomicModel());

    task.doInBackground();
    task.done();
  }
}
