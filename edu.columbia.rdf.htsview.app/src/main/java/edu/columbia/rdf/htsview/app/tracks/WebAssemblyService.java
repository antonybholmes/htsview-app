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
package edu.columbia.rdf.htsview.app.tracks;

import edu.columbia.rdf.htsview.app.tracks.peaks.PeakAssembly;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;

/**
 * Stores references to assembly services connecting to databases. This is so
 * only one connection object need be in existence.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class WebAssemblyService {
  /**
   * The Class SettingsServiceLoader.
   */
  private static class AssemblyServiceLoader {

    /** The Constant INSTANCE. */
    private static final WebAssemblyService INSTANCE = new WebAssemblyService();
  }

  /**
   * Gets the single instance of SettingsService.
   *
   * @return single instance of SettingsService
   */
  public static WebAssemblyService getInstance() {
    return AssemblyServiceLoader.INSTANCE;
  }

  /** The m peak assembly. */
  private PeakAssembly mPeakAssembly = null;

  /** The m sample assembly. */
  private SampleAssembly mSampleAssembly = null;

  /**
   * Instantiates a new web assembly service.
   */
  private WebAssemblyService() {
    // Do nothing
  }

  /**
   * Sets the peak assembly.
   *
   * @param peakAssembly the new peak assembly
   */
  public void setPeakAssembly(PeakAssembly peakAssembly) {
    mPeakAssembly = peakAssembly;
  }

  /**
   * Gets the peak assembly.
   *
   * @return the peak assembly
   */
  public PeakAssembly getPeakAssembly() {
    return mPeakAssembly;
  }

  /**
   * Sets the sample assembly.
   *
   * @param sampleAssembly the new sample assembly
   */
  public void setSampleAssembly(SampleAssembly sampleAssembly) {
    mSampleAssembly = sampleAssembly;
  }

  /**
   * Gets the sample assembly.
   *
   * @return the sample assembly
   */
  public SampleAssembly getSampleAssembly() {
    return mSampleAssembly;
  }
}
