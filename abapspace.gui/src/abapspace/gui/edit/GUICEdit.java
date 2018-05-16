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

import abapspace.gui.main.GUICMain;

public class GUICEdit {

	private GUIEdit guiedit;
	private GUIMEdit guimedit;
	private GUICMain guicmain;

	public GUICEdit(GUICMain parent, Object[][] data) {

		this.guicmain = parent;

		this.guimedit = new GUIMEdit(data);

		this.guiedit = new GUIEdit(this);
	}

	public void startGUI() {
		this.guicmain.getGuimain().getFrameMain().setEnabled(false);
		this.guiedit.setVisible(true);
		this.guiedit.toFront();
		this.guiedit.requestFocus();
	}

	public void refactor(Object[][] data) {
		this.guicmain.responseEdit(data, false);
		this.stopGUI();
	}

	public void cancel() {
		this.guicmain.responseEdit(null, true);
		this.stopGUI();
	}

	public Object[][] getData() {
		return this.guimedit.getData();
	}

	private void stopGUI() {
		this.guiedit.setVisible(false);
		this.guicmain.getGuimain().getFrameMain().setEnabled(true);
		this.guicmain.getGuimain().getFrameMain().toFront();
		this.guicmain.getGuimain().getFrameMain().requestFocus();
	}

}
