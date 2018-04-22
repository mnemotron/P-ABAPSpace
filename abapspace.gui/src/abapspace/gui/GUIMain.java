package abapspace.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIMain {

	private GUICMain guimainc;

	private JFrame frameMain;
	private JPanel panelMain;

	public GUIMain(GUICMain guimainc) {

		this.guimainc = guimainc;

		// init GUI
		initialize();
	}

	private void initialize() {
		this.frameMain = new JFrame();
		this.frameMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				guimainc.stopGUI();
			}
		});
		this.frameMain.setBounds(10, 10, 700, 700);
		this.frameMain.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frameMain.getContentPane().add(getPanelMain(), BorderLayout.CENTER);
	}

	private JPanel getPanelMain() {
		if (this.panelMain == null) {
			this.panelMain = new JPanel();
			this.panelMain.setBorder(null);
			// this.panelMain.setLayout(new GridLayout(0, 1, 0, 0));
		}
		return this.panelMain;
	}

	public JFrame getFrameMain() {
		return this.frameMain;
	}
}
