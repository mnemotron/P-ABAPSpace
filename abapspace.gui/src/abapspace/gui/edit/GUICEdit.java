package abapspace.gui.edit;

import abapspace.gui.GUICMain;

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
//		this.guicmain.getGuimain().getFrameMain().setEnabled(false);
		this.guiedit.setVisible(true);
	}

	public void stopGUI() {
		this.guiedit.setVisible(false);
//		this.guicmain.getGuimain().getFrameMain().setEnabled(true);
	}

	public Object[][] getData() {
		return this.guimedit.getData();
	}

}
