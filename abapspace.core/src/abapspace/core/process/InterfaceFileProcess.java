package abapspace.core.process;

import java.io.File;

public interface InterfaceFileProcess {

	public void processFile(File sourcefile, StringBuffer contextBuffer) throws Exception;

}
