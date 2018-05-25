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

	public static synchronized void fireLog(LogType logType, Throwable exception) {
		LogEventManager locLogEventManager = LogEventManager.getInstance();

		LogEvent locLogEvent = new LogEvent(locLogEventManager, logType, new String(), null);

		locLogEventManager.fireLogEvent(locLogEvent);
	}

	public static synchronized void fireLog(LogType logType, String message) {

		LogEventManager locLogEventManager = LogEventManager.getInstance();

		LogEvent locLogEvent = new LogEvent(locLogEventManager, logType, message, null);

		locLogEventManager.fireLogEvent(locLogEvent);
	}

	private LogEventManager() {
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
