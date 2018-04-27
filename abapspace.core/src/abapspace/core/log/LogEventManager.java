package abapspace.core.log;

import java.util.ArrayList;
import java.util.List;

public class LogEventManager {

    private static LogEventManager logEventManager;

    private List<LogListener> logEventListeners;

    public synchronized static LogEventManager getInstance() {
	if (LogEventManager.logEventManager == null) {
	    LogEventManager.logEventManager = new LogEventManager();
	}

	return LogEventManager.logEventManager;
    }

    public static synchronized void fireLog(LogType logType, String message, Throwable exception) {

	LogEventManager locLogEventManager = LogEventManager.getInstance();

	LogEvent locLogEvent = new LogEvent(locLogEventManager, logType, message, exception);

	locLogEventManager.fireLogEvent(locLogEvent);
    }
    
    public static synchronized void fireLog(LogType logType, String message) {

	LogEventManager locLogEventManager = LogEventManager.getInstance();

	LogEvent locLogEvent = new LogEvent(locLogEventManager, logType, message, null);

	locLogEventManager.fireLogEvent(locLogEvent);
    }
    
    private LogEventManager()
    {
	this.logEventListeners = new ArrayList<LogListener>();
    }

    public synchronized void addLogListener(LogListener logListener) {
	logEventListeners.add(logListener);
    }

    public synchronized void removeLogListener(LogListener logListener) {
	logEventListeners.remove(logListener);
    }

    private synchronized void fireLogEvent(LogEvent logEvent) {
	for (LogListener logListener : logEventListeners) {
	    logListener.log(logEvent);
	}

    }

}
