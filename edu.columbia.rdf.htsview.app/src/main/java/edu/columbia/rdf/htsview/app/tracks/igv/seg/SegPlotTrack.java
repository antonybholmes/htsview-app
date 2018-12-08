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
package edu.columbia.rdf.htsview.app.tracks.igv.seg;

import java.io.IOException;
import java.nio.file.Path;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapService;
import org.jebtk.modern.window.ModernWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.htsview.tracks.GraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * The Class SegPlotTrack.
 */
public class SegPlotTrack extends GraphPlotTrack {

  private static final long serialVersionUID = 1L;

  /** The m file. */
  private Path mFile;

  /** The m segments. */
  private SegmentSamples mSegments;

  /** The m name. */
  private String mName;

  /** The m color map. */
  private ColorMap mColorMap;

  /** The Constant BAR_HEIGHT. */
  public static final int BAR_HEIGHT = 20;

  /** The Constant HALF_BAR_HEIGHT. */
  public static final int HALF_BAR_HEIGHT = BAR_HEIGHT / 2;

  /** The gap. */
  public static int GAP = 0;

  /** The Constant BLOCK_HEIGHT. */
  public static final int BLOCK_HEIGHT = BAR_HEIGHT + GAP;

  /**
   * Instantiates a new seg plot track.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public SegPlotTrack(Path file) throws IOException {
    mFile = file;
    mSegments = SegmentSamples.parse(file);
    mName = PathUtils.getNameNoExt(file);

    setColorMap(ColorMapService.getInstance().get("Blue White Red"));
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getType()
   */
  @Override
  public String getType() {
    return "Segments";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getName()
   */
  @Override
  public String getName() {
    return mName;
  }

  /**
   * Sets the color map.
   *
   * @param colorMap the new color map
   */
  public void setColorMap(ColorMap colorMap) {
    mColorMap = colorMap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(Genome genome,
      TitleProperties titlePosition) {
    mSubFigure = SegmentsSubFigure.create(mSegments, mColorMap, titlePosition);

    // mPlot.getGraphSpace().setPlotSize(PLOT_SIZE);

    setMargins(getName(), titlePosition, mSubFigure);

    mSubFigure.currentAxes().getX1Axis().getTitle().setText(null);
    mSubFigure.currentAxes().getY1Axis().setLimits(0, 1);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#updateGraph(org.jebtk.bioinformatics.
   * genome.GenomicRegion, int, int, int, int)
   */
  @Override
  public TrackSubFigure updateGraph(GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {

    mSubFigure.setColorMap(mColorMap);
    // mPlot.setForwardCanvasEventsEnabled(false);
    mSubFigure.update(displayRegion, resolution, width, height, margin);
    // mPlot.setForwardCanvasEventsEnabled(true);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.GraphPlotTrack#edit(org.abh.common.ui.
   * window. ModernWindow)
   */
  @Override
  public void edit(ModernWindow parent) {
    SegEditDialog dialog = new SegEditDialog(parent, this);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    setName(dialog.getName());
    setColorMap(dialog.getColorMap());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toXml(org.w3c.dom.Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "seg");
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("file", PathUtils.toString(mFile));
    trackElement.setAttribute("colormap", mColorMap.getName());

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toJson(org.abh.common.json.
   * JsonBuilder)
   */
  @Override
  public void toJson(JsonBuilder json) {
    json.startObject();

    json.add("type", "seg");
    json.add("name", getName());
    json.add("file", PathUtils.toString(mFile));
    json.add("colormap", mColorMap.getName());

    json.endObject();
  }

  /**
   * Gets the color map.
   *
   * @return the color map
   */
  public ColorMap getColorMap() {
    return mColorMap;
  }
}
