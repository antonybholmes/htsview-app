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
package edu.columbia.rdf.htsview.app.modules;

import org.jebtk.modern.help.GuiAppInfo;

import edu.columbia.rdf.htsview.app.HTSViewInfo;

// TODO: Auto-generated Javadoc
/**
 * The class CalcModule.
 */
public abstract class HTSViewModule extends Module {

	/** The Constant DEFAULT_INFO. */
	private static final GuiAppInfo DEFAULT_INFO = 
			new HTSViewInfo();

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#getModuleInfo()
	 */
	@Override
	public GuiAppInfo getModuleInfo() {
		return DEFAULT_INFO;
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) {
		// Do nothing
	}
}
