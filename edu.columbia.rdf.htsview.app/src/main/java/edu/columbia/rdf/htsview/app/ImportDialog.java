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

import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;

import org.jebtk.bioinformatics.genomic.ChromosomeService;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomeService;
import org.jebtk.bioinformatics.ui.external.samtools.SamGuiFileFilter;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.input.InputFieldBox;
import org.jebtk.modern.input.InputNumericalFieldBox;
import org.jebtk.modern.io.ChooseFilePanel;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernSubHeadingLabel;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class ImportDialog.
 */
public class ImportDialog extends ModernDialogTaskWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Loads a list of the genomes in use in the chr database since this is what a
   * user can use for mapping purposes.
   * 
   * @author Antony Holmes
   *
   */
  public class GenomeChrCombo extends ModernComboBox {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new genome chr combo.
     */
    public GenomeChrCombo() {
      for (Genome g : ChromosomeService.getInstance()) {
        addScrollMenuItem(g.getAssembly());
      }
    }
  }

  /**
   * The m choose sam file panel.
   */
  private ChooseFilePanel mChooseSamFilePanel;

  /**
   * The m check1bp.
   */
  private ModernCheckBox mCheck1bp = new ModernCheckBox("1 bp");

  /**
   * The m check10bp.
   */
  private ModernCheckBox mCheck10bp = new ModernCheckBox("10 bp", true);

  /**
   * The m check100bp.
   */
  private ModernCheckBox mCheck100bp = new ModernCheckBox("100 bp", true);

  /**
   * The m check1kb.
   */
  private ModernCheckBox mCheck1kb = new ModernCheckBox("1 kb", true);

  /**
   * The m check10kb.
   */
  private ModernCheckBox mCheck10kb = new ModernCheckBox("10 kb", true);

  /**
   * The m check100kb.
   */
  private ModernCheckBox mCheck100kb = new ModernCheckBox("100 kb", true);

  /**
   * The m check1 mb.
   */
  private ModernCheckBox mCheck1Mb = new ModernCheckBox("1 Mb");

  /**
   * The m choose dir panel.
   */
  private ChooseFilePanel mChooseDirPanel;

  /**
   * The m field name.
   */
  private InputFieldBox mFieldName = new InputFieldBox("Name", 100, 300);

  /**
   * The m field organism.
   */
  private InputFieldBox mFieldOrganism = new InputFieldBox("Organism",
      "Homo Sapiens", 100, 300);

  /**
   * The m field read length.
   */
  private InputNumericalFieldBox mFieldReadLength = new InputNumericalFieldBox(
      "Read Length", 101, 100, 50);

  /**
   * The m genome chr combo.
   */
  private GenomeChrCombo mGenomeChrCombo = new GenomeChrCombo();

  /**
   * Instantiates a new import dialog.
   *
   * @param parent the parent
   */
  public ImportDialog(ModernWindow parent) {
    super(parent);

    setTitle("Import");

    mChooseSamFilePanel = new ChooseFilePanel(parent, new SamGuiFileFilter());

    mChooseDirPanel = new ChooseFilePanel(parent, true);

    createUi();

    UI.centerWindowToScreen(this);

    setSize(640, 480);
  }

  /**
   * Creates the ui.
   */
  public final void createUi() {

    Box content = VBox.create();

    Box box = HBox.create();

    box.add(new ModernAutoSizeLabel("Sam File", 100));
    box.add(mChooseSamFilePanel);

    content.add(box);
    content.add(UI.createVGap(5));

    box = HBox.create();

    box.add(new ModernAutoSizeLabel("Output Directory", 100));
    box.add(mChooseDirPanel);

    content.add(box);
    content.add(UI.createVGap(20));

    content.add(new ModernSubHeadingLabel("Details"));
    content.add(UI.createVGap(5));
    content.add(mFieldName);
    content.add(UI.createVGap(5));
    content.add(mFieldOrganism);

    content.add(UI.createVGap(5));

    box = HBox.create();
    box.add(new ModernAutoSizeLabel("Genome", 100));
    box.add(mGenomeChrCombo);
    content.add(box);

    content.add(UI.createVGap(5));
    content.add(mFieldReadLength);

    content.add(UI.createVGap(20));

    content.add(new ModernSubHeadingLabel("Resolutions"));
    content.add(UI.createVGap(5));

    int[] cols = { 100, 100, 100, 100 };
    int[] rows = { ModernWidget.WIDGET_HEIGHT };

    MatrixPanel panel = new MatrixPanel(rows, cols, 0, 0);

    panel.add(mCheck1bp);
    panel.add(mCheck10bp);
    panel.add(mCheck100bp);
    panel.add(mCheck1kb);
    panel.add(mCheck10kb);
    panel.add(mCheck100kb);
    panel.add(mCheck1Mb);

    content.add(panel);

    setContent(content);
  }

  /**
   * Gets the sam file.
   *
   * @return the sam file
   */
  public Path getSamFile() {
    return mChooseSamFilePanel.getFile();
  }

  /**
   * Gets the dir.
   *
   * @return the dir
   */
  public Path getDir() {
    return mChooseDirPanel.getFile();
  }

  /**
   * Gets the read length.
   *
   * @return the read length
   * @throws ParseException the parse exception
   */
  public int getReadLength() {
    return mFieldReadLength.getInt();
  }

  /**
   * Gets the resolutions.
   *
   * @return the resolutions
   */
  public List<Integer> getResolutions() {
    List<Integer> ret = new ArrayList<Integer>();

    if (mCheck1bp.isSelected()) {
      ret.add(1);
    }

    if (mCheck10bp.isSelected()) {
      ret.add(10);
    }

    if (mCheck100bp.isSelected()) {
      ret.add(100);
    }

    if (mCheck1kb.isSelected()) {
      ret.add(1000);
    }

    if (mCheck10kb.isSelected()) {
      ret.add(10000);
    }

    if (mCheck100kb.isSelected()) {
      ret.add(100000);
    }

    if (mCheck1Mb.isSelected()) {
      ret.add(1000000);
    }

    return ret;
  }

  /**
   * Gets the genome.
   *
   * @return the genome
   */
  public Genome getGenome() {
    return GenomeService.getInstance().guessGenome(mGenomeChrCombo.getText());
  }

  /**
   * Gets the organism.
   *
   * @return the organism
   */
  public String getOrganism() {
    return mFieldOrganism.getText();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.Component#getName()
   */
  public String getName() {
    return mFieldName.getText();
  }
}
