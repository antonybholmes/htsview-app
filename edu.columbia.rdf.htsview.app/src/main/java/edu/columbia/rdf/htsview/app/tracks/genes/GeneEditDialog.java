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
package edu.columbia.rdf.htsview.app.tracks.genes;

import java.awt.Color;

import javax.swing.Box;

import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.graphics.color.ColorSwatchButton2;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.widget.ModernTwoStateWidget;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;

/**
 * The Class GeneEditDialog.
 */
public class GeneEditDialog extends ModernDialogTaskWindow {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m color button. */
  private ColorSwatchButton2 mColorButton;

  /** The m other color button. */
  private ColorSwatchButton2 mOtherColorButton;

  /** The m UTR color button. */
  private ColorSwatchButton2 mUTRColorButton;

  private ColorSwatchButton2 mExonColorButton;

  /** The m name field. */
  private ModernTextField mNameField = new ModernClipboardTextField("Name");

  /** The m track. */
  private GenesPlotTrack mTrack;

  /** The m check draw tss arrows. */
  private ModernTwoStateWidget mCheckDrawTssArrows = new ModernCheckSwitch(
      "TSS arrows");

  /** The m check draw exon arrows. */
  private ModernTwoStateWidget mCheckDrawExonArrows = new ModernCheckSwitch(
      "Exon arrows");

  /** The m check draw arrows. */
  private ModernTwoStateWidget mCheckDrawArrows = new ModernCheckSwitch(
      "Arrows", true);

  /** The m check full. */
  private ModernRadioButton mCheckFull = new ModernRadioButton("Full");

  /** The m check dense. */
  private ModernRadioButton mCheckDense = new ModernRadioButton("Dense");

  /** The m check compact. */
  private ModernRadioButton mCheckCompact = new ModernRadioButton("Compact");

  private ColorSwatchButton2 mArrowColorButton;

  /**
   * Instantiates a new gene edit dialog.
   *
   * @param parent the parent
   * @param track the track
   */
  public GeneEditDialog(ModernWindow parent, GenesPlotTrack track) {
    super(parent);

    mTrack = track;

    setTitle("Genes Editor", track.getName());

    setup();

    createUi();
  }

  /**
   * Setup.
   */
  private void setup() {
    mOkButton.addClickListener(this);
    mCancelButton.addClickListener(this);

    mCheckDrawTssArrows.setSelected(mTrack.getShowTssArrows());
    mCheckDrawExonArrows.setSelected(mTrack.getShowExonArrows());
    mCheckDrawArrows.setSelected(mTrack.getShowArrows());

    new ModernButtonGroup(mCheckFull, mCheckDense, mCheckCompact);

    switch (mTrack.getView()) {
    case DENSE:
      mCheckDense.doClick();
      break;
    case COMPACT:
      mCheckCompact.doClick();
      break;
    default:
      mCheckFull.doClick();
      break;
    }

    setSize(520, 520);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    Box content = VBox.create();

    mNameField.setText(mTrack.getName());
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    int[] rows = { ModernWidget.WIDGET_HEIGHT };
    int[] cols = { 100, 300 };

    MatrixPanel matrixPanel = new MatrixPanel(rows, cols, ModernWidget.PADDING,
        ModernWidget.PADDING);

    mColorButton = new ColorSwatchButton2(mParent, mTrack.getFillColor());

    matrixPanel.add(new ModernAutoSizeLabel("Name"));
    matrixPanel.add(new ModernTextBorderPanel(mNameField));
    matrixPanel.add(new ModernAutoSizeLabel("Gene Color"));

    Box box = HBox.create();
    box.add(mColorButton);

    matrixPanel.add(box);

    // matrixPanel.add(new ModernAutoSizeLabel("Other Color"));
    // box = HBox.create();
    mOtherColorButton = new ColorSwatchButton2(mParent, mTrack.getOtherColor());
    // box.add(mOtherColorButton);
    // matrixPanel.add(box);

    matrixPanel.add(new ModernAutoSizeLabel("Exon Color"));
    box = HBox.create();
    mExonColorButton = new ColorSwatchButton2(mParent,
        mTrack.getExonFillColor());
    box.add(mExonColorButton);
    matrixPanel.add(box);

    // matrixPanel.add(new ModernAutoSizeLabel("UTR Color"));

    // box = HBox.create();

    mUTRColorButton = new ColorSwatchButton2(mParent, mTrack.getUTRFillColor());

    matrixPanel.add(new ModernAutoSizeLabel("Arrow Color"));
    box = HBox.create();
    mArrowColorButton = new ColorSwatchButton2(mParent, mTrack.getArrowColor());
    box.add(mArrowColorButton);
    matrixPanel.add(box);

    // box.add(mUTRColorButton);

    // matrixPanel.add(box);

    content.add(matrixPanel);
    // content.add(ModernPanel.createVGap());

    content.add(mCheckDrawArrows);
    content.add(ModernPanel.createVGap());
    content.add(mCheckDrawTssArrows);
    content.add(ModernPanel.createVGap());
    content.add(mCheckDrawExonArrows);
    content.add(UI.createVGap(20));
    content.add(mCheckFull);
    content.add(mCheckDense);
    content.add(mCheckCompact);

    setCard(content);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.Component#getName()
   */
  public String getName() {
    return mNameField.getName();
  }

  /**
   * Gets the color.
   *
   * @return the color
   */
  public Color getColor() {
    return mColorButton.getSelectedColor();
  }

  /**
   * Gets the other color.
   *
   * @return the other color
   */
  public Color getOtherColor() {
    return mOtherColorButton.getSelectedColor();
  }

  /**
   * Gets the UTR color.
   *
   * @return the UTR color
   */
  public Color getUTRColor() {
    return mUTRColorButton.getSelectedColor();
  }

  /**
   * Gets the draw tss arrows.
   *
   * @return the draw tss arrows
   */
  public boolean getDrawTssArrows() {
    return mCheckDrawTssArrows.isSelected();
  }

  /**
   * Gets the draw exon arrows.
   *
   * @return the draw exon arrows
   */
  public boolean getDrawExonArrows() {
    return mCheckDrawExonArrows.isSelected();
  }

  /**
   * Gets the draw arrows.
   *
   * @return the draw arrows
   */
  public boolean getDrawArrows() {
    return mCheckDrawArrows.isSelected();
  }

  /**
   * Gets the view.
   *
   * @return the view
   */
  public GenesView getView() {
    if (mCheckDense.isSelected()) {
      return GenesView.DENSE;
    } else if (mCheckCompact.isSelected()) {
      return GenesView.COMPACT;
    } else {
      return GenesView.FULL;
    }
  }

  public Color getExonColor() {
    return mExonColorButton.getSelectedColor();
  }

  public Color getArrowColor() {
    return mArrowColorButton.getSelectedColor();
  }
}
