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
package edu.columbia.rdf.htsview.app.tracks.peaks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.network.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.edb.EDB;
import edu.columbia.rdf.edb.EDBWLogin;

// TODO: Auto-generated Javadoc
/**
 * Maintains a connection to a caArray server.
 */
public class PeakAssemblyWeb extends PeakAssembly {
	
	/** The Constant LOG. */
	private static final Logger LOG = 
			LoggerFactory.getLogger(PeakAssemblyWeb.class);
					
	/** The m auth V 1. */
	private UrlBuilder mAuthV1;
	
	/**
	 * Instantiates a new track assembly web.
	 *
	 * @param login the login
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PeakAssemblyWeb(EDBWLogin login) throws IOException {
		mAuthV1 = login.getOTKAuthUrl();
	}
	
	/* (non-Javadoc)
	 * @see org.htsview.tracks.peaks.PeakAssembly#getJsonPeaks(int)
	 */
	@Override
	public List<PeakSet> getJsonPeaks(int sampleId) throws IOException {
		List<PeakSet> ret = new ArrayList<PeakSet>(1000);
		
		UrlBuilder peaksUrl = mAuthV1
				.resolve("samples")
				.resolve(sampleId)
				.resolve("chipseq")
				.resolve("peaks");
		
		LOG.info("peaks url: {}", peaksUrl);
		
		Json json = new JsonParser().parse(peaksUrl.toUrl());
				
		for (int i = 0; i < json.size(); ++i) {
			ret.add(PeakSet.createPeaks(json.get(i).getAsInt(EDB.HEADING_ID), json.get(i).getAsString(EDB.HEADING_NAME)));
		}

		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.htsview.tracks.peaks.PeakAssembly#downloadJsonPeaks(int, int)
	 */
	@Override
	public List<GenomicRegion> downloadJsonPeaks(int sampleId, int peaksId) throws IOException {
		List<GenomicRegion> ret = new ArrayList<GenomicRegion>(1000);
		
		UrlBuilder peaksUrl = mAuthV1
				//.resolve("samples")
				//.resolve(sampleId)
				.resolve("chipseq")
				.resolve("peaks")
				.resolve("download")
				.resolve(peaksId);
				//.param("id", peaksId);
		
		LOG.info("peaks url: {}", peaksUrl);
		
		Json json = new JsonParser().parse(peaksUrl.toUrl());
			
		//Json locationsJson = json.get(0).get("l");
			
		for (int i = 0; i < json.size(); ++i) {
			GenomicRegion region = GenomicRegion.parse(json.get(i).getAsString());
			
			if (region != null) {
				ret.add(region);
			}
		}

		return ret;
	}
}
