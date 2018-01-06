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

import org.xml.sax.SAXException;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.ui.LoginDialog;
import edu.columbia.rdf.edb.ui.network.ServerException;

// TODO: Auto-generated Javadoc
/**
 * The class LoginDialog.
 */
public class HTSViewLoginDialog extends LoginDialog {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new login dialog.
   *
   * @param login
   *          the login
   */
  public HTSViewLoginDialog(EDBWLogin login) {
    super(new HTSViewInfo(), login);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.edb.ui.LoginDialog#success()
   */
  @Override
  protected void success() {
    try {
      MainHtsView.main(mLogin);
    } catch (ServerException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (ClassNotFoundException e1) {
      e1.printStackTrace();
    } catch (SAXException e1) {
      e1.printStackTrace();
    } catch (ParserConfigurationException e1) {
      e1.printStackTrace();
    }
  }
}
