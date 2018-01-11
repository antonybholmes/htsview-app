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

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;

import org.jebtk.graphplot.figure.Axes;
import org.jebtk.modern.collapsepane.ModernCollapsePane;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.tabs.TabsModel;
import org.jebtk.modern.tabs.TabsViewPanel;
import org.jebtk.modern.tabs.TextTabs;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.app.tracks.genes.GenePlotElement;
import edu.columbia.rdf.htsview.app.tracks.genes.GenesProperties;
import edu.columbia.rdf.matcalc.figure.AxisControl;

// TODO: Auto-generated Javadoc
/**
 * The class FormatPanel.
 */
public class FormatPanel extends ModernWidget {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new format panel.
   *
   * @param parent the parent
   * @param space the space
   * @param genes the genes
   */
  public FormatPanel(ModernWindow parent, Axes space, GenesProperties genes) {

    TabsModel groupTabsModel = new TabsModel();
    TextTabs groupTabs = new TextTabs(groupTabsModel);

    ModernCollapsePane rightPanel;

    Box box;

    Component element;

    //
    // Axes
    //

    rightPanel = new ModernCollapsePane();

    box = Box.createVerticalBox();

    element = new AxisControl(parent, space.getX1Axis(), false);

    box.add(element);

    box.add(createVGap());

    rightPanel.addTab("X Axis", box, true);

    box = Box.createVerticalBox();

    element = new AxisControl(parent, space.getY1Axis(), false);

    box.add(element);

    box.add(createVGap());

    rightPanel.addTab("Y Axis", box, true);

    groupTabsModel.addTab("Axes", new ModernScrollPane(rightPanel));

    //
    // Series
    //

    /*
     * rightPanel = new ModernCollapsePane();
     * 
     * box = Box.createVerticalBox();
     * 
     * 
     * element = new AxisPlotElement(parent,
     * space.getGraphProperties().getXAxisProperties(), false);
     * 
     * box.add(element);
     * 
     * box.add(ModernTheme.createVerticalGap());
     * 
     * rightPanel.addTab("X Axis", box, true);
     * 
     * box = Box.createVerticalBox();
     * 
     * element = new AxisPlotElement(parent,
     * space.getGraphProperties().getYAxisProperties(), false);
     * 
     * box.add(element);
     * 
     * box.add(ModernTheme.createVerticalGap());
     * 
     * rightPanel.addTab("Y Axis", box, true);
     * 
     * groupTabsModel.addTab("Axes", new ModernScrollPane(rightPanel));
     */

    //
    // Genes
    //

    rightPanel = new ModernCollapsePane();

    box = Box.createVerticalBox();

    element = new GenePlotElement(parent, genes.getVariantGene());

    box.add(element);

    box.add(createVGap());

    rightPanel.addTab("Main Variant", box, true);

    box = Box.createVerticalBox();

    element = new GenePlotElement(parent, genes.getOtherGene());

    box.add(element);

    box.add(createVGap());

    rightPanel.addTab("Other Genes", box, true);

    groupTabsModel.addTab("Genes", new ModernScrollPane(rightPanel));

    add(groupTabs, BorderLayout.PAGE_START);

    TabsViewPanel viewPanel = new TabsViewPanel(groupTabsModel);
    viewPanel.setBorder(BORDER);
    add(viewPanel, BorderLayout.CENTER);

    groupTabsModel.changeTab(0);
  }
}
