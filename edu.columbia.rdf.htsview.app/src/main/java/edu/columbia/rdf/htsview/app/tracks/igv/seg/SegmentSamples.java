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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.ChromosomeService;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomeService;
import org.jebtk.core.NameProperty;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.Io;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.text.TextUtils;

/**
 * The Class SegmentSamples.
 */
public class SegmentSamples implements Iterable<String>, NameProperty {

  /** The m segments. */
  private Map<String, ChrSegments> mSegments = new TreeMap<String, ChrSegments>();

  /** The m name. */
  private String mName;

  /**
   * Instantiates a new segment samples.
   *
   * @param name the name
   */
  public SegmentSamples(String name) {
    mName = name;
  }

  /**
   * Adds the.
   *
   * @param segments the segments
   */
  public void add(ChrSegments segments) {
    mSegments.put(segments.getName(), segments);
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
   * @param name the name
   * @return the chr segments
   */
  public ChrSegments get(String name) {
    return mSegments.get(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<String> iterator() {
    return mSegments.keySet().iterator();
  }

  /**
   * Parses the.
   *
   * @param file the file
   * @return the segment samples
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static SegmentSamples parse(Path file) throws IOException {
    BufferedReader reader = FileUtils.newBufferedReader(file);

    String line;
    List<String> tokens;

    SegmentSamples samples = new SegmentSamples(PathUtils.getNameNoExt(file));

    try {
      // Skip header
      reader.readLine();

      while ((line = reader.readLine()) != null) {
        if (Io.isEmptyLine(line)) {
          continue;
        }

        tokens = TextUtils.tabSplit(line);

        String name = tokens.get(0);
        Genome genome = GenomeService.getInstance().guessGenome(file);
        Chromosome chr = ChromosomeService.getInstance().guessChr(file,
            tokens.get(1));
        
        int start = Integer.parseInt(tokens.get(2));
        int end = Integer.parseInt(tokens.get(3));
        int markers = Integer.parseInt(tokens.get(4));
        double mean = Double.parseDouble(tokens.get(5));

        if (!samples.mSegments.containsKey(name)) {
          samples.mSegments.put(name, new ChrSegments(name));
        }

        if (!samples.mSegments.get(name).contains(chr)) {
          samples.mSegments.get(name).add(chr, new Segments());
        }

        samples.mSegments.get(name).get(chr)
            .add(new Segment(genome, chr, start, end, markers, mean));
      }
    } finally {
      reader.close();
    }

    return samples;
  }

}
