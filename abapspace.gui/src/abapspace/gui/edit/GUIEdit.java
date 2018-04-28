package abapspace.gui.edit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import javax.swing.JTable;
import javax.swing.JScrollPane;

public class GUIEdit extends JFrame {

	private static final long serialVersionUID = -2761424855122643093L;
	private GUICEdit guicedit;
	private JScrollPane spEdit;
	private JTable tblEdit;

	public GUIEdit(GUICEdit guicedit) {

		this.guicedit = guicedit;

		initialize();
	}

	private void initialize() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				guicedit.stopGUI();
			}
		});

		setResizable(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(10, 10, 700, 652);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getSpEdit(), BorderLayout.CENTER);

	}

	private JScrollPane getSpEdit() {
		if (spEdit == null) {
			spEdit = new JScrollPane(getTblEdit());
		}
		return spEdit;
	}

	private JTable getTblEdit() {

		if (tblEdit == null) {
			String[] locColNames = { "Found Object", "Replacement", "Maximum Length", "Length" };
			tblEdit = new JTable(new TableModelEdit(locColNames, guicedit.getData()));
		}
		return tblEdit;
	}
}
