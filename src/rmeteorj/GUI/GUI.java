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

import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.ArrayUtils;

import rmeteorj.module.ModuleManager;

/**
 * GUI.java
 * Static GUI utility methods
 *
 * @author Dario Zubovic
 * @version 0.1
 */
public class GUI {
	/** true for GUI mode, false for GUI-less mode */
	public static boolean enabled = true;
	
	/** icon to show when module is enabled */
	public static Icon enabledModuleIcon = null;
	
	public static Image mainWindowIcon = null;
	
	static {
		try {
			enabledModuleIcon = new ImageIcon(ImageIO.read(GUI.class.getResourceAsStream("checkmark.png")));
			mainWindowIcon = ImageIO.read(GUI.class.getResourceAsStream("icon.png"));
		} catch(Exception e) {}
	}
	
	private GUI(){}
	
	/**
	 * Open file for detection or analysis
	 */
	public static void openFile() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open file");
		fc.setMultiSelectionEnabled(true);
		
		int response = fc.showOpenDialog(MainWindow.getInstance());
		
		if(response == JFileChooser.APPROVE_OPTION) {
			String[] options = ArrayUtils.addAll(ModuleManager.getManager().getEnabledDetectModulesNames(), ModuleManager.getManager().getEnabledAnalyzeModulesNames());
			if(options.length==0) {
				JOptionPane.showMessageDialog(MainWindow.getInstance(), "Enable detect or analyze module first.");
				return;
			}
			options[options.length] = "Cancel";
			
			String moduleName = (String)JOptionPane.showInputDialog(
			                    MainWindow.getInstance(),
			                    "Send opened files to: ", "Open file",
			                    JOptionPane.QUESTION_MESSAGE, null,
			                    options, options[0]);
			
			if(!moduleName.equals("Cancel")) {
				ModuleManager.getManager().openFilesAnalyzeOrDetect(fc.getSelectedFiles(), moduleName);
			}	
		}
	}

}
