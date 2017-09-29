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
package edu.columbia.rdf.edb.reads.app;

import java.io.IOException;

import edu.columbia.rdf.htsview.ngs.CountAssembly;
import edu.columbia.rdf.htsview.ngs.ReadCountsFile32Bit;
import edu.columbia.rdf.htsview.ngs.ReadCountsFileBRT;
import org.jebtk.core.io.PathUtils;
import org.junit.Test;

public class MultiResTest {
	/*
	@Test
	public void testEncode() {
		try {
			// /home/antony/Desktop/cb_40_res_test/CB4_BCL6_RK040_hg19.sorted.rmdup.sam /home/antony/Desktop/cb_40_res_test/test.sam
			ImportMultiRes.encode(PathUtils.getPath("/home/antony/Desktop/cb_40_res_test/CB4_BCL6_RK040_hg19.sorted.rmdup.sam"), 
					PathUtils.getPath("/home/antony/Desktop/cb_40_res_test/"),
					"Test",
					"Homo Sapiens",
					"hg19", 
					101, 
					1000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDecode() {
		CountAssembly test = new ReadCountsFileBRT(PathUtils.getPath("/home/antony/Desktop/cb_40_res_test/"));
		
		try {
			System.err.println("starts " + test.getCounts("chr3:187463056-187463436", 1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDecodeStrands() {
		CountAssembly test = new ReadCountsFileBRT(PathUtils.getPath("/home/antony/Desktop/cb_40_res_test/"));
		
		try {
			System.err.println("strands " + test.getStrands("chr3:187463056-187463436", 100));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDecode16bit() {
		CountAssembly test = new ReadCountsFile16Bit(PathUtils.getPath("/ifs/scratch/cancer/Lab_RDF/abh2138/ChIP_seq/data/samples/hg19/rdf/CB5_BCL6_RK050/reads"));
		
		try {
			System.err.println("16 bit  " + test.getCounts("chr3:187439165-187463513", 10));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	*/
	
}
