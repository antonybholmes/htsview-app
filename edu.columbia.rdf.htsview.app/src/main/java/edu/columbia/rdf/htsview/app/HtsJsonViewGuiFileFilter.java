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

import org.jebtk.modern.io.GuiFileExtFilter;

/**
 * The class ReadsJsonViewGuiFileFilter.
 */
public class HtsJsonViewGuiFileFilter extends GuiFileExtFilter {

  /**
   * The constant EXT.
   */
  public static final String EXT = "htsj";

  /**
   * Instantiates a new reads json view gui file filter.
   */
  public HtsJsonViewGuiFileFilter() {
    super(EXT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.filechooser.FileFilter#getDescription()
   */
  @Override
  public String getDescription() {
    return "HTS Track View (*." + EXT + ")";
  }

}
