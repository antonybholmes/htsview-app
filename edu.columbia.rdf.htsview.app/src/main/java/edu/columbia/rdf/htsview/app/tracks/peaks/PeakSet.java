/**
 * Copyright 2017 Antony Holmes
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
package edu.columbia.rdf.htsview.app.tracks.peaks;

import org.jebtk.bioinformatics.annotation.Type;

// TODO: Auto-generated Javadoc
/**
 * The Class PeakSet represents a set of peaks associated to a sample.
 */
public class PeakSet extends Type {

	/**
	 * Instantiates a new peaks.
	 *
	 * @param id the id
	 * @param name the name
	 */
	public PeakSet(int id, String name) {
		super(id, name);
	}

	/**
	 * Creates the peaks.
	 *
	 * @param id the id
	 * @param name the name
	 * @return the peaks
	 */
	public static PeakSet createPeaks(int id, String name) {
		return new PeakSet(id, name);
	}

}
