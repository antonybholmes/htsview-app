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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.http.UrlBuilder;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.edb.EDB;

/**
 * Maintains a connection to a caArray server.
 */
public class PeakAssemblyWeb extends PeakAssembly {

  /** The Constant LOG. */
  private static final Logger LOG = LoggerFactory
      .getLogger(PeakAssemblyWeb.class);

  /** The m auth V 1. */
  private UrlBuilder mAuthV1;

  /**
   * Instantiates a new track assembly web.
   *
   * @param url the login
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public PeakAssemblyWeb(UrlBuilder url) throws IOException {
    mAuthV1 = url;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.htsview.tracks.peaks.PeakAssembly#getJsonPeaks(int)
   */
  @Override
  public List<PeakSet> getJsonPeaks(int sampleId) throws IOException {
    List<PeakSet> ret = new ArrayList<PeakSet>(1000);

    UrlBuilder peaksUrl = mAuthV1.resolve("peaks").param("id", sampleId);

    LOG.info("peaks url: {}", peaksUrl);

    Json json = new JsonParser().parse(peaksUrl.toURL());

    for (int i = 0; i < json.size(); ++i) {
      Json j = json.get(i);

      ret.add(PeakSet.createPeaks(j.getInt(EDB.HEADING_ID),
          j.getString(EDB.HEADING_NAME)));
    }

    return ret;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.htsview.tracks.peaks.PeakAssembly#downloadJsonPeaks(int, int)
   */
  @Override
  public List<GenomicRegion> downloadJsonPeaks(Genome genome,
      int sampleId,
      int peaksId) throws IOException {
    List<GenomicRegion> ret = new ArrayList<GenomicRegion>(1000);

    UrlBuilder peaksUrl = mAuthV1.resolve("peaks").resolve(peaksId);

    LOG.info("peaks url: {}", peaksUrl);

    Json json = new JsonParser().parse(peaksUrl.toURL());

    Json locationsJson = json.get("locations");

    for (int i = 0; i < locationsJson.size(); ++i) {
      GenomicRegion region = GenomicRegion.parse(genome,
          locationsJson.getString(i));

      if (region != null) {
        ret.add(region);
      }
    }

    return ret;
  }
}
