package abapspace.core.exception;

public class TargetFileContentNotWrittenException extends Exception {

	private static final long serialVersionUID = 1157525420470451432L;

	public TargetFileContentNotWrittenException(String message) {
		super(message);
	}

	public TargetFileContentNotWrittenException(String message, Throwable cause) {
		super(message, cause);
	}

}
