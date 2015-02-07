/**
 * 
 */
package com.andrewsoft.aromabuilder;

import java.io.File;

import javax.swing.SwingWorker;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.*;

/**
 * @author Andrew
 *
 */
public class ProjectObject {

	/**
	 * 
	 */
	private String mName = null;
	private String mDirectory = null;
	//private ProgressMonitor monitor;
	private File mydir;
	private String mRomDir;
	private String mKernelDir;
	private String mAddOnsDir;
	
	public ProjectObject(String dir) {
		// TODO Auto-generated constructor stub
		
		mDirectory = dir;
		mRomDir = dir + "\\ROM";
		mKernelDir = dir + "\\Kernel";
		mAddOnsDir = dir + "\\AddOn";
		
		mydir = new File(mDirectory);
		if ( ! mydir.exists() ) mydir.mkdirs();
		mydir = new File(mRomDir);
		if ( ! mydir.exists() ) mydir.mkdirs();
		mydir = new File(mKernelDir);
		if ( ! mydir.exists() ) mydir.mkdirs();
		mydir = new File(mAddOnsDir);
		if ( ! mydir.exists() ) mydir.mkdirs();
		
	}
	
	public boolean addRom(ZipFile rom) throws ZipException {
		final ZipCompress zDialog = new ZipCompress(null, "Kicsomagolás");
		SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				// TODO Auto-generated method stub
				rom.setRunInThread(true);
				rom.extractAll(mRomDir);
				final ProgressMonitor monitor = rom.getProgressMonitor();
				while (monitor.getState() == ProgressMonitor.STATE_BUSY) {
					zDialog.setFileName(monitor.getFileName());
				}
				return null;
			}
			
			@Override
			protected void done() {
				zDialog.dispose();
			}
			
		};
		sw.execute();
		zDialog.setVisible(true);
		ParseRomAssets();
		return true;
	}
	
	/**
	 * 
	 */
	private void ParseRomAssets() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}

	/**
	 * @param mName the mName to set
	 */
	public void setmName(String mName) {
		this.mName = mName;
	}

	/**
	 * @return the mDirectory
	 */
	public String getmDirectory() {
		return mDirectory;
	}

	/**
	 * @param mDirectory the mDirectory to set
	 */
	public void setmDirectory(String mDirectory) {
		this.mDirectory = mDirectory;
	}

	public boolean buildProject()
	{
		return true;
	}

}
