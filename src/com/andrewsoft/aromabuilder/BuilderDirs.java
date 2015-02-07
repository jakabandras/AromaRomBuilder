package com.andrewsoft.aromabuilder;

public class BuilderDirs {
	
	private String mBaseDir = null;
	//private String mProjectName = null;
	private static final String BUILDER_DIR = "\\AromaRomBuilder";

	public BuilderDirs( String basedir ) {
		// TODO Auto-generated constructor stub
		setBaseDir(basedir + BUILDER_DIR);
	}
	
	/**
	 * @return the mBaseDir
	 */
	public String getBaseDir() {
		return mBaseDir;
	}

	/**
	 * @param mBaseDir the mBaseDir to set
	 */
	public void setBaseDir(String mBaseDir) {
		this.mBaseDir = mBaseDir;
	}

	public String getProjectDir() {
		return  getBaseDir() + "\\Projects";
	}
	
	public String getPluginDir() {
		return getBaseDir() + "\\Plugins";
	}
	
	public String getProjectDir(String project) {
		return getProjectDir() + "\\" + project;
	}
	
	public String getStuffDir() {
		return getBaseDir() + "\\Stuffs";
	}
	
	

}
