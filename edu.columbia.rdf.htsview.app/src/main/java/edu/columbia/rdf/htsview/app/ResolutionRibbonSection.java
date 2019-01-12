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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;
import org.jebtk.modern.ribbon.RibbonSection;

import edu.columbia.rdf.htsview.tracks.ResolutionModel;

/**
 * Allows user to select the resolution to view sequences.
 *
 * @author Antony Holmes
 */
public class ResolutionRibbonSection extends RibbonSection
    implements ModernClickListener, ChangeListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  // private ModernComboBox mResCombo = new
  // ModernComboBox(ModernWidget.SMALL_SIZE);

  /**
   * The m res combo.
   */
  // private ResolutionComboBox mResCombo =
  // new ResolutionComboBox(); //ResolutionRibbonButton

  /**
   * The m model.
   */
  private ResolutionModel mModel;

  private RibbonLargeDropDownButton2 mButton = new ResolutionRibbonButton();

  /**
   * The class ModelEvents.
   */
  private class ModelEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      update();
    }

  }

  /**
   * Instantiates a new resolution ribbon section2.
   *
   * @param ribbon the ribbon
   * @param model the model
   */
  public ResolutionRibbonSection(Ribbon ribbon, ResolutionModel model) {
    super(ribbon, "Resolution");

    mModel = model;

    mModel.addChangeListener(new ModelEvents());

    // mResCombo.addMenuItem("10 bp");
    // mResCombo.addMenuItem("100 bp");
    // mResCombo.addMenuItem("1 kb");
    // mResCombo.addMenuItem("10 kb");
    // mResCombo.addMenuItem("100 kb");

    // UI.setSize(mResCombo, ModernWidget.SMALL_SIZE);

    // Box box = new RibbonStripContainer();
    // box.add(mResCombo);
    // add(box);

    // mResCombo.addClickListener(this);

    mButton.setText("10 bp");
    mButton.addClickListener(this);
    add(mButton);

    update();
  }

  /**
   * Update.
   */
  private void update() {
    switch (mModel.get()) {
    case 1:
      mButton.setText("1 bp");
      break;
    case 10:
      mButton.setText("10 bp");
      break;
    case 100:
      mButton.setText("100 bp");
      break;
    case 1000:
      mButton.setText("1 kb");
      break;
    case 10000:
      mButton.setText("10 kb");
      break;
    case 100000:
      mButton.setText("100 kb");
      break;
    case 1000000:
      mButton.setText("1 Mb");
      break;
    default:
      mButton.setText("100 bp");
      break;
    }
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
    action();
  }

  /**
   * Action.
   */
  private void action() {
    if (mButton.getText().equals("1 bp")) {
      mModel.set(1);
    } else if (mButton.getText().equals("10 bp")) {
      mModel.set(10);
    } else if (mButton.getText().equals("100 bp")) {
      mModel.set(100);
    } else if (mButton.getText().equals("1 kb")) {
      mModel.set(1000);
    } else if (mButton.getText().equals("10 kb")) {
      mModel.set(10000);
    } else if (mButton.getText().equals("100 kb")) {
      mModel.set(100000);
    } else if (mButton.getText().equals("1 Mb")) {
      mModel.set(1000000);
    } else {
      mModel.set(100);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
   */
  @Override
  public void changed(ChangeEvent e) {
    action();
  }
}
