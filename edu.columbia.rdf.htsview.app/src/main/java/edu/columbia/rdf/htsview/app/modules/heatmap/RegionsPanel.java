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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.panel.ModernLineBorderPanel;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.text.ModernClipboardTextArea;
import org.jebtk.modern.text.ModernTextArea;

/**
 * The Class RegionsPanel.
 */
public class RegionsPanel extends ModernWidget {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m genes field. */
  private ModernTextArea mGenesField = new ModernClipboardTextArea();

  /** The m genome model. */
  private GenomeModel mGenomeModel;

  /**
   * Instantiates a new regions panel.
   *
   * @param genomeModel the genome model
   */
  public RegionsPanel(GenomeModel genomeModel) {
    mGenomeModel = genomeModel;

    // super(BoxLayout.PAGE_AXIS);

    /// Box box = Box.createVerticalBox();
    // box.setAlignmentY(TOP_ALIGNMENT);

    // box.add(new ModernDialogHeadingLabel("Gene Symbols"));
    // box.add(ModernTheme.createVerticalGap());

    // add(new ModernLabel("Plot locations"), BorderLayout.PAGE_START);

    ModernScrollPane scrollPane = new ModernScrollPane(mGenesField);
    scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    // Ui.setSize(scrollPane, new Dimension(150, 400));
    // box.add(new ModernDialogContentPanel(scrollPane));
    // box.setMinimumSize(new Dimension(150, 0));

    setBody(new ModernLineBorderPanel(scrollPane));
  }

  /**
   * Gets the regions.
   *
   * @return the regions
   * @throws ParseException the parse exception
   */
  public List<HeatMapIdLocation> getRegions() {
    List<String> lines = TextUtils.fastSplit(mGenesField.getText().trim(),
        TextUtils.NEW_LINE_DELIMITER);

    List<HeatMapIdLocation> ret = new ArrayList<HeatMapIdLocation>();

    for (String line : lines) {
      // line = line.replaceAll(TextUtils.COMMA_DELIMITER,
      // TextUtils.EMPTY_STRING);

      if (line.length() > 0) {
        for (String id : TextUtils.scSplit(line)) {
          HeatMapIdLocation loc = HeatMapIdLocation.parse(id, mGenomeModel);

          if (loc != null) {
            ret.add(loc);
          }
        }
      }
    }

    return ret;
  }

  /**
   * Sets the text.
   *
   * @param text the new text
   */
  public void setText(String text) {
    mGenesField.setText(text);
  }
}
