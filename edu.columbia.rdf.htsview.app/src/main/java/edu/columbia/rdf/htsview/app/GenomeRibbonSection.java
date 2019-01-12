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

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomeService;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.ModernIcon;
import org.jebtk.modern.menu.ModernPopupMenu2;
import org.jebtk.modern.menu.ModernTwoLineMenuItem;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.ribbon.RibbonSize;

/**
 * Allows user to select the resolution to view sequences.
 *
 * @author Antony Holmes
 */
public class GenomeRibbonSection extends RibbonSection
    implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The Constant ICON. */
  private static final ModernIcon ICON = AssetService.getInstance()
      .loadIcon("genome", 24);

  /**
   * The m human button.
   */
  // private RibbonLargeRadioButton mHumanButton = new
  // RibbonLargeRadioButton("Human",
  // UIService.getInstance().loadIcon("human", 24));

  /**
   * The m mouse button.
   */
  // private RibbonLargeRadioButton mMouseButton = new
  // RibbonLargeRadioButton("Mouse",
  // UIService.getInstance().loadIcon("mouse_dark", 24));

  /**
   * The m model.
   */
  private GenomeModel mModel;

  /** The m button. */
  private RibbonLargeDropDownButton2 mButton;

  /**
   * Instantiates a new genome ribbon section.
   *
   * @param ribbon the ribbon
   * @param model the model
   */
  public GenomeRibbonSection(Ribbon ribbon, GenomeModel model) {
    super(ribbon, "Genome");

    mModel = model;

    // ModernButtonGroup group = new ModernButtonGroup();

    ModernPopupMenu2 popup = new ModernPopupMenu2();

    // for (Genome genome : CollectionUtils
    // .sort(GenesService.getInstance().getGenomes())) {
    for (Genome g : GenomeService.getInstance()) {
      popup.addMenuItem(new ModernTwoLineMenuItem(g.getAssembly(),
          "Switch to the " + g.getAssembly() + " genome.", ICON));
    }

    mButton = new RibbonLargeDropDownButton2("Genome", popup)
        .setMinWidth(RibbonSize.COMPACT, 72);
    mButton.setToolTip("Genome", "Change the genome reference.");
    add(mButton);

    mButton.addClickListener(this);

    mButton.setText(mModel.get().getAssembly());

    // mHumanButton.setToolTip("Human", "Human Genome Mode.");
    // mHumanButton.setShowText(false);
    // add(mHumanButton);

    // mMouseButton.setToolTip("Mouse", "Mouse Genome Mode.");
    // mMouseButton.setShowText(false);
    // add(mMouseButton);

    // group.add(mHumanButton);
    // group.add(mMouseButton);

    // mHumanButton.addClickListener(this);
    // mMouseButton.addClickListener(this);

    // if (mModel.get().equals(GenomeAssembly.MM10)) {
    // mMouseButton.setSelected(true);
    // } else {
    // mHumanButton.setSelected(true);
    // }
  }

  /**
   * Change.
   *
   * @param e the e
   */
  private void change(ModernClickEvent e) {
    // if (mHumanButton.isSelected()) {
    // mModel.set(GenomeAssembly.HG19);
    // } else if (mMouseButton.isSelected()) {
    // mModel.set(GenomeAssembly.MM10);
    // } else {
    //
    // }

    mModel.set(e.getMessage());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * ui. event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    change(e);
  }
}
