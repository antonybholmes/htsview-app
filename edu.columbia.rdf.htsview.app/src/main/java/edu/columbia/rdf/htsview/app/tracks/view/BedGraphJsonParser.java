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
import java.nio.file.Path;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraph;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackTreeNode;
import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedGraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.view.TrackJsonParser;

/**
 * The Class BedGraphJsonParser.
 */
public class BedGraphJsonParser extends TrackJsonParser {

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
      Genome genome,
      ModernTree<Track> annotationTree,
      final Json trackJson,
      TreeNode<Track> rootNode) throws IOException {
    Color color = null;

    if (trackJson.containsKey("color")) {
      color = trackJson.getColor("color");
    } else {
      color = Color.LIGHT_GRAY;
    }

    Path file = getFile(trackJson);

    boolean validTrack = false;

    if (FileUtils.exists(file)) {
      try {
        List<UCSCTrack> bedGraphs = BedGraph.parse(file);

        for (UCSCTrack bedGraph : CollectionUtils.reverse(bedGraphs)) {
          bedGraph.setColor(color);

          TrackTreeNode child = new TrackTreeNode(
              new BedGraphPlotTrack(bedGraph, file));

          rootNode.addChild(child);
        }

        validTrack = true;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return validTrack;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "BedGraph";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.view.TrackJsonParser#getType()
   */
  @Override
  public String getType() {
    return "bedgraph";
  }
}
