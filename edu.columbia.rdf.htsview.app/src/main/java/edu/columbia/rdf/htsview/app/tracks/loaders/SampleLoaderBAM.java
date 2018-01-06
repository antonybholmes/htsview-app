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

import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.sample.SampleAssemblyBam;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.app.SampleDialog;

// TODO: Auto-generated Javadoc
/**
 * The Class SampleLoaderBAM.
 */
public class SampleLoaderBAM extends SampleLoaderBin {

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "BAM";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.loaders.SampleLoader#getExt()
   */
  @Override
  public String getExt() {
    return "bam";
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.htsview.tracks.loaders.SampleLoaderBin#open(org.abh.common.ui.window.
   * ModernWindow, java.nio.file.Path, org.abh.common.tree.TreeNode)
   */
  @Override
  public Track open(ModernWindow parent, Path file, TreeNode<Track> root) throws IOException {
    if (!FileUtils.exists(file)) {
      return null;
    }

    Sample sample = new Sample(PathUtils.namePrefix(file));

    SampleDialog dialog = new SampleDialog(parent, sample, true, null);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return null;
    }

    Track ret = null;

    if (dialog.getShowSample()) {
      ret = openSample(parent, sample, file, null, root);
    }

    if (dialog.getShowReads()) {
      openReads(parent, sample, file, null, root);
    }

    return ret;
  }

  @Override
  public Track openSample(ModernWindow parent, Path file, TreeNode<Track> root) throws IOException {
    if (!FileUtils.exists(file)) {
      return null;
    }

    Sample sample = new Sample(PathUtils.namePrefix(file));

    return openSample(parent, sample, file, null, root);
  }

  @Override
  public Track openSample(ModernWindow parent, Sample sample, Path file, Json json, TreeNode<Track> root)
      throws IOException {
    // Sample sample = new Sample(PathUtils.namePrefix(file));

    return openSampleFs(sample, new SampleAssemblyBam(file), file, root);
  }

  @Override
  public Track openReads(ModernWindow parent, Path file, TreeNode<Track> root) throws IOException {
    if (!FileUtils.exists(file)) {
      return null;
    }

    Sample sample = new Sample(PathUtils.namePrefix(file));

    return openReads(parent, sample, file, null, root);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.htsview.tracks.loaders.SampleLoaderBin#openReads(org.abh.common.ui.window
   * .ModernWindow, edu.columbia.rdf.edb.Sample, java.nio.file.Path,
   * org.abh.common.json.Json, org.abh.common.tree.TreeNode)
   */
  @Override
  public Track openReads(ModernWindow parent, Sample sample, Path file, Json json, TreeNode<Track> root)
      throws IOException {
    return openReadsFs(sample, new SampleAssemblyBam(file), file, root);
  }
}
