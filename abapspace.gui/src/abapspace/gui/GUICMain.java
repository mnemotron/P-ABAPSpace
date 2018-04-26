package abapspace.gui;

public class GUICMain {

    private GUIMMain guimmain;
    private GUIMain guimain;

    public GUICMain() {

	// init model
	this.guimmain = new GUIMMain();

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
