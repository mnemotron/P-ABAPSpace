package abapspace.core.process;

import java.io.File;
import abapspace.core.exception.FileProcessException;

public interface InterfaceFileProcess {

	public void processFile(File sourcefile, StringBuffer contextBuffer) throws FileProcessException;

}
