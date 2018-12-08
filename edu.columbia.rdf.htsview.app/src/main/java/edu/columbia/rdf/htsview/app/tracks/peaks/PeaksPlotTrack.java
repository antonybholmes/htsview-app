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
package edu.columbia.rdf.htsview.app.tracks.peaks;

import org.jebtk.bioinformatics.ext.ucsc.Bed;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.json.JsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.htsview.tracks.TrackDisplayMode;
import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedPlotTrack;

/**
 * The Class PeaksPlotTrack.
 */
public class PeaksPlotTrack extends BedPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** The Constant BAR_HEIGHT. */
  public static final int BAR_HEIGHT = 20;

  /** The Constant HALF_BAR_HEIGHT. */
  public static final int HALF_BAR_HEIGHT = BAR_HEIGHT / 2;

  /** The gap. */
  public static int GAP = HALF_BAR_HEIGHT / 2;

  /** The Constant BLOCK_HEIGHT. */
  public static final int BLOCK_HEIGHT = BAR_HEIGHT + GAP;

  /** The m peaks. */
  private PeakSet mPeaks;

  /** The m sample id. */
  private int mSampleId;

  /**
   * Instantiates a new peaks plot track.
   *
   * @param sampleId the sample id
   * @param peaks the peaks
   * @param bed the bed
   */
  public PeaksPlotTrack(int sampleId, PeakSet peaks, Bed bed) {
    this(sampleId, peaks, bed, TrackDisplayMode.COMPACT);
  }

  /**
   * Instantiates a new peaks plot track.
   *
   * @param sampleId the sample id
   * @param peaks the peaks
   * @param bed the bed
   * @param mode the mode
   */
  public PeaksPlotTrack(int sampleId, PeakSet peaks, Bed bed,
      TrackDisplayMode mode) {
    super(null, bed, mode);

    mSampleId = sampleId;
    mPeaks = peaks;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.ext.ucsc.UcscPlotTrack#getType()
   */
  @Override
  public String getType() {
    return "Peaks";
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.ext.ucsc.UcscPlotTrack#toXml(org.w3c.dom.
   * Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "peaks");
    trackElement.setAttribute("id", Integer.toString(mSampleId));
    trackElement.setAttribute("peak-id", Integer.toString(mPeaks.getId()));
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("color", ColorUtils.toHtml(getFillColor()));

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.ext.ucsc.UcscPlotTrack#toJson(org.abh.
   * common. json.JsonBuilder)
   */
  public void toJson(JsonBuilder json) {
    json.startObject();

    json.add("type", "peaks");
    json.add("id", mSampleId);
    json.add("peak-id", mPeaks.getId());
    json.add("name", getName());
    json.add("color", ColorUtils.toHtml(getFillColor()));

    json.endObject();
  }
}
