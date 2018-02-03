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
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import javax.swing.Box;

import org.jebtk.bioinformatics.ext.ucsc.Bed;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.ui.BioInfDialog;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.CrossVectorIcon;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.ribbon.RibbonButton;
import org.jebtk.modern.ribbon.RibbonSubSectionSeparator;
import org.jebtk.modern.search.SearchModel;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.app.SampleDialog;
import edu.columbia.rdf.htsview.app.icons.OpenTrack16VectorIcon;
import edu.columbia.rdf.htsview.app.tracks.peaks.PeakAssembly;
import edu.columbia.rdf.htsview.app.tracks.peaks.PeakSet;
import edu.columbia.rdf.htsview.app.tracks.peaks.PeaksPlotTrack;
import edu.columbia.rdf.htsview.chipseq.ChipSeqSamplesDialog;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackTree;
import edu.columbia.rdf.htsview.tracks.TrackTreeNode;
import edu.columbia.rdf.htsview.tracks.TracksPanel;
import edu.columbia.rdf.htsview.tracks.loaders.SampleLoaderService;
import edu.columbia.rdf.htsview.tracks.sample.ReadsPlotTrack;
import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;

// TODO: Auto-generated Javadoc
/**
 * The Class HTSTracksPanel.
 */
public class HTSTracksPanel extends TracksPanel implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m open button. */
  private ModernButton mOpenButton = new ModernButton(
      UIService.getInstance().loadIcon(OpenTrack16VectorIcon.class, 16));

  /** The m delete button. */
  private ModernButton mDeleteButton = new RibbonButton(
      UIService.getInstance().loadIcon("trash_bw", 16));

  /** The m tracks button. */
  private ModernButton mTracksButton = new RibbonButton(
      UIService.getInstance().loadIcon("tracks", 16));

  /** The m edit button. */
  private ModernButton mEditButton = new RibbonButton(
      UIService.getInstance().loadIcon("edit_bw", 16));

  /** The m clear button. */
  private ModernButton mClearButton = new RibbonButton(
      UIService.getInstance().loadIcon(CrossVectorIcon.class, 16));

  /** The m samples button. */
  private ModernButton mSamplesButton = new RibbonButton("Samples",
      UIService.getInstance().loadIcon("samples", 16));

  /** The m search model. */
  private SearchModel mSearchModel = new SearchModel();

  private GenomeModel mGenomeModel;

  // private Map<String, ChromosomeSizes> mChrMap;

  /**
   * Instantiates a new HTS tracks panel.
   *
   * @param parent the parent
   * @param tree the tree
   * @param trackList the track list
   */
  public HTSTracksPanel(ModernRibbonWindow parent, GenomeModel genomeModel,
      ModernTree<Track> tree,
      TrackTree trackList) {
    super(parent, tree, trackList);

    mGenomeModel = genomeModel;
    
    setup();

    createUi();
  }

  /**
   * Creates the ui.
   */
  private void createUi() {
    // Box box = VBox.create();

    // HTabToolbar toolbar = new HTabToolbar("Tracks");

    // box.add(toolbar);

    Box box2 = HBox.create();

    if (SettingsService.getInstance().getAsBool("edb.modules.edbw.enabled")) {
      mSamplesButton.setToolTip("Samples Database",
          "Load ChIP-seq samples from database.");

      box2.add(mSamplesButton);
      box2.add(ModernPanel.createHGap());
      box2.add(new RibbonSubSectionSeparator());
      box2.add(ModernPanel.createHGap());

    }

    // mOpenButton.setToolTip("Open Samples",
    // "Load additional samples from disk.");
    // toolbar.add(mOpenButton);
    // toolbar.add(ModernPanel.createHGap());

    mTracksButton.setToolTip("Annotation Tracks",
        "Load additional annotation tracks.");
    box2.add(mTracksButton);
    // toolbar.add(ModernPanel.createHGap());
    mEditButton.setToolTip("Edit Tracks", "Edit track properties.");
    box2.add(mEditButton);
    // toolbar.add(ModernPanel.createHGap());
    mDeleteButton.setToolTip("Delete", "Delete selected tracks.");
    box2.add(mDeleteButton);

    box2.setBorder(DOUBLE_BORDER);

    // box.add(box2);

    // toolbar.setBorder(BorderService.getInstance().createBottomBorder(DOUBLE_PADDING));

    setHeader(box2);

    setBorder(BORDER);
  }

  /**
   * Setup.
   */
  private void setup() {
    // mTrackList.addMouseListener(new TrackMouseEvents());

    mSamplesButton.addClickListener(this);
    mOpenButton.addClickListener(this);
    mDeleteButton.addClickListener(this);
    mClearButton.addClickListener(this);
    mTracksButton.addClickListener(this);
    mEditButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * event. ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mSamplesButton)) {
      try {
        loadSamples();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    } else if (e.getSource().equals(mTracksButton)) {
      try {
        loadTracks();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    } else if (e.getSource().equals(mEditButton)) {
      editTracks();
    } else if (e.getSource().equals(mDeleteButton)) {
      deleteTracks();
    } else if (e.getSource().equals(mClearButton)) {
      clearTracks();
    } else {
      // do nothing
    }
  }

  // Allow user to edit tracks

  /**
   * Load samples.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void loadSamples() throws IOException {
    ChipSeqSamplesDialog dialog = new ChipSeqSamplesDialog(mParent,
        mSearchModel);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    loadSamples(dialog.getSelectedSamples());
  }

  /**
   * Load samples.
   *
   * @param samples the samples
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void loadSamples(Collection<Sample> samples) throws IOException {
    loadSamples(samples, true);
  }

  /**
   * Load samples.
   *
   * @param samples the samples
   * @param interactive the interactive
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void loadSamples(Collection<Sample> samples, boolean interactive)
      throws IOException {
    if (CollectionUtils.isNullOrEmpty(samples)) {
      return;
    }

    SampleAssembly sampleAssembly = WebAssemblyService.getInstance()
        .getSampleAssembly();

    PeakAssembly peakAssembly = WebAssemblyService.getInstance()
        .getPeakAssembly();

    for (Sample sample : samples) {
      // tracks.add(new SamplePlotTrack(sample, mAssembly));

      boolean hasReadSupport = sampleAssembly.hasReadSupport(sample);

      int n = peakAssembly.getJsonPeaks(sample).size();

      // Only show the dialog if either the sample supports having
      // reads or there are peak lists we can show
      if (interactive && (hasReadSupport || n > 0)) {

        SampleDialog dialog = new SampleDialog(mParent, sample, hasReadSupport,
            peakAssembly);

        dialog.setVisible(true);

        if (dialog.getStatus() == ModernDialogStatus.OK) {
          if (hasReadSupport) {
            SamplePlotTrack track;
            if (dialog.getShowSample()) {
              track = new SamplePlotTrack(sample, sampleAssembly);

              mTrackList.getRoot().addChild(new TrackTreeNode(track));
            }

            if (dialog.getShowReads()) {
              track = new ReadsPlotTrack(sample, sampleAssembly);

              mTrackList.getRoot().addChild(new TrackTreeNode(track));
            }
          } else {
            mTrackList.getRoot().addChild(new TreeNode<Track>(sample.getName(),
                new SamplePlotTrack(sample, sampleAssembly)));
          }

          if (peakAssembly != null) {
            // Display some peaks
            for (PeakSet peaks : dialog.getShowPeaks()) {

              List<GenomicRegion> locations = peakAssembly
                  .downloadJsonPeaks(mGenomeModel.get(), sample, peaks);

              Bed bed = Bed.create(peaks.getName(), locations);

              mTrackList.getRoot().addChild(new TreeNode<Track>(peaks.getName(),
                  new PeaksPlotTrack(sample.getId(), peaks, bed)));
            }
          }
        }
      } else {
        mTrackList.getRoot().addChild(new TreeNode<Track>(sample.getName(),
            new SamplePlotTrack(sample, sampleAssembly)));
      }
    }

    // mListModel.addValues(tracks);
  }

  /**
   * Browse for file.
   *
   * @throws Exception the exception
   */
  public void browseForFile() throws Exception {
    browseForFile(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Browse for file.
   *
   * @param pwd the pwd
   * @throws Exception the exception
   */
  public void browseForFile(Path pwd) throws Exception {
    openFiles(BioInfDialog.open(mParent).bedAndBedgraph().getFiles(pwd));
  }

  /**
   * Open files.
   *
   * @param files the files
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void openFiles(List<Path> files) throws IOException {
    SampleLoaderService.getInstance()
        .openFiles(mParent, files, mTrackList.getRoot());
  }

  /**
   * Open file.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void openFile(Path file) throws IOException {
    openFiles(CollectionUtils.asList(file));
  }

  /**
   * Browse for tracks.
   *
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  // public void browseForSamples() throws IOException {
  // browseForSamples(RecentFilesService.getInstance().getPwd());
  // }

  /**
   * Browse for tracks.
   *
   * @param pwd the pwd
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  // public void browseForSamples(Path pwd) throws IOException {
  // openSamples(FileDialog.open(mParent).dirs().getFiles(pwd));
  // }

  /**
   * Open tracks.
   *
   * @param dirs the dirs
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  // public void openSamples(List<Path> dirs) throws IOException {
  // for (Path dir : dirs) {
  // openSample(dir);
  // }
  // }

  /*
   * public void openSample(Path dir) throws IOException { if
   * (SampleTracks.isBRT2Track(dir)) { openBRT2Sample(dir); } else if
   * (SampleTracks.isBRTTrack(dir)) { openBRTSample(dir); } else if
   * (SampleTracks.isBVTTrack(dir)) { openBVTSample(dir); } else {
   * open16bitSample(dir); } }
   */

  /**
   * public void open16bitSample(Path metaFile) throws IOException { Json json =
   * new JsonParser().parse(metaFile);
   * 
   * Sample sample = SampleTracks.getSampleFromTrack(json);
   * 
   * openSampleFs(sample, new SampleAssembly16bit(metaFile,
   * json.getAsInt("Mapped Reads")), metaFile); }
   */

}
