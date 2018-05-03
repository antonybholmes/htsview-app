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
package edu.columbia.rdf.htsview.app.tracks;

import java.awt.Color;

import org.jebtk.core.settings.SettingsService;

import edu.columbia.rdf.htsview.app.tracks.genes.GeneProperties;

/**
 * The Class OtherGeneProperties.
 */
public class OtherGeneProperties extends GeneProperties {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant COLOR. */
  private static final Color COLOR = SettingsService.getInstance()
      .getAsColor("edb.reads.genes.color");

  /**
   * Instantiates a new other gene properties.
   */
  public OtherGeneProperties() {
    mLineStyle.setColor(COLOR);
    mExons.getLineStyle().setColor(COLOR);
    mExons.setFillColor(COLOR);
    mFont.setColor(COLOR);
  }
}
