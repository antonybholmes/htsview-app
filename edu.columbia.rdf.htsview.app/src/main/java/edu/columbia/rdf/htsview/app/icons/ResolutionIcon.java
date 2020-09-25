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
package edu.columbia.rdf.htsview.app.icons;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;

/**
 * The Class ResolutionIcon.
 */
public class ResolutionIcon extends ModernVectorIcon {

  /** The m name. */
  private String mName;

  /**
   * Instantiates a new resolution icon.
   *
   * @param name the name
   */
  public ResolutionIcon(String name) {
    mName = name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.graphics.icons.ModernIcon#drawIcon(java.awt.Graphics2D,
   * int, int, int, int)
   */
  @Override
  public void drawIcon(Graphics2D g2,
      int x,
      int y,
      int w,
      int h,
      Props props) {
    g2.setColor(Color.WHITE);
    g2.fillRect(x, y, w, h);

    g2.setColor(ModernWidget.LINE_COLOR);
    g2.drawRect(x, y, w - 1, h - 1);

    g2.setFont(ModernWidget.FONT);

    x = x + (w - g2.getFontMetrics().stringWidth(mName)) / 2;
    y = y + (h + g2.getFontMetrics().getAscent()
        - g2.getFontMetrics().getDescent()) / 2;

    g2.setColor(Color.BLACK);

    g2.drawString(mName, x, y);
  }

}
