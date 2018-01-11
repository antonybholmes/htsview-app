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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.core.NameProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class ChrSegments.
 */
public class ChrSegments implements Iterable<Chromosome>, NameProperty {

  /** The m segments. */
  private Map<Chromosome, Segments> mSegments = new TreeMap<Chromosome, Segments>();

  /** The m name. */
  private String mName;

  /**
   * Instantiates a new chr segments.
   *
   * @param name the name
   */
  public ChrSegments(String name) {
    mName = name;
  }

  /**
   * Adds the.
   *
   * @param chr the chr
   * @param segments the segments
   */
  public void add(Chromosome chr, Segments segments) {
    mSegments.put(chr, segments);
  }

  /**
   * Size.
   *
   * @return the int
   */
  public int size() {
    return mSegments.size();
  }

  /**
   * Gets the.
   *
   * @param chr the chr
   * @return the segments
   */
  public Segments get(Chromosome chr) {
    return mSegments.get(chr);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Chromosome> iterator() {
    return mSegments.keySet().iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.NameProperty#getName()
   */
  @Override
  public String getName() {
    return mName;
  }

  /**
   * Contains.
   *
   * @param chr the chr
   * @return true, if successful
   */
  public boolean contains(Chromosome chr) {
    return mSegments.containsKey(chr);
  }
}
