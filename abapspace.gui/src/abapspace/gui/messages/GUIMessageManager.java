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
