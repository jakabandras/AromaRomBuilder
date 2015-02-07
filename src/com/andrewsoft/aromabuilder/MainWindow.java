package com.andrewsoft.aromabuilder;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import net.lingala.zip4j.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@SuppressWarnings("unused")
public class MainWindow implements ActionListener, ItemListener{

	private JFrame frame;
	private JList<?> list_1;
	private JList<String> mStuffList;
	private final DefaultListModel<String> mSelectedItems = new DefaultListModel<String>();
	private BuilderDirs mMyDirs;
	private String mProjectName = null;
	private PluginManager pluginmanager;
	private ProjectObject activeProject;
	
	private boolean mRomAdded = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		initEnvironment();
		initPluginSystem();
	}
	
	/**
	 * 
	 */
	private void initPluginSystem() {
		// TODO Auto-generated method stub
		pluginmanager = new DefaultPluginManager();
		pluginmanager.loadPlugins();
	}

	@Override
	public void actionPerformed(ActionEvent event){
		JMenuItem menu = (JMenuItem) (event.getSource());
		switch (menu.getName()) {
		case "Kilepes":
			JOptionPane.showMessageDialog(null, "Kilépés");
			this.frame.dispose();
			break;

		default:
			break;
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 730, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Alkatr\u00E9szek", null, panel, null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {10, 0};
		gbl_panel.rowHeights = new int[] {100, 0, 30};
		gbl_panel.columnWeights = new double[]{1.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setHgap(20);
		flowLayout_1.setAlignOnBaseline(true);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		
		JTextArea txtrRom = new JTextArea();
		txtrRom.setEditable(false);
		txtrRom.setBackground(new Color(199, 21, 133));
		txtrRom.setFont(new Font("Eras Light ITC", Font.BOLD, 17));
		txtrRom.setDropMode(DropMode.USE_SELECTION);
		txtrRom.setText("ROM");
		txtrRom.setColumns(10);
		txtrRom.setRows(2);
		txtrRom.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					RomParse(droppedFiles);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel_1.add(txtrRom);
		
		JTextArea txtrScripts = new JTextArea();
		txtrScripts.setEditable(false);
		txtrScripts.setBackground(new Color(199, 21, 133));
		txtrScripts.setDropMode(DropMode.USE_SELECTION);
		txtrScripts.setFont(new Font("Eras Light ITC", Font.BOLD, 17));
		txtrScripts.setText("Scripts");
		txtrScripts.setRows(2);
		txtrScripts.setColumns(10);
		txtrScripts.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						// process files
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel_1.add(txtrScripts);
		
		JTextArea txtrSytemApps = new JTextArea();
		txtrSytemApps.setDropMode(DropMode.USE_SELECTION);
		txtrSytemApps.setFont(new Font("Eras Light ITC", Font.BOLD, 17));
		txtrSytemApps.setBackground(new Color(199, 21, 133));
		txtrSytemApps.setText("Sytem Apps");
		txtrSytemApps.setRows(2);
		txtrSytemApps.setColumns(10);
		txtrSytemApps.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						// process files
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel_1.add(txtrSytemApps);
		
		JTextArea txtrUserApps = new JTextArea();
		txtrUserApps.setDropMode(DropMode.USE_SELECTION);
		txtrUserApps.setBackground(new Color(199, 21, 133));
		txtrUserApps.setText("User Apps");
		txtrUserApps.setFont(new Font("Eras Light ITC", Font.BOLD, 17));
		txtrUserApps.setRows(2);
		txtrUserApps.setColumns(10);
		txtrUserApps.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						// process files
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel_1.add(txtrUserApps);
		
		JTextArea txtrKernel = new JTextArea();
		txtrKernel.setBackground(new Color(199, 21, 133));
		txtrKernel.setText("Kernel");
		txtrKernel.setFont(new Font("Eras Light ITC", Font.BOLD, 17));
		txtrKernel.setRows(2);
		txtrKernel.setColumns(10);
		txtrKernel.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings({ "unchecked" })
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						// process files
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel_1.add(txtrKernel);
		
		JTextArea txtrBinary = new JTextArea();
		txtrBinary.setText("Binary");
		txtrBinary.setFont(new Font("Eras Light ITC", Font.BOLD, 17));
		txtrBinary.setRows(2);
		txtrBinary.setEditable(false);
		txtrBinary.setColumns(10);
		txtrBinary.setBackground(new Color(199, 21, 133));
		txtrBinary.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings({ "unchecked" })
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						// process files
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel_1.add(txtrBinary);
		
		mStuffList = new JList<String>();
		mStuffList.setAlignmentX(3.0f);
		mStuffList.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		mStuffList.setBackground(new Color(255, 250, 205));
		GridBagConstraints gbc_mStuffList = new GridBagConstraints();
		gbc_mStuffList.gridwidth = 2;
		gbc_mStuffList.insets = new Insets(0, 0, 0, 5);
		gbc_mStuffList.fill = GridBagConstraints.BOTH;
		gbc_mStuffList.gridx = 0;
		gbc_mStuffList.gridy = 1;
		panel.add(mStuffList, gbc_mStuffList);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_2, null);
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				RowSpec.decode("max(50dlu;pref)"),}));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, "2, 3, fill, fill");
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));
		
		JTree tree = new JTree();
		panel_3.add(tree);
		
		list_1 = new JList<Object>();
		panel_3.add(list_1);
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, "2, 4, fill, fill");
		panel_4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton btnNewButton = new JButton("New button");
		panel_4.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		panel_4.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		panel_4.add(btnNewButton_2);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mFileMenu = new JMenu("Fájl");
		mFileMenu.setMnemonic('F');
		menuBar.add(mFileMenu);
		
		JMenu mMenuProject = new JMenu("Project");
		mMenuProject.setMnemonic('P');
		mMenuProject.setActionCommand("Project");
		mFileMenu.add(mMenuProject);
		
		JMenuItem mMenuOpen = new JMenuItem("Megynitás");
		mMenuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_MASK));
		mMenuOpen.setActionCommand("Megnyitás");
		mMenuOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String prjdir = mMyDirs.getProjectDir();
				prepareProject(prjdir);
			}
		});
		mMenuProject.add(mMenuOpen);
		
		JMenuItem mUj = new JMenuItem("Új project");
		mUj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
		mUj.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Új project létrehozása
				String prjname = JOptionPane.showInputDialog(null, "Új project neve : ");
				if ( ( prjname.isEmpty() ) || ( prjname == null ) || prjname.equals("") ) {
					JOptionPane.showMessageDialog(null, "Nincs megadva project név!", "HIBA", JOptionPane.ERROR_MESSAGE);
				}
				activeProject = new ProjectObject(mMyDirs.getProjectDir(prjname));
			}
		});

		mMenuProject.add(mUj);
		
		JMenuItem mMenuTulajdonsag = new JMenuItem("Tulajdonságok");
		mMenuTulajdonsag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_MASK));
		mMenuProject.add(mMenuTulajdonsag);
		
		JMenuItem mMenuExit = new JMenuItem("Kilépés");
		mMenuExit.setName("Kilepes");
		mMenuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.ALT_MASK));
		mMenuExit.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Biztos, hogy ki akarsz lépni?", "Figyelem", JOptionPane.YES_NO_OPTION);
				if ( dialogResult == JOptionPane.YES_OPTION ) System.exit(0);
			}
		});
		//mntmKilps.
		mFileMenu.add(mMenuExit);
		
		JMenu mnNewMenu = new JMenu("New menu");
		menuBar.add(mnNewMenu);
		
		JMenu mnNewMenu_1 = new JMenu("New menu");
		menuBar.add(mnNewMenu_1);
		
		JMenu mnNewMenu_2 = new JMenu("New menu");
		mnNewMenu_2.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar.add(mnNewMenu_2);
	}

	/**
	 * @param prjdir
	 */
	protected void prepareProject(String prjdir) {
		// TODO Auto-generated method stub
		File mDirs = new File(prjdir);
		File[] projects = mDirs.listFiles();
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setCurrentDirectory(mDirs);
		chooser.showOpenDialog(null);
		File selected_project = chooser.getSelectedFile();
		//JOptionPane.showMessageDialog(null, selected_project.getPath());
		activeProject = new ProjectObject(selected_project.getPath());
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void RomParse(List<File> roms) {
		if ( mRomAdded ) {
			JOptionPane.showMessageDialog(null, "Rom hozzáadása már megtörtént!");
			return;
		}
		if ( (roms.size() > 1) ) {
			JOptionPane.showMessageDialog(null, "Csak egy ROM kezelése támogatott!");
			return;
		}
		File file = roms.get(0);
		
		// Rom ellenőrzése és kicsomagolása
		
		mSelectedItems.addElement(file.getName());
		mStuffList.setModel(mSelectedItems);
		ZipFile zip;
		try {
			zip = new ZipFile(file);
			if ( zip.isValidZipFile() ) {
				if ( activeProject != null ) {
					activeProject.addRom(zip);
				} else {
					JOptionPane.showMessageDialog(null, "Nincs project megnyitva!", "HIBA!", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	private void initEnvironment()
	{
		String mUserDir = System.getProperty("user.home");
		mMyDirs = new BuilderDirs(mUserDir);
		//JOptionPane.showMessageDialog(null, mUserDir);
		
		//Check and create missing dirs
		File mDir = new File(mMyDirs.getBaseDir());
		if ( ! mDir.exists() ) mDir.mkdir();
		mDir = new File(mMyDirs.getProjectDir());
		if ( ! mDir.exists() ) mDir.mkdir();
		mDir = new File(mMyDirs.getPluginDir());
		if ( ! mDir.exists() ) mDir.mkdir();
		mDir = new File(mMyDirs.getStuffDir());
		if ( ! mDir.exists() ) mDir.mkdir();
	}

}
