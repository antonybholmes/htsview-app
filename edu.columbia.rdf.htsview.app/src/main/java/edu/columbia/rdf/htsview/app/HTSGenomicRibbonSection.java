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
package edu.columbia.rdf.htsview.app;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicRegionModel;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.bioinformatics.ui.GenomicRegionRibbonSection;
import org.jebtk.modern.ribbon.Ribbon;

/**
 * Functions the same as {@code GenomicRegionRibbonSection}, but restricts users
 * to entering coordinates with a minimum size.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class HTSGenomicRibbonSection extends GenomicRegionRibbonSection {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Minimum width of a region.
   */
  private static final int MIN_BASES = 100;

  /**
   * Instantiates a new HTS genomic ribbon section.
   *
   * @param ribbon the ribbon
   * @param model the model
   * @param genomeModel the genome model
   */
  public HTSGenomicRibbonSection(Ribbon ribbon, GenomicRegionModel model,
      GenomeModel genomeModel) {
    super(ribbon, model, genomeModel);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.bioinformatics.ui.GenomicRegionRibbonSection#parse()
   */
  @Override
  protected GenomicRegion parse(Genome genome) {
    GenomicRegion region = super.parse(genome);

    if (region != null) {
      // Restrict users so they can't look at a region smaller than
      // MIN_SIZE

      if (region.getEnd() - region.getStart() < MIN_BASES) {
        int size = region.getChr().getSize();

        region = GenomicRegion.create(region.getChr(),
            region.getStart(),
            Math.min(region.getStart() + MIN_BASES, size));
      }
    }

    return region;
  }
}
