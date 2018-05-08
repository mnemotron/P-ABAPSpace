package abapspace.core.context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileContext extends File {

    private static final long serialVersionUID = 6780539753747307909L;

    private Map<String, InterfaceContext> contextMap;

    public FileContext(String pathname) {
	super(pathname);
	this.contextMap = new HashMap<String, InterfaceContext>();
    }

}
