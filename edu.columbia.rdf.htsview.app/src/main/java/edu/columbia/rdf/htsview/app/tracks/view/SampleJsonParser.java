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

import org.jebtk.core.json.Json;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ui.Repository;
import edu.columbia.rdf.edb.ui.RepositoryService;
import edu.columbia.rdf.htsview.app.tracks.WebAssemblyService;
import edu.columbia.rdf.htsview.tracks.GraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackTreeNode;
import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;
import edu.columbia.rdf.htsview.tracks.view.TrackJsonParser;

/**
 * The Class SampleJsonParser.
 */
public class SampleJsonParser extends TrackJsonParser {

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
    Repository store = RepositoryService.getInstance().getCurrentRepository();

    Sample sample = null;

    try {
      sample = store.getSample(id);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (sample == null) {
      try {
        sample = store.getSample(name);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    boolean validTrack = false;

    if (sample != null) {
      Track track = null;

      Color lineColor = null;

      if (trackJson.containsKey("line-color")) {
        lineColor = trackJson.getAsColor("line-color");
      } else if (trackJson.containsKey("color")) {
        lineColor = trackJson.getAsColor("color");
      } else {
        lineColor = Color.GRAY;
      }

      Color fillColor = null;

      if (trackJson.containsKey("fill-color")) {
        fillColor = trackJson.getAsColor("fill-color");
      } else {
        fillColor = Color.LIGHT_GRAY;
      }

      int height = -1;

      if (trackJson.containsKey("height-px")) {
        height = trackJson.getAsInt("height-px");
      } else if (trackJson.containsKey("height")) {
        height = trackJson.getAsInt("height");
      } else {
        height = GraphPlotTrack.PLOT_SIZE.height;
      }

      System.err.println("sample " + name);

      track = new SamplePlotTrack(name, sample,
          WebAssemblyService.getInstance().getSampleAssembly(), lineColor,
          fillColor, height);

      if (trackJson.containsKey("auto-y")) {
        track.setAutoY(trackJson.getAsBool("auto-y"));
      }

      if (trackJson.containsKey("common-y")) {
        track.setCommonY(trackJson.getAsBool("common-y"));
      }

      if (trackJson.containsKey("normalize-y")) {
        track.setNormalizeY(trackJson.getAsBool("normalize-y"));
      }

      if (trackJson.containsKey("y-max")) {
        track.setYMax(trackJson.getAsDouble("y-max"));
      }

      TrackTreeNode child = new TrackTreeNode(track);

      rootNode.addChild(child);

      validTrack = true;
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
    return "Sample";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.view.TrackJsonParser#getType()
   */
  @Override
  public String getType() {
    return "sample";
  }
}
