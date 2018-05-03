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
package edu.columbia.rdf.htsview.app.tracks.conservation;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;

/**
 * The Class Conservation46WayCanvasLayer.
 */
public class Conservation46WayCanvasLayer extends ConservationCanvasLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new conservation 46 way canvas layer.
   *
   * @param assembly the assembly
   */
  public Conservation46WayCanvasLayer(ConservationAssembly assembly) {
    super(Conservation46WayPlotTrack.TITLE, assembly, 100);
  }
}
