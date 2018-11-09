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

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.core.json.Json;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackTreeNode;
import edu.columbia.rdf.htsview.tracks.loaders.SampleLoaderService;
import edu.columbia.rdf.htsview.tracks.sample.ReadsFsPlotTrack;
import edu.columbia.rdf.htsview.tracks.view.TrackJsonParser;

/**
 * The Class ReadsFSJsonParser.
 */
public class ReadsFSJsonParser extends TrackJsonParser {

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
    Path metaFile = getFile(trackJson);

    ReadsFsPlotTrack track = (ReadsFsPlotTrack) SampleLoaderService
        .getInstance().openReads(window, metaFile, rootNode); // tracksPanel.loadReadTrack(metaFile);

    boolean visible = trackJson.getBool("visible");

    Color lineColor = null;

    if (trackJson.containsKey("line-color")) {
      lineColor = trackJson.getColor("line-color");
    } else if (trackJson.containsKey("color")) {
      lineColor = trackJson.getColor("color");
    } else {
      lineColor = Color.GRAY;
    }

    Color fillColor = null;

    if (trackJson.containsKey("fill-color")) {
      fillColor = trackJson.getColor("fill-color");
    } else {
      fillColor = Color.LIGHT_GRAY;
    }

    boolean antiSenseVisible = trackJson.getBool("anti-sense-visible");

    Color antiSenseLineColor = trackJson.getColor("anti-sense-color");

    Color antiSenseFillColor = trackJson.getColor("anti-sense-fill-color");

    int readHeight = trackJson.getInt("read-height");
    int gap = trackJson.getInt("gap");

    track.setForwardVisible(visible);
    track.setAntiSenseVisible(antiSenseVisible);

    track.setLineColor(lineColor);
    track.setFillColor(fillColor);

    track.setAntiSenseLineColor(antiSenseLineColor);
    track.setAntiSenseFillColor(antiSenseFillColor);

    track.setReadHeight(readHeight);
    track.setGap(gap);

    TrackTreeNode child = new TrackTreeNode(track);

    rootNode.addChild(child);

    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "ReadsFS";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.view.TrackJsonParser#getType()
   */
  @Override
  public String getType() {
    return "reads-fs";
  }
}
