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
package edu.columbia.rdf.htsview.app.tracks.dna;

import java.awt.Color;

import org.jebtk.bioinformatics.genomic.GenomeAssembly;
import edu.columbia.rdf.htsview.tracks.AnnotationPlotTrack;

// TODO: Auto-generated Javadoc
/**
 * The Class DnaPlotTrack.
 */
public abstract class DnaPlotTrack extends AnnotationPlotTrack {

  private static final long serialVersionUID = 1L;

  /** The m genome assembly. */
  protected GenomeAssembly mGenomeAssembly;

  /** The Constant MAX_DISPLAY_BASES. */
  public static final int MAX_DISPLAY_BASES = 100;

  public static final int MAX_DISPLAY_COLOR_BASES = 1000;

  /**
   * Instantiates a new dna plot track.
   *
   * @param name
   *          the name
   * @param genomeAssembly
   *          the genome assembly
   */
  public DnaPlotTrack(String name, GenomeAssembly genomeAssembly) {
    super(name);

    mGenomeAssembly = genomeAssembly;
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
