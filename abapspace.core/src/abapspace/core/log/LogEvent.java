package abapspace.core.log;

import java.util.EventObject;

public class LogEvent extends EventObject {

    private static final long serialVersionUID = -4410352208849146766L;

    private LogType logType;
    private String message;
    private Throwable exception;

    public LogEvent(Object source, LogType logType, String message, Throwable exception) {
	super(source);
	this.logType = logType;
	this.message = message;
	this.exception = exception;
    }

    public String getMessage() {
	return message;
    }

    public Throwable getException() {
	return exception;
    }

    public LogType getLogType() {
	return logType;
    }
}
