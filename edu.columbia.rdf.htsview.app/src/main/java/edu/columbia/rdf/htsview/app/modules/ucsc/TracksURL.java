package edu.columbia.rdf.htsview.app.modules.ucsc;

import org.jebtk.core.http.URLPath;

import edu.columbia.rdf.edb.EDBWLogin;

public class TracksURL extends URLPath {

  private static final long serialVersionUID = 1L;

  public TracksURL(EDBWLogin login) {
    super(login.getURL());

    mParts.add("ucsc");
    mParts.add("tracks");
  }
}
