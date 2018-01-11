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

import java.awt.Graphics2D;

import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.UI;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton;

// TODO: Auto-generated Javadoc
/**
 * The class ResolutionRibbonButton.
 */
public class ResolutionRibbonButton extends RibbonLargeDropDownButton {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /*
   * private class ClickEvents implements ModernClickListener {
   * 
   * 
   * 
   * @Override public void clicked(ModernClickEvent e) { mText1 =
   * e.getMessage();
   * 
   * repaint(); }
   * 
   * }
   */

  /**
   * Instantiates a new resolution ribbon button.
   */
  public ResolutionRibbonButton() {
    super(TextUtils.EMPTY_STRING, new ResolutionMenu());

    // addClickListener(new ClickEvents());

    UI.setSize(this, Ribbon.TEXT_BUTTON_SIZE);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.ribbon2.RibbonLargeDropDownMenuButton2#
   * drawForegroundAA( java.awt.Graphics2D)
   */
  @Override
  public void drawForegroundAAText(Graphics2D g2) {
    int iconX = PADDING;
    int iconY = getTextYPosCenter(g2, getHeight());

    g2.setColor(TEXT_COLOR);
    g2.drawString(mText1, iconX, iconY);

    iconX = mRect.getW() - 16 - PADDING;
    iconY = (mRect.getH() - 16) / 2;

    TRIANGLE_ICON.drawIcon(g2, iconX, iconY, 16);
  }
}
