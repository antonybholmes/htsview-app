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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.figure.properties.LineProperties;

import edu.columbia.rdf.htsview.app.tracks.OtherGeneProperties;
import edu.columbia.rdf.htsview.app.tracks.VariantGeneProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class GenesProperties.
 */
public class GenesProperties extends ChangeListeners implements ChangeListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m variant gene. */
  private GeneProperties mVariantGene = new VariantGeneProperties();

  /** The m other gene. */
  private GeneProperties mOtherGene = new OtherGeneProperties();

  /** The m UTR. */
  private ExonProperties mUTR = new ExonProperties();

  /** The m style. */
  private LineProperties mStyle = new LineProperties();

  /** The m max genes. */
  private int mMaxGenes = SettingsService.getInstance().getAsInt("edb.reads.max-display-genes");

  /** The m draw tss arrows. */
  private boolean mDrawTssArrows = false;

  /** The m draw exon arrows. */
  private boolean mDrawExonArrows = false;

  /** The m view. */
  private GenesView mView = GenesView.FULL;

  /** The m draw arrows. */
  private boolean mDrawArrows = true;

  /** The m arrow color. */
  private Color mArrowColor = Color.GRAY;

  /**
   * Instantiates a new genes properties.
   */
  public GenesProperties() {
    mVariantGene.addChangeListener(this);
    mOtherGene.addChangeListener(this);
    mUTR.addChangeListener(this);

    mUTR.setFillColor(Color.WHITE);
    mUTR.getLineStyle().setColor(Color.BLACK);

    mStyle.setStroke(2);
  }

  /**
   * Gets the variant gene.
   *
   * @return the variant gene
   */
  public GeneProperties getVariantGene() {
    return mVariantGene;
  }

  /**
   * Gets the other gene.
   *
   * @return the other gene
   */
  public GeneProperties getOtherGene() {
    return mOtherGene;
  }

  /**
   * Gets the utr.
   *
   * @return the utr
   */
  public ExonProperties getUTR() {
    return mUTR;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.event.ChangeListener#changed(org.abh.common.event.ChangeEvent)
   */
  @Override
  public void changed(ChangeEvent e) {
    fireChanged();
  }

  /**
   * Gets the max genes.
   *
   * @return the max genes
   */
  public int getMaxGenes() {
    return mMaxGenes;
  }

  /**
   * Sets the max genes.
   *
   * @param max
   *          the new max genes
   */
  public void setMaxGenes(int max) {
    mMaxGenes = max;

    fireChanged();
  }

  /**
   * Gets the draw tss arrows.
   *
   * @return the draw tss arrows
   */
  public boolean getDrawTssArrows() {
    return mDrawTssArrows;
  }

  /**
   * Sets the show tss arrows.
   *
   * @param draw
   *          the new show tss arrows
   */
  public void setShowTssArrows(boolean draw) {
    mDrawTssArrows = draw;

    fireChanged();
  }

  /**
   * Gets the show exon arrows.
   *
   * @return the show exon arrows
   */
  public boolean getShowExonArrows() {
    return mDrawExonArrows;
  }

  /**
   * Sets the show exon arrows.
   *
   * @param draw
   *          the new show exon arrows
   */
  public void setShowExonArrows(boolean draw) {
    mDrawExonArrows = draw;

    fireChanged();
  }

  /**
   * Gets the style.
   *
   * @return the style
   */
  public LineProperties getStyle() {
    return mStyle;
  }

  /**
   * Sets the view.
   *
   * @param view
   *          the new view
   */
  public void setView(GenesView view) {
    mView = view;
  }

  /**
   * Gets the view.
   *
   * @return the view
   */
  public GenesView getView() {
    return mView;
  }

  /**
   * Sets the show arrows.
   *
   * @param draw
   *          the new show arrows
   */
  public void setShowArrows(boolean draw) {
    mDrawArrows = draw;

    fireChanged();
  }

  /**
   * Gets the show arrows.
   *
   * @return the show arrows
   */
  public boolean getShowArrows() {
    return mDrawArrows;
  }

  /**
   * Gets the arrow color.
   *
   * @return the arrow color
   */
  public Color getArrowColor() {
    return mArrowColor;
  }

  public void setArrowColor(Color color) {
    mArrowColor = color;

    fireChanged();
  }
}
