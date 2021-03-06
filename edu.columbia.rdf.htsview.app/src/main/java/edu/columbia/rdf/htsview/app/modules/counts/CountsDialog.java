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
package edu.columbia.rdf.htsview.app.modules.counts;

import java.awt.Dimension;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

import javax.swing.Box;

import org.jebtk.bioinformatics.ext.ucsc.Bed;
import org.jebtk.bioinformatics.ext.ucsc.BedGraph;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.file.BioPathUtils;
import org.jebtk.bioinformatics.genomic.ChromosomeService;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.bioinformatics.ui.Bioinformatics;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGraphGuiFileFilter;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGraphTableModel;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGuiFileFilter;
import org.jebtk.bioinformatics.ui.external.ucsc.BedTableModel;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.ui.external.microsoft.AllXlsxGuiFileFilter;
import org.jebtk.math.ui.external.microsoft.XlsxGuiFileFilter;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dataview.ModernDataModel;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.io.TxtGuiFileFilter;
import org.jebtk.modern.list.ModernList;
import org.jebtk.modern.list.ModernListModel;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernLineBorderPanel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.ribbon.RibbonButton;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.htsview.app.modules.heatmap.GenomicRegionsPanel;
import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;

/**
 * The Class ReadDistDialog.
 */
public class CountsDialog extends ModernDialogHelpWindow {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m samples button. */
  private ModernButton mSamplesButton = new RibbonButton(
      "Samples...");

  /** The m file button. */
  private ModernButton mFileButton = new ModernButton(UI.MENU_LOAD,
      AssetService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));

  /** The m genes button. */
  private ModernButton mGenesButton = new ModernButton("All Genes");

  /** The m samples list. */
  private ModernList<String> mSamplesList = new ModernList<String>();

  /** The m check plot. */
  // private CheckBox mCheckPlot = new ModernCheckSwitch("Create plot", true);

  /** The m regions panel. */
  private GenomicRegionsPanel mRegionsPanel;

  /** The m name field. */
  private ModernTextField mNameField = new ModernTextField("Samples");

  /** The m samples. */
  private List<SamplePlotTrack> mSamples;

  /** The Constant LIST_SIZE. */
  private static final Dimension LIST_SIZE = new Dimension(540, 140);

  private NormCombo mNormCombo = new NormCombo();

  private GenomeModel mGenomeModel;

  /**
   * Instantiates a new read dist dialog.
   *
   * @param parent the parent
   * @param genomeModel the genome model
   * @param samples the samples
   */
  public CountsDialog(ModernWindow parent, GenomeModel genomeModel,
      List<SamplePlotTrack> samples) {
    super(parent, "htsview.modules.read-dist.help.url");

    mGenomeModel = genomeModel;

    mRegionsPanel = new GenomicRegionsPanel();

    setTitle("Read Distribution");

    setup();

    createUi();

    loadSamples(samples);
  }

  /**
   * Setup.
   */
  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    mGenesButton.addClickListener(this);
    mFileButton.addClickListener(this);
    mSamplesButton.addClickListener(this);

    setSize(720, 640);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    Box box = VBox.create();
    Box box2;

    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Name", 100));
    box2.add(
        new ModernTextBorderPanel(mNameField, ModernWidget.VERY_LARGE_SIZE));
    box.add(box2);

    midSectionHeader("Samples", box);

    ModernScrollPane scrollPane = new ModernScrollPane(mSamplesList);
    scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    ModernLineBorderPanel panel = new ModernLineBorderPanel(scrollPane,
        LIST_SIZE);
    box.add(panel);

    midSectionHeader("Plot Locations", box);

    UI.setSize(mRegionsPanel, LIST_SIZE);

    box2 = HBox.create();
    mRegionsPanel.setAlignmentY(TOP_ALIGNMENT);
    box2.add(mRegionsPanel);

    box2.add(UI.createHGap(5));

    Box box3 = VBox.create();
    box3.setAlignmentY(TOP_ALIGNMENT);
    box3.add(mFileButton);
    box3.add(ModernPanel.createVGap());
    box3.add(mGenesButton);
    box2.add(box3);
    box.add(box2);

    box.add(UI.createVGap(20));

    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Normalization", 100));
    box2.add(mNormCombo);
    box.add(box2);

    // box.add(UI.createVGap(20));

    // box.add(mCheckPlot);

    setCard(box);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.dialog.ModernDialogTaskWindow#clicked(org.abh.common.ui.
   * event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOkButton)) {
      if (mRegionsPanel.getRegions(mGenomeModel.get()).size() > 0) {
        super.clicked(e);
      } else {
        ModernMessageDialog.createWarningDialog(mParent,
            "You must enter at least one gene or region to plot.");
      }
    } else if (e.getSource().equals(mFileButton)) {
      try {
        browseForFile();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else {
      super.clicked(e);
    }
  }

  /*
   * private void loadSamples() throws IOException { ChipSeqSamplesDialog dialog
   * = new ChipSeqSamplesDialog(mParent, mAssembly, mSearchModel);
   * 
   * dialog.setVisible(true);
   * 
   * if (dialog.getStatus() == ModernDialogStatus.CANCEL) { return; }
   * 
   * loadSamples(dialog.getSelectedSamples()); }
   */

  /**
   * Load samples.
   *
   * @param samples the samples
   */
  private void loadSamples(List<SamplePlotTrack> samples) {
    mSamples = samples;

    ModernListModel<String> model = new ModernListModel<String>();

    for (SamplePlotTrack sample : mSamples) {
      model.addValue(sample.getName());
    }

    mSamplesList.setModel(model);
  }

  /**
   * Browse for file.
   *
   * @throws Exception the exception
   */
  private void browseForFile() throws Exception {
    browseForFile(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Browse for file.
   *
   * @param workingDirectory the working directory
   * @throws Exception the exception
   */
  private void browseForFile(Path workingDirectory) throws Exception {
    openFile(FileDialog.openFile(getParentWindow(),
        workingDirectory,
        new AllXlsxGuiFileFilter(),
        new XlsxGuiFileFilter(),
        new TxtGuiFileFilter(),
        new BedGuiFileFilter(),
        new BedGraphGuiFileFilter()));
  }

  /**
   * Open file.
   *
   * @param file the file
   * @throws Exception the exception
   */
  private void openFile(Path file) throws Exception {
    if (file == null) {
      return;
    }

    if (!FileUtils.exists(file)) {
      ModernMessageDialog.createFileDoesNotExistDialog(getParentWindow(),
          getAppInfo().getName(),
          file);

      return;
    }

    ModernDataModel model;

    if (BioPathUtils.ext().bed().test(file)) {
      UCSCTrack bed = Bed.parseTracks(GenomicType.REGION, file).get(0);

      model = new BedTableModel(bed);
    } else if (BioPathUtils.ext().bedgraph().test(file)) {
      UCSCTrack bed = BedGraph.parse(file).get(0);

      model = new BedGraphTableModel(bed);
    } else {
      model = Bioinformatics
          .getModel(file, 1, TextUtils.emptyList(), 0, TextUtils.TAB_DELIMITER);
    }

    StringBuilder buffer = new StringBuilder();
    GenomicRegion region = null;

    for (int i = 0; i < model.getRowCount(); ++i) {
      if (GenomicRegion.isGenomicRegion(model.getValueAsString(i, 0))) {
        region = GenomicRegion.parse(mGenomeModel.get(),
            model.getValueAsString(i, 0));

        GenomicRegion mid = GenomicRegion.midRegion(region);

        buffer.append(mid.toString()).append(TextUtils.NEW_LINE_DELIMITER);
      } else if (model.getValueAsString(i, 0).startsWith("chr")) {
        // three column format

        region = new GenomicRegion(
            ChromosomeService.getInstance().guessChr(file,
                model.getValueAsString(i, 0)),
            model.getValueAsInt(i, 1), model.getValueAsInt(i, 2));

        GenomicRegion mid = GenomicRegion.midRegion(region);

        buffer.append(mid.toString()).append(TextUtils.NEW_LINE_DELIMITER);
      } else {
        // assume its a gene id/symbol etc.
        buffer.append(model.getValueAsString(i, 0))
            .append(TextUtils.NEW_LINE_DELIMITER);
      }
    }

    mRegionsPanel.setText(buffer.toString());

    RecentFilesService.getInstance().add(file);
  }

  /**
   * Gets the regions.
   *
   * @return the regions
   * @throws ParseException the parse exception
   */
  public List<GenomicRegion> getRegions() {
    return mRegionsPanel.getRegions(mGenomeModel.get());
  }

  /**
   * Gets the should plot.
   *
   * @return the should plot
   */
  // public boolean getShouldPlot() {
  // return mCheckPlot.isSelected();
  // }

  /**
   * Gets the plot name.
   *
   * @return the plot name
   */
  public String getPlotName() {
    return mNameField.getText();
  }

  public NormalizationMethod getNorm() {
    return mNormCombo.getNorm();
  }
}
