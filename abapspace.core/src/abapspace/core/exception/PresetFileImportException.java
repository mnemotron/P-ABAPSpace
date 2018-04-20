package abapspace.core.exception;

public class PresetFileImportException extends Exception {

	private static final long serialVersionUID = 1157525420470451432L;

	public PresetFileImportException(String message) {
		super(message);
	}

	public PresetFileImportException(String message, Throwable cause) {
		super(message, cause);
	}

}
