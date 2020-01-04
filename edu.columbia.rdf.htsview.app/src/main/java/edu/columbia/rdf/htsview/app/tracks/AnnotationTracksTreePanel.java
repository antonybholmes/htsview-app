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

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;

import edu.columbia.rdf.htsview.app.AnnotationTracksTree;
import edu.columbia.rdf.htsview.tracks.Track;

/**
 * The Class AnnotationTracksTreePanel.
 */
public class AnnotationTracksTreePanel extends ModernWidget {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant NO_TRACKS. */
  private static final List<Track> NO_TRACKS = Collections
      .unmodifiableList(new ArrayList<Track>());

  /** The m tree. */
  private AnnotationTracksTree mTree;

  /**
   * Instantiates a new annotation tracks tree panel.
   *
   * @param tree the tree
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public AnnotationTracksTreePanel(AnnotationTracksTree tree)
      throws IOException {
    mTree = tree;

    createUi();
  }

  /**
   * Creates the ui.
   */
  public void createUi() {
    ModernScrollPane scrollPane = new ModernScrollPane(mTree);
    scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);

    add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Sets the selected.
   *
   * @param i the new selected
   */
  public void setSelected(int i) {
    mTree.selectNode(i);
  }

  /**
   * Gets the selected tracks.
   *
   * @return the selected tracks
   */
  public List<Track> getSelectedTracks() {
    if (mTree.getSelectedNodes().size() == 0) {
      return NO_TRACKS; // new ArrayList<ExperimentSearchResult>();
    }

    List<Track> tracks = new ArrayList<Track>();

    for (TreeNode<Track> node : mTree.getSelectedNodes()) {
      selectedTracks(node, tracks);
    }

    return tracks;
  }

  /**
   * Recursively examine a node and its children to find those with experiments.
   *
   * @param node the node
   * @param tracks the tracks
   */
  private void selectedTracks(TreeNode<Track> node, List<Track> tracks) {
    if (node.getValue() != null) {
      tracks.add(node.getValue());
    }

    for (TreeNode<Track> child : node) {
      selectedTracks(child, tracks);
    }
  }
}
