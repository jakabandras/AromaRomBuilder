package com.andrewsoft.aromabuilder;

import javax.swing.JMenu;

import ro.fortsoft.pf4j.ExtensionPoint;

public interface AromaPlugin extends ExtensionPoint {
	
	public String getPluginName();
	
	public boolean hasMenu();
	
	public JMenu getMenu();
}
