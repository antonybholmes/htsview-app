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
package edu.columbia.rdf.htsview.app.tracks.mouse;

import java.io.IOException;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;

import edu.columbia.rdf.htsview.app.tracks.conservation.ConservationPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * The Class MouseConservationPlotTrack.
 */
public class MouseConservationPlotTrack extends ConservationPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** The title. */
  public static String TITLE = "Mouse Conservation";

  /**
   * Instantiates a new mouse conservation plot track.
   *
   * @param conservationAssembly the conservation assembly
   */
  public MouseConservationPlotTrack(ConservationAssembly conservationAssembly) {
    super(TITLE, conservationAssembly);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(Genome genome,
      TitleProperties titlePosition) throws IOException {

    //
    // Display some genes
    //

    mSubFigure = MouseConservationPlotCanvas.create(mConservationAssembly,
        titlePosition);

    setMargins(getName(), titlePosition, mSubFigure);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#updateGraph(org.jebtk.bioinformatics.
   * genome.GenomicRegion, int, int, int, int)
   */
  @Override
  public TrackSubFigure updateGraph(GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {
    // mSubFigure.setForwardCanvasEventsEnabled(false);
    mSubFigure.update(displayRegion, resolution, width, height, margin);
    // mSubFigure.setForwardCanvasEventsEnabled(true);

    return mSubFigure;
  }
}
