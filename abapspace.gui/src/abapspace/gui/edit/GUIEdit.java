package abapspace.gui.edit;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;

import javax.swing.JTable;
import javax.swing.JScrollPane;

public class GUIEdit extends JDialog{

	private GUICEdit guicedit;
	private JPanel contentPane;
	private JScrollPane spEdit;
	private JTable tblEdit;

	public GUIEdit(GUICEdit guicedit) {

		this.guicedit = guicedit;

		initialize();
	}

	private void initialize() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 450, 300);
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				guicedit.stopGUI();
//			}
//		});

		setBounds(100, 100, 450, 300);
		setModal(true);
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
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
			String[] locColNames = {"Found Object", "Replacement", "Maximum Length", "Length"};
			tblEdit = new JTable(new TableModelEdit(locColNames, guicedit.getData()));
		}
		return tblEdit;
	}
}
