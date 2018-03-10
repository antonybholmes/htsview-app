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
package edu.columbia.rdf.htsview.app;

import java.io.IOException;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;
import org.jebtk.bioinformatics.ext.ucsc.TrackDisplayMode;
import org.jebtk.bioinformatics.genomic.SequenceReader;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.core.tree.TreeRootNode;
import org.jebtk.modern.tree.ModernTree;

import edu.columbia.rdf.htsview.app.tracks.conservation.Conservation46WayGraphPlotTrack;
import edu.columbia.rdf.htsview.app.tracks.conservation.Conservation46WayPlotTrack;
import edu.columbia.rdf.htsview.app.tracks.dna.CytobandsPlotTrack;
import edu.columbia.rdf.htsview.app.tracks.dna.DnaBasesPlotTrack;
import edu.columbia.rdf.htsview.app.tracks.dna.DnaColorPlotTrack;
import edu.columbia.rdf.htsview.app.tracks.dna.DnaRepeatMaskPlotTrack;
import edu.columbia.rdf.htsview.app.tracks.genes.GencodeGenesPlotTrack;
import edu.columbia.rdf.htsview.app.tracks.genes.RefSeqGenesPlotTrack;
import edu.columbia.rdf.htsview.app.tracks.mouse.MouseConservationPlotTrack;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedPlotTrack;
import edu.columbia.rdf.htsview.tracks.measurement.RangePlotTrack;
import edu.columbia.rdf.htsview.tracks.measurement.RulerPlotTrack;
import edu.columbia.rdf.htsview.tracks.measurement.ScalePlotTrack;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnotationTracksTree.
 */
public class AnnotationTracksTree extends ModernTree<Track> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new annotation tracks tree.
   *
   * @param genomeAssembly the genome assembly
   * @param conservationAssembly the conservation assembly
   * @param mouseConservationAssembly the mouse conservation assembly
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public AnnotationTracksTree(SequenceReader dnaAssembly,
      ConservationAssembly conservationAssembly,
      ConservationAssembly mouseConservationAssembly) throws IOException {
    Track track;
    TreeNode<Track> node;

    TreeRootNode<Track> root = new TreeRootNode<Track>();

    TreeNode<Track> genomicNode = new TreeNode<Track>("Genomic");

    TreeNode<Track> genesNode = new TreeNode<Track>("Gene Annotation");

    // track = new EnsemblGenesPlotTrack();
    // node = new TreeNode<Track>(track.getName(), track);
    // genesNode.addChild(node);

    track = new RefSeqGenesPlotTrack();
    node = new TreeNode<Track>(track.getName(), track);
    genesNode.addChild(node);

    track = new GencodeGenesPlotTrack();
    node = new TreeNode<Track>(track.getName(), track);
    genesNode.addChild(node);

    // track = new GencodePolyAPlotTrack();
    // node = new TreeNode<Track>(track.getName(), track);
    // genesNode.addChild(node);

    // track = new UcscGenesPlotTrack();
    // node = new TreeNode<Track>(track.getName(), track);
    // genesNode.addChild(node);

    // track = new VegaGenesPlotTrack();
    // node = new TreeNode<Track>(track.getName(), track);
    // genesNode.addChild(node);

    genomicNode.addChild(genesNode);

    TreeNode<Track> dnaNode = new TreeNode<Track>("DNA Annotation");

    track = new DnaBasesPlotTrack(dnaAssembly);
    node = new TreeNode<Track>(track.getName(), track);
    dnaNode.addChild(node);

    track = new DnaColorPlotTrack(dnaAssembly);
    node = new TreeNode<Track>(track.getName(), track);
    dnaNode.addChild(node);

    track = new DnaRepeatMaskPlotTrack(dnaAssembly);
    node = new TreeNode<Track>(track.getName(), track);
    dnaNode.addChild(node);

    genomicNode.addChild(dnaNode);

    TreeNode<Track> conservationNode = new TreeNode<Track>("Conservation");

    track = new Conservation46WayPlotTrack(conservationAssembly);
    node = new TreeNode<Track>(track.getName(), track);
    conservationNode.addChild(node);

    track = new Conservation46WayGraphPlotTrack(conservationAssembly);
    node = new TreeNode<Track>(track.getName(), track);
    conservationNode.addChild(node);

    track = new MouseConservationPlotTrack(mouseConservationAssembly);
    node = new TreeNode<Track>(track.getName(), track);
    conservationNode.addChild(node);

    genomicNode.addChild(conservationNode);

    TreeNode<Track> chrNode = new TreeNode<Track>("Chromosome");

    track = new CytobandsPlotTrack();
    node = new TreeNode<Track>(track.getName(), track);
    chrNode.addChild(node);
    genomicNode.addChild(chrNode);

    TreeNode<Track> microarrayNode = new TreeNode<Track>("Microarray");

    track = new BedPlotTrack(
        PathUtils.getPath("res/tracks/HG-U133_Plus_2.probes.hg19.bed.gz"),
        TrackDisplayMode.FULL);
    node = new TreeNode<Track>(track.getName(), track);
    microarrayNode.addChild(node);

    genomicNode.addChild(microarrayNode);

    /*
     * TreeNode<Track> otherNode = new TreeNode<Track>("Other");
     * 
     * track = new AnnotationBedPlotTrack(Bed.parseTracks("DDS_IgLocusMap",
     * Resources.getResGzipReader("res/tracks/DDS_IgLocusMap.bed.gz")).get(0));
     * node = new TreeNode<Track>(track.getName(), track);
     * otherNode.addChild(node); genomicNode.addChild(otherNode);
     */

    root.addChild(genomicNode);

    //
    // Mouse
    //

    // TreeNode<Track> mouseNode = new TreeNode<Track>("Mouse");

    // genesNode = new TreeNode<Track>("Gene Annotation");

    // track = new MouseRefSeqGenesPlotTrack(geneMap);
    // node = new TreeNode<Track>(track.getName(), track);
    // mouseNode.addChild(node);

    // mouseNode.addChild(genesNode);

    // track = new MouseCytobandsPlotTrack();
    // node = new TreeNode<Track>(track.getName(), track);
    // mouseNode.addChild(node);

    // root.addChild(mouseNode);

    //
    // Measure
    //

    TreeNode<Track> measureNode = new TreeNode<Track>("Measurement");

    track = new RangePlotTrack();
    node = new TreeNode<Track>(track.getName(), track);
    measureNode.addChild(node);

    track = new RulerPlotTrack();
    node = new TreeNode<Track>(track.getName(), track);
    measureNode.addChild(node);

    track = new ScalePlotTrack();
    node = new TreeNode<Track>(track.getName(), track);
    measureNode.addChild(node);

    root.addChild(measureNode);

    setRoot(root);
  }

}
