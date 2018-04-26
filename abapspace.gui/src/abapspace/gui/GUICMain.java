package abapspace.gui;

import javax.swing.UnsupportedLookAndFeelException;

public class GUICMain {

    	private GUIMMain guimmain;
	private GUIMain guimain;

	public GUICMain() {
	    
	    	// init model
	    	try {
		    this.guimmain = new GUIMMain();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			| UnsupportedLookAndFeelException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    
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
