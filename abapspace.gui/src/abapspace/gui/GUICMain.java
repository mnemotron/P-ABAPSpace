package abapspace.gui;

public class GUICMain {

	private GUIMain guimain;

	public GUICMain() {
		// init GUI
		this.guimain = new GUIMain(this);
	}

	public void startGUI() {
		this.guimain.getFrameMain().setVisible(true);
	}

	public void stopGUI() {
		this.guimain.getFrameMain().setVisible(false);
		System.exit(0);
	}

}
