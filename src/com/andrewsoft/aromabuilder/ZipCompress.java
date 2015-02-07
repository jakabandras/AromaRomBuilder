/**
 * 
 */
package com.andrewsoft.aromabuilder;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.awt.Cursor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.awt.Dialog.ModalityType;


/**
 * @author Andrew
 *
 */
public class ZipCompress extends JDialog implements ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5738327247711989132L;
	
	private JProgressBar prgPercent;
	private JLabel lblFileName;
	private JLabel lblZipCommand;
	private JLabel lblTxtPercent;


	/**
	 * Create the dialog.
	 */
	public ZipCompress(JFrame parent, String title) {
		super(parent,title);
		setRootPaneCheckingEnabled(false);
		setLocale(new Locale("hu", "HU"));
		getContentPane().setLocale(new Locale("hu", "HU"));
		getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 462, 140);
		this.setMinimumSize(getSize());
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		lblZipCommand = new JLabel("New label");
		lblZipCommand.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblZipCommand.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblZipCommand, BorderLayout.NORTH);
		
		lblFileName = new JLabel("New label");
		getContentPane().add(lblFileName, BorderLayout.CENTER);
		
		lblTxtPercent = new JLabel("New label");
		getContentPane().add(lblTxtPercent, BorderLayout.EAST);
		
		prgPercent = new JProgressBar();
		prgPercent.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		prgPercent.setStringPainted(true);
		getContentPane().add(prgPercent, BorderLayout.SOUTH);
		getContentPane().setVisible(true);
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		pack();
		
		
	}
	
	public void setZipCommand(String command) {
		lblZipCommand.setText(command);
	}
	
	public void setMin(int min) {
		prgPercent.setMinimum(min);
	}
	
	public void setMax(int max) {
		prgPercent.setMaximum(max);
	}
	
	public void setMinMax(int min, int max) {
		prgPercent.setMinimum(min);
		prgPercent.setMaximum(max);
		
	}
	
	public void setFileName(String fn) {
		lblFileName.setText(fn);
	}
	
	public void setProgress(int percent){
		prgPercent.setValue(percent);
	}
	
	public void close() {
		setVisible(false);
		dispose();
	}
	
	public void uncompress(ZipFile zip, String path) throws ZipException, ReflectiveOperationException, InterruptedException {
		zip.setRunInThread(true);
		zip.extractAll(path);
		
		final ProgressMonitor monitor = zip.getProgressMonitor();
		
		while (monitor.getState() == ProgressMonitor.STATE_BUSY) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					setFileName(monitor.getFileName());
					System.out.println(monitor.getFileName());

				}
			});
			Thread.sleep(100);
		}
		close();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
}
