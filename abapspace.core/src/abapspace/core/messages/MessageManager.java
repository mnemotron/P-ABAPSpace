package abapspace.core.messages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import abapspace.core.locale.LocaleManager;

public class MessageManager {

    private static final String PROPERTY_RESOURCE_BUNDLE_MESSAGES = "abapspace.core.messages.messages";

    private static MessageManager msgObj;
    private ResourceBundle messages;

    public static MessageManager getInstance() throws MissingResourceException {
	if (MessageManager.msgObj == null) {
	    LocaleManager locManager = LocaleManager.getInstance();
	    MessageManager.msgObj = new MessageManager(locManager.getLocale());
	}

	return MessageManager.msgObj;
    }

    private MessageManager(Locale locale) throws MissingResourceException {
	this.messages = ResourceBundle.getBundle(PROPERTY_RESOURCE_BUNDLE_MESSAGES, locale);
    }

    public String getMessage(String key) {
	String locMsg = this.messages.getString(key);
	return locMsg;
    }

}
