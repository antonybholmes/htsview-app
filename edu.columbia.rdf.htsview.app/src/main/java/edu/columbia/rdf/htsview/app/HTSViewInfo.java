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

import org.jebtk.core.AppVersion;
import org.jebtk.modern.help.GuiAppInfo;

import edu.columbia.rdf.htsview.app.icons.HTSViewIcon;

/**
 * The class ReadsInfo.
 */
public class HTSViewInfo extends GuiAppInfo {

  // public static final Path RES_DIR = PathUtils.getPath("res/modules/dna");

  /**
   * Instantiates a new reads info.
   */
  public HTSViewInfo() {
    super("HTS View", new AppVersion(15),
        "Copyright (C) 2014-${year} Antony Holmes",
        new HTSViewIcon(),
        "View read track data at multiple resolutions.");
  }

}
