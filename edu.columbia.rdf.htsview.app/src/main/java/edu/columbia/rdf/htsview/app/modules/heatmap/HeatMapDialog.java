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
package edu.columbia.rdf.htsview.app.modules.heatmap;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Box;

import org.jebtk.bioinformatics.ext.ucsc.Bed;
import org.jebtk.bioinformatics.ext.ucsc.BedGraph;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.file.BioPathUtils;
import org.jebtk.bioinformatics.genomic.ChromosomeService;
import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomeService;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.bioinformatics.genomic.GenomicEntity;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.bioinformatics.ui.Bioinformatics;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGraphGuiFileFilter;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGraphTableModel;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGuiFileFilter;
import org.jebtk.bioinformatics.ui.external.ucsc.BedTableModel;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.ui.external.microsoft.AllXlsxGuiFileFilter;
import org.jebtk.math.ui.external.microsoft.XlsxGuiFileFilter;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dataview.ModernDataModel;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.dialog.ModernDialogStatus;
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
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.search.SearchModel;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.chipseq.ChipSeqSamplesDialog;
import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;

/**
 * The Class HeatMapDialog.
 */
public class HeatMapDialog extends ModernDialogHelpWindow {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant LIST_SIZE. */
  private static final Dimension LIST_SIZE = new Dimension(500, 100);

  /** The m sample button. */
  private ModernButton mSampleButton = new ModernButton(
      AssetService.getInstance().loadIcon("database", 16));

  /** The m input button. */
  private ModernButton mInputButton = new ModernButton(
      AssetService.getInstance().loadIcon("database", 16));

  /** The m file button. */
  private ModernButton mFileButton = new ModernButton(UI.MENU_LOAD,
      AssetService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));

  /** The m genes button. */
  private ModernButton mGenesButton = new ModernButton("All Genes");

  /** The m text padding. */
  private ModernTextField mTextPadding = new ModernTextField("3000");

  /** The m text input. */
  private ModernTextField mTextInput = new ModernTextField("");

  /** The m text bin. */
  private ModernTextField mTextBin = new ModernTextField("100");

  /** The m check use input. */
  private ModernCheckBox mCheckUseInput = new ModernCheckBox("Subtract Input",
      120);

  /** The m check plot. */
  private CheckBox mCheckPlot = new ModernCheckSwitch("Create plot", true);

  /** The m check sort none. */
  private ModernRadioButton mCheckSortNone = new ModernRadioButton("None",
      true);

  /** The m check sort tss dist. */
  private ModernRadioButton mCheckSortTssDist = new ModernRadioButton(
      "TSS distance");

  /** The m check sort intensity. */
  private ModernRadioButton mCheckSortIntensity = new ModernRadioButton(
      "Intensity");

  /** The m regions panel. */
  private RegionsPanel mRegionsPanel;

  /** The m search model. */
  private SearchModel mSearchModel = new SearchModel();

  /** The m input. */
  private Sample mInput;

  /** The m genome model. */
  private GenomeModel mGenomeModel;

  /** The m samples. */
  private List<SamplePlotTrack> mSamples;

  /** The m samples list. */
  private ModernList<String> mSamplesList = new ModernList<String>();

  /**
   * Instantiates a new heat map dialog.
   *
   * @param parent the parent
   * @param genomeModel the genome model
   * @param samples the samples
   */
  public HeatMapDialog(ModernWindow parent, GenomeModel genomeModel,
      List<SamplePlotTrack> samples) {
    super(parent, "htsview.modules.heat-map.help.url");

    mGenomeModel = genomeModel;
    mSamples = samples;

    mRegionsPanel = new RegionsPanel(genomeModel);

    setTitle("Heat Map");

    setup();

    createUi();

    loadSamples(samples);
  }

  /**
   * Setup.
   */
  private void setup() {
    mGenesButton.addClickListener(this);
    mFileButton.addClickListener(this);
    mSampleButton.addClickListener(this);
    mInputButton.addClickListener(this);

    setSize(720, 700);

    UI.centerWindowToScreen(this);
  }

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
   * Creates the ui.
   */
  private final void createUi() {
    Box box = VBox.create();
    Box box2;

    // box.add(new ModernDialogSectionSeparator("Samples"));
    // box.add(Ui.createVGap(5));

    sectionHeader("Samples", box);

    ModernScrollPane scrollPane = new ModernScrollPane(mSamplesList);
    scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    ModernLineBorderPanel panel = new ModernLineBorderPanel(scrollPane,
        LIST_SIZE);
    box.add(panel);

    /*
     * box.add(Ui.createVGap(5));
     * 
     * box2 = HBox.create(); box2.add(mCheckUseInput); box2.add(new
     * ModernTextBorderPanel(mTextInput, ModernWidget.EXTRA_LARGE_SIZE));
     * box2.add(ModernPanel.createHGap()); box2.add(mInputButton);
     * box.add(box2);
     */

    midSectionHeader("Genomic Regions", box);

    UI.setSize(mRegionsPanel, LIST_SIZE);

    box2 = HBox.create();

    mRegionsPanel.setAlignmentY(TOP_ALIGNMENT);
    box2.add(mRegionsPanel);

    box2.add(ModernPanel.createHGap());

    Box box3 = VBox.create();
    box3.setAlignmentY(TOP_ALIGNMENT);
    box3.add(mFileButton);
    box3.add(ModernPanel.createVGap());
    box3.add(mGenesButton);
    box2.add(box3);
    box.add(box2);

    box.add(UI.createVGap(20));

    // box.add(new ModernDialogSectionSeparator("View"));
    // box.add(Ui.createVGap(5));

    // box.add(ModernPanel.createVGap());

    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Extend", 120));
    box2.add(new ModernTextBorderPanel(mTextPadding, 100));
    box2.add(ModernPanel.createHGap());
    box2.add(new ModernAutoSizeLabel("bp"));
    box.add(box2);

    box.add(ModernPanel.createVGap());

    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Bin size", 120));
    box2.add(new ModernTextBorderPanel(mTextBin, 100));
    box2.add(ModernPanel.createHGap());
    box2.add(new ModernAutoSizeLabel("bp"));
    box.add(box2);

    midSectionHeader("Sorting", box);

    box.add(mCheckSortNone);
    box.add(mCheckSortIntensity);
    box.add(mCheckSortTssDist);

    box.add(UI.createVGap(10));
    box.add(mCheckPlot);

    setCard(box);

    ModernButtonGroup buttonGroup = new ModernButtonGroup();

    buttonGroup.add(mCheckSortNone);
    buttonGroup.add(mCheckSortTssDist);
    buttonGroup.add(mCheckSortIntensity);

    mTextInput.setEditable(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * event. ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mGenesButton)) {
      loadTss();
    } else if (e.getSource().equals(mInputButton)) {
      try {
        mInput = loadSample(mTextInput);

        if (mInput != null) {
          mCheckUseInput.setSelected(true);
        }
      } catch (IOException e1) {
        e1.printStackTrace();
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

  /**
   * Load sample.
   *
   * @param field the field
   * @return the sample
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private Sample loadSample(ModernTextField field) throws IOException {
    List<Sample> samples = loadSamples();

    if (!samples.isEmpty()) {
      field.setText(samples.get(0).getName());

      return samples.get(0);
    } else {
      return null;
    }
  }

  /**
   * Load samples.
   *
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private List<Sample> loadSamples() throws IOException {
    ChipSeqSamplesDialog dialog = new ChipSeqSamplesDialog(mParent,
        mSearchModel);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return Collections.emptyList();
    }

    return dialog.getSelectedSamples();
  }

  /**
   * Load tss.
   */
  private void loadTss() {
    Set<String> genes = new HashSet<String>();

    Genome genome = mGenomeModel.get();

    Genome g = GenesService.getInstance().getFirstGeneDb(genome.getAssembly());

    for (String refseq : GenesService.getInstance().getGenes(g)
        .getIds(GenomicEntity.REFSEQ_ID)) {

      GenomicElement gene = null;

      try {
        gene = GenesService.getInstance().getGenes(g)
            .getElement(g, refseq, GenomicType.TRANSCRIPT);
      } catch (IOException e) {
        e.printStackTrace();
      }

      // GenomicRegion tss = Gene.tssRegion(gene);

      if (gene != null) {
        genes.add(gene.getProperty(GenomicEntity.GENE_NAME));
      }
    }

    mRegionsPanel.setText(TextUtils.join(CollectionUtils.sort(genes),
        TextUtils.NEW_LINE_DELIMITER));
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
   * @param pwd the pwd
   * @throws Exception the exception
   */
  private void browseForFile(Path pwd) throws Exception {
    openFile(FileDialog.open(mParent)
        .filter(new AllXlsxGuiFileFilter(),
            new XlsxGuiFileFilter(),
            new TxtGuiFileFilter(),
            new BedGuiFileFilter(),
            new BedGraphGuiFileFilter())
        .getFile(pwd));
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

    Genome genome = mGenomeModel.get();

    for (int i = 0; i < model.getRowCount(); ++i) {
      if (GenomicRegion.isGenomicRegion(model.getValueAsString(i, 0))) {
        region = GenomicRegion.parse(genome, model.getValueAsString(i, 0));

        GenomicRegion mid = GenomicRegion.midRegion(region);

        buffer.append(mid.toString()).append(TextUtils.NEW_LINE_DELIMITER);
      } else if (model.getValueAsString(i, 0).startsWith("chr")) {
        // three column format

        region = new GenomicRegion(
            ChromosomeService.getInstance().guessChr(file,
                model.getValueAsString(i, 0)),
            Integer.parseInt(model.getValueAsString(i, 1)),
            Integer.parseInt(model.getValueAsString(i, 2)));

        GenomicRegion mid = GenomicRegion.midRegion(region);

        buffer.append(mid.toString()).append(TextUtils.NEW_LINE_DELIMITER);
      } else {
        // assume its a gene id/symbol etc.
        buffer.append(model.getValueAsString(i, 0))
            .append(TextUtils.NEW_LINE_DELIMITER);
      }
    }

    mRegionsPanel.setText(buffer.toString());

    RecentFilesService.getInstance().setPwd(file.getParent());
  }

  /**
   * Gets the regions.
   *
   * @return the regions
   * @throws ParseException the parse exception
   */
  public List<HeatMapIdLocation> getRegions() {
    return mRegionsPanel.getRegions();
  }

  /**
   * Gets the padding.
   *
   * @return the padding
   */
  public int getPadding() {
    return Integer.parseInt(mTextPadding.getText());
  }

  /**
   * Gets the should plot.
   *
   * @return the should plot
   */
  public boolean getShouldPlot() {
    return mCheckPlot.isSelected();
  }

  /**
   * Gets the bin size.
   *
   * @return the bin size
   */
  public int getBinSize() {
    // Min resolution is 10
    return Math.max(10, Integer.parseInt(mTextBin.getText()));
  }

  /**
   * Gets the sort type.
   *
   * @return the sort type
   */
  public HeatMapSort getSortType() {
    if (mCheckSortTssDist.isSelected()) {
      return HeatMapSort.TSS_DISTANCE;
    } else if (mCheckSortIntensity.isSelected()) {
      return HeatMapSort.INTENSITY;
    } else {
      return HeatMapSort.NONE;
    }
  }

  /**
   * Gets the input.
   *
   * @return the input
   */
  public Sample getInput() {
    return mCheckUseInput.isSelected() ? mInput : null;
  }
}
