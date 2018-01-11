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

import edu.columbia.rdf.htsview.app.tracks.genes.GenesPlotTrack;

// TODO: Auto-generated Javadoc
/**
 * The Class VegaGenesPlotTrack.
 */
public class VegaGenesPlotTrack extends GenesPlotTrack {

  /**
   * Instantiates a new vega genes plot track.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public VegaGenesPlotTrack() throws IOException {
    super("Vega genes", "vega");
  }

  /*
   * @Override public TrackSubFigure getGraph(String genome, TitleProperties
   * titlePosition) throws IOException {
   * 
   * if (mGenes == null) { mGenes = new
   * Genes(Resources.getResGzipReader("res/rdf_ucsc_vega_genes_hg19.txt.gz")); }
   * 
   * return super.getGraph(genome, titlePosition); }
   */

}
