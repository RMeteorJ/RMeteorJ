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
package rmeteorj.GUI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import rmeteorj.module.ModuleManager;

/**
 * ModuleAction.java
 * Default action for modules without specific menu item (that action is enable/disable) 
 *
 * @author Dario Zubovic
 * @version 0.1
 */
public class DefaultModuleAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private int type = 0;
	private String name = "";
	
	public DefaultModuleAction(int type, String name) {
		super(name);
		this.type = type;
		this.name = name;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		boolean b;
		if(type == ModuleManager.CAPTURE_TYPE) {
			b = ModuleManager.getManager().switchCapture(name);
		} else if(type == ModuleManager.DETECT_TYPE) {
			b = ModuleManager.getManager().switchDetect(name);
		} else if(type == ModuleManager.ANALYZE_TYPE) {
			b = ModuleManager.getManager().switchAnalyze(name);
		} else {
			b = ModuleManager.getManager().switchMisc(name);
		}
		
		if(b) {
			((JMenuItem) ae.getSource()).setIcon(GUI.enabledModuleIcon);
		} else {
			((JMenuItem) ae.getSource()).setIcon(null);
		}
	}

}
