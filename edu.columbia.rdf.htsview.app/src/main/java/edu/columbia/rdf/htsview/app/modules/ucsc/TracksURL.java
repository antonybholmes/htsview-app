package edu.columbia.rdf.htsview.app.modules.ucsc;

import org.jebtk.core.http.UrlBuilder;

import edu.columbia.rdf.edb.EDBWLogin;

public class TracksURL extends UrlBuilder {

  private static final long serialVersionUID = 1L;

  public TracksURL(EDBWLogin login) {
    super(login.getURL());
    
    _resolve("ucsc");
    _resolve("tracks");
  }
}
