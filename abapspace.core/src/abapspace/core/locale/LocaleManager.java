package abapspace.core.locale;

import java.util.Locale;

public class LocaleManager {

    private static LocaleManager locManager;

    private Locale locale;

    public static LocaleManager getInstance() {
	if (LocaleManager.locManager == null) {
	    LocaleManager.locManager = new LocaleManager();
	}

	return LocaleManager.locManager;
    }

    private LocaleManager() {
	this.locale = Locale.getDefault();
    }

    public Locale getLocale() {
	return locale;
    }
}
