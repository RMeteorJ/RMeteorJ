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
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rmeteorj.GUI.GUI;

/**
 * ModuleLoader.java
 * Load modules
 *
 * @author Dario Zubovic
 * @version 0.1
 */
public class ModuleLoader  {
	private static ModuleLoader ml = null;
	private static Logger log = LogManager.getLogger();
	
	private URLClassLoader ucl = null;
	
	private ModuleLoader() {}
	
	public static ModuleLoader getLoader() {
		if(ml == null) {
			ml = new ModuleLoader();
		}
		
		return ml;
	}

	/**
	 * Retrieve module .jar files from moduleDir directory
	 *
	 *@param moduleDir directory to be searched for modules
	 */
	public void getModules(File moduleDir) {
		File[] moduleDirFiles = moduleDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});
		
		URL[] moduleURLs = new URL[moduleDirFiles.length];
		for(int i=0; i<moduleDirFiles.length; i++) {
			try {
				moduleURLs[i] = moduleDirFiles[i].toURI().toURL();
			} catch (MalformedURLException e) {
				log.debug(e.getMessage());
			}
		}
		
		ucl = new URLClassLoader(moduleURLs);
	}
	
	/**
	 * Load capture modules
	 * 
	 * @return list of loaded modules
	 */
	public List<CaptureModule> loadCapture() {
		List<CaptureModule> list = new ArrayList<CaptureModule>();
		
		ServiceLoader<CaptureModule> captureSL = ServiceLoader.load(CaptureModule.class, ucl);
        Iterator<CaptureModule> itr = captureSL.iterator();
        while (itr.hasNext()) {
            CaptureModule next = itr.next();
            if(next.setGUIMode(GUI.enabled)) {
                next.onLoad();
                list.add(next);
            }
        }
		
		return list;
	}

	/**
	 * Load detect modules
	 * 
	 * @return list of loaded modules
	 */
	public List<DetectModule> loadDetect() {
		List<DetectModule> list = new ArrayList<DetectModule>();
		
		ServiceLoader<DetectModule> detectSL = ServiceLoader.load(DetectModule.class, ucl);
        Iterator<DetectModule> itr = detectSL.iterator();
        while (itr.hasNext()) {
        	DetectModule next = itr.next();
            if(next.setGUIMode(GUI.enabled)) {
                next.onLoad();
                list.add(next);
            }
        }
		
		return list;
	}

	/**
	 * Load analysis modules
	 * 
	 * @return list of loaded modules
	 */
	public List<AnalyzeModule> loadAnalyze() {
		List<AnalyzeModule> list = new ArrayList<AnalyzeModule>();
		
		ServiceLoader<AnalyzeModule> analysisSL = ServiceLoader.load(AnalyzeModule.class, ucl);
        Iterator<AnalyzeModule> itr = analysisSL.iterator();
        while (itr.hasNext()) {
        	AnalyzeModule next = itr.next();
            if(next.setGUIMode(GUI.enabled)) {
                next.onLoad();
                list.add(next);
            }
        }
		
		return list;
	}

	/**
	 * Load generic modules
	 * 
	 * @return list of loaded modules
	 */
	public List<Module> loadMisc() {
		List<Module> list = new ArrayList<Module>();
		
		ServiceLoader<Module> miscSL = ServiceLoader.load(Module.class, ucl);
        Iterator<Module> itr = miscSL.iterator();
        while (itr.hasNext()) {
        	Module next = itr.next();
            if(next.setGUIMode(GUI.enabled)) {
                next.onLoad();
                list.add(next);
            }
        }
		
		return list;
	}
}