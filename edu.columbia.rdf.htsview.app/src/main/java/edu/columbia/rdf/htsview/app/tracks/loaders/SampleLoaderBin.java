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
package edu.columbia.rdf.htsview.app.tracks.loaders;

import java.io.IOException;
import java.nio.file.Path;

import org.jebtk.core.io.FileUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.app.SampleDialog;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.loaders.SampleLoaderFS;
import edu.columbia.rdf.htsview.tracks.sample.SampleTracks;

// TODO: Auto-generated Javadoc
/**
 * Base class for binary file readers that support .
 *
 * @author Antony Holmes Holmes
 */
public abstract class SampleLoaderBin extends SampleLoaderFS {

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.loaders.SampleLoader#open(org.abh.common.
   * ui. window.ModernWindow, java.nio.file.Path, org.abh.common.tree.TreeNode)
   */
  @Override
  public Track open(ModernWindow parent, Path metaFile, TreeNode<Track> root)
      throws IOException {
    if (!FileUtils.exists(metaFile)) {
      return null;
    }

    Json json = JsonParser.json(metaFile);

    Sample sample = SampleTracks.getSampleFromTrack(json);

    SampleDialog dialog = new SampleDialog(parent, sample, true, null);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return null;
    }

    Track ret = null;

    if (dialog.getShowSample()) {
      ret = openSample(parent, sample, metaFile, json, root);
    }

    if (dialog.getShowReads()) {
      openReads(parent, sample, metaFile, json, root);
    }

    return ret;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.loaders.SampleLoader#openSample(org.abh.
   * common.ui.window.ModernWindow, java.nio.file.Path,
   * org.abh.common.tree.TreeNode)
   */
  @Override
  public Track openSample(ModernWindow parent,
      Path metaFile,
      TreeNode<Track> root) throws IOException {
    if (!FileUtils.exists(metaFile)) {
      return null;
    }

    Json json = JsonParser.json(metaFile);

    Sample sample = SampleTracks.getSampleFromTrack(json);

    return openSample(parent, sample, metaFile, json, root);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.loaders.SampleLoader#openReads(org.abh.
   * common .ui.window.ModernWindow, java.nio.file.Path,
   * org.abh.common.tree.TreeNode)
   */
  @Override
  public Track openReads(ModernWindow parent,
      Path metaFile,
      TreeNode<Track> root) throws IOException {
    if (!FileUtils.exists(metaFile)) {
      return null;
    }

    Json json = JsonParser.json(metaFile);

    Sample sample = SampleTracks.getSampleFromTrack(json);

    return openReads(parent, sample, metaFile, json, root);
  }

  /**
   * Open sample.
   *
   * @param parent the parent
   * @param sample the sample
   * @param metaFile the meta file
   * @param json the json
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Track openSample(ModernWindow parent,
      Sample sample,
      Path metaFile,
      Json json,
      TreeNode<Track> root) throws IOException {
    return null;
  }

  /**
   * Open reads.
   *
   * @param parent the parent
   * @param sample the sample
   * @param metaFile the meta file
   * @param json the json
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Track openReads(ModernWindow parent,
      Sample sample,
      Path metaFile,
      Json json,
      TreeNode<Track> root) throws IOException {
    return null;
  }
}
