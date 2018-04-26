package abapspace;

import java.awt.EventQueue;

import abapspace.gui.GUICMain;

public class ABAPSpace {

    public static void main(String[] args) {

	EventQueue.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		GUICMain locGUICMain = new GUICMain();

		locGUICMain.startGUI();
	    }
	});
    }
}
