/*
 * MIT License
 *
 * Copyright (c) 2018 mnemotron
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
	private JButton btnRefactor;

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
		this.frameMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

	private JPanel getPanelToolBar() {
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

	public JButton getBtnRefactor() {

		if (btnRefactor == null) {
			btnRefactor = new JButton(GUIMessageManager.getMessage("button.refactor"));

			btnRefactor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guicmain.startPreRefactor();
				}
			});
		}

		return btnRefactor;
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
