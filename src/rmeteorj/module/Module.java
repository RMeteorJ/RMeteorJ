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

import javax.swing.JDesktopPane;
import javax.swing.JMenuItem;

/**
 * Module.java
 * Generic module interface
 *
 * @author Dario Zubovic
 * @version 0.1
 */
public interface Module {
	/**
	 * @return name of this module
	 */
	public String getName();
	
	/**
	 * Called when module is loaded
	 */
	public void onLoad();
	
	/**
	 * Called when program is closing. Module should save all data
	 */
	public void onUnload();

	/**
	 * Called when module is enabled
	 * 
	 * @return true if module is successfully enabled, false otherwise
	 */
	public boolean onEnable();

	/**
	 * Called when module is disabled
	 * 
	 * @return true if module is successfully disabled, false otherwise
	 */
	public boolean onDisable();

	/**
	 * Set GUI mode
	 *
	 * @param mode true for GUI mode, false for GUI-less mode
	 * 
	 * @return true if selected mode is supported, false will disable module
	 */
	public boolean setGUIMode(boolean mode);
	
	/**
	 * Generate menu item
	 *
	 * @return menu item to be added in menu
	 */
	public JMenuItem getJMenuItem();
}