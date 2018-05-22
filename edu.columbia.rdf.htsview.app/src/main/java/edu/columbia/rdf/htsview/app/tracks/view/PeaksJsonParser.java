/**
 * Copyright 2017 Antony Holmes
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
package edu.columbia.rdf.htsview.app.tracks.view;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.Bed;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.json.Json;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.app.tracks.WebAssemblyService;
import edu.columbia.rdf.htsview.app.tracks.peaks.PeakSet;
import edu.columbia.rdf.htsview.app.tracks.peaks.PeaksPlotTrack;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.view.TrackJsonParser;

/**
 * The Class PeaksJsonParser.
 */
public class PeaksJsonParser extends TrackJsonParser {

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.view.TrackJsonParser#parse(org.abh.common.
   * ui. window.ModernWindow, java.lang.String, int,
   * org.abh.common.ui.tree.ModernTree, org.abh.common.json.Json,
   * org.abh.common.tree.TreeNode)
   */
  @Override
  public boolean parse(ModernWindow window,
      final String name,
      int id,
      String genome,
      ModernTree<Track> annotationTree,
      final Json trackJson,
      TreeNode<Track> rootNode) throws IOException {
    Color color = null;

    if (trackJson.containsKey("color")) {
      color = trackJson.getColor("color");
    } else {
      color = Color.BLUE;
    }

    int peaksId = trackJson.getInt("peak-id");

    PeakSet peaks = PeakSet.createPeaks(peaksId, name);

    List<GenomicRegion> locations = WebAssemblyService.getInstance()
        .getPeakAssembly().downloadJsonPeaks(genome, id, peaks);

    Bed bed = Bed.create(peaks.getName(), locations);
    bed.setColor(color);

    rootNode.addChild(new TreeNode<Track>(peaks.getName(),
        new PeaksPlotTrack(id, peaks, bed)));

    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Peaks";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.view.TrackJsonParser#getType()
   */
  @Override
  public String getType() {
    return "peaks";
  }
}
