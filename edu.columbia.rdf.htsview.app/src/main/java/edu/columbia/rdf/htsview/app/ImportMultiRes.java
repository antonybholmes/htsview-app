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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomeService;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.collections.TreeMapCreator;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonObject;
import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.window.ModernWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class ImportMultiRes.
 */
public class ImportMultiRes {

  /**
   * The constant LOG.
   */
  private static final Logger LOG = LoggerFactory
      .getLogger(ImportMultiRes.class);

  /**
   * The constant LOWER_MASK.
   */
  private static final int LOWER_MASK = Integer.parseInt("11111111", 2);

  /**
   * The constant BLOCK_SIZE_BYTES.
   */
  private static final int BLOCK_SIZE_BYTES = 20;

  /**
   * The constant READ_START_BYTES.
   */
  private static final int READ_START_BYTES = 4;

  /**
   * The constant MAX_BLOCK_WIDTH.
   */
  public static final int MAX_BLOCK_WIDTH = 1000000000;

  /**
   * The constant BINS.
   */
  private static final int BINS = 10;

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
     * The m name.
     */
    private String mName;

    /**
     * The m organism.
     */
    private String mOrganism;

    /**
     * The m min res.
     */
    private int mMinRes;

    /**
     * Instantiates a new encode worker.
     *
     * @param parent the parent
     * @param samFile the sam file
     * @param dir the dir
     * @param name the name
     * @param organism the organism
     * @param genome the genome
     * @param readLength the read length
     * @param minRes the min res
     */
    public EncodeWorker(ModernWindow parent, Path samFile, Path dir,
        String name, String organism, String genome, int readLength,
        int minRes) {
      mParent = parent;
      mSamFile = samFile;
      mDir = dir;
      mName = name;
      mOrganism = organism;
      mMinRes = minRes;
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

      encode(mSamFile, mDir, mName, mOrganism, mGenome, mReadLength, mMinRes);

      return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.SwingWorker#done()
     */
    @Override
    public void done() {
      ModernMessageDialog.createInformationDialog(mParent,
          PathUtils.getName(mSamFile) + " has been imported.",
          "You can now load this track.");
    }
  }

  /**
   * The class Block.
   */
  public static class Block implements Comparable<Block> {

    /**
     * The start.
     */
    public int start;

    /**
     * The width.
     */
    public int width;

    /**
     * The start offset.
     */
    public int startOffset;

    /**
     * The end offset.
     */
    public int endOffset;

    /**
     * The children.
     */
    List<Block> children = new ArrayList<Block>(10);

    /**
     * Instantiates a new block.
     *
     * @param start the start
     * @param width the width
     * @param so the so
     * @param eo the eo
     */
    public Block(int start, int width, int so, int eo) {
      this.start = start;
      this.width = width;
      this.startOffset = so;
      this.endOffset = eo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Block b) {
      if (start > b.start) {
        return 1;
      } else if (start < b.start) {
        return -1;
      } else {
        return 0;
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return start + " " + width;
    }
  }

  /**
   * Creates the meta file.
   *
   * @param dir the dir
   * @param name the name
   * @param organism the organism
   * @param genome the genome
   * @param readLength the read length
   * @param reads the reads
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void createMetaFile(Path dir,
      String name,
      String organism,
      String genome,
      int readLength,
      int reads) throws IOException {
    Json json = new JsonObject();

    json.add("Name", name);
    json.add("Organism", organism);
    json.add("Genome", genome);
    json.add("Read Length", readLength);
    json.add("Mapped Reads", reads);

    Json.write(json, dir.resolve("meta.json"));

  }

  /**
   * Encode.
   *
   * @param samFile the sam file
   * @param dir the dir
   * @param name the name
   * @param organism the organism
   * @param genome the genome
   * @param readLength the read length
   * @param minRes the min res
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  public static void encode(Path samFile,
      Path dir,
      String name,
      String organism,
      String genome,
      int readLength,
      int minRes) throws IOException, ParseException {

    FileUtils.mkdir(dir);

    int reads = encodeMultiResFileOrdered(samFile,
        dir,
        minRes,
        genome,
        readLength);

    createMetaFile(dir, name, organism, genome, readLength, reads);

  }

  /**
   * Read count.
   *
   * @param samFile the sam file
   * @return the int
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static int readCount(Path samFile) throws IOException {
    LOG.info("Counting reads {}...", samFile);

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

  /*
   * public static void encodeMultiResFile(Path samFile, Path dir, Chromosome
   * chr, int minRes, String genome, int readLength, int readCount) throws
   * IOException, ParseException {
   * 
   * LOG.info("Finding read starts {}...", samFile);
   * 
   * // first lets bunch all the counts up together
   * 
   * List<Integer> starts = new ArrayList<Integer>(readCount);
   * 
   * BufferedReader reader = FileUtils.newBufferedReader(samFile);
   * 
   * String line; List<String> tokens;
   * 
   * boolean started = false;
   * 
   * try { while ((line = reader.readLine()) != null) { tokens =
   * TextUtils.tabSplit(line);
   * 
   * Chromosome c = Chromosome.parse(tokens.get(2));
   * 
   * if (!c.equals(chr)) { if (started) { break; } else { continue; } }
   * 
   * if (c == null || chr == null) { System.err.print(c + " " + tokens.get(2) +
   * " " + chr); }
   * 
   * int start = TextUtils.parseInt(tokens.get(3)) - 1;
   * 
   * starts.add(start);
   * 
   * if (!started) { started = true; } } } finally { reader.close(); }
   * 
   * encodeMultiResFile(samFile, dir, chr, minRes, readLength, starts); }
   */

  /**
   * Import an ordered sam file.
   *
   * @param samFile the sam file
   * @param dir the dir
   * @param minRes the min res
   * @param genome the genome
   * @param readLength the read length
   * @return the int
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  public static int encodeMultiResFileOrdered(Path samFile,
      Path dir,
      int minRes,
      String genome,
      int readLength) throws IOException, ParseException {

    LOG.info("Finding read starts {}...", samFile);

    // first lets bunch all the counts up together

    List<Integer> starts = new ArrayList<Integer>();

    BufferedReader reader = FileUtils.newBufferedReader(samFile);

    String line;
    List<String> tokens;

    Chromosome chr = null;
    Chromosome c = null;

    int count = 0;

    try {
      while ((line = reader.readLine()) != null) {
        tokens = TextUtils.tabSplit(line);

        c = GenomeService.getInstance().guessChr(samFile, tokens.get(2));

        if (c != null) {
          if (chr != null && !c.equals(chr)) {
            System.err.println(c + " " + chr + " " + starts.size());
            encodeMultiResFile(samFile, dir, chr, minRes, readLength, starts);

            starts.clear();

            // DEBUG: test chr1 encoding only.
            // Comment out for normal use
            // break;
          }

          int start = TextUtils.parseInt(tokens.get(3)) - 1;

          starts.add(start);

          chr = c;
        }

        ++count;
      }
    } finally {
      reader.close();
    }

    if (starts.size() > 0) {
      // Enclode whatever is left
      encodeMultiResFile(samFile, dir, chr, minRes, readLength, starts);
    }

    return count;
  }

  /**
   * Encode multi res file.
   *
   * @param samFile the sam file
   * @param dir the dir
   * @param chr the chr
   * @param minRes the min res
   * @param readLength the read length
   * @param starts the starts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void encodeMultiResFile(Path samFile,
      Path dir,
      Chromosome chr,
      int minRes,
      int readLength,
      List<Integer> starts) throws IOException {
    int blockCount = 0;

    Map<Integer, IterMap<Integer, Block>> blockMap = DefaultTreeMap
        .create(new TreeMapCreator<Integer, Block>());

    for (int i = 0; i < starts.size(); ++i) {
      int s = starts.get(i);

      int sbin = s / minRes;
      int sbs = sbin * minRes;

      // if (!blockMap.containsKey(minRes)) {
      /// blockMap.put(minRes, new TreeMap<Integer, Block>());
      // }

      if (!blockMap.get(minRes).containsKey(sbs)) {
        blockMap.get(minRes).put(sbs, new Block(sbs, minRes, i, i));
      }

      blockMap.get(minRes).get(sbs).endOffset = i;
    }

    // Turn into a stack

    Deque<Block> blockQueue = new ArrayDeque<Block>();

    // Reverse the order of the blocks so we build the tree left to right
    for (int bin : blockMap.keySet()) {
      for (int bs : CollectionUtils.reverse(blockMap.get(bin).keySet())) {
        blockQueue.push(blockMap.get(bin).get(bs));
        ++blockCount;
      }
    }

    LOG.info("Building R tree {}...", samFile);

    // Now we need to build the tree in reverse

    Block startBlock = null;

    while (!blockQueue.isEmpty()) {
      Block child = blockQueue.pop();

      int binWidth = child.width * BINS;
      int bin = child.start / binWidth;
      int bs = bin * binWidth;

      // System.err.println("new bin " + binWidth + " " + bs + " start:" +
      // child.start
      // + " " + MAX_BLOCK_WIDTH);

      if (binWidth <= MAX_BLOCK_WIDTH) {
        //// if (!blockMap.containsKey(binWidth)) {
        // blockMap.put(binWidth, new TreeMap<Integer, Block>());
        // }

        if (!blockMap.get(binWidth).containsKey(bs)) {
          Block block = new Block(bs, binWidth, -1, -1);

          // System.err.println("new block " + bs + " " + binWidth);

          blockMap.get(binWidth).put(bs, block);
          blockQueue.push(block);

          // Keep track of the top of the tree
          if (binWidth == MAX_BLOCK_WIDTH) {
            startBlock = block;
          }

          ++blockCount;
        }

        // Children are added in reverse order so always add new
        // children to the head of the list so that they remain in
        // order from left to right by start position
        // blockMap.get(binWidth).get(bs).children.add(0, child);
        blockMap.get(binWidth).get(bs).children.add(child);

        // System.err.println("add child " + bs + " " + binWidth + " " +
        // child.start + "
        // " + child.width);
      }
    }

    // For each block find the min/max index in the read list

    blockQueue = new ArrayDeque<Block>();

    blockQueue.push(startBlock);

    while (!blockQueue.isEmpty()) {
      Block block = blockQueue.pop();

      block.startOffset = getMinS(block);
      block.endOffset = getMaxS(block);

      for (Block child : block.children) {
        blockQueue.push(child);
      }
    }

    // The tree was build rightmost so reverse start blocks so they are
    // in order
    // Collections.reverse(startBlocks);

    Deque<List<Block>> groupBlockQueue = new ArrayDeque<List<Block>>();

    groupBlockQueue.add(CollectionUtils.asList(startBlock));

    // Each block on file consists of start|end|so|eo|isData
    // start (32 bits, 4 bytes),
    // end (4 bytes) end coordinate of file
    // so file offset to next block if isData == 0,
    // otherwise file offset to where reads start
    // eo file offset to end of next block if isData = 0,
    // otherwise file offset to where reads end
    // Total 17 bytes

    int dataStartOffset = blockCount * BLOCK_SIZE_BYTES;

    Path file = dir.resolve(chr.toString() + ".reads.rtb");
    DataOutputStream out = new DataOutputStream(
        new BufferedOutputStream(FileUtils.newOutputStream(file)));

    LOG.info("Writing {}...", file);

    // start with the largest resolution first

    try {
      out.writeInt(readLength);
      out.writeInt(dataStartOffset);
      // out.writeInt(MAX_BLOCK_WIDTH);

      while (!groupBlockQueue.isEmpty()) {
        List<Block> blocks = groupBlockQueue.pop();

        // Where the next batch of blocks will occur
        int fo = blocks.size() * BLOCK_SIZE_BYTES;

        for (Block block : blocks) {
          out.writeInt(block.start);
          out.writeInt(block.width);

          if (block.start == 199999000) {
            System.err.println("write block " + block.start + " " + block.width
                + " " + block.children.size() + " " + fo);
            System.err.println(
                "write block2 " + dataStartOffset + " " + block.endOffset + " "
                    + (dataStartOffset + block.endOffset * READ_START_BYTES));
          }

          // out.writeInt(dataStartOffset + block.startOffset *
          // READ_START_BYTES);
          // out.writeInt(dataStartOffset + block.endOffset * READ_START_BYTES);

          out.writeInt(block.startOffset);
          out.writeInt(block.endOffset);

          if (block.children.size() == 0) {
            out.writeInt(-1);
          } else {
            out.writeInt(fo);
          }

          // The next block at this level will occur after all children
          // of this block have been written
          fo += countChildren(block) * BLOCK_SIZE_BYTES;
        }

        for (Block block : CollectionUtils.reverse(blocks)) {
          groupBlockQueue.push(block.children);
        }

      }

      // Now write the read position data
      for (int start : starts) {
        out.writeInt(start);
      }
    } finally {
      out.close();
    }
  }

  /**
   * Write int to bytes.
   *
   * @param v the v
   * @param bytes the bytes
   * @param p the p
   * @return the int
   */
  public static int writeIntToBytes(int v, byte[] bytes, int p) {
    bytes[p] = (byte) ((v >> 24) & LOWER_MASK);
    bytes[p + 1] = (byte) ((v >> 16) & LOWER_MASK);
    bytes[p + 2] = (byte) ((v >> 8) & LOWER_MASK);
    bytes[p + 3] = (byte) (v & LOWER_MASK);

    return p + 4;
  }

  /**
   * Count children.
   *
   * @param block the block
   * @return the int
   */
  public static int countChildren(final Block block) {
    int ret = 0;

    Deque<Block> queue = new ArrayDeque<Block>();

    queue.push(block);

    while (!queue.isEmpty()) {
      Block b = queue.pop();

      ret += b.children.size();

      for (Block c : b.children) {
        queue.push(c);
      }
    }

    // for (Block child : block.children) {
    // ret += countChildren(child);
    // }

    return ret;
  }

  /**
   * Gets the min s.
   *
   * @param block the block
   * @return the min s
   */
  public static int getMinS(final Block block) {

    int ret = Integer.MAX_VALUE;

    Deque<Block> queue = new ArrayDeque<Block>();

    queue.push(block);

    while (!queue.isEmpty()) {
      Block b = queue.pop();

      // Only consider valid starts in the comparison.
      if (b.startOffset != -1) {
        ret = Math.min(ret, b.startOffset);
      }

      if (b.children.size() > 0) {
        queue.push(b.children.get(0));
      }
    }

    return ret;
  }

  /**
   * Gets the max s.
   *
   * @param block the block
   * @return the max s
   */
  public static int getMaxS(final Block block) {

    int ret = Integer.MIN_VALUE;

    Deque<Block> queue = new ArrayDeque<Block>();

    queue.push(block);

    while (!queue.isEmpty()) {
      Block b = queue.pop();

      ret = Math.max(ret, b.endOffset);

      if (b.children.size() > 0) {
        queue.push(b.children.get(b.children.size() - 1));
      }
    }

    return ret;
  }
}
