package abapspace.core.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import abapspace.core.locale.LocaleManager;

public class MessageManager {

    private static final String PROPERTY_RESOURCE_BUNDLE_MESSAGES = "abapspace.core.messages.messages";

    private static ResourceBundle messages;

    public static ResourceBundle getMessages() throws MissingResourceException {
	if (MessageManager.messages == null) {
	    LocaleManager locManager = LocaleManager.getInstance();
	    MessageManager.messages = ResourceBundle.getBundle(PROPERTY_RESOURCE_BUNDLE_MESSAGES,
		    locManager.getLocale());
	}

	return MessageManager.messages;
    }

    public static String getMessage(String key) {
	ResourceBundle locRB = MessageManager.getMessages();
	return locRB.getString(key);
    }

    public static String getMessageFormat(String key, Object ...arguments) {
	String locMessage = MessageManager.getMessage(key);

	locMessage = MessageFormat.format(locMessage, arguments);

	return locMessage;
    }
}
