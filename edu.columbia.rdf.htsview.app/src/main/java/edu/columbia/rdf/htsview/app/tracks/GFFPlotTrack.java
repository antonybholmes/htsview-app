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
package edu.columbia.rdf.htsview.app.tracks;

import java.io.IOException;
import java.nio.file.Path;

import org.jebtk.bioinformatics.genomic.Genes;
import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.core.io.PathUtils;

import edu.columbia.rdf.htsview.app.tracks.genes.GenesPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * The Class GFFPlotTrack.
 */
public class GFFPlotTrack extends GenesPlotTrack {

  private static final long serialVersionUID = 1L;
  
  
  /** The m file. */
  private Path mFile;

  /**
   * Instantiates a new GFF plot track.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public GFFPlotTrack(Path file) throws IOException {
    super(PathUtils.getName(file), PathUtils.getName(file));

    mFile = file;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.htsview.tracks.genes.GenesPlotTrack#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(String genome,
      TitleProperties titlePosition) throws IOException {

    if (GenesService.getInstance().getGenes(genome, mDb) == null) {
      GenesService.getInstance()
          .put(genome, mDb, Genes.gff3Parser().parse(mFile, mDb, genome));
    }

    return super.createGraph(genome, titlePosition);
  }
}
