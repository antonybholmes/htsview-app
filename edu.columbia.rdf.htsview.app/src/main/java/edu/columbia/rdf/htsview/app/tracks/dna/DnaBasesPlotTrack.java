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

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.SequenceReader;
import org.jebtk.graphplot.figure.Axes;

import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * The Class DnaBasesPlotTrack.
 */
public class DnaBasesPlotTrack extends DnaPlotTrack {

  private static final long serialVersionUID = 1L;

  private boolean mColorMode = true;

  /**
   * Instantiates a new dna bases plot track.
   *
   * @param genomeAssembly the genome assembly
   */
  public DnaBasesPlotTrack(SequenceReader genomeAssembly) {
    super("DNA Bases", genomeAssembly);
  }

  public void setColorMode(boolean colorMode) {
    mColorMode = colorMode;
  }

  public boolean getColorMode() {
    return mColorMode;
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

    mSubFigure = DnaBasesPlotSubFigure
        .create(genome, mGenomeAssembly, titlePosition, mColorMode);

    setMargins(getName(), titlePosition, mSubFigure);

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
  public TrackSubFigure updateGraph(Genome genome,
      GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {
    mSubFigure.update(genome, displayRegion, resolution, width, height, margin);

    return mSubFigure;
  }
}
