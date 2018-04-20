package abapspace.core.exception;

public class FileProcessException extends Exception {

	private static final long serialVersionUID = 1157525420470451432L;

	public FileProcessException(String message) {
		super(message);
	}

	public FileProcessException(String message, Throwable cause) {
		super(message, cause);
	}

}
