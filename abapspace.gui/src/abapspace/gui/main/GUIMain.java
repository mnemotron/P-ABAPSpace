package abapspace.gui.main;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jidesoft.swing.FolderChooser;

import abapspace.gui.messages.GUIMessageManager;

public class GUIMain {

	private GUICMain guicmain;

	private JFrame frameMain;
	private PanelMain panelMain;

	public GUIMain(GUICMain guicmain) {

		this.guicmain = guicmain;

		// init GUI
		initialize();
	}

	private void initialize() {
		this.frameMain = new JFrame(GUIMessageManager.getMessage("frame.title.abapspace"));
		this.frameMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				guicmain.stopGUI();
			}
		});
		this.frameMain.setBounds(10, 10, 700, 652);
		this.frameMain.setExtendedState(Frame.NORMAL);
		this.frameMain.setResizable(false);
		this.frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frameMain.getContentPane().add(getPanelMain(), BorderLayout.CENTER);
		this.frameMain.getContentPane().add(getLblLogo(), BorderLayout.WEST);
		this.frameMain.getContentPane().add(getPanelToolBar(), BorderLayout.SOUTH);
	}

	public PanelMain getPanelMain() {
		if (this.panelMain == null) {
			this.panelMain = new PanelMain(this.guicmain);
		}
		return this.panelMain;
	}

	private JLabel getLblLogo() {
		JLabel locLblLogo = new JLabel(new ImageIcon(getClass().getResource("/abapspace/gui/res/logo.png")));

		return locLblLogo;
	}
	
	private JPanel getPanelToolBar()
	{
		JPanel locPanel = new JPanel();
		locPanel.setLayout(new BorderLayout(0, 0));
		
		locPanel.add(getBtnExit(), BorderLayout.WEST);
		locPanel.add(getBtnRefactor(), BorderLayout.CENTER);
		
		return locPanel;
	}
	
	private JButton getBtnExit() {

		JButton locBtnExit = new JButton(GUIMessageManager.getMessage("button.exit"));
		locBtnExit.setIcon(new ImageIcon(GUIMain.class.getResource("/abapspace/gui/res/exit.png")));

		locBtnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guicmain.stopGUI();
			}
		});

		return locBtnExit;
	}

	private JButton getBtnRefactor() {

		JButton locBtn = new JButton(GUIMessageManager.getMessage("button.refactor"));

		locBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guicmain.startPreRefactor();
			}
		});

		return locBtn;
	}

	public JFrame getFrameMain() {
		return this.frameMain;
	}
	
	public void showMessage(String message, String title, int msgType) {
		JOptionPane.showMessageDialog(this.frameMain, message, title, msgType);
	}

	public File showDirectoryChooser(String currentDirPath) {

		File locDir = null;

		FolderChooser locFolderChooser = new FolderChooser(currentDirPath);

		int result = locFolderChooser.showOpenDialog(this.frameMain);

		if (result == FolderChooser.APPROVE_OPTION) {
			locDir = locFolderChooser.getSelectedFile();
		}

		return locDir;
	}

}
