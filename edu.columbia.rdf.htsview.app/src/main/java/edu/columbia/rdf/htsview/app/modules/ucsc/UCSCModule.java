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
package edu.columbia.rdf.htsview.app.modules.ucsc;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.core.http.URLUtils;
import org.jebtk.core.http.URLPath;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.text.Join;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.edb.EDBWLoginService;
import edu.columbia.rdf.edb.Sample;
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
public class UCSCModule extends HTSViewModule implements ModernClickListener {
  private static final Logger LOG = LoggerFactory.getLogger(UCSCModule.class);

  /**
   * The member convert button.
   */
  private ModernButton mTracksButton = new RibbonLargeButton("UCSC",
      AssetService.getInstance().loadIcon(GBIcon.class, 24));

  private static final URLPath BASE_URL = URLPath.fromUrl(
      SettingsService.getInstance().getUrl("htsview.ucsc.tracks.base-url"));

  /**
   * The member window.
   */
  private MainHtsViewWindow mWindow;

  private TracksURL mUrl;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "UCSC Tracks";
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
    mTracksButton.setToolTip("Tracks",
        "Look at tracks in UCSC Genome Browser.");
    mWindow.getRibbon().getToolbar("Tools").getSection("UCSC")
        .add(mTracksButton);

    mTracksButton.addClickListener(this);

    if (EDBWLoginService.getInstance().getLogin() != null) {
      mUrl = new TracksURL(EDBWLoginService.getInstance().getLogin());
    }
  }

  @Override
  public void clicked(ModernClickEvent e) {
    try {
      tracks();
    } catch (URISyntaxException | IOException e1) {
      e1.printStackTrace();
    }
  }

  private void tracks() throws URISyntaxException, IOException {
    URLPath url = mUrl;

    Join join = Join.onColon();

    for (Track track : mWindow.getTracksPanel().getSelectedTracks()) {
      if (track instanceof SamplePlotTrack) {
        Sample sample = ((SamplePlotTrack) track).getSample();

        url = url.param("id",
            join.values(sample.getId(), colorToUCSCColor(track.getFillColor()))
                .toString());
      }
    }

    System.err.println(url);

    Genome genome = mWindow.getGenomeModel().get();

    URLPath dispUrl = BASE_URL.param("org", genome.getName())
        .param("db", genome.getAssembly())
        .param("position", mWindow.getGenomicModel().get().getLocation()) // =chr3:187439165-187454285")
        .param("hgt.customText", url.toString());

    System.err.println(dispUrl);

    URLUtils.launch(dispUrl);

    /*
     * List<URL> urls = UCSCTrackService.getInstance().getURLs(samples);
     * 
     * if (urls.size() == 0) { ModernMessageDialog.createWarningDialog(mWindow,
     * "There are no samples available.");
     * 
     * return; }
     * 
     * for (URL url : urls) { UrlBuilder dispUrl = BASE_URL .param("org=human")
     * .param("db=hg19") .param("position=chr1:1-10000")
     * .param("hgt.customText", url);
     * 
     * URLUtils.launch(dispUrl); }
     */
  }

  private String colorToUCSCColor(Color c) {
    return Join.onComma().values(c.getRed(), c.getGreen(), c.getBlue())
        .toString();
  }
}
