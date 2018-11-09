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
package edu.columbia.rdf.htsview.app.tracks.ext.ucsc;

import java.io.IOException;

import edu.columbia.rdf.htsview.app.tracks.genes.GenesPlotTrack;

/**
 * The Class UcscGenesPlotTrack.
 */
public class UcscGenesPlotTrack extends GenesPlotTrack {

  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new ucsc genes plot track.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public UcscGenesPlotTrack() throws IOException {
    super("UCSC genes", "ucsc");
  }

  /*
   * @Override public TrackSubFigure getGraph(Genome genome, TitleProperties
   * titlePosition) throws IOException {
   * 
   * if (mGenes == null) { mGenes = new
   * Genes(Resources.getResGzipReader("res/rdf_ucsc_ucsc_genes_hg19.txt.gz")); }
   * 
   * return super.getGraph(genome, titlePosition); }
   */
}
