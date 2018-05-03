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

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.batik.transcoder.TranscoderException;
import org.jebtk.bioinformatics.ext.ucsc.BedGraph;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.file.BioPathUtils;
import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomeService;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicRegionModel;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.bioinformatics.ui.external.samtools.BamGuiFileFilter;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGraphGuiFileFilter;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGuiFileFilter;
import org.jebtk.bioinformatics.ui.filters.GFFGuiFileFilter;
import org.jebtk.bioinformatics.ui.filters.SegGuiFileFilter;
import org.jebtk.core.Plugin;
import org.jebtk.core.PluginService;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.io.Temp;
import org.jebtk.core.json.Json;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.core.tree.TreeNodeEventListener;
import org.jebtk.graphplot.Image;
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.graphplot.figure.Graph2dStyleModel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
import org.jebtk.modern.contentpane.CloseableHTab;
import org.jebtk.modern.dialog.DialogEvent;
import org.jebtk.modern.dialog.DialogEventListener;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.font.FontRibbonSection;
import org.jebtk.modern.graphics.icons.QuickOpenVectorIcon;
import org.jebtk.modern.graphics.icons.QuickSaveVectorIcon;
import org.jebtk.modern.help.ModernAboutDialog;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.JpgGuiFileFilter;
import org.jebtk.modern.io.OpenRibbonPanel;
import org.jebtk.modern.io.PdfGuiFileFilter;
import org.jebtk.modern.io.PngGuiFileFilter;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.io.SaveAsRibbonPanel;
import org.jebtk.modern.io.SvgGuiFileFilter;
import org.jebtk.modern.ribbon.QuickAccessButton;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.ribbon.RibbonMenuItem;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarLocation;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.tabs.SizableTab;
import org.jebtk.modern.widget.ModernClickWidget;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.jebtk.modern.zoom.ModernStatusZoomSlider;
import org.jebtk.modern.zoom.ZoomModel;
import org.jebtk.modern.zoom.ZoomRibbonSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import edu.columbia.rdf.bedgraph.app.MainBedGraph;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ui.network.ServerException;
import edu.columbia.rdf.htsview.app.Import.EncodeWorker;
import edu.columbia.rdf.htsview.app.modules.Module;
import edu.columbia.rdf.htsview.app.modules.dist.ReadDistDialog;
import edu.columbia.rdf.htsview.app.modules.dist.ReadDistTask;
import edu.columbia.rdf.htsview.app.modules.heatmap.HeatMapDialog;
import edu.columbia.rdf.htsview.app.modules.heatmap.HeatMapTask;
import edu.columbia.rdf.htsview.app.tracks.HTSTracksPanel;
import edu.columbia.rdf.htsview.ngs.BctGuiFileFilter;
import edu.columbia.rdf.htsview.ngs.Brt2GuiFileFilter;
import edu.columbia.rdf.htsview.ngs.Brt3GuiFileFilter;
import edu.columbia.rdf.htsview.ngs.BvtGuiFileFilter;
import edu.columbia.rdf.htsview.tracks.AxisLimitsModel;
import edu.columbia.rdf.htsview.tracks.HeightModel;
import edu.columbia.rdf.htsview.tracks.LayoutRibbonSection;
import edu.columbia.rdf.htsview.tracks.MarginModel;
import edu.columbia.rdf.htsview.tracks.MarginRibbonSection;
import edu.columbia.rdf.htsview.tracks.ResolutionModel;
import edu.columbia.rdf.htsview.tracks.ResolutionService;
import edu.columbia.rdf.htsview.tracks.ScaleRibbonSection;
import edu.columbia.rdf.htsview.tracks.SizeRibbonSection;
import edu.columbia.rdf.htsview.tracks.TitlePositionModel;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackTree;
import edu.columbia.rdf.htsview.tracks.TracksFigure;
import edu.columbia.rdf.htsview.tracks.TracksFigurePanel;
import edu.columbia.rdf.htsview.tracks.WidthModel;
import edu.columbia.rdf.htsview.tracks.abi.ABIGuiFileFilter;
import edu.columbia.rdf.htsview.tracks.loaders.SampleLoaderService;
import edu.columbia.rdf.htsview.tracks.locations.LocationsPanel;
import edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack;
import edu.columbia.rdf.htsview.tracks.view.TrackView;
import edu.columbia.rdf.matcalc.MainMatCalc;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.bio.BioModuleLoader;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dStyleRibbonSection;

/**
 * Display BedGraph and other track data.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class MainHtsViewWindow extends ModernRibbonWindow
    implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The Constant LOG. */
  private final static Logger LOG = LoggerFactory
      .getLogger(MainHtsViewWindow.class);

  /**
   * The constant MAX_PLOT_POINTS.
   */
  private static final int MAX_PLOT_POINTS = 100000;

  // private static final String MAX_POINTS_MESSAGE =
  // "Please adjust the display to show fewer than " +
  // TextUtils.commaFormat(MAX_PLOT_POINTS) + " data points.";

  /**
   * The m open panel.
   */
  private OpenRibbonPanel mOpenPanel = new OpenRibbonPanel();

  /**
   * The m save as panel.
   */
  private SaveAsRibbonPanel mSaveAsPanel = new SaveAsRibbonPanel();

  /**
   * The m zoom model.
   */
  private ZoomModel mZoomModel = new ReadsZoomModel();

  /**
   * The m genomic model.
   */
  private GenomicRegionModel mGenomicModel = new GenomicRegionModel();

  /**
   * The m y axis limit model.
   */
  private AxisLimitsModel mYAxisLimitModel = new AxisLimitsModel();

  /**
   * The m genome model.
   */
  private GenomeModel mGenomeModel = new GenomeModel();

  /**
   * The m resolution model.
   */
  private ResolutionModel mResolutionModel = new ResolutionModel();

  /**
   * The m peak style model.
   */
  private Graph2dStyleModel mPeakStyleModel = new ReadsStyleModel();

  /** The m width model. */
  private WidthModel mWidthModel = new WidthModel();

  /** The m height model. */
  private HeightModel mHeightModel = new HeightModel();

  /** The m margin model. */
  private MarginModel mMarginModel = new MarginModel();

  /** The m track list. */
  private TrackTree mTrackList = new TrackTree();

  /**
   * The m canvas.
   */
  private TracksFigure mTracksFigure;

  /**
   * The m tracks panel.
   */
  private HTSTracksPanel mTracksPanel;

  /**
   * The m title position model.
   */
  private TitlePositionModel mTitlePositionModel = new TitlePositionModel();

  /**
   * The m annotation tree.
   */
  private AnnotationTracksTree mAnnotationTree;

  /**
   * The m locations panel.
   */
  private LocationsPanel mLocationsPanel;

  /** The m font section. */
  private FontRibbonSection mFontSection;

  private boolean mSuggestSave = false;

  private Path mViewFile;

  private TracksFigurePanel mTracksFigurePanel;

  /**
   * The member modules.
   */
  private List<Module> mModules = new ArrayList<Module>();

  /**
   * The member module map.
   */
  private Map<String, Module> mModuleMap = new HashMap<String, Module>();

  /**
   * The class ResolutionEvents.
   */
  private class ResolutionEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      setResolution();
    }
  }

  /**
   * The class PeakStyleEvents.
   */
  private class PeakStyleEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      try {
        style();
      } catch (IOException | TransformerException
          | ParserConfigurationException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class GenomeEvents.
   */
  private class GenomeEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      changeGenome();
    }
  }

  /**
   * The class GenomicEvents.
   */
  private class GenomicEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      try {
        updateResolution();
      } catch (IOException | ParseException | TransformerException
          | ParserConfigurationException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class AxisEvents.
   */
  private class AxisEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      try {
        axis();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class TitlePositionEvents.
   */
  private class TitlePositionEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      try {
        recreatePlots();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The Class WidthEvents.
   */
  private class WidthEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.common.event.ChangeListener#changed(org.abh.common.event.
     * ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      try {
        updatePlots();
      } catch (TransformerException | ParserConfigurationException
          | IOException e1) {
        e1.printStackTrace();
      }
    }

  }

  /**
   * The class TreeEvents.
   */
  private class TrackEvents implements TreeNodeEventListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.tree.TreeNodeEventListener#nodeChanged(org.abh.lib.event.
     * ChangeEvent)
     */
    @Override
    public void nodeChanged(ChangeEvent e) {
      try {
        recreatePlots();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.tree.TreeNodeEventListener#nodeUpdated(org.abh.lib.event.
     * ChangeEvent)
     */
    @Override
    public void nodeUpdated(ChangeEvent e) {
      try {
        updatePlots();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * So we can respond to the user deciding whether to overwrite a file.
   * 
   * @author Antony Holmes Holmes
   *
   */
  private class ExportCallBack implements DialogEventListener {

    /**
     * The m file.
     */
    protected Path mFile;

    /**
     * The m pwd.
     */
    protected Path mPwd;

    /**
     * Instantiates a new export call back.
     *
     * @param pwd the pwd
     * @param file the file
     */
    public ExportCallBack(Path pwd, Path file) {
      mFile = file;
      mPwd = pwd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.dialog.DialogEventListener#statusChanged(org.abh.
     * common. ui.ui.dialog.DialogEvent)
     */
    @Override
    public void statusChanged(DialogEvent e) {
      if (e.getStatus() == ModernDialogStatus.OK) {
        try {
          save(mFile);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } else {
        try {
          export(mPwd);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   * The Class CloseSaveCallBack.
   */
  private class CloseSaveCallBack extends ExportCallBack {

    /**
     * Instantiates a new close save call back.
     *
     * @param pwd the pwd
     * @param file the file
     */
    public CloseSaveCallBack(Path pwd, Path file) {
      super(file, pwd);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.htsview.app.MainHtsViewWindow.ExportCallBack#statusChanged(org.abh.
     * common .ui.dialog.DialogEvent)
     */
    @Override
    public void statusChanged(DialogEvent e) {
      if (e.getStatus() == ModernDialogStatus.OK) {
        try {
          save(mFile);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } else {
        try {
          closeSave(mPwd);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   * The class SaveAction.
   */
  private class SaveAction extends AbstractAction {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        export();
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (TranscoderException e1) {
        e1.printStackTrace();
      } catch (TransformerException e1) {
        e1.printStackTrace();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class OpenAction.
   */
  private class OpenAction extends AbstractAction {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        browseForFiles();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * Instantiates a new main reads window.
   *
   * @param genome the genome
   * @param tree the tree
   * @param samples the samples
   * @throws SAXException the SAX exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParserConfigurationException the parser configuration exception
   */
  public MainHtsViewWindow(String genome, AnnotationTracksTree tree,
      Collection<Sample> samples)
      throws SAXException, IOException, ParserConfigurationException {
    super(new HTSViewInfo());

    mGenomeModel.set(genome);

    mAnnotationTree = tree;

    init(samples);
  }

  /**
   * Inits the.
   *
   * @param samples the samples
   * @throws SAXException the SAX exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParserConfigurationException the parser configuration exception
   */
  private void init(Collection<Sample> samples)
      throws SAXException, IOException, ParserConfigurationException {
    JComponent content = (JComponent) getWindowContentPanel();

    content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
        KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK),
        "open");
    content.getActionMap().put("open", new OpenAction());

    content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
        KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK),
        "save");
    content.getActionMap().put("save", new SaveAction());

    // mTracksFigure = new TracksFigure(mGenomicModel,
    // GenomeService.getInstance().getSizes(GenomeAssembly.HG19));

    mTracksFigure = new TracksFigure();

    mTracksFigurePanel = new TracksFigurePanel(mTracksFigure, mGenomicModel);

    mLocationsPanel = new LocationsPanel(this, mGenomeModel, mGenomicModel);

    mTracksPanel = new HTSTracksPanel(this, mGenomeModel, mAnnotationTree, mTrackList);

    /*
     * mFormatPanel = new FormatPanel(this, mTracksFigure.getcu,
     * mTracksFigure.getGenesProperties());
     */

    // We'll default to looking at chr1 for the region of the plot
    // GenomicRegion region = new GenomicRegion(Chromosome.CHR1,
    // 100000,
    // 101000);

    // Goto the position of an interesting gene as a default
    // GenomicRegion region =
    // mGeneMap.get(mGenomeModel.get()).findMainVariant("BCL6");

    GenomicRegion region = GenomicRegion.parse(mGenomeModel.get(),
        SettingsService.getInstance()
        .getAsString("edb.reads.default-location"));

    mGenomicModel.set(region);

    createRibbon();

    createUi();

    try {
      loadModules();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    addModulesUI();

    mGenomeModel.addChangeListener(new GenomeEvents());
    mGenomicModel.addChangeListener(new GenomicEvents());
    // mSamplesModel.addSelectionListener(new SampleEvents());
    mResolutionModel.addChangeListener(new ResolutionEvents());
    mYAxisLimitModel.addChangeListener(new AxisEvents());
    mWidthModel.addChangeListener(new WidthEvents());
    mHeightModel.addChangeListener(new WidthEvents());
    mMarginModel.addChangeListener(new WidthEvents());
    mPeakStyleModel.addChangeListener(new PeakStyleEvents());
    // mTracksModel.addChangeListener(new TrackEvents());
    mTitlePositionModel.addChangeListener(new TitlePositionEvents());

    // Monitor how the tracks are being updated
    mTracksPanel.getTree().addTreeNodeListener(new TrackEvents());

    setSize(1400, 800);

    UI.centerWindowToScreen(this);

    // Set the initial look

    // load what the user was last looking at
    // if (FileUtils.exists(PREVIOUS_XML_VIEW_FILE) &&
    // SettingsService.getInstance().getAsBool("edb.reads.auto-load-previous-view"))
    // {
    // loadXmlView(PREVIOUS_XML_VIEW_FILE);
    // }

    // Load samples in the non-interactive mode so users aren't annoyed
    // with screens asking them for options
    loadSamples(samples, false);
  }

  /**
   * Load samples.
   *
   * @param samples the samples
   * @param interactive the interactive
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void loadSamples(Collection<Sample> samples, boolean interactive)
      throws IOException {
    mTracksPanel.loadSamples(samples, interactive);
  }

  /**
   * Creates the ribbon.
   */
  public final void createRibbon() {
    // RibbongetRibbonMenu() getRibbonMenu() = new RibbongetRibbonMenu()(0);
    RibbonMenuItem menuItem;

    menuItem = new RibbonMenuItem(UI.MENU_NEW_WINDOW);
    getRibbonMenu().addTabbedMenuItem(menuItem);

    menuItem = new RibbonMenuItem(UI.MENU_OPEN);
    getRibbonMenu().addTabbedMenuItem(menuItem, mOpenPanel);

    // menuItem = new RibbonMenuItem("Open Track");
    // getRibbonMenu().addTabbedMenuItem(menuItem);

    // menuItem = new RibbonMenuItem("Open BRT Track");
    // getRibbonMenu().addTabbedMenuItem(menuItem);

    // menuItem = new RibbonMenuItem("Import");
    // getRibbonMenu().addTabbedMenuItem(menuItem);

    // menuItem = new RibbonMenuItem("Import Multi-Res");
    // getRibbonMenu().addTabbedMenuItem(menuItem);

    menuItem = new RibbonMenuItem(UI.MENU_SAVE_AS);
    getRibbonMenu().addTabbedMenuItem(menuItem, mSaveAsPanel);

    // menuItem = new RibbonMenuItem("Export To BedGraph");
    // getRibbonMenu().addTabbedMenuItem(menuItem);

    getRibbonMenu().addDefaultItems(getAppInfo());

    getRibbonMenu().addClickListener(this);

    getRibbonMenu().setDefaultIndex(1);

    ModernClickWidget button;

    // Ribbon2 ribbon = new Ribbon2();
    getRibbon().setHelpButtonEnabled(getAppInfo());

    button = new QuickAccessButton(UIService.getInstance()
        .loadIcon(QuickOpenVectorIcon.class, 16));
    button.setClickMessage(UI.MENU_OPEN);
    button.setToolTip("Open", "Open peak files.");
    button.addClickListener(this);
    addQuickAccessButton(button);

    button = new QuickAccessButton(
        UIService.getInstance().loadIcon(QuickSaveVectorIcon.class, 16));
    button.setClickMessage(UI.MENU_SAVE);
    button.setToolTip("Save", "Save the current image.");
    button.addClickListener(this);
    addQuickAccessButton(button);

    // RibbonSection toolbarContainer;

    //// home
    // RibbonToolbar toolbar = new RibbonToolbar("Home");

    // toolbarSection = new ClipboardRibbonSection(ribbon);
    // toolbar.add(toolbarSection);

    getRibbon().getHomeToolbar()
        .add(new GenomeRibbonSection(getRibbon(), mGenomeModel));

    getRibbon().getHomeToolbar().add(
        new HTSGenomicRibbonSection(getRibbon(), mGenomicModel, mGenomeModel));
    getRibbon().getHomeToolbar()
        .add(new ScaleRibbonSection(getRibbon(), "Y Scale", mYAxisLimitModel));
    // getRibbon().getHomeToolbar().add(new HeightRibbonSection(mHeightModel));
    getRibbon().getHomeToolbar()
        .add(new ResolutionRibbonSection(getRibbon(), mResolutionModel));
    getRibbon().getHomeToolbar()
        .add(new Graph2dStyleRibbonSection(getRibbon(), mPeakStyleModel));

    mFontSection = new FontRibbonSection(this);
    mFontSection.setup(mTracksFigurePanel.getFont(), Color.BLACK);
    getRibbon().getToolbar("Layout").add(mFontSection);
    getRibbon().getToolbar("Layout")
        .add(new LayoutRibbonSection(getRibbon(), mTitlePositionModel));
    getRibbon().getToolbar("Layout")
        .add(new MarginRibbonSection(getRibbon(), mMarginModel));
    getRibbon().getToolbar("Layout")
        .add(new SizeRibbonSection(getRibbon(), mWidthModel, mHeightModel));

    //
    // View
    //

    button = new RibbonLargeButton("Locations",
        UIService.getInstance().loadIcon("locations", 24), "Locations List",
        "Show a list of locations");
    button.addClickListener(this);
    getRibbon().getToolbar("View").getSection("Locations").add(button);

    mFontSection.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        mTracksFigure.setFont(mFontSection.getUserFont(),
            mFontSection.getFontColor());
      }
    });

    getRibbon().getToolbar("View").add(new ZoomRibbonSection(this, mZoomModel));

    //
    // Tools
    //

    button = new RibbonLargeButton("Read Distribution",
        UIService.getInstance().loadIcon("read_dist", 32),
        UIService.getInstance().loadIcon("read_dist", 24), "Read Distribution",
        "Read distribution plot");
    button.addClickListener(this);
    getRibbon().getToolbar("Tools").getSection("Tools").add(button);

    button = new RibbonLargeButton("Heat Map",
        UIService.getInstance().loadIcon("tss_heatmap", 32), "TSS Heat Map",
        "Create TSS Heat Map");
    button.addClickListener(this);
    getRibbon().getToolbar("Tools").getSection("Tools").add(button);

    button = new RibbonLargeButton("Reads",
        UIService.getInstance().loadIcon("reads", 24), "Reads",
        "Create table of reads from selected samples");
    button.addClickListener(this);
    getRibbon().getToolbar("Tools").getSection("Tools").add(button);

    // toolbarSection = new PlotSizeRibbonSection(mCanvas.getSubPlotLayout());
    // toolbar.add(toolbarSection);

    // ZoomRibbonSection zoomSection =
    // new ZoomRibbonSection(this, zoomModel, ribbon);

    // toolbar.add(zoomSection);

    // LegendRibbonSection legendSection =
    // new LegendRibbonSection(mCanvas.getGraphProperties().getLegend());

    // toolbar.add(legendSection);

    /*
     * button = new RibbonLargeButton2("Format", new Raster32Icon(new
     * FormatPlot32VectorIcon())); button.addClickListener(new
     * ModernClickListener() {
     * 
     * @Override public void clicked(ModernClickEvent e) { addFormatPane(); }});
     * 
     * getRibbon().getToolbar("View").getSection("Format").add(button);
     */

    // setRibbon(ribbon, getRibbonMenu());

    getRibbon().setSelectedIndex(1);
  }

  /*
   * public void setFormatPane(FormatPlotPane formatPane) { this.formatPane =
   * formatPane;
   * 
   * addFormatPane(); }
   */

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.window.ModernWindow#createUi()
   */
  @Override
  public final void createUi() {

    // mSamplesPanel = new SamplesTreePanel(mReadAssembly,
    // mSamplesModel);

    // ImageCanvas imageCanvas = new ImageCanvas(mCanvas);

    // mFormatPane = new AxesControlPanel(this, mCanvas);

    // ZoomCanvas zoomCanvas = new ZoomCanvas(mTracksFigure);
    mTracksFigurePanel.setZoomModel(mZoomModel);

    // ContainerCanvas cc = new FrameCanvas(zoomCanvas);

    // ZoomCanvas zoomCanvas = new ZoomCanvas(canvas);

    // BackgroundCanvas backgroundCanvas = new BackgroundCanvas(zoomCanvas);

    ModernScrollPane scrollPane = new ModernScrollPane(mTracksFigurePanel)
        .setScrollBarLocation(ScrollBarLocation.FLOATING)
        .setScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW);
    
    // scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    // ModernPanel panel = new ModernPanel(scrollPane);

    // ModernPanel panel = new ModernPanel(zoomCanvas);

    // panel.setBorder(ModernPanel.BORDER);

    setCard(new ModernComponent(scrollPane, ModernWidget.BORDER));

    // mPanel = new Graph2dPanel(this,
    // mCanvas,
    // history,
    // zoomModel,
    // mContentPane.getModel());

    // setFormatPane(mPanel);

    getStatusBar().addRight(new ModernStatusZoomSlider(mZoomModel));

    addTracksPane();

    // addLocationsPane();
  }

  private void loadModules()
      throws InstantiationException, IllegalAccessException {
    Module module;

    for (Plugin plugin : PluginService.getInstance().iterator("htsview")) {

      System.err.println("Loading plugin " + plugin.getName());
      module = (Module) plugin.getPluginClass().newInstance();

      // System.err.println("Loading module " + module.getName());

      mModules.add(module);

      mModuleMap.put(module.getName(), module);
    }
  }

  /**
   * Run module.
   *
   * @param module the module
   * @param args the args
   * @return true, if successful
   */
  public boolean runModule(String module, String... args) {
    System.err.println("run module " + module + " " + args);

    if (mModuleMap.containsKey(module)) {
      mModuleMap.get(module).run(args);

      return true;
    } else {
      return false;
    }
  }

  /**
   * Allow modules to initialize and customize the UI.
   */
  private void addModulesUI() {
    for (Module module : mModules) {
      module.init(this);
    }
  }

  /**
   * Adds the group pane to the layout if it is not already showing.
   */
  /*
   * private void addSamplesPanel() { if
   * (mContentPane.getModel().contains("Samples")) { return; }
   * 
   * 
   * mContentPane.getModel().addLeft(new SizableContentPane("Samples", new
   * CloseableHTab("Samples", mSamplesPanel, mContentPane.getModel()), 250, 200,
   * 500)); }
   */

  /*
   * private void addFormatPane() { if
   * (mContentPane.getModel().containsTab("Format Plot")) { return; }
   * 
   * mContentPane.getModel().addTab(new SizableContentPane("Format Plot", new
   * CloseableHTab("Format Plot", mFormatPanel, mContentPane.getModel()), 300,
   * 200, 500)); }
   */

  private void addTracksPane() {
    if (tabsPane().tabs().left().contains("Tracks")) {
      return;
    }

    SizableTab sizePane = new SizableTab("Tracks", mTracksPanel,
        250, 100, 500);

    // CollapseHTab htab = new CollapseHTab(sizePane, mTracksPanel);

    // sizePane.setComponent(htab);

    tabsPane().tabs().left().add(sizePane);
  }

  /**
   * Adds the locations pane.
   */
  private void addLocationsPane() {
    if (tabsPane().tabs().right().contains("Locations")) {
      return;
    }

    // CollapseHTab htab = new CollapseHTab(sizePane, mTracksPanel);

    // sizePane.setComponent(htab);

    tabsPane().tabs().right()
        .add(new SizableTab("Locations",
            new CloseableHTab("Locations", mLocationsPanel, tabsPane()), 250,
            100, 500));
  }

  /*
   * private void addFormatPane() { if
   * (mContentPane.getModel().getRightTabs().containsTab("Format")) { return; }
   * 
   * mContentPane.getModel().getRightTabs().addTab(new
   * SizableContentPane("Format", new CloseableHTab2("Format", mFormatPane,
   * mContentPane), 300, 200, 500)); }
   */

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * ui. event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getMessage().equals(UI.MENU_NEW_WINDOW)) {
      MainHtsViewWindow window;
      try {
        window = new MainHtsViewWindow(mGenomeModel.get(), mAnnotationTree,
            null);

        window.setVisible(true);
      } catch (SAXException | IOException | ParserConfigurationException e1) {
        e1.printStackTrace();
      }

    } else if (e.getMessage().equals(UI.MENU_OPEN)
        || e.getMessage().equals(UI.MENU_BROWSE)) {
      try {
        browseForFiles();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(OpenRibbonPanel.FILE_SELECTED)) {
      try {
        openFiles(mOpenPanel.getSelectedFile());
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(OpenRibbonPanel.DIRECTORY_SELECTED)) {
      try {
        browseForFiles(mOpenPanel.getSelectedDirectory());
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(UI.MENU_SAVE)) {
      try {
        export();
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (TranscoderException e1) {
        e1.printStackTrace();
      } catch (TransformerException e1) {
        e1.printStackTrace();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(SaveAsRibbonPanel.DIRECTORY_SELECTED)) {
      try {
        export(mSaveAsPanel.getSelectedDirectory());
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (TranscoderException e1) {
        e1.printStackTrace();
      } catch (TransformerException e1) {
        e1.printStackTrace();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals("Import")) {
      try {
        importSam();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals("Locations")) {
      addLocationsPane();
    } else if (e.getMessage().equals("Read Distribution")) {
      try {
        readDist();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals("Heat Map")) {
      try {
        heatMap();
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (SAXException e1) {
        e1.printStackTrace();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals("Reads")) {
      reads();
    } else if (e.getMessage().equals(UI.MENU_ABOUT)) {
      ModernAboutDialog.show(this, getAppInfo());
    } else if (e.getMessage().equals(UI.MENU_EXIT)) {
      close();
    } else {

    }
  }

  /**
   * Import sam.
   *
   * @throws ParseException the parse exception
   */
  private void importSam() throws ParseException {
    ImportDialog dialog = new ImportDialog(this);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    if (dialog.getSamFile() == null) {
      ModernMessageDialog.createWarningDialog(this, "You must select a file.");

      return;
    }

    if (dialog.getDir() == null) {
      ModernMessageDialog.createWarningDialog(this,
          "You must select a directory.");

      return;
    }

    EncodeWorker task = new Import.EncodeWorker(this, dialog.getSamFile(),
        dialog.getDir(), dialog.getName(), dialog.getOrganism(),
        dialog.getGenome(), dialog.getReadLength(), dialog.getResolutions());

    try {
      task.doInBackground();
    } catch (Exception e) {
      e.printStackTrace();
    } // task.execute();

  }

  /**
   * Force the whole plot to be recreated. This is more expensive than updating.
   *
   * @throws Exception the exception
   */
  private void recreatePlots() throws Exception {

    mTracksFigure.setTracks(mTracksPanel.getTree(),
        mGenomeModel.get(),
        mPeakStyleModel.get(),
        mTitlePositionModel.get());

    // Save the view for reloading.
    updatePlots();
  }

  /**
   * Update plots.
   *
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void updatePlots()
      throws TransformerException, ParserConfigurationException, IOException {
    mTracksFigure.refresh(mGenomicModel.get(),
        mResolutionModel.get(),
        mWidthModel.get(),
        mHeightModel.get(),
        mMarginModel.get());

    mSuggestSave = true;

    // Save the view for reloading.

    // saveXmlView(PREVIOUS_XML_VIEW_FILE);
    // saveJsonView(PREVIOUS_JSON_VIEW_FILE);
  }

  /**
   * Sets the resolution.
   */
  private void setResolution() {
    try {
      updateResolution();
    } catch (IOException | ParseException | TransformerException
        | ParserConfigurationException e) {
      createResolutionErrorDialog();

      e.printStackTrace();
    }
  }

  /**
   * Creates the resolution error dialog.
   */
  private void createResolutionErrorDialog() {
    ModernMessageDialog.createWarningDialog(this,
        "The samples cannot be displayed at a "
            + ResolutionService.getHumanReadable(mResolutionModel.get())
            + " resolution.",
        "The resolution will be adjusted back to the previous setting of "
            + ResolutionService.getHumanReadable(mResolutionModel.getPrevious())
            + ".");

    mResolutionModel.set(mResolutionModel.getPrevious());
  }
  
  /**
   * Change the genome by updating the genomic position with the new
   * chromosome on a different genome.
   */
  private void changeGenome() {
    String genome = mGenomeModel.get();
    
    GenomicRegion region = mGenomicModel.get();
    
    // Get the same chromosome on a different assembly
    Chromosome chr = GenomeService.instance().genome(genome).chr(region.getChr());
    
    // Change the genomic reference to reflect the new genome
    mGenomicModel.set(chr, region.getStart(), region.getEnd());
    
    //try {
      // Force plot recreation
    //  recreatePlots();
    //} catch (Exception e) {
    //  e.printStackTrace();
    //}
  }

  /**
   * Update resolution.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  private void updateResolution() throws IOException, ParseException,
      TransformerException, ParserConfigurationException {

    GenomicRegion displayRegion = mGenomicModel.get();

    int resolution = mResolutionModel.get();

    int bins = displayRegion.getLength() / resolution;

    // System.err.println("update res " + bins + " " + resolution);

    // Auto scale the resolution to match the region of interest,
    // Users can of course change it
    if (bins == 0) {
      mResolutionModel.set(mResolutionModel.get() / 10);
    } else if (bins > MAX_PLOT_POINTS) {
      // lets decrease the resolution until we find something that works

      mResolutionModel.set(mResolutionModel.get() * 10);

      // createInformationDialog(MAX_POINTS_MESSAGE);
    } else {
      updatePlots();
    }
  }

  /**
   * Update axis events.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  private void axis() throws IOException, ParseException, TransformerException,
      ParserConfigurationException {
    for (TreeNode<Track> node : mTracksPanel.getTree()) {
      Track track = node.getValue();

      if (track != null) {
        track.setYProperties(mYAxisLimitModel);
      }
    }

    updatePlots();
  }

  /**
   * Style.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  private void style()
      throws IOException, TransformerException, ParserConfigurationException {
    for (TreeNode<Track> node : mTracksPanel.getTree()) {
      Track track = node.getValue();

      if (track != null) {
        track.setStyle(mPeakStyleModel.get());
      }
    }

    updatePlots();
  }

  /**
   * Heat map.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws SAXException the SAX exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  private void heatMap() throws IOException, SAXException,
      ParserConfigurationException, ParseException {
    List<SamplePlotTrack> tracks = new ArrayList<SamplePlotTrack>();

    for (Track track : mTracksPanel.getSelectedTracks()) {
      if (track instanceof SamplePlotTrack) {
        tracks.add((SamplePlotTrack) track);
      }
    }

    if (tracks.size() == 0) {
      ModernMessageDialog.createWarningDialog(this,
          "You must select at least one sample.");
      return;
    }

    HeatMapDialog dialog = new HeatMapDialog(this, mGenomeModel, tracks);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    HeatMapTask task = new HeatMapTask(this, tracks, dialog.getInput(),
        dialog.getRegions(), dialog.getPadding(), dialog.getBinSize(),
        dialog.getSortType(), mGenomeModel);

    // task.execute();
    task.doInBackground();
    task.done();
  }

  /**
   * Read dist.
   *
   * @throws ParseException the parse exception
   */
  private void readDist() throws ParseException {

    List<SamplePlotTrack> sampleTracks = new ArrayList<SamplePlotTrack>();

    for (Track track : mTracksPanel.getSelectedTracks()) {
      if (track instanceof SamplePlotTrack) {
        sampleTracks.add((SamplePlotTrack) track);
      }
    }

    if (sampleTracks.size() == 0) {
      ModernMessageDialog.createWarningDialog(this,
          "You must select a sample.");
      return;
    }

    ReadDistDialog dialog = new ReadDistDialog(this, mGenomeModel,
        sampleTracks);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    ModernDialogStatus status = ModernMessageDialog.createOkCancelInfoDialog(
        this,
        "Generating distribution plots can take several minutes.");

    if (status == ModernDialogStatus.CANCEL) {
      return;
    }

    ReadDistTask task = new ReadDistTask(this, dialog.getPlotName(),
        sampleTracks, dialog.getRegions(), dialog.getPadding(),
        dialog.getBinSize(), dialog.getAverage());

    task.doInBackground(); // execute();

    /*
     * HeatMapTask task = new HeatMapTask(this, dialog.getSample(),
     * dialog.getInput(), dialog.getRegions(), dialog.getPadding(),
     * dialog.getBinSize(), dialog.getSortType(), dialog.getShouldPlot(),
     * mGeneMap, mGenomeModel);
     * 
     * task.execute();
     */
  }

  /**
   * Extract reads in the given region.
   */
  private void reads() {

    List<SamplePlotTrack> sampleTracks = new ArrayList<SamplePlotTrack>();

    for (Track track : mTracksPanel.getSelectedTracks()) {
      if (track instanceof SamplePlotTrack) {
        sampleTracks.add((SamplePlotTrack) track);
      }
    }

    if (sampleTracks.size() == 0) {
      ModernMessageDialog.createWarningDialog(this,
          "You must select some samples");

      return;
    }

    List<Integer> starts = new ArrayList<Integer>();

    GenomicRegion region = mGenomicModel.get();

    int l = -1;

    for (SamplePlotTrack track : sampleTracks) {
      try {
        starts.addAll(
            track.getAssembly().getStarts(track.getSample(), region, -1));

        if (track.getAssembly().getReadLength(track.getSample()) > 0) {
          l = track.getAssembly().getReadLength(track.getSample());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    DataFrame m = DataFrame.createDataFrame(starts.size(), 3);

    m.setName("Reads");
    m.setColumnNames("chr", "start", "end");

    for (int i = 0; i < starts.size(); ++i) {
      int start = starts.get(i);

      m.set(i, 0, region.getChr());
      m.set(i, 1, start);
      m.set(i, 2, start + l - 1);
    }

    try {
      MainMatCalcWindow window = MainMatCalc.main(getAppInfo(),
          new BioModuleLoader());

      window.openMatrix(m);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (FontFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  /**
   * Browse for files.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   * @throws SAXException the SAX exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  public void browseForFiles() throws IOException, ParseException, SAXException,
      ParserConfigurationException {
    browseForFiles(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Browse for files.
   *
   * @param pwd the pwd
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   * @throws SAXException the SAX exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  public void browseForFiles(Path pwd) throws IOException, ParseException,
      SAXException, ParserConfigurationException {
    openFiles(FileDialog.open(this)
        .filter(new HTSViewAllSupportedGuiFileFilter(),
            new BamGuiFileFilter(),
            new BctGuiFileFilter(),
            new Brt2GuiFileFilter(),
            new Brt3GuiFileFilter(),
            new BvtGuiFileFilter(),
            new BedGuiFileFilter(),
            new BedGraphGuiFileFilter(),
            new GFFGuiFileFilter(),
            new SegGuiFileFilter(),
            new ABIGuiFileFilter(),
            new HTSViewGuiFileFilter())
        .getFiles(pwd));
  }

  /**
   * Open files.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   * @throws SAXException the SAX exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  public void openFiles(Path file) throws IOException, ParseException,
      SAXException, ParserConfigurationException {
    openFiles(CollectionUtils.asList(file));
  }

  /**
   * Open files.
   *
   * @param files the files
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws SAXException the SAX exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  public void openFiles(List<Path> files) throws ParseException, IOException,
      SAXException, ParserConfigurationException {
    List<Path> otherFiles = new ArrayList<Path>();

    for (Path file : files) {
      if (PathUtils.ext().type(HtsJsonViewGuiFileFilter.EXT).test(file)) {
        loadJsonView(file);
      } else if (PathUtils.ext().type(ReadsJsonViewGuiFileFilter.EXT).test(file)) {
          loadJsonView(file);
      } else if (PathUtils.ext().type(ReadsXmlViewGuiFileFilter.EXT)
          .test(file)) {
        // loadXmlView(file);
      } else {
        otherFiles.add(file);
      }

      RecentFilesService.getInstance().add(file);
    }

    if (otherFiles.size() > 0) {
      SampleLoaderService.getInstance()
          .openFiles(this, files, mTrackList.getRoot());
    }
  }

  /**
   * Load xml view.
   *
   * @param jsonFile the json file
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  /*
   * public void loadXmlView(Path file) throws SAXException, IOException,
   * ParserConfigurationException { SAXParserFactory factory =
   * SAXParserFactory.newInstance(); SAXParser saxParser =
   * factory.newSAXParser();
   * 
   * TracksXmlHandler handler = new TracksXmlHandler(mAssembly, mAnnotationTree,
   * mTracksPanel);
   * 
   * saxParser.parse(file.toFile(), handler);
   * 
   * // load the tracks TreeRootNode<Track> tracks = handler.getTracks();
   * 
   * mTracksPanel.setTracks(tracks);
   * 
   * mGenomicModel.set(handler.getRegion()); }
   */

  /**
   * Load json view.
   *
   * @param jsonFile the json file
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void loadJsonView(Path jsonFile) throws ParseException, IOException {
    TrackView.loadJsonView(this,
        jsonFile,
        mTracksPanel,
        mAnnotationTree,
        mWidthModel,
        mMarginModel,
        mGenomeModel,
        mGenomicModel,
        mTitlePositionModel);

    mViewFile = jsonFile;
    mSuggestSave = false;
  }

  /**
   * Export to bed graph.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private void exportToBedGraph() throws IOException {
    Path tmp = Temp.createTempFile("bedgraph");

    BedGraph.write(getBedGraphs(), tmp);

    MainBedGraph.main(tmp);
  }

  /**
   * Gets the bed graphs.
   *
   * @return the bed graphs
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private List<UCSCTrack> getBedGraphs() throws IOException {
    List<UCSCTrack> bedgraphs = new ArrayList<UCSCTrack>();

    for (TreeNode<Track> node : mTracksPanel.getTree()) {
      Track track = node.getValue();

      UCSCTrack bedGraph = track.getBedGraph(mGenomicModel.get(),
          mResolutionModel.get());

      if (bedGraph == null) {
        bedgraphs.add(bedGraph);
      }
    }

    return bedgraphs;
  }

  /**
   * Export.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TranscoderException the transcoder exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  private void export() throws IOException, TranscoderException,
      TransformerException, ParserConfigurationException, ParseException {
    export(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Export.
   *
   * @param pwd the pwd
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TranscoderException the transcoder exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  private void export(Path pwd) throws IOException, TranscoderException,
      TransformerException, ParserConfigurationException, ParseException {
    Path file = FileDialog.save(this)
        .filter(new SvgGuiFileFilter(),
            new PngGuiFileFilter(),
            new PdfGuiFileFilter(),
            new JpgGuiFileFilter(),
            new BedGraphGuiFileFilter(),
            new HtsJsonViewGuiFileFilter())
        .getFile(pwd);

    save(pwd, file);

    /*
     * if (FileUtils.exists(file)) { ModernDialogStatus status =
     * ModernMessageDialog.createFileReplaceDialog(this, file);
     * 
     * if (status == ModernDialogStatus.CANCEL) { export(pwd);
     * 
     * return; } }
     * 
     * if (Io.getFileExtension(file).equals("readsx")) { saveView(file); } else
     * if (Io.getFileExtension(file).equals("bed")) {
     * Bed.writeBedGraphAsBed(mTracksModel.getBedGraphs(mGenomicModel.get(),
     * mResolutionModel.get()), file);
     * 
     * //ModernMessageDialog.createFileSavedDialog(this, getAppInfo().getName(),
     * file); } else if (Io.getFileExtension(file).equals("bedgraph")) {
     * BedGraph.write(mTracksModel.getBedGraphs(mGenomicModel.get(),
     * mResolutionModel.get()), file);
     * 
     * //ModernMessageDialog.createFileSavedDialog(this, getAppInfo().getName(),
     * file); } else { Image.write(this, getCanvas(), file); }
     * 
     * RecentFilesService.getInstance().add(file);
     * 
     * createFileSavedDialog(file);
     */
  }

  private boolean saveView() throws IOException, TranscoderException,
      TransformerException, ParserConfigurationException, ParseException {
    return saveView(RecentFilesService.getInstance().getPwd());
  }

  public boolean saveView(Path pwd) throws IOException, TranscoderException,
      TransformerException, ParserConfigurationException, ParseException {
    Path file = FileDialog.save(this).filter(new HtsJsonViewGuiFileFilter())
        .getFile(pwd);

    return save(pwd, file);
  }

  /**
   * Save.
   *
   * @param pwd the pwd
   * @param file the file
   * @return 
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TranscoderException the transcoder exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  private boolean save(Path pwd, Path file)
      throws IOException, TranscoderException, TransformerException,
      ParserConfigurationException, ParseException {
    return save(pwd, file, new ExportCallBack(pwd, file));
  }

  /**
   * Save.
   *
   * @param pwd the pwd
   * @param file the file
   * @param l the l
   * @return
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TranscoderException the transcoder exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  private boolean save(Path pwd, Path file, DialogEventListener l)
      throws IOException, TranscoderException, TransformerException,
      ParserConfigurationException, ParseException {
    if (file == null) {
      return false;
    }

    if (FileUtils.exists(file)) {
      return ModernMessageDialog.createFileReplaceDialog(this, file, l) == ModernDialogStatus.OK;
    } else {
      return save(file);
    }
  }

  /**
   * Save.
   *
   * @param file the file
   * @return
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TranscoderException the transcoder exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  private boolean save(Path file) throws IOException, TranscoderException,
      TransformerException, ParserConfigurationException {
    if (file == null) {
      return false;
    }

    if (PathUtils.ext().type(ReadsXmlViewGuiFileFilter.EXT).test(file)) {
      saveXmlView(file);
    } else if (PathUtils.ext().type(HtsJsonViewGuiFileFilter.EXT)
        .test(file)) {
      saveJsonView(file);
    } else if (PathUtils.ext().type(ReadsJsonViewGuiFileFilter.EXT)
        .test(file)) {
      saveJsonView(file);
    } else if (BioPathUtils.ext().bedgraph().test(file)) {
      exportToBedGraph();
    } else {
      Image.write(mTracksFigure, file);
    }

    // RecentFilesService.getInstance().add(file);

    ModernMessageDialog.createFileSavedDialog(this, file);

    return true;
  }

  /**
   * Save xml view.
   *
   * @param file the file
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  private void saveXmlView(Path file)
      throws TransformerException, ParserConfigurationException {
    mTracksPanel.saveXmlView(file,
        mGenomicModel.get(),
        mWidthModel.get(),
        mMarginModel.get());

    RecentFilesService.getInstance().add(file);
  }

  /**
   * Save json view.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void saveJsonView(Path file) throws IOException {
    TrackView.saveJsonView(file,
        mTrackList,
        mGenomicModel.get(),
        mTitlePositionModel.get(),
        mWidthModel.get(),
        mMarginModel.get());

    // Once a view is saved, stop asking the user until they
    // update something.
    mSuggestSave = false;

    RecentFilesService.getInstance().add(file);
  }

  /**
   * Gets the canvas.
   *
   * @return the canvas
   */
  public ModernPlotCanvas getCanvas() {
    return mTracksFigurePanel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.window.ModernWindow#close()
   */
  @Override
  public void close() {
    if (mSuggestSave) {
      ModernDialogStatus status = ModernMessageDialog.createDialog(this,
          MessageDialogType.WARNING_YES_NO,
          "The current view has not been saved.",
          "Would you like to save it?");

      if (status == ModernDialogStatus.OK) {
        boolean saved = false;
        
        if (mViewFile != null) {
          // If the view exists, save it.
          try {
            saved = save(mViewFile);
          } catch (IOException | TranscoderException | TransformerException
              | ParserConfigurationException e) {
            e.printStackTrace();
          }
        } else {
          // If the view does not exist, prompt user for location.
          try {
            saved = saveView();
          } catch (IOException | TranscoderException | TransformerException
              | ParserConfigurationException | ParseException e) {
            e.printStackTrace();
          }
        }
        
        if (!saved) {
          // If not saved assume user pressed cancel so given another chance.
          return;
        }
      } else {
        // If the user chooses not to save, prompt one last time that
        // the view is unsaved if they exit.

        // status = ModernMessageDialog.createDialog(this,
        // MessageDialogType.WARNING_OK_CANCEL,
        // "The current view has not been saved.",
        // "Are you sure you want to exit?");

        // if (status == ModernDialogStatus.CANCEL) {
        // return;
        // }
        
        // If user clicks save and then cancel, to be safe just return with
      }
    }

    /*
     * if (status == ModernDialogStatus.OK) { try { if (mViewFile != null) {
     * closeSave(RecentFilesService.getInstance().getPwd(), mViewFile); } else {
     * closeSave(); } } catch (IOException e) { e.printStackTrace(); } catch
     * (TranscoderException e) { e.printStackTrace(); } }
     */

    Temp.deleteTempFiles();

    super.close();
  }

  /**
   * Close save.
   *
   * @param pwd the pwd
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TranscoderException the transcoder exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  private void closeSave(Path pwd) throws IOException, TranscoderException,
      TransformerException, ParserConfigurationException, ParseException {
    closeSave(pwd, null);
  }

  /**
   * Close save.
   *
   * @param pwd the pwd
   * @param suggested the suggested
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TranscoderException the transcoder exception
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  private void closeSave(Path pwd, Path suggested)
      throws IOException, TranscoderException, TransformerException,
      ParserConfigurationException, ParseException {
    Path file;

    if (suggested != null) {
      file = FileDialog.save(this).filter(new HtsJsonViewGuiFileFilter())
          .suggested(suggested).getFile(pwd);
    } else {
      file = FileDialog.save(this).filter(new HtsJsonViewGuiFileFilter())
          .getFile(pwd);
    }

    save(pwd, file, new CloseSaveCallBack(pwd, file));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.window.ModernWindow#restart()
   */
  @Override
  public void restart() {
    if (ModernMessageDialog
        .createRestartDialog(this) == ModernDialogStatus.OK) {
      close();

      try {
        MainHtsView.main((String[]) null);
      } catch (ServerException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      } catch (SAXException e) {
        e.printStackTrace();
      } catch (ParserConfigurationException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (FontFormatException e) {
        e.printStackTrace();
      } catch (UnsupportedLookAndFeelException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Gets the file.
   *
   * @param json the json
   * @return the file
   */
  private static Path getFile(Json json) {
    String path = json.getAsString("file");

    if (path != null) {
      return PathUtils.getPath(path);
    }

    path = json.getAsString("meta-file");

    if (path != null) {
      return PathUtils.getPath(path);
    }

    return null;
  }

  public HTSTracksPanel getTracksPanel() {
    return mTracksPanel;
  }

  public GenomicRegionModel getGenomicModel() {
    return mGenomicModel;
  }

  public GenomeModel getGenomeModel() {
    return mGenomeModel;
  }
}
