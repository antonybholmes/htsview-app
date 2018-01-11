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

import java.io.IOException;

import org.jebtk.bioinformatics.genomic.GenomeAssembly;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;

import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class DnaColorPlotTrack.
 */
public class DnaColorPlotTrack extends DnaPlotTrack {

  /** The m sub figure. */
  private DnaColorPlotCanvas mSubFigure;

  /**
   * Instantiates a new dna color plot track.
   *
   * @param genomeAssembly the genome assembly
   */
  public DnaColorPlotTrack(GenomeAssembly genomeAssembly) {
    super("DNA Color", genomeAssembly);
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

    mSubFigure = DnaColorPlotCanvas
        .create(genome, mGenomeAssembly, titlePosition);

    switch (titlePosition.getPosition()) {
    case RIGHT:
    case COMPACT_RIGHT:
      setSmallMargins(getName(), mSubFigure);
      break;
    default:
      setStandardMargins(getName(), mSubFigure);
      break;
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

    // mPlot.setForwardCanvasEventsEnabled(false);
    mSubFigure.update(displayRegion, resolution, width, height, margin);
    // mPlot.setForwardCanvasEventsEnabled(true);

    return mSubFigure;
  }
}
