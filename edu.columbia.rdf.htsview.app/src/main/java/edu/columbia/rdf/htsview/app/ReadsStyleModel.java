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

import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.figure.Graph2dStyleModel;
import org.jebtk.graphplot.figure.PlotStyle;

// TODO: Auto-generated Javadoc
/**
 * The class ReadsStyleModel.
 */
public class ReadsStyleModel extends Graph2dStyleModel {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new reads style model.
   */
  public ReadsStyleModel() {
    set(PlotStyle.parse(SettingsService.getInstance().getAsString("edb.reads.peak-style")));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.model.ItemModel#set(java.lang.Object)
   */
  @Override
  public void set(PlotStyle peakStyle) {
    super.set(peakStyle);

    // Store the setting
    SettingsService.getInstance().update("edb.reads.peak-style", peakStyle.toString().toLowerCase());
  }
}
