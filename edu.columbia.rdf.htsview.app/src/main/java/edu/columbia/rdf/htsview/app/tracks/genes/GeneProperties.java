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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.graphplot.figure.properties.FontProperties;
import org.jebtk.graphplot.figure.properties.LineProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class GeneProperties.
 */
public class GeneProperties extends ChangeListeners implements ChangeListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The m exons. */
	protected ExonProperties mExons = new ExonProperties();
	
	/** The m line style. */
	protected LineProperties mLineStyle = new LineProperties();
	
	/** The m font. */
	protected FontProperties mFont = new FontProperties();
	
	/**
	 * Instantiates a new gene properties.
	 */
	public GeneProperties() {
		mExons.addChangeListener(this);
		mLineStyle.addChangeListener(this);
		mFont.addChangeListener(this);	
	}
	
	/**
	 * Gets the line style.
	 *
	 * @return the line style
	 */
	public LineProperties getLineStyle() {
		return mLineStyle;
	}
	
	/**
	 * Gets the font.
	 *
	 * @return the font
	 */
	public FontProperties getFont() {
		return mFont;
	}
	
	/**
	 * Gets the exons.
	 *
	 * @return the exons
	 */
	public ExonProperties getExons() {
		return mExons;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.event.ChangeListener#changed(org.abh.common.event.ChangeEvent)
	 */
	@Override
	public void changed(ChangeEvent e) {
		fireChanged();
	}
}
