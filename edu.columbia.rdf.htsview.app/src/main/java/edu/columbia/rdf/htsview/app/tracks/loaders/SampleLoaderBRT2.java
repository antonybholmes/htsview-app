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

import org.jebtk.core.json.Json;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.sample.SampleAssemblyBRT2;

/**
 * The Class SampleLoaderBRT2.
 */
public class SampleLoaderBRT2 extends SampleLoaderBin {

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "BRT2";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.loaders.SampleLoader#getExt()
   */
  @Override
  public String getExt() {
    return "brt2j";
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.htsview.tracks.loaders.SampleLoaderBin#openSample(org.abh.common.ui.
   * window.ModernWindow, edu.columbia.rdf.edb.Sample, java.nio.file.Path,
   * org.abh.common.json.Json, org.abh.common.tree.TreeNode)
   */
  @Override
  public Track openSample(ModernWindow parent,
      Sample sample,
      Path metaFile,
      Json json,
      TreeNode<Track> root) throws IOException {
    return openSampleFs(sample,
        new SampleAssemblyBRT2(metaFile),
        metaFile,
        root);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.htsview.tracks.loaders.SampleLoaderBin#openReads(org.abh.common.ui.
   * window .ModernWindow, edu.columbia.rdf.edb.Sample, java.nio.file.Path,
   * org.abh.common.json.Json, org.abh.common.tree.TreeNode)
   */
  @Override
  public Track openReads(ModernWindow parent,
      Sample sample,
      Path metaFile,
      Json json,
      TreeNode<Track> root) throws IOException {
    return openReadsFs(sample,
        new SampleAssemblyBRT2(metaFile),
        metaFile,
        root);
  }
}
