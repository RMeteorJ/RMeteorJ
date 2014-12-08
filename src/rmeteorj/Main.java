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

package rmeteorj;

import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.docopt.Docopt;

import rmeteorj.GUI.GUI;
import rmeteorj.GUI.MainWindow;
import rmeteorj.module.ModuleManager;


/**
 * Main.java
 * Starts RMeteorJ barebone, initializes logger, shows splash screen, loads modules and shows GUI
 * 
 * @author Dario Zubovic
 * @version 0.1
 */
public class Main {
	static Logger log = LogManager.getLogger();
	
	private static final String doc =
		    "RMeteorJ.\n"
		    + "\n"
		    + "Usage:\n"
		    + "  RMeteorJ\n"
		    + "  RMeteorJ (-g | --nogui)\n"
		    + "  RMeteorJ (-h | --help)\n"
		    + "  RMeteorJ --version\n"
		    + "\n"
		    + "Options:\n"
		    + "  -h --help     Show this screen.\n"
		    + "  --version     Show version.\n"
		    + "  -g --nogui    Run in GUI-less mode.\n"
		    + "\n";
	
	/**
	 * The main method for RMeteorJ.
	 * 
	 * @param args Arguments for RMateorJ
	 */
	public static void main(String[] args) {
		Map<String, Object> opts = new Docopt(doc).withVersion("RMeteorJ v0.1").parse(args);
		if(opts.get("--nogui").equals(true)) {
			log.info("Running in GUI-less mode");
			GUI.enabled = false;
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			log.warn(e.getMessage());
		}
		
		if(GUI.enabled) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
					createAndShowGUI();
	            }
	        });
		}

		ModuleManager.getManager().loadModules();
	}

	/**
	 * Show splash screen, wait for modules to load, and show main window
	 */
	private static void createAndShowGUI() {
		//SplashScreen splash = SplashScreen.getSplashScreen(); //create splash screen
		//TODO: stylize splash screen
		
		while(ModuleManager.modulesLoaded==false) { }
		
		//splash.close();
		MainWindow.getInstance().setVisible(true);
	}
}
