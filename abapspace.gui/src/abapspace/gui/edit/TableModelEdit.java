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

import javax.swing.table.AbstractTableModel;

public class TableModelEdit extends AbstractTableModel {

	private static final long serialVersionUID = -9070204988905662706L;
	public static final int COLUMN_INDEX_IGNORE = 0;
	public static final int COLUMN_INDEX_OBJECT = 1;
	public static final int COLUMN_INDEX_REPLACEMENT = 2;
	public static final int COLUMN_INDEX_MAX_LENGTH = 3;
	public static final int COLUMN_INDEX_LENGTH = 4;

	private String[] colNames;
	private Object[][] data;

	public TableModelEdit(String[] colNames, Object[][] data) {
		this.colNames = colNames;
		this.data = data;
	}

	@Override
	public int getRowCount() {
		return this.data.length;
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getColumnCount() {
		return this.colNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.data[rowIndex][columnIndex];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (col != COLUMN_INDEX_REPLACEMENT && col != COLUMN_INDEX_IGNORE) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {

		// set changed cell data
		data[row][col] = value;

		switch (col) {
		case COLUMN_INDEX_REPLACEMENT: // replacement changed > update length
			String locString = (String) value;
			data[row][COLUMN_INDEX_LENGTH] = locString.length();
			fireTableCellUpdated(row, COLUMN_INDEX_LENGTH);
			break;
		}

		fireTableCellUpdated(row, col);
	}

	public Object[][] getData() {
		return data;
	}

	@Override
	public Class<?> getColumnClass(int column) {
	
		switch (column) {
		case TableModelEdit.COLUMN_INDEX_IGNORE:
			return Boolean.class;
		case TableModelEdit.COLUMN_INDEX_OBJECT:
			return String.class;
		case TableModelEdit.COLUMN_INDEX_REPLACEMENT:
			return String.class;
		case TableModelEdit.COLUMN_INDEX_MAX_LENGTH:
			return String.class;
		case TableModelEdit.COLUMN_INDEX_LENGTH:
			return String.class;
		default:
			return String.class;
		}
	}
}
