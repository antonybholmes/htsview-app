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
package edu.columbia.rdf.htsview.app.modules.dna;

import javax.swing.SwingWorker;

import org.jebtk.bioinformatics.genomic.GenomicRegionModel;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.core.cli.ArgParser;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.matcalc.MainMatCalc;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.ModuleLoader;
import edu.columbia.rdf.matcalc.bio.BioModuleLoader;

/**
 * The Class HeatMapTask.
 */
public class DnaTask extends SwingWorker<Void, Void> {

  /** The m matrices. */
  private DataFrame mMatrice;

  /** The m parent. */
  private ModernRibbonWindow mParent;

  private GenomicRegionModel mGenomicModel;

  private GenomeModel mGenomeModel;

  /**
   * Instantiates a new DNA task.
   *
   * @param parent the parent
   * @param genomicModel the genome model
   */
  public DnaTask(ModernRibbonWindow parent, GenomeModel model,
      GenomicRegionModel genomicModel) {
    mParent = parent;
    mGenomeModel = model;
    mGenomicModel = genomicModel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.SwingWorker#doInBackground()
   */
  @Override
  public Void doInBackground() {
    try {
      mMatrice = createMatrix();
    } catch (Exception e) {
      e.printStackTrace();
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
    MainMatCalcWindow window;

    try {

      ModuleLoader ml = new BioModuleLoader()
          .addModule(edu.columbia.rdf.matcalc.toolbox.dna.DnaModule.class)
          .addModule(
              edu.columbia.rdf.matcalc.toolbox.motifs.MotifsModule.class);

      window = MainMatCalc.main(mParent.getAppInfo(), ml);

      window.openMatrix(mMatrice);

      window.runModule("DNA",
          ArgParser.longArg("genome", mGenomeModel.get().getAssembly()),
          ArgParser.longArg("mode", "seq"),
          ArgParser.longArg("ui"));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Creates the heat map matrices.
   *
   * @return the list
   * @throws Exception the exception
   */
  private DataFrame createMatrix() throws Exception {
    DataFrame m = DataFrame.createTextMatrix(1, 1);

    m.setName("DNA");

    m.setColumnName(0, "Location");
    m.set(0, 0, mGenomicModel.get().getLocation());

    return m;
  }
}
