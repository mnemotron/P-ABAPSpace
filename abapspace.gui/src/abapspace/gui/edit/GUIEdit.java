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
package abapspace.gui.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import abapspace.gui.main.GUIMain;
import abapspace.gui.messages.GUIMessageManager;

import javax.swing.JScrollPane;

public class GUIEdit extends JFrame {

	private static final long serialVersionUID = -2761424855122643093L;
	private GUICEdit guicedit;
	private JScrollPane spEdit;
	private JTable tblEdit;
	private JButton btnCancel;
	private JButton btnRefactor;
	private JButton btnUpperCase;
	private JButton btnLowerCase;
	private JButton btnIgnore;

	public GUIEdit(GUICEdit guicedit) {

		this.guicedit = guicedit;

		initialize();
	}

	private void initialize() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				guicedit.cancel();
			}
		});

		setResizable(true);
		setTitle(GUIMessageManager.getMessage("frame.title.edit"));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(10, 10, 700, 652);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getPanelTopToolBar(), BorderLayout.NORTH);
		getContentPane().add(getSpEdit(), BorderLayout.CENTER);
		getContentPane().add(getPanelBottomToolBar(), BorderLayout.SOUTH);

	}

	private JScrollPane getSpEdit() {
		if (spEdit == null) {
			spEdit = new JScrollPane(getTblEdit());
		}
		return spEdit;
	}

	private JTable getTblEdit() {

		if (tblEdit == null) {
			String[] locColNames = { GUIMessageManager.getMessage("table.col.ignore"),
					GUIMessageManager.getMessage("table.col.object"),
					GUIMessageManager.getMessage("table.col.replacement"),
					GUIMessageManager.getMessage("table.col.maxLength"),
					GUIMessageManager.getMessage("table.col.length") };
			tblEdit = new JTable(new TableModelEdit(locColNames, guicedit.getData()));
			tblEdit.getTableHeader().setReorderingAllowed(false);
			tblEdit.setDefaultRenderer(Object.class, new TableCellRendererEdit());
			// tblEdit.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			Font locFont = tblEdit.getFont();
			Font locNewFont = new Font(locFont.getFontName(), locFont.getStyle(), 16);
			tblEdit.setFont(locNewFont);

			// adjust row height
			try {
				for (int row = 0, rc = tblEdit.getRowCount(); row < rc; row++) {
					int rowHeight = tblEdit.getRowHeight();

					for (int column = 0, cc = tblEdit.getColumnCount(); column < cc; column++) {
						Component locComponent = tblEdit.prepareRenderer(tblEdit.getCellRenderer(row, column), row,
								column);
						rowHeight = Math.max(rowHeight, locComponent.getPreferredSize().height);

						// set cell editor
						TableColumn locCol = tblEdit.getColumnModel().getColumn(column);

						switch (column) {
						case TableModelEdit.COLUMN_INDEX_IGNORE:
							locCol.setCellEditor(new TableCellEditorBooleanEdit());
							locCol.setMinWidth(150);
							locCol.setMaxWidth(200);
							locCol.setPreferredWidth(150);
							break;
						case TableModelEdit.COLUMN_INDEX_MAX_LENGTH:
							locCol.setMinWidth(150);
							locCol.setMaxWidth(200);
							locCol.setPreferredWidth(150);
							break;
						case TableModelEdit.COLUMN_INDEX_LENGTH:
							locCol.setMinWidth(150);
							locCol.setMaxWidth(200);
							locCol.setPreferredWidth(150);
							break;
						default:
							locCol.setCellEditor(new TableCellEditorStringEdit());
							break;
						}

					}

					tblEdit.setRowHeight(row, rowHeight);
				}
			} catch (ClassCastException e) {
				e.printStackTrace();
			}

		}

		return tblEdit;
	}

	private JPanel getPanelTopToolBar() {
		JPanel locPanel = new JPanel();
		FlowLayout locFL = new FlowLayout();
		locFL.setAlignment(FlowLayout.LEFT);
		locPanel.setLayout(locFL);

		locPanel.add(getBtnIgnore());
		locPanel.add(getBtnUpperCase());
		locPanel.add(getBtnLowerCase());
		locPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		return locPanel;
	}

	private JPanel getPanelBottomToolBar() {
		JPanel locPanel = new JPanel();
		locPanel.setLayout(new BorderLayout(0, 0));

		locPanel.add(getBtnRefactor(), BorderLayout.WEST);
		locPanel.add(getBtnCancel(), BorderLayout.EAST);

		return locPanel;
	}

	private JButton getBtnLowerCase() {

		if (btnLowerCase == null) {
			btnLowerCase = new JButton();
			btnLowerCase.setIcon(new ImageIcon(GUIMain.class.getResource("/abapspace/gui/res/lowercase.png")));
			btnLowerCase.setToolTipText(GUIMessageManager.getMessage("button.tooltip.lowerCase"));
			btnLowerCase.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					toCase(true);
				}
			});
		}

		return btnLowerCase;
	}

	private JButton getBtnUpperCase() {

		if (btnUpperCase == null) {
			btnUpperCase = new JButton();
			btnUpperCase.setIcon(new ImageIcon(GUIMain.class.getResource("/abapspace/gui/res/uppercase.png")));
			btnUpperCase.setToolTipText(GUIMessageManager.getMessage("button.tooltip.upperCase"));
			btnUpperCase.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					toCase(false);
				}
			});
		}

		return btnUpperCase;
	}

	private JButton getBtnIgnore() {

		if (btnIgnore == null) {
			btnIgnore = new JButton();
			btnIgnore.setIcon(new ImageIcon(GUIMain.class.getResource("/abapspace/gui/res/ignore.png")));
			btnIgnore.setToolTipText(GUIMessageManager.getMessage("button.tooltip.ignore"));
			btnIgnore.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

				}
			});
		}

		return btnIgnore;
	}

	public JButton getBtnRefactor() {

		if (btnRefactor == null) {
			btnRefactor = new JButton(GUIMessageManager.getMessage("button.refactor"));
			btnRefactor.setIcon(new ImageIcon(GUIMain.class.getResource("/abapspace/gui/res/ok.png")));

			btnRefactor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guicedit.refactor(((TableModelEdit) tblEdit.getModel()).getData());
				}
			});
		}

		return btnRefactor;
	}

	public JButton getBtnCancel() {

		if (btnCancel == null) {
			btnCancel = new JButton(GUIMessageManager.getMessage("button.cancel"));
			btnCancel.setIcon(new ImageIcon(GUIMain.class.getResource("/abapspace/gui/res/cancel.png")));
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guicedit.cancel();
				}
			});
		}

		return btnCancel;
	}

	private void toCase(boolean lowerCase) {
		JTable locTbl = getTblEdit();
		int[] locSelRows = locTbl.getSelectedRows();

		for (int row : locSelRows) {
			String locObj = (String) locTbl.getValueAt(row, TableModelEdit.COLUMN_INDEX_REPLACEMENT);

			if (lowerCase) {
				locObj = locObj.toLowerCase();
			} else {
				locObj = locObj.toUpperCase();
			}

			locTbl.setValueAt(locObj, row, TableModelEdit.COLUMN_INDEX_REPLACEMENT);
		}
	}
}
