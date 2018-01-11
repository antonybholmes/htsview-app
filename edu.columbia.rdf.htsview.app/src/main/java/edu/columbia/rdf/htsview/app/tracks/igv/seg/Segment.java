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
package edu.columbia.rdf.htsview.app.tracks.igv.seg;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;

// TODO: Auto-generated Javadoc
/**
 * The Class Segment.
 */
public class Segment extends GenomicRegion {

  /** The m markers. */
  private int mMarkers;

  /** The m mean. */
  private double mMean;

  /**
   * Instantiates a new segment.
   *
   * @param chr the chr
   * @param start the start
   * @param end the end
   * @param markers the markers
   * @param mean the mean
   */
  public Segment(Chromosome chr, int start, int end, int markers, double mean) {
    super(chr, start, end);

    mMarkers = markers;
    mMean = mean;
  }

  /**
   * Gets the markers.
   *
   * @return the markers
   */
  public int getMarkers() {
    return mMarkers;
  }

  /**
   * Gets the mean.
   *
   * @return the mean
   */
  public double getMean() {
    return mMean;
  }

}
