package abapspace;

import java.awt.EventQueue;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import abapspace.gui.main.GUICMain;

public class ABAPSpace {

	private static final String LOOKANDFEEL_CLASSNAME_PGS = "com.pagosoft.plaf.PgsLookAndFeel";
	private static final String SYSTEM_PROPERTY_KEY_LOG4J2_CONFIG_FILE = "log4j2.configurationFile";
	private static final String SYSTEM_PROPERTY_VALUE_LOG4J2_CONFIG_FILE = "abapspace/log/log4j2.xml";

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					ABAPSpace locAbapSpace = new ABAPSpace();

					GUICMain locGUICMain = new GUICMain();

					locGUICMain.startGUI();

				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.getStackTrace();
				}

			}
		});
	}

	private ABAPSpace() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		this.setSystemProperties();
		this.setLookAndFeel();
	}

	private void setSystemProperties() {

		Properties locSystemProperties = System.getProperties();

		locSystemProperties.setProperty(SYSTEM_PROPERTY_KEY_LOG4J2_CONFIG_FILE,
				SYSTEM_PROPERTY_VALUE_LOG4J2_CONFIG_FILE);

	}

	private void setLookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(LOOKANDFEEL_CLASSNAME_PGS);
	}
}