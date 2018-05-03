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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.window.ModernTaskSplashScreen2;
import org.xml.sax.SAXException;

import edu.columbia.rdf.edb.ui.network.ServerException;

/**
 * The class MatCalcSplashScreen.
 */
public class HTSViewSplashScreen extends ModernTaskSplashScreen2 {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Load a matrix then run a module function.
   *
   * @param m the m
   * @param module the module
   * @param args the args
   */
  public HTSViewSplashScreen(DataFrame m, String module, String... args) {
    super(new HTSViewInfo());

    init();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.window.ModernTaskSplashScreen2#appSetup()
   */
  @Override
  public void appSetup() {

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.window.ModernTaskSplashScreen2#appSetupFinished()
   */
  @Override
  public void appSetupFinished() {
    try {
      MainHtsView.main(null, Genome.HG19, null);
    } catch (ServerException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
  }
}