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

// TODO: Auto-generated Javadoc
/**
 * The Enum GenesView.
 */
public enum GenesView {

  /** The full. */
  FULL,

  /** The dense. */
  DENSE,

  /** The compact. */
  COMPACT;

  /**
   * Parses the.
   *
   * @param v
   *          the v
   * @return the genes view
   */
  public static GenesView parse(String v) {
    if (v == null) {
      return FULL;
    }

    String vl = v.toLowerCase();

    if (vl.equals("dense")) {
      return DENSE;
    } else if (vl.equals("compact")) {
      return COMPACT;
    } else {
      return FULL;
    }
  }
}
