package abapspace.core.exception;

public class TargetDirectoryNotCreatedException extends Exception {

	private static final long serialVersionUID = 1157525420470451432L;

	public TargetDirectoryNotCreatedException(String message) {
		super(message);
	}

	public TargetDirectoryNotCreatedException(String message, Throwable cause) {
		super(message, cause);
	}

}
