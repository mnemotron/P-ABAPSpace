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
