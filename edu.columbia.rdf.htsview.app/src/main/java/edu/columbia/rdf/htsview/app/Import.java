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
package edu.columbia.rdf.htsview.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

import javax.swing.SwingWorker;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.ChromosomeService;
import org.jebtk.bioinformatics.genomic.ChromosomeSizesService;
import org.jebtk.bioinformatics.genomic.Human;
import org.jebtk.core.Mathematics;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonObject;
import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.window.ModernWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The class Import.
 */
public class Import {

  /**
   * The constant LOG.
   */
  private static final Logger LOG = LoggerFactory.getLogger(Import.class);

  /**
   * The constant UPPER_MASK.
   */
  private static final int UPPER_MASK = Integer.parseInt("1111111100000000", 2);

  /**
   * The constant LOWER_MASK.
   */
  private static final int LOWER_MASK = Integer.parseInt("11111111", 2);

  /**
   * The class EncodeWorker.
   */
  public static class EncodeWorker extends SwingWorker<Void, Void> {

    /**
     * The m genome.
     */
    private String mGenome;

    /**
     * The m dir.
     */
    // private Chromosome mChr;
    private Path mDir;

    /**
     * The m sam file.
     */
    private Path mSamFile;

    /**
     * The m read length.
     */
    private int mReadLength;

    /**
     * The m parent.
     */
    private ModernWindow mParent;

    /**
     * The m windows.
     */
    private List<Integer> mWindows;

    /**
     * The m name.
     */
    private String mName;

    /**
     * The m organism.
     */
    private String mOrganism;

    /**
     * Instantiates a new encode worker.
     *
     * @param parent
     *          the parent
     * @param samFile
     *          the sam file
     * @param dir
     *          the dir
     * @param name
     *          the name
     * @param organism
     *          the organism
     * @param genome
     *          the genome
     * @param readLength
     *          the read length
     * @param windows
     *          the windows
     */
    public EncodeWorker(ModernWindow parent, Path samFile, Path dir, String name, String organism, String genome,
        int readLength, List<Integer> windows) {
      mParent = parent;
      mSamFile = samFile;
      mDir = dir;
      mName = name;
      mOrganism = organism;
      mWindows = windows;
      mGenome = genome;
      mReadLength = readLength;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.SwingWorker#doInBackground()
     */
    @Override
    protected Void doInBackground() throws Exception {

      FileUtils.mkdir(mDir);

      int reads = readCount(mSamFile);

      createMetaFile(mDir, mName, mOrganism, mGenome, mReadLength, reads);

      for (int window : mWindows) {
        for (Chromosome chr : Human.CHROMOSOMES) {
          encodeSam16Bit(mSamFile, mDir, chr, window, mGenome, mReadLength);
        }
      }

      return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.SwingWorker#done()
     */
    @Override
    public void done() {
      ModernMessageDialog.createInformationDialog(mParent, PathUtils.getName(mSamFile) + " has been imported.",
          "You can now load this track.");
    }
  }

  /**
   * Creates the meta file.
   *
   * @param dir
   *          the dir
   * @param name
   *          the name
   * @param organism
   *          the organism
   * @param genome
   *          the genome
   * @param readLength
   *          the read length
   * @param reads
   *          the reads
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static void createMetaFile(Path dir, String name, String organism, String genome, int readLength, int reads)
      throws IOException {

    Path file = dir.resolve("meta.json");

    LOG.info("Writing {}...", file);

    Json json = new JsonObject();

    json.add("Name", name);
    json.add("Organism", organism);
    json.add("Genome", genome);
    json.add("Read Length", readLength);
    json.add("Mapped Reads", reads);

    Json.write(json, file);

  }

  /**
   * Read count.
   *
   * @param samFile
   *          the sam file
   * @return the int
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static int readCount(Path samFile) throws IOException {
    BufferedReader reader = FileUtils.newBufferedReader(samFile);

    int count = 0;

    try {
      while (reader.readLine() != null) {
        ++count;
      }
    } finally {
      reader.close();
    }

    return count;
  }

  /**
   * Encode sam16 bit.
   *
   * @param samFile
   *          the sam file
   * @param dir
   *          the dir
   * @param chr
   *          the chr
   * @param window
   *          the window
   * @param genome
   *          the genome
   * @param readLength
   *          the read length
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   * @throws ParseException
   *           the parse exception
   */
  public static void encodeSam16Bit(Path samFile, Path dir, Chromosome chr, int window, String genome, int readLength)
      throws IOException, ParseException {

    int size = (ChromosomeSizesService.getInstance().getSizes(genome).getSize(chr) / window) + 1;

    LOG.info("Reading {} {} {}...", dir, samFile, size);

    int[] counts = Mathematics.zerosIntArray(size);

    BufferedReader reader = FileUtils.newBufferedReader(samFile);

    String line;
    List<String> tokens;

    Chromosome currentChr = null;

    try {
      while ((line = reader.readLine()) != null) {
        tokens = TextUtils.tabSplit(line);

        Chromosome c = ChromosomeService.getInstance().guess(samFile, tokens.get(2));

        if (c == null || (currentChr != null && !c.equals(currentChr))) {
          break;
        }

        if (!c.equals(currentChr)) {
          continue;
        }

        int start = TextUtils.parseInt(tokens.get(3)) - 1;
        int end = start + readLength - 1;

        int winStart = start / window;
        int winEnd = end / window;

        for (int i = winStart; i <= winEnd; ++i) {
          ++counts[i];
        }

        if (c.equals(chr)) {
          currentChr = c;
        }
      }
    } finally {
      reader.close();
    }

    byte[] bytes = Mathematics.zerosByteArray(size * 2);

    int p = 0;
    int encode;

    LOG.info("Encoding {}...", size);

    for (int i = 0; i < size; ++i) {
      encode = Math.min(counts[i], 65535);

      // shift the upper 8 bits into the lower 8 bits so we can write
      // it out as a byte
      bytes[p] = (byte) ((encode & UPPER_MASK) >> 8);

      bytes[p + 1] = (byte) (encode & LOWER_MASK);

      p += 2;
    }

    Path out = dir.resolve(chr.toString() + ".counts.win." + window + ".16bit");

    LOG.info("Writing {}...", out);

    OutputStream fo = FileUtils.newOutputStream(out);

    try {
      fo.write(bytes);
    } finally {
      fo.close();
    }

  }

}
