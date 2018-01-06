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

import java.awt.Dimension;

import org.jebtk.modern.UI;
import org.jebtk.modern.graphics.icons.ModernIcon;
import org.jebtk.modern.menu.ModernIconMenuItem;
import org.jebtk.modern.menu.ModernScrollPopupMenu;

// TODO: Auto-generated Javadoc
/**
 * The class ResolutionMenu.
 */
public class ResolutionMenu extends ModernScrollPopupMenu {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant MENU_SIZE.
   */
  private static final Dimension MENU_SIZE = new Dimension(200, 36);

  /**
   * The class ResolutionMenuItem.
   */
  private class ResolutionMenuItem extends ModernIconMenuItem {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new resolution menu item.
     *
     * @param text
     *          the text
     */
    public ResolutionMenuItem(String text) {
      super(text);

      UI.setSize(this, MENU_SIZE);
    }

    /**
     * Instantiates a new resolution menu item.
     *
     * @param text
     *          the text
     * @param icon
     *          the icon
     */
    public ResolutionMenuItem(String text, ModernIcon icon) {
      super(text, icon);

      UI.setSize(this, MENU_SIZE);
    }

  }

  /**
   * Instantiates a new resolution menu.
   */
  public ResolutionMenu() {
    addScrollMenuItem(new ResolutionMenuItem("1 bp"));
    addScrollMenuItem(new ResolutionMenuItem("10 bp"));
    addScrollMenuItem(new ResolutionMenuItem("100 bp"));
    addScrollMenuItem(new ResolutionMenuItem("1 kb"));
    addScrollMenuItem(new ResolutionMenuItem("10 kb"));
    addScrollMenuItem(new ResolutionMenuItem("100 kb"));
    addScrollMenuItem(new ResolutionMenuItem("1 Mb"));
  }
}
