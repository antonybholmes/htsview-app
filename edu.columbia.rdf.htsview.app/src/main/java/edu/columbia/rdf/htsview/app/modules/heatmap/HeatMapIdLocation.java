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
package edu.columbia.rdf.htsview.app.modules.heatmap;

import java.text.ParseException;

import org.jebtk.bioinformatics.genomic.Gene;
import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.ui.GenomeModel;

// TODO: Auto-generated Javadoc
/**
 * The Class HeatMapIdLocation.
 */
public class HeatMapIdLocation {

  /** The m id. */
  private String mId = null;

  /** The m region. */
  private GenomicRegion mRegion = null;

  /**
   * Instantiates a new heat map id location.
   *
   * @param id the id
   * @param region the region
   */
  public HeatMapIdLocation(String id, GenomicRegion region) {
    mId = id;
    mRegion = region;
  }

  /**
   * The id of the feature to center peaks around. This will either be a string
   * representation of a genomic location or else a gene symbol or refseq id.
   *
   * @return the id
   */
  public String getId() {
    return mId;
  }

  /**
   * Return the mid point location of the id if is a location or else the tss of
   * the gene symbol or refseq. If the id is unrecognized, returns null.
   *
   * @return the region
   */
  public GenomicRegion getRegion() {
    return mRegion;
  }

  /**
   * Parses the.
   *
   * @param id the id
   * @param model the model
   * @return the heat map id location
   * @throws ParseException the parse exception
   */
  public static HeatMapIdLocation parse(String id, GenomeModel model) {
    GenomicRegion region = GenomicRegion.parse(id);

    if (region != null) {
      // It's a region, so add as is
      GenomicRegion mid = GenomicRegion.midRegion(region);

      return new HeatMapIdLocation(id, mid);
    } else {
      // might be a gene symbol, in which case report the tss

      Gene gene = GenesService.getInstance().getGenes(model.get(), "refseq")
          .lookup(id);

      if (gene != null) {
        GenomicRegion tss = Gene.tssRegion(gene);

        return new HeatMapIdLocation(id, tss);
      }
    }

    return null;
  }
}
