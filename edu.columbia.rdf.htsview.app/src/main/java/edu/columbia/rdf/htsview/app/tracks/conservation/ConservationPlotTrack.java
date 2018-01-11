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

import java.awt.Color;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;

import edu.columbia.rdf.htsview.tracks.AnnotationPlotTrack;

// TODO: Auto-generated Javadoc
/**
 * The Class ConservationPlotTrack.
 */
public abstract class ConservationPlotTrack extends AnnotationPlotTrack {

  /** The m conservation assembly. */
  protected ConservationAssembly mConservationAssembly;

  /** The Constant MAX_DISPLAY_BASES. */
  protected static final int MAX_DISPLAY_BASES = 100000;

  /**
   * Instantiates a new conservation plot track.
   *
   * @param name the name
   * @param conservationAssembly the conservation assembly
   */
  public ConservationPlotTrack(String name,
      ConservationAssembly conservationAssembly) {
    super(name);

    mConservationAssembly = conservationAssembly;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getFillColor()
   */
  @Override
  public Color getFillColor() {
    return Color.BLACK;
  }
}
