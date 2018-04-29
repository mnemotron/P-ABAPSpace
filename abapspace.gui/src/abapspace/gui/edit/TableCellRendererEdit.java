package abapspace.gui.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class TableCellRendererEdit extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -1254525574172848599L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Component locComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		switch (column) {
		case 1: // replacement}
			JTextField locTxfReplacement = new JTextField();
			locTxfReplacement.setText((String) value);
			locTxfReplacement.setBackground(this.getColorCheckMaxLength(table, row));
			Font locFont1 = locTxfReplacement.getFont();
			Font locNewFont1 = new Font(locFont1.getFontName(), locFont1.getStyle(), 16);
			locTxfReplacement.setFont(locNewFont1);
			locComponent = locTxfReplacement;
			break;
		case 3: // length
			JTextField locTxfLength = new JTextField();
			locTxfLength.setText((new Integer((int) value)).toString());
			locTxfLength.setEditable(false);
			locTxfLength.setBackground(this.getColorCheckMaxLength(table, row));
			Font locFont3 = locTxfLength.getFont();
			Font locNewFont3 = new Font(locFont3.getFontName(), locFont3.getStyle(), 16);
			locTxfLength.setFont(locNewFont3);
			locComponent = locTxfLength;
			break;
		}

		return locComponent;
	}

	private Color getColorCheckMaxLength(JTable table, int row) {
		TableModel locTableModel = table.getModel();

		Integer locMaxLength = (Integer) locTableModel.getValueAt(row, 2); // get max length
		Integer locLength = (Integer) locTableModel.getValueAt(row, 3); // get length

		if (locLength > locMaxLength) {
			return new Color(255, 205, 205);
		} else {
			return new Color(213, 255, 233);
		}
	}
}
