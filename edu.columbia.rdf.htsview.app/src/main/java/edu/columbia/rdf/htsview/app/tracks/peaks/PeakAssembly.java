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

import java.io.IOException;
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;

import edu.columbia.rdf.edb.Sample;

// TODO: Auto-generated Javadoc
/**
 * The Class PeakAssembly.
 */
public abstract class PeakAssembly {

  /**
   * Download json peaks.
   *
   * @param sample the sample
   * @param peaksId the peaks id
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<GenomicRegion> downloadJsonPeaks(String genome, Sample sample, int peaksId)
      throws IOException {
    return downloadJsonPeaks(genome, sample.getId(), peaksId);
  }

  /**
   * Download json peaks.
   *
   * @param sampleId the sample id
   * @param peaks the peaks
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<GenomicRegion> downloadJsonPeaks(String genome, int sampleId, PeakSet peaks)
      throws IOException {
    return downloadJsonPeaks(genome, sampleId, peaks.getId());
  }

  /**
   * Download json peaks.
   *
   * @param sample the sample
   * @param peaks the peaks
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<GenomicRegion> downloadJsonPeaks(String genome, Sample sample, PeakSet peaks)
      throws IOException {
    return downloadJsonPeaks(genome, sample.getId(), peaks.getId());
  }

  /**
   * Download json peaks.
   *
   * @param sampleId the sample id
   * @param peaksId the peaks id
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public abstract List<GenomicRegion> downloadJsonPeaks(String genome, int sampleId,
      int peaksId) throws IOException;

  /**
   * Gets the json peaks.
   *
   * @param sample the sample
   * @return the json peaks
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<PeakSet> getJsonPeaks(Sample sample) throws IOException {
    return getJsonPeaks(sample.getId());
  }

  /**
   * Returns a list of the Peak sets associated with a given sample.
   *
   * @param sampleId the sample id
   * @return the json peaks
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public abstract List<PeakSet> getJsonPeaks(int sampleId) throws IOException;

}
