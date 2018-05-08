package abapspace.core.context;

import java.io.File;

public class DirectoryContext extends File {

    private static final long serialVersionUID = -3077572976733503870L;

    private InterfaceContext iContext;
    private String object;

    public DirectoryContext(String pathname) {
	super(pathname);
    }

}
