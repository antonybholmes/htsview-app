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

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.window.ModernWindow;
import edu.columbia.rdf.matcalc.figure.ColoredPlotControl;

// TODO: Auto-generated Javadoc
/**
 * The Class GenePlotElement.
 */
public class GenePlotElement extends Box {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m genes. */
  private GeneProperties mGenes;

  /** The m line color. */
  private ColoredPlotControl mLineColor;

  /** The m exon fill color. */
  private ColoredPlotControl mExonFillColor;

  /** The m exon line color. */
  private ColoredPlotControl mExonLineColor;

  /**
   * The Class LineEvents.
   */
  private class LineEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.event.
     * ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      mGenes.getLineStyle().setColor(mLineColor.getSelectedColor());
    }
  }

  /**
   * The Class ExonFillEvents.
   */
  private class ExonFillEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.event.
     * ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      mGenes.getExons().setFillColor(mExonFillColor.getSelectedColor());
    }
  }

  /**
   * The Class ExonLineEvents.
   */
  private class ExonLineEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.event.
     * ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      mGenes.getExons().getLineStyle().setColor(mExonLineColor.getSelectedColor());
    }
  }

  /**
   * Instantiates a new gene plot element.
   *
   * @param parent
   *          the parent
   * @param genes
   *          the genes
   */
  public GenePlotElement(ModernWindow parent, GeneProperties genes) {
    super(BoxLayout.PAGE_AXIS);

    mGenes = genes;

    mLineColor = new ColoredPlotControl(parent, "Line", genes.getLineStyle().getColor());

    add(mLineColor);

    add(ModernPanel.createVGap());

    mExonLineColor = new ColoredPlotControl(parent, "Exon Outline", genes.getExons().getLineStyle().getColor());

    add(mExonLineColor);

    add(ModernPanel.createVGap());

    mExonFillColor = new ColoredPlotControl(parent, "Exon Fill", genes.getExons().getFillStyle().getColor());

    add(mExonFillColor);

    add(ModernPanel.createVGap());

    mLineColor.addClickListener(new LineEvents());
    mExonLineColor.addClickListener(new ExonLineEvents());
    mExonFillColor.addClickListener(new ExonFillEvents());
  }

}
