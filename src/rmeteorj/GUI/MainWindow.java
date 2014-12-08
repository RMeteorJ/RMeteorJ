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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import rmeteorj.module.AnalyzeModule;
import rmeteorj.module.CaptureModule;
import rmeteorj.module.DetectModule;
import rmeteorj.module.Module;
import rmeteorj.module.ModuleManager;

/**
 * MainWindow.java
 * Main window of RMeteorJ
 *
 * @author Dario Zubovic
 * @version 0.1
 */
public class MainWindow extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	private static MainWindow INSTANCE = null;
	
	private JDesktopPane desktop = null;
	private JPanel tabBar = null;
	
	private MainWindow() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		addWindowListener(this);
		
		setTitle("RMeteorJ");
		setIconImage(new ImageIcon(GUI.mainWindowIcon).getImage());
		setJMenuBar(createMenuBar());

		desktop = new JDesktopPane();
		desktop.setBackground(UIManager.getColor("Panel.background"));
		add(createTabBar(), BorderLayout.PAGE_START);
		add(desktop, BorderLayout.CENTER);
		
		setPreferredSize(new Dimension(500, 500));
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
	}

	public static MainWindow getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new MainWindow();
		}
		
		return INSTANCE;
	}
	
	/**
	 * Add internal frame to desktop pane
	 * 
	 * @param frame frame to be added
	 */
	public void addInternalFrame(final JInternalFrame frame) {
		desktop.add(frame);
		JButton button = new JButton(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				desktop.setSelectedFrame(frame);
			}
		});
		button.setText(frame.getTitle());
		tabBar.add(button);
	}
	
	/**
	 * Remove internal frame from desktop pane
	 * 
	 * @param frame frame to be removed
	 */
	public void removeInternalFrame(final JInternalFrame frame) {
		desktop.remove(frame);
		for(int i=0; i<tabBar.getComponentCount(); i++) {
			JButton button = (JButton) tabBar.getComponent(i);
			if(button.getText().equals(frame.getTitle())) {
				tabBar.remove(button);
			}
		}
	}


	/** Create bar that keeps list of opened internal frames
	 */
	private Component createTabBar() {
		tabBar = new JPanel();
		return tabBar;
	}
	
	/** Creates MenuBar */
	private JMenuBar createMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		bar.add(createFileMenu());

		JMenu captureMenu = new JMenu("Capture");
		Iterator<CaptureModule> captureItr = ModuleManager.getManager().getCaptureModulesIterator();
		while(captureItr.hasNext()) {
			CaptureModule module = captureItr.next();
			JMenuItem item = module.getJMenuItem();
			
			if(item!=null) {
				captureMenu.add(item);
			} else {
				captureMenu.add(new JMenuItem(new DefaultModuleAction(ModuleManager.CAPTURE_TYPE, module.getName())));
			}
		}
		bar.add(captureMenu);
		
		JMenu detectMenu = new JMenu("Detect");
		Iterator<DetectModule> detectItr = ModuleManager.getManager().getDetectModulesIterator();
		while(detectItr.hasNext()) {
			DetectModule module = detectItr.next();
			JMenuItem item = module.getJMenuItem();
			
			if(item!=null) {
				detectMenu.add(item);
			} else {
				detectMenu.add(new JMenuItem(new DefaultModuleAction(ModuleManager.DETECT_TYPE, module.getName())));
			}
		}
		bar.add(detectMenu);
		
		JMenu analyzeMenu = new JMenu("Analyze");
		Iterator<AnalyzeModule> analyzeItr = ModuleManager.getManager().getAnalyzeModulesIterator();
		while(analyzeItr.hasNext()) {
			AnalyzeModule module = analyzeItr.next();
			JMenuItem item = module.getJMenuItem();
			
			if(item!=null) {
				analyzeMenu.add(item);
			} else {
				analyzeMenu.add(new JMenuItem(new DefaultModuleAction(ModuleManager.ANALYZE_TYPE, module.getName())));
			}
		}
		bar.add(analyzeMenu);

		JMenu miscMenu = new JMenu("Misc");
		Iterator<Module> miscItr = ModuleManager.getManager().getMiscModulesIterator();
		while(miscItr.hasNext()) {
			Module module = miscItr.next();
			JMenuItem item = module.getJMenuItem();
			
			if(item!=null) {
				miscMenu.add(item);
			} else {
				miscMenu.add(new JMenuItem(new DefaultModuleAction(ModuleManager.MISC_TYPE, module.getName())));
			}
		}
		bar.add(miscMenu);
		
		return bar;
	}

	/** @return file JMenu */
	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem openItem = new JMenuItem(new AbstractAction("Open") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae) {
				GUI.openFile();
			}
		});
		openItem.setIcon(UIManager.getIcon("Tree.openIcon"));
		fileMenu.add(openItem);
		
		fileMenu.add(new JSeparator());
		
		JMenuItem closeItem = new JMenuItem(new AbstractAction("Close") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae) {
				MainWindow.getInstance().dispatchEvent(new WindowEvent(MainWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
			}
		});
		fileMenu.add(closeItem);
		
		return fileMenu;
	}

	
	@Override
	public void windowClosing(WindowEvent we) {
		String[] options = {"Yes", "No"};
		int result = JOptionPane.showOptionDialog(null,
				"Are you sure you want to exit?", "Confirm exit",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[1]);
		if(result == JOptionPane.YES_OPTION) {
			//TODO: stop all
			ModuleManager.getManager().unloadAll();
			setVisible(false);
			dispose();
			System.exit(0);
		}
	}
	@Override
	public void windowDeactivated(WindowEvent we) {}
	@Override
	public void windowDeiconified(WindowEvent we) {}
	@Override
	public void windowIconified(WindowEvent we) {}
	@Override
	public void windowOpened(WindowEvent we) {}
	@Override
	public void windowActivated(WindowEvent we) {}
	@Override
	public void windowClosed(WindowEvent we) {}
}
