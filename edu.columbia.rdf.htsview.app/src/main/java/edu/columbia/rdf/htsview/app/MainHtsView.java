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

import java.awt.FontFormatException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import org.jebtk.bioinformatics.conservation.ConservationAssembly;
import org.jebtk.bioinformatics.conservation.ConservationAssemblyWeb;
import org.jebtk.bioinformatics.dna.GenomeAssemblyWeb;
import org.jebtk.bioinformatics.dna.GenomeAssemblyZip;
import org.jebtk.bioinformatics.ext.ucsc.CytobandsService;
import org.jebtk.bioinformatics.genomic.ChromosomeSizesService;
import org.jebtk.bioinformatics.genomic.Dna;
import org.jebtk.bioinformatics.genomic.GTBZGenes;
import org.jebtk.bioinformatics.genomic.GTBZParser;
import org.jebtk.bioinformatics.genomic.GeneType;
import org.jebtk.bioinformatics.genomic.Genes;
import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomeAssembly;
import org.jebtk.bioinformatics.genomic.LazyGenes;
import org.jebtk.core.AppService;
import org.jebtk.core.PluginService;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.network.UrlBuilder;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.modern.theme.ThemeService;
import org.xml.sax.SAXException;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ui.Repository;
import edu.columbia.rdf.edb.ui.RepositoryService;
import edu.columbia.rdf.edb.ui.network.ServerException;
import edu.columbia.rdf.htsview.app.modules.counts.CountsModule;
import edu.columbia.rdf.htsview.app.modules.dna.DnaModule;
import edu.columbia.rdf.htsview.app.tracks.WebAssemblyService;
import edu.columbia.rdf.htsview.app.tracks.loaders.SampleLoaderBAM;
import edu.columbia.rdf.htsview.app.tracks.loaders.SampleLoaderBCT;
import edu.columbia.rdf.htsview.app.tracks.loaders.SampleLoaderBRT2;
import edu.columbia.rdf.htsview.app.tracks.loaders.SampleLoaderBRT3;
import edu.columbia.rdf.htsview.app.tracks.loaders.SampleLoaderBVT;
import edu.columbia.rdf.htsview.app.tracks.loaders.SampleLoaderBed;
import edu.columbia.rdf.htsview.app.tracks.loaders.SampleLoaderGFF;
import edu.columbia.rdf.htsview.app.tracks.loaders.SampleLoaderSeg;
import edu.columbia.rdf.htsview.app.tracks.peaks.PeakAssemblyWeb;
import edu.columbia.rdf.htsview.app.tracks.view.AnnotationJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.BedGraphJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.BedJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.GenesJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.PeaksJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.ReadsFSJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.ReadsJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.SampleFSJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.SampleJsonParser;
import edu.columbia.rdf.htsview.app.tracks.view.SegJsonParser;
import edu.columbia.rdf.htsview.chipseq.ChipSeqRepositoryCache;
import edu.columbia.rdf.htsview.tracks.loaders.SampleLoaderABI;
import edu.columbia.rdf.htsview.tracks.loaders.SampleLoaderBedGraph;
import edu.columbia.rdf.htsview.tracks.loaders.SampleLoaderService;
import edu.columbia.rdf.htsview.tracks.sample.SampleAssemblyWeb;
import edu.columbia.rdf.htsview.tracks.view.ABIJsonParser;
import edu.columbia.rdf.htsview.tracks.view.TrackParserService;

// TODO: Auto-generated Javadoc
/**
 * The class MainReads.
 */
public class MainHtsView {
  /**
   * The main method.
   *
   * @param args
   *          the arguments
   * @throws FontFormatException
   *           the font format exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   * @throws SAXException
   *           the SAX exception
   * @throws ParserConfigurationException
   *           the parser configuration exception
   * @throws ClassNotFoundException
   *           the class not found exception
   * @throws InstantiationException
   *           the instantiation exception
   * @throws IllegalAccessException
   *           the illegal access exception
   * @throws UnsupportedLookAndFeelException
   *           the unsupported look and feel exception
   * @throws ServerException
   *           the server exception
   * @throws ParseException
   *           the parse exception
   */
  public static final void main(String[] args)
      throws FontFormatException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException,
      InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, ServerException, ParseException {
    AppService.getInstance().setAppInfo("htsview");

    ThemeService.getInstance().setTheme(); // ColorTheme.GREEN);

    /*
     * try { GeneService.getInstance().load(Resources.getResGzipReader(
     * "res/rdf_ucsc_refseq_genes_hg19.txt.gz"));
     * 
     * // Load the cytobands Cytobands cytobands = new
     * Cytobands(Resources.getResGzipReader("res/ucsc_cytobands_hg19.txt.gz"));
     * 
     * GenomeAssembly genomeAssembly = new GenomeAssemblyWeb(new
     * URL(SettingsService.getInstance().getAsString("edb.reads.dna.remote-url")));
     * 
     * ConservationAssembly conservationAssembly = new ConservationAssemblyWeb(new
     * URL(SettingsService.getInstance().getAsString(
     * "edb.reads.conservation.remote-url")));
     * 
     * ConservationAssembly mouseConservationAssembly = new
     * ConservationAssemblyWeb(new URL(SettingsService.getInstance().getAsString(
     * "edb.reads.mouse.conservation.remote-url")));
     * 
     * 
     * //ReadAssembly readAssembly = // new ReadAssemblyWeb(new
     * URL(SettingsService.getInstance().getSetting("edb.reads.remote-url").getValue
     * ()));
     * 
     * ChipSeqAssembly readAssembly = new ChipSeqAssemblyWeb(new
     * URL(SettingsService.getInstance().getAsString("edb.reads.chip-seq.remote-url"
     * )));
     * 
     * 
     * ChromosomeSizes sizes = new
     * ChromosomeSizes(Resources.getResGzipReader("res/hg19_chromosome_sizes.txt.gz"
     * ));
     * 
     * AnnotationTracksTree tree = new AnnotationTracksTree(genomeAssembly,
     * conservationAssembly, mouseConservationAssembly, sizes, cytobands);
     * 
     * MainReadsWindow mWindow = new MainReadsWindow(readAssembly, sizes, tree);
     * 
     * mWindow.setVisible(true); } catch (Exception e) { e.printStackTrace(); }
     */

    // ReadsSplashScreen window = new ReadsSplashScreen(new ReadsInfo());

    // window.setVisible(true);

    PluginService.getInstance().addPlugin("htsview", CountsModule.class);
    PluginService.getInstance().addPlugin("htsview", DnaModule.class);

    EDBWLogin login = null;

    if (SettingsService.getInstance().getAsBool("edb.modules.edbw.enabled")) {
      try {
        /*
         * login = new
         * EDBWLogin(SettingsService.getInstance().getAsString("edb.modules.edbw.server"
         * ), SettingsService.getInstance().getAsString("edb.modules.edbw.user"),
         * SettingsService.getInstance().getAsString("edb.modules.edbw.key"),
         * SettingsService.getInstance().getAsInt("edb.modules.edbw.topt.epoch"),
         * SettingsService.getInstance().getAsInt("edb.modules.edbw.topt.step-size"));
         */

        login = EDBWLogin.loadFromSettings();

        HTSViewLoginDialog window = new HTSViewLoginDialog(login);

        window.setVisible(true);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    } else {
      MainHtsView.main(null, Genome.HG19, null);
    }
  }

  /**
   * Main.
   *
   * @param login
   *          the login
   * @throws ServerException
   *           the server exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   * @throws ClassNotFoundException
   *           the class not found exception
   * @throws SAXException
   *           the SAX exception
   * @throws ParserConfigurationException
   *           the parser configuration exception
   */
  public static void main(EDBWLogin login)
      throws ServerException, IOException, ClassNotFoundException, SAXException, ParserConfigurationException {
    main(login, Genome.HG19, null);
  }

  /**
   * Main.
   *
   * @param login
   *          the login
   * @param genome
   *          the genome
   * @param samples
   *          the samples
   * @throws ServerException
   *           the server exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   * @throws ClassNotFoundException
   *           the class not found exception
   * @throws SAXException
   *           the SAX exception
   * @throws ParserConfigurationException
   *           the parser configuration exception
   */
  public static void main(EDBWLogin login, String genome, Collection<Sample> samples)
      throws ServerException, IOException, ClassNotFoundException, SAXException, ParserConfigurationException {
    // Map<String, Genes> geneMap = new HashMap<String, Genes>();

    List<Path> dirs = FileUtils.lsdir(PathUtils.getPath("res", "genomes"));

    for (Path dir : dirs) {
      String g = PathUtils.namePrefix(dir);

      String namePrefix = "_" + g;

      CytobandsService.getInstance().load(g, FileUtils.newBufferedReader(dir.resolve("cytobands_" + g + ".txt.gz")));

      ChromosomeSizesService.getInstance().load(g,
          FileUtils.newBufferedReader(dir.resolve("chromosome_sizes_" + g + ".txt.gz")));

      List<Path> files = FileUtils.ls(dir.resolve("genes"));

      for (Path file : files) {
        String filename = PathUtils.getName(file);

        String db = PathUtils.namePrefix(file, namePrefix);

        if (filename.contains("gff3")) {
          GenesService.getInstance().put(g, db,
              new LazyGenes(file, Genes.gff3Parser().setKeepExons(true).setLevels(GeneType.GENE, GeneType.TRANSCRIPT)));
        } else if (filename.contains("gtf")) {
          GenesService.getInstance().put(g, db,
              new LazyGenes(file, Genes.gff3Parser().setKeepExons(true).setLevels(GeneType.TRANSCRIPT)));
        } else if (filename.contains("gtbz")) {
          GenesService.getInstance().put(g, db,
              new GTBZGenes(file, new GTBZParser().setKeepExons(true).setLevels(GeneType.TRANSCRIPT)));
        } else if (filename.contains("gtb2")) {
          GenesService.getInstance().put(g, db,
              new LazyGenes(file, Genes.gtb2Parser().setKeepExons(true).setLevels(GeneType.TRANSCRIPT)));
        } else if (filename.contains("gtb")) {
          GenesService.getInstance().put(g, db,
              new LazyGenes(file, Genes.gtbParser().setKeepExons(true).setLevels(GeneType.TRANSCRIPT)));
        } else {
          // Do nothing
        }
      }

    }

    /*
     * Path path = SettingsService.getInstance().getAsFile(
     * "htsview.annotation.ucsc.hg19.refseq.genes.gff");
     * geneMap.put(GenomeAssembly.HG19,
     * Genes.fromGFF3(Resources.getGzipReader(path)));
     * 
     * path = SettingsService.getInstance().getAsFile(
     * "htsview.annotation.ucsc.mm10.refseq.genes.gff");
     * geneMap.put(GenomeAssembly.MM10,
     * Genes.fromGFF3(Resources.getGzipReader(path)));
     * 
     * path = SettingsService.getInstance().getAsFile(
     * "htsview.annotation.ucsc.hg19.cytobands");
     * CytobandsService.getInstance().load(GenomeAssembly.HG19,
     * Resources.getGzipReader(path));
     * 
     * path = SettingsService.getInstance().getAsFile(
     * "htsview.annotation.ucsc.mm10.cytobands");
     * CytobandsService.getInstance().load(GenomeAssembly.MM10,
     * Resources.getGzipReader(path));
     * 
     * path = SettingsService.getInstance().getAsFile(
     * "htsview.annotation.ucsc.hg19.chr-sizes");
     * ChromosomeSizesService.getInstance().load(GenomeAssembly.HG19,
     * Resources.getGzipReader(path));
     * 
     * path = SettingsService.getInstance().getAsFile(
     * "htsview.annotation.ucsc.mm10.chr-sizes");
     * ChromosomeSizesService.getInstance().load(GenomeAssembly.MM10,
     * Resources.getGzipReader(path));
     */

    GenomeAssembly genomeAssembly = new GenomeAssemblyWeb(
        new URL(SettingsService.getInstance().getAsString("edb.reads.dna.remote-url")));

    ConservationAssembly conservationAssembly = new ConservationAssemblyWeb(
        new URL(SettingsService.getInstance().getAsString("edb.reads.conservation.remote-url")));

    ConservationAssembly mouseConservationAssembly = new ConservationAssemblyWeb(
        new URL(SettingsService.getInstance().getAsString("edb.reads.mouse.conservation.remote-url")));

    if (SettingsService.getInstance().getAsBool("edb.modules.edbw.enabled")) {
      ChipSeqRepositoryCache session = new ChipSeqRepositoryCache(login);

      Repository repository = session.restore();

      RepositoryService.getInstance().setRepository("chipseq", repository);

      UrlBuilder url = SettingsService.getInstance().getSetting("edb.reads.chip-seq.remote-url").getAsUrlBuilder();

      WebAssemblyService.getInstance().setSampleAssembly(new SampleAssemblyWeb(login, url));
      WebAssemblyService.getInstance().setPeakAssembly(new PeakAssemblyWeb(login));
    }

    GenomeAssembly dnaAssembly;

    if (SettingsService.getInstance().getAsBool("htsview.dna.web-mode")) {
      dnaAssembly = new GenomeAssemblyWeb(
          new URL(SettingsService.getInstance().getAsString("edb.reads.dna.remote-url")));
    } else {
      dnaAssembly = new GenomeAssemblyZip(Dna.RES_DIR);
    }

    AnnotationTracksTree tree = new AnnotationTracksTree(dnaAssembly, conservationAssembly, mouseConservationAssembly);

    // Register some functions to parse various files
    TrackParserService.getInstance().register(new SampleJsonParser());
    TrackParserService.getInstance().register(new SampleFSJsonParser());
    TrackParserService.getInstance().register(new ReadsJsonParser());
    TrackParserService.getInstance().register(new ReadsFSJsonParser());
    TrackParserService.getInstance().register(new PeaksJsonParser());
    TrackParserService.getInstance().register(new GenesJsonParser());
    TrackParserService.getInstance().register(new BedJsonParser());
    TrackParserService.getInstance().register(new BedGraphJsonParser());
    TrackParserService.getInstance().register(new AnnotationJsonParser());
    TrackParserService.getInstance().register(new SegJsonParser());
    TrackParserService.getInstance().register(new ABIJsonParser());

    SampleLoaderService.getInstance().register(new SampleLoaderBCT());
    SampleLoaderService.getInstance().register(new SampleLoaderBAM());
    SampleLoaderService.getInstance().register(new SampleLoaderBRT2());
    SampleLoaderService.getInstance().register(new SampleLoaderBRT3());
    SampleLoaderService.getInstance().register(new SampleLoaderBVT());
    SampleLoaderService.getInstance().register(new SampleLoaderBedGraph());
    SampleLoaderService.getInstance().register(new SampleLoaderBed());
    SampleLoaderService.getInstance().register(new SampleLoaderGFF());
    SampleLoaderService.getInstance().register(new SampleLoaderSeg());
    SampleLoaderService.getInstance().register(new SampleLoaderABI());

    MainHtsViewWindow window = new MainHtsViewWindow(genome, tree, samples);

    window.setVisible(true);

  }
}
