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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jebtk.bioinformatics.gapsearch.FixedGapSearch;
import org.jebtk.bioinformatics.genomic.GenomicRegion;

// TODO: Auto-generated Javadoc
/**
 * The Class Segments.
 */
public class Segments implements Iterable<Segment> {

  /** The m segments. */
  private List<Segment> mSegments = new ArrayList<Segment>();

  /** The m updated. */
  // private String mName;
  private boolean mUpdated = false;

  /** The m search. */
  private FixedGapSearch<Segment> mSearch;
  // private GapSearch<Segment> mSearch;

  /**
   * Adds the.
   *
   * @param segment the segment
   */
  public void add(Segment segment) {
    mSegments.add(segment);

    mUpdated = true;
  }

  // @Override
  // public String getName() {
  // return mName;
  // }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Segment> iterator() {
    return mSegments.iterator();
  }

  /**
   * Search for segments in a region.
   *
   * @param region the region
   * @return the list
   */
  public List<Segment> search(GenomicRegion region) {

    if (mUpdated) {
      mSearch = new FixedGapSearch<Segment>();

      for (Segment s : mSegments) {
        mSearch.add(s, s);
      }

      mUpdated = false;
    }

    return mSearch.getFeatureSet(region);
  }
}
