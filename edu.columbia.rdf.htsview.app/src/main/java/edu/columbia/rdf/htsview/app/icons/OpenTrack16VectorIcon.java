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
import java.awt.geom.GeneralPath;

import org.jebtk.modern.graphics.icons.ModernVectorIcon;

// TODO: Auto-generated Javadoc
/**
 * Vector based save icon.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class OpenTrack16VectorIcon extends ModernVectorIcon {

  /**
   * The constant CORNER.
   */
  private static final int CORNER = 3;

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
   * java.awt.Rectangle)
   */
  @Override
  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Object... params) {
    g2.setColor(Color.BLACK);

    GeneralPath gp = new GeneralPath();

    gp.moveTo(x, y + 1);
    gp.lineTo(x + 6, y + 1);
    gp.lineTo(x + 6 + CORNER, y + CORNER + 1);
    gp.lineTo(x + w - CORNER - 1, y + CORNER + 1);
    gp.lineTo(x + w - CORNER - 1, y + h - 2);
    gp.lineTo(x, y + h - 2);
    gp.closePath();

    // g2.setStroke(new BasicStroke(2));
    g2.draw(gp);

    gp = new GeneralPath();

    gp.moveTo(x, y + h - 2);
    gp.lineTo(x + CORNER, y + 7);
    gp.lineTo(x + w, y + 7);
    gp.lineTo(x + w - CORNER, y + h - 2);
    gp.closePath();

    g2.fill(gp);
  }
}
