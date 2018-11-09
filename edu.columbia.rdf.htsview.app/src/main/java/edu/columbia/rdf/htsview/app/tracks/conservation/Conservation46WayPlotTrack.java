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

import java.io.IOException;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;

import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * The Class Conservation46WayPlotTrack.
 */
public class Conservation46WayPlotTrack extends ConservationPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  
  /** The Constant TITLE. */
  public static final String TITLE = "46-way Conservation";

  /**
   * Instantiates a new conservation 46 way plot track.
   *
   * @param conservationAssembly the conservation assembly
   */
  public Conservation46WayPlotTrack(ConservationAssembly conservationAssembly) {
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

    mSubFigure = Conservation46WayPlotCanvas.create(mConservationAssembly,
        titlePosition);

    switch (titlePosition.getPosition()) {
    case RIGHT:
    case COMPACT_RIGHT:
      int right = rightTitleWidth(getName());
      mSubFigure.currentAxes()
          .setMargins(SMALL_MARGIN, MARGINS.getLeft(), SMALL_MARGIN, right);
      break;
    default:
      mSubFigure.currentAxes().setMargins(MARGINS);
    }

    Axes.disableAllFeatures(mSubFigure.currentAxes());

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

    mSubFigure.update(displayRegion, resolution, width, height, margin);

    return mSubFigure;
  }
}
