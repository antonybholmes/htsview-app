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
package edu.columbia.rdf.htsview.app.tracks.genes;

import java.awt.Color;
import java.io.IOException;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import edu.columbia.rdf.htsview.tracks.AnnotationPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;
import edu.columbia.rdf.htsview.tracks.TrackTreeNode;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.core.text.TextUtils;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.htsview.app.AnnotationTracksTree;

// TODO: Auto-generated Javadoc
/**
 * The Class GenesPlotTrack.
 */
public abstract class GenesPlotTrack extends AnnotationPlotTrack {

	private static final long serialVersionUID = 1L;

	/** The m genes properties. */
	protected GenesProperties mGenesProperties = new GenesProperties();
	
	/** The m sub figure. */
	private TrackSubFigure mSubFigure;
	
	/** The m genes id. */
	protected String mGenesId;

	/**
	 * Instantiates a new genes plot track.
	 *
	 * @param name the name
	 * @param id the id
	 */
	public GenesPlotTrack(String name, String id) {
		super(name);
		
		mGenesId = id;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.Track#getFillColor()
	 */
	@Override
	public Color getFillColor() {
		return mGenesProperties.getVariantGene().getLineStyle().getColor();
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.Track#setFillColor(java.awt.Color)
	 */
	@Override
	public void setFillColor(Color color) {
		mGenesProperties.getVariantGene().getLineStyle().setColor(color);
		//mGenesProperties.getVariantGene().getExons().getLineStyle().setColor(color);
		//mGenesProperties.getVariantGene().getExons().setFillColor(color);
		//mGenesProperties.getVariantGene().getFont().setColor(color);
	}
	
	/**
	 * Sets the other color.
	 *
	 * @param color the new other color
	 */
	public void setOtherColor(Color color) {
		mGenesProperties.getOtherGene().getLineStyle().setColor(color);
		//mGenesProperties.getOtherGene().getExons().getLineStyle().setColor(color);
		//mGenesProperties.getOtherGene().getExons().setFillColor(color);
		//mGenesProperties.getOtherGene().getFont().setColor(color);
	}
	
	/**
	 * Gets the other color.
	 *
	 * @return the other color
	 */
	public Color getOtherColor() {
		return mGenesProperties.getOtherGene().getLineStyle().getColor();
	}
	
	/**
	 * Gets the UTR fill color.
	 *
	 * @return the UTR fill color
	 */
	public Color getUTRFillColor() {
		return mGenesProperties.getUTR().getFillStyle().getColor();
	}
	
	/**
	 * Sets the UTR fill color.
	 *
	 * @param color the new UTR fill color
	 */
	public void setUTRFillColor(Color color) {
		mGenesProperties.getUTR().getFillStyle().setColor(color);
	}
	
	public Color getExonFillColor() {
		return mGenesProperties.getVariantGene().getExons().getLineStyle().getColor();
	}
	
	/**
	 * Sets the UTR fill color.
	 *
	 * @param color the new UTR fill color
	 */
	public void setArrowColor(Color color) {
		mGenesProperties.setArrowColor(color);
		//mGenesProperties.getVariantGene().getExons().getFillStyle().setColor(color);
	}
	
	public Color getArrowColor() {
		return mGenesProperties.getArrowColor();
	}
	
	/**
	 * Sets the UTR fill color.
	 *
	 * @param color the new UTR fill color
	 */
	public void setExonFillColor(Color color) {
		mGenesProperties.getVariantGene().getExons().getLineStyle().setColor(color);
		//mGenesProperties.getVariantGene().getExons().getFillStyle().setColor(color);
	}
	
	/**
	 * Gets the show tss arrows.
	 *
	 * @return the show tss arrows
	 */
	public boolean getShowTssArrows() {
		return mGenesProperties.getDrawTssArrows();
	}
	
	/**
	 * Sets the show tss arrows.
	 *
	 * @param show the new show tss arrows
	 */
	public void setShowTssArrows(boolean show) {
		mGenesProperties.setShowTssArrows(show);
	}
	
	/**
	 * Gets the show exon arrows.
	 *
	 * @return the show exon arrows
	 */
	public boolean getShowExonArrows() {
		return mGenesProperties.getShowExonArrows();
	}
	
	/**
	 * Sets the show exon arrows.
	 *
	 * @param show the new show exon arrows
	 */
	public void setShowExonArrows(boolean show) {
		mGenesProperties.setShowExonArrows(show);
	}
	
	/**
	 * Gets the show arrows.
	 *
	 * @return the show arrows
	 */
	public boolean getShowArrows() {
		return mGenesProperties.getShowArrows();
	}
	
	/**
	 * Sets the show arrows.
	 *
	 * @param show the new show arrows
	 */
	public void setShowArrows(boolean show) {
		mGenesProperties.setShowArrows(show);
	}
	
	/**
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(GenesView view) {
		mGenesProperties.setView(view);
	}
	
	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public GenesView getView() {
		return mGenesProperties.getView();
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String, edu.columbia.rdf.htsview.tracks.TitleProperties)
	 */
	@Override
	public TrackSubFigure createGraph(String genome,
			TitleProperties titlePosition) throws IOException {
		//
		// Display some genes
		//

		mSubFigure = GenesPlotSubFigure.create(getName(), 
				mGenesProperties, 
				genome, 
				mGenesId,
				titlePosition);
		
		setMargins(getName(), titlePosition, mSubFigure);
		
		return mSubFigure;
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.Track#updateGraph(org.jebtk.bioinformatics.genome.GenomicRegion, int, int, int, int)
	 */
	@Override
	public TrackSubFigure updateGraph(GenomicRegion displayRegion, 
			int resolution,
			int width,
			int height,
			int margin) throws IOException {
		//
		// Display some genes
		//

		mSubFigure.update(displayRegion, resolution, width, height, margin);

		//mPlot.setForwardCanvasEventsEnabled(true);
			
		return mSubFigure;
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.AnnotationPlotTrack#getGraph()
	 */
	@Override
	public TrackSubFigure getGraph() {	
		return mSubFigure;
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.Track#edit(org.abh.common.ui.window.ModernWindow)
	 */
	@Override
	public void edit(ModernWindow parent) {
		GeneEditDialog dialog = new GeneEditDialog(parent, this);
		
		dialog.setVisible(true);
		
		if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
			return;
		}
		
		setFillColor(dialog.getColor());
		setOtherColor(dialog.getOtherColor());
		setUTRFillColor(dialog.getUTRColor());
		setExonFillColor(dialog.getExonColor());
		setArrowColor(dialog.getArrowColor());
		setShowTssArrows(dialog.getDrawTssArrows());
		setShowExonArrows(dialog.getDrawExonArrows());
		setShowArrows(dialog.getDrawArrows());
		setView(dialog.getView());
	}
	
	/**
	 * Process json view.
	 *
	 * @param name 			The name of the track.
	 * @param annotationTree The annotation tree of current tracks
	 * @param trackJson 		The json to process
	 * @param rootNode 		The root node o
	 * @return true, if successful
	 */
	public boolean processJsonView(String name,
			AnnotationTracksTree annotationTree, 
			Json trackJson,
			TreeNode<Track> rootNode) {
		TreeNode<Track> node = annotationTree.matchFirst(name);

		GenesPlotTrack track = (GenesPlotTrack)node.getValue();

		Color color = ColorUtils.decodeHtmlColor(trackJson.getAsString("color"));

		if (color != null) {
			track.setFillColor(color);
		}

		Color otherColor = 
				ColorUtils.decodeHtmlColor(trackJson.getAsString("other-color"));

		if (otherColor != null) {
			track.setOtherColor(otherColor);
		}
		
		Color utrColor = 
				ColorUtils.decodeHtmlColor(trackJson.getAsString("utr-color"));

		if (utrColor != null) {
			track.setUTRFillColor(utrColor);
		}

		track.setShowTssArrows(trackJson.getAsBool("show-tss-arrows"));
		track.setShowExonArrows(trackJson.getAsBool("show-exon-arrows"));
		track.setShowArrows(trackJson.getAsBool("show-arrows"));
		track.setView(GenesView.parse(trackJson.getAsString("compact")));

		TrackTreeNode child = new TrackTreeNode(track);

		rootNode.addChild(child);

		return true;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.AnnotationPlotTrack#toXml(org.w3c.dom.Document)
	 */
	@Override
	public Element toXml(Document doc) {
		Element element = doc.createElement("track");

		element.setAttribute("type", "genes");
		element.setAttribute("name", getName());
		element.setAttribute("color", ColorUtils.toHtml(getFillColor()));
		element.setAttribute("other-color", ColorUtils.toHtml(getOtherColor()));
		element.setAttribute("show-tss-arrows", getShowTssArrows() ? TextUtils.TRUE : TextUtils.FALSE);
		element.setAttribute("show-exon-arrows", getShowExonArrows() ? TextUtils.TRUE : TextUtils.FALSE);
		element.setAttribute("show-arrows", getShowArrows() ? TextUtils.TRUE : TextUtils.FALSE);
		element.setAttribute("view", getView().toString().toLowerCase());
		
		return element;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.AnnotationPlotTrack#toJson(org.abh.common.json.JsonBuilder)
	 */
	@Override
	public void toJson(JsonBuilder json) {
		json.startObject();

		json.add("type", "genes");
		json.add("name", getName());
		json.add("color", ColorUtils.toHtml(getFillColor()));
		json.add("other-color", ColorUtils.toHtml(getOtherColor()));
		json.add("utr-fill-color", ColorUtils.toHtml(getUTRFillColor()));
		json.add("show-tss-arrows", getShowTssArrows() ? true : false);
		json.add("show-exon-arrows", getShowExonArrows() ? true : false);
		json.add("view", getView().toString().toLowerCase());
		
		json.endObject();
	}

	

	
}
