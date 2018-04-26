package abapspace.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import abapspace.gui.panel.PanelMain;

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
	this.frameMain.setBounds(10, 10, 700, 648);
	this.frameMain.setExtendedState(Frame.NORMAL);
	this.frameMain.setResizable(false);
	this.frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.frameMain.getContentPane().add(getPanelMain(), BorderLayout.CENTER);
	this.frameMain.getContentPane().add(getLblLogo(), BorderLayout.WEST);
	this.frameMain.getContentPane().add(getBtnRefactor(), BorderLayout.SOUTH);
    }

    private JPanel getPanelMain() {
	if (this.panelMain == null) {
	    this.panelMain = new PanelMain();
	}
	return this.panelMain;
    }

    private JLabel getLblLogo() {
	// TODO Logo
	JLabel locLblLogo = new JLabel(new ImageIcon(getClass().getResource("/abapspace/gui/res/logo.png")));

	return locLblLogo;
    }

    private JMenuBar getMenuBar() {
	JMenuBar locMenuBar = new JMenuBar();
	locMenuBar.add(getBtnRefactor());
	return locMenuBar;
    }

    private JButton getBtnRefactor() {

	JButton locBtn = new JButton("Refactor");

	locBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	    }
	});

	return locBtn;
    }

    public JFrame getFrameMain() {
	return this.frameMain;
    }
}
