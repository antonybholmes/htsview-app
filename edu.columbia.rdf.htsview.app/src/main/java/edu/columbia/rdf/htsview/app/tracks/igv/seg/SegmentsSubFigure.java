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

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.ArrayListCreator;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.modern.graphics.colormap.ColorMap;

import edu.columbia.rdf.htsview.tracks.FixedYSubFigure;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;

/**
 * The Class SegmentsSubFigure.
 */
public class SegmentsSubFigure extends FixedYSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m segments. */
  private SegmentSamples mSegments;

  /** The m layer. */
  private SegmentsPlotLayer mLayer;

  /** The m color map. */
  private ColorMap mColorMap;

  // private GenomicRegionsModel mGenomicModel;

  // private GenomicRegion mDisplayRegion;

  /**
   * Instantiates a new segments sub figure.
   *
   * @param segments the segments
   * @param colorMap the color map
   * @param titlePosition the title position
   */
  public SegmentsSubFigure(SegmentSamples segments, ColorMap colorMap,
      TitleProperties titlePosition) {
    mSegments = segments;
    mLayer = new SegmentsPlotLayer(colorMap);

    setColorMap(colorMap);

    // set the graph limits
    currentAxes().getX1Axis().getTitle().setText(null);
    currentAxes().getY1Axis().setLimits(0, 1);
    currentAxes().addChild(mLayer);

    Track.setTitle(segments.getName(), titlePosition, currentAxes());
  }

  /**
   * Creates the.
   *
   * @param segments the segments
   * @param colorMap the color map
   * @param titlePosition the title position
   * @return the segments sub figure
   */
  public static SegmentsSubFigure create(SegmentSamples segments,
      ColorMap colorMap,
      TitleProperties titlePosition) {

    // Now lets create a plot

    SegmentsSubFigure canvas = new SegmentsSubFigure(segments, colorMap,
        titlePosition);

    return canvas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.SubFigure#setColorMap(org.abh.common.ui.graphics.
   * colormap.ColorMap)
   */
  @Override
  public void setColorMap(ColorMap colorMap) {
    mColorMap = colorMap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.FixedSubFigure#update(org.jebtk.
   * bioinformatics.genome.GenomicRegion, int, double, int, int, int,
   * java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
   */
  @Override
  public void update(GenomicRegion region,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin,
      Color lineColor,
      Color fillColor,
      PlotStyle style) throws IOException {

    IterMap<String, List<Segment>> regions = DefaultTreeMap
        .create(new ArrayListCreator<Segment>());

    for (String name : mSegments) {
      Segments segments = mSegments.get(name).get(region.getChr());

      regions.put(name, segments.search(region));
    }

    height = SegPlotTrack.BLOCK_HEIGHT * mSegments.size();

    mLayer.update(regions, mColorMap);

    super.update(region,
        resolution,
        yMax,
        width,
        height,
        margin,
        lineColor,
        fillColor,
        style);

    // GenesPlotCanvasLayer.GAP;

    Axes.disableAllFeatures(currentAxes());

    // Need to make the title visible
    currentAxes().getTitle().getFontStyle().setVisible(true);
  }

  /*
   * @Override public void update(GenomicRegion displayRegion, int resolution,
   * Color lineColor, Color fillColor) {
   * 
   * int start = displayRegion.getStart();
   * 
   * int end = displayRegion.getEnd();
   * 
   * // The end cannot be equal to the start for display purposes if (end ==
   * start) { ++end; }
   * 
   * // set the graph limits and limit to the size of the chromosome
   * getCurrentAxes().getXAxis().setLimits(start, end);
   * getCurrentAxes().getXAxis().startEndTicksOnly();
   * 
   * // Create a series for each bedgraph in the group Plot plot =
   * getCurrentAxes().getCurrentPlot();
   * 
   * plot.setBarWidth(1);
   * 
   * // Use the default series for plotting. XYSeries series =
   * plot.getColumnSeriesGroup().getCurrent();
   * 
   * // Use the bedgraph to set the series color
   * series.getStyle().getLineStyle().setColor(lineColor);
   * series.getStyle().getFillStyle().setColor(fillColor);
   * series.addRegex("start"); series.addRegex("end");
   * 
   * List<UCSCTrackRegion> regions =
   * mBed.getRegions().getFeatureList(displayRegion.getChr(),
   * displayRegion.getStart(), displayRegion.getEnd());
   * 
   * if (regions != null) { DataFrame m = new AnnotatableMatrix(regions.size(),
   * 2);
   * 
   * m.setColumnNames("start", "end");
   * 
   * for (int i = 0; i < regions.size(); i++) { UCSCTrackRegion region =
   * regions.get(i);
   * 
   * // Each series consists of the start and end points of each peak. //
   * Duplicates are removed later. m.setValue(i, 0, region.getStart());
   * m.setValue(i, 1, region.getEnd()); }
   * 
   * plot.setMatrix(m); }
   * 
   * int h = Track.MEDIUM_TRACK_HEIGHT;
   * 
   * switch(mDisplayMode) { case FULL: if (regions != null) { h *= (1 +
   * regions.size()); }
   * 
   * break; default: break; }
   * 
   * getCurrentAxes().setInternalPlotSize(Track.PLOT_WIDTH, h); }
   */
}
