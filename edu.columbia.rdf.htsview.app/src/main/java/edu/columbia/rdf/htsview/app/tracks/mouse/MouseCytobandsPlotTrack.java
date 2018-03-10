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

import java.awt.Color;
import java.io.IOException;

import org.jebtk.bioinformatics.ext.ucsc.CytobandsService;
import org.jebtk.bioinformatics.genomic.SequenceReader;
import org.jebtk.bioinformatics.genomic.GenomicRegion;

import edu.columbia.rdf.htsview.app.tracks.dna.CytobandsSubFigure;
import edu.columbia.rdf.htsview.tracks.AnnotationPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class MouseCytobandsPlotTrack.
 */
public class MouseCytobandsPlotTrack extends AnnotationPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new mouse cytobands plot track.
   */
  public MouseCytobandsPlotTrack() {
    super("Mouse Cytobands");
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getFillColor()
   */
  @Override
  public Color getFillColor() {
    return Color.GRAY;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(String genome,
      TitleProperties titlePosition) throws IOException {

    //
    // Display some genes
    //

    mSubFigure = CytobandsSubFigure.create("Cytobands mm10",
        CytobandsService.getInstance().getCytobands(SequenceReader.MM10),
        titlePosition);

    switch (titlePosition.getPosition()) {
    case RIGHT:
    case COMPACT_RIGHT:
      mSubFigure.currentAxes().setMargins(SMALL_MARGIN,
          MARGINS.getLeft(),
          SMALL_MARGIN,
          HUGE_MARGIN);
      break;
    default:
      mSubFigure.currentAxes().setMargins(MARGINS);
    }

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
