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
					@SuppressWarnings("unused")
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