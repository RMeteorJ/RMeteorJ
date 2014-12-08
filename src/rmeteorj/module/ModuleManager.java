/* 
 * RMeteorJ is a highly modular software written in Java for radio meteor scatter observation.
 * Copyright (C) 2014  Dario Zubovic
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */

package rmeteorj.module;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ModuleManager.java
 * Singleton class that searches for and loads modules, holds modules, and notifies them
 * 
 * @author Dario Zubovic
 * @version 0.1
 */
public class ModuleManager {
	private static ModuleManager manager = null;
	private static Logger log = LogManager.getLogger();

	public static final int MISC_TYPE = 0;
	public static final int CAPTURE_TYPE = 1;
	public static final int DETECT_TYPE = 1;
	public static final int ANALYZE_TYPE = 1;
	
	/** indicate if all modules are loaded */
	public static boolean modulesLoaded = false;
	
	private List<CaptureModule> captureModules = null;
	private List<DetectModule> detectModules = null;
	private List<AnalyzeModule> analyzeModules = null;
	private List<Module> miscModules = null;
	
	private Map<String, CaptureModule> enabledCaptureModules = new HashMap<String, CaptureModule>();
	private Map<String, DetectModule> enabledDetectModules = new HashMap<String, DetectModule>();
	private Map<String, AnalyzeModule> enabledAnalyzeModules = new HashMap<String, AnalyzeModule>();
	private Map<String, Module> enabledMiscModules = new HashMap<String, Module>();
	
	private ModuleManager() {}

	public static ModuleManager getManager() {
		if(manager == null) {
			ModuleManager.manager = new ModuleManager();
		}
		
		return ModuleManager.manager;
	}
	
	
	////////////////////////////
	//      LOAD/UNLOAD       //
	////////////////////////////
	
	/** Load modules */
	public void loadModules() {
		File moduleDir = new File("modules");
		if(!moduleDir.isDirectory()) {
			moduleDir.mkdir();
			return;
		}
		
		ModuleLoader ml = ModuleLoader.getLoader();
		ml.getModules(moduleDir);

		captureModules = ml.loadCapture();
		detectModules = ml.loadDetect();
		analyzeModules = ml.loadAnalyze();
		miscModules = ml.loadMisc();
		
		ModuleManager.modulesLoaded = true;
	}

	/** Unload all loaded modules */
	public void unloadAll() {
		this.unload(this.getCaptureModulesIterator());
		this.unload(this.getDetectModulesIterator());
		this.unload(this.getAnalyzeModulesIterator());
		this.unload(this.getMiscModulesIterator());
	}
	
	/** Unload modules using provided Iterator */
	private void unload(Iterator<? extends Module> itr) {
		while(itr.hasNext()) {
			itr.next().onUnload();
		}
	}

	
	/** @return capture modules iterator */
	public Iterator<CaptureModule> getCaptureModulesIterator() {
		return this.captureModules.iterator();
	}
	/** @return detect modules iterator */
	public Iterator<DetectModule> getDetectModulesIterator() {
		return this.detectModules.iterator();
	}
	/** @return capture modules iterator */
	public Iterator<AnalyzeModule> getAnalyzeModulesIterator() {
		return this.analyzeModules.iterator();
	}
	/** @return misc modules iterator */
	public Iterator<Module> getMiscModulesIterator() {
		return this.miscModules.iterator();
	}
	
	
	////////////////////////////
	//     SPECIFIC TASKS     //
	////////////////////////////
	
	/**
	 * Send opened files for detecting or analyzing
	 *
	 * @param files files to be sent
	 * @param name name of module
	 */
	public void openFilesAnalyzeOrDetect(File[] files, String name) {
		if(this.enabledDetectModules.containsKey(name)) {
			this.enabledDetectModules.get(name).openFiles(files);
		} else if(this.enabledAnalyzeModules.containsKey(name)) {
			this.enabledAnalyzeModules.get(name).openFiles(files);
		}
	}
	
	
	////////////////////////////
	//     ENABLE/DISABLE     //
	////////////////////////////

	/** @return array of names of enabled capture modules */
	public String[] getEnabledCaptureModulesNames() {
		return this.enabledCaptureModules.keySet().toArray(new String[0]);
	}
	/** @return array of names of enabled detect modules */
	public String[] getEnabledDetectModulesNames() {
		return this.enabledDetectModules.keySet().toArray(new String[0]);
	}
	/** @return array of names of enabled analyze modules */
	public String[] getEnabledAnalyzeModulesNames() {
		return this.enabledAnalyzeModules.keySet().toArray(new String[0]);
	}
	/** @return array of names of enabled misc modules */
	public String[] getEnabledMiscModulesNames() {
		return this.enabledMiscModules.keySet().toArray(new String[0]);
	}
	
	/**
	 * Switches between enable/disable state of capture module
	 *
	 * @param name name of module
	 * 
	 * @return state after switching
	 */
	public boolean switchCapture(String name) {
		if(enabledCaptureModules.containsKey(name)) {
			if(enabledCaptureModules.get(name).onDisable()) {
				enabledCaptureModules.remove(name);
				return false;
			} else {
				log.warn("Module " + name + " failed to disable");
				return true;
			}
		} else {
			Iterator<CaptureModule> itr = captureModules.iterator();
			while(itr.hasNext()) {
				CaptureModule module = itr.next();
				if(module.getName().equals(name)) {
					if(module.onEnable()) {
						enabledCaptureModules.put(name, module);
						return true;
					} else {
						log.warn("Module " + name + " failed to enable");
						return false;
					}
				}
			}
			return false;
		}
	}

	/**
	 * Switches between enable/disable state of detect module
	 *
	 * @param name name of module
	 * 
	 * @return state after switching
	 */
	public boolean switchDetect(String name) {
		if(enabledDetectModules.containsKey(name)) {
			if(enabledDetectModules.get(name).onDisable()) {
				enabledDetectModules.remove(name);
				return false;
			} else {
				log.warn("Module " + name + " failed to disable");
				return true;
			}
		} else {
			Iterator<DetectModule> itr = detectModules.iterator();
			while(itr.hasNext()) {
				DetectModule module = itr.next();
				if(module.getName().equals(name)) {
					if(module.onEnable()) {
						enabledDetectModules.put(name, module);
						return true;
					} else {
						log.warn("Module " + name + " failed to enable");
						return false;
					}
				}
			}
			return false;
		}
	}

	/**
	 * Switches between enable/disable state of analyze module
	 *
	 * @param name name of module
	 * 
	 * @return state after switching
	 */
	public boolean switchAnalyze(String name) {
		if(enabledAnalyzeModules.containsKey(name)) {
			if(enabledAnalyzeModules.get(name).onDisable()) {
				enabledAnalyzeModules.remove(name);
				return false;
			} else {
				log.warn("Module " + name + " failed to disable");
				return true;
			}
		} else {
			Iterator<AnalyzeModule> itr = analyzeModules.iterator();
			while(itr.hasNext()) {
				AnalyzeModule module = itr.next();
				if(module.getName().equals(name)) {
					if(module.onEnable()) {
						enabledAnalyzeModules.put(name, module);
						return true;
					} else {
						log.warn("Module " + name + " failed to enable");
						return false;
					}
				}
			}
			return false;
		}
	}

	/**
	 * Switches between enable/disable state of misc module
	 *
	 * @param name name of module
	 * 
	 * @return state after switching
	 */
	public boolean switchMisc(String name) {
		if(enabledMiscModules.containsKey(name)) {
			if(enabledMiscModules.get(name).onDisable()) {
				enabledMiscModules.remove(name);
				return false;
			} else {
				log.warn("Module " + name + " failed to disable");
				return true;
			}
		} else {
			Iterator<Module> itr = miscModules.iterator();
			while(itr.hasNext()) {
				Module module = itr.next();
				if(module.onEnable()) {
					enabledMiscModules.put(name, module);
					return true;
				} else {
					log.warn("Module " + name + " failed to enable");
					return false;
				}
			}
			return false;
		}
	}
}
