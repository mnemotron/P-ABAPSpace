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
package abapspace.gui.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import abapspace.core.locale.LocaleManager;

public class GUIMessageManager {

    private static final String PROPERTY_RESOURCE_BUNDLE_MESSAGES = "abapspace.gui.messages.messages";

    private static ResourceBundle messages;

    public static ResourceBundle getMessages() throws MissingResourceException {
	if (GUIMessageManager.messages == null) {
	    LocaleManager locManager = LocaleManager.getInstance();
	    GUIMessageManager.messages = ResourceBundle.getBundle(PROPERTY_RESOURCE_BUNDLE_MESSAGES,
		    locManager.getLocale());
	}

	return GUIMessageManager.messages;
    }

    public static String getMessage(String key) {
	ResourceBundle locRB = GUIMessageManager.getMessages();
	return locRB.getString(key);
    }

    public static String getMessageFormat(String key, Object ...arguments) {
	String locMessage = GUIMessageManager.getMessage(key);

	locMessage = MessageFormat.format(locMessage, arguments);

	return locMessage;
    }
}
