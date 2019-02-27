/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.htsview.app;

import java.awt.Color;
import java.awt.Font;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeEventProducer;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernCheckButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.font.FontService;
import org.jebtk.modern.font.FontSwatchButton;
import org.jebtk.modern.ribbon.RibbonCheckButton;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.window.ModernRibbonWindow;

/**
 * Standardized ribbon menu section for providing basic cut, copy and paste
 * functionality to the currently highlighted control that supports clipboard
 * operations.
 *
 * @author Antony Holmes
 *
 */
public class FontRibbonSection extends RibbonSection
    implements ModernClickListener, ChangeEventProducer {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The bold button.
   */
  private ModernCheckButton mBoldButton = new RibbonCheckButton(
      AssetService.getInstance().loadIcon("font_bold", 16));

  /**
   * The italic button.
   */
  private ModernCheckButton mItalicButton = new RibbonCheckButton(
      AssetService.getInstance().loadIcon("font_italic", 16));

  /**
   * The underline button.
   */
  private ModernCheckButton mUnderlineButton = new RibbonCheckButton(
      AssetService.getInstance().loadIcon("font_underline", 16));

  /** The m strikethrough button. */
  private ModernCheckButton mStrikethroughButton = new RibbonCheckButton(
      AssetService.getInstance().loadIcon("font_strikethrough", 16));

  /**
   * The font color button.
   */
  private FontSwatchButton mFontColorButton = null;

  /** The m listeners. */
  private ChangeListeners mListeners = new ChangeListeners();

  /**
   * Instantiates a new font ribbon section.
   *
   * @param parent the parent
   */
  public FontRibbonSection(ModernRibbonWindow parent) {
    super(parent.getRibbon(), "Font");

    mFontColorButton = new FontSwatchButton(parent);

    add(mFontColorButton);
    add(mBoldButton);
    add(mItalicButton);
    add(mUnderlineButton);
    add(mStrikethroughButton);

    mBoldButton.setToolTip("Bold", "Make your text bold.");
    mItalicButton.setToolTip("Italic", "Italicize your text.");
    mUnderlineButton.setToolTip("Underline", "Underline your text.");
    mStrikethroughButton.setToolTip("Strikethough",
        "Cross something out by drawing a line through it.");

    // mUnderlineButton.setEnabled(false);

    mBoldButton.addClickListener(this);
    mItalicButton.addClickListener(this);
    mUnderlineButton.addClickListener(this);
    mFontColorButton.addClickListener(this);
    mStrikethroughButton.addClickListener(this);
  }

  /**
   * Setup.
   *
   * @param font the font
   * @param fontColor the font color
   */
  public void setup(Font font, Color fontColor) {
    if (font == null) {
      return;
    }

    mBoldButton.setSelected(font.isBold());
    mItalicButton.setSelected(font.isItalic());

    mFontColorButton.setSelectedColor(fontColor);
  }

  /**
   * Gets the user font.
   *
   * @return the user font
   */
  public Font getUserFont() {
    return FontService.getInstance().loadFont("Arial",
        16,
        mBoldButton.isSelected(),
        mItalicButton.isSelected(),
        mUnderlineButton.isSelected(),
        mStrikethroughButton.isSelected());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  public void clicked(ModernClickEvent e) {
    fireChanged();
  }

  /**
   * Gets the font color.
   *
   * @return the font color
   */
  public Color getFontColor() {
    return mFontColorButton.getSelectedColor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.event.ChangeEventProducer#addChangeListener(org.abh.common.
   * event.ChangeListener)
   */
  @Override
  public void addChangeListener(ChangeListener l) {
    mListeners.addChangeListener(l);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.event.ChangeEventProducer#removeChangeListener(org.abh.
   * common. event.ChangeListener)
   */
  @Override
  public void removeChangeListener(ChangeListener l) {
    mListeners.removeChangeListener(l);
  }

  /**
   * Fire changed.
   */
  public void fireChanged() {
    fireChanged(new ChangeEvent(this));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.event.ChangeEventProducer#fireChanged(org.abh.common.event.
   * ChangeEvent)
   */
  @Override
  public void fireChanged(ChangeEvent e) {
    mListeners.fireChanged(e);
  }
}
