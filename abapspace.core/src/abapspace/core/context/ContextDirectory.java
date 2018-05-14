package abapspace.core.context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abapspace.core.exception.FileProcessException;
import abapspace.core.process.InterfaceFileProcess;

public class ContextDirectory extends File implements InterfaceFileProcess {

    private static final long serialVersionUID = -3077572976733503870L;

    private ContextManager contextManager;
    private List<File> childList;
    private InterfaceContext iContext;
    private String object;

    public ContextDirectory(String pathname, ContextManager contextManager) {
	super(pathname);
	this.contextManager = contextManager;
	this.iContext = null;
	this.object = new String();
	this.childList = new ArrayList<File>();
    }

    @Override
    public void collectContext() throws FileProcessException {
	File[] locFileList = this.listFiles();

	// TODO directory name

	// directory children
	for (File file : locFileList) {
	    if (file.isDirectory()) {
		ContextDirectory locCD = new ContextDirectory(file.getAbsolutePath(), this.contextManager);
		locCD.collectContext();
		this.childList.add(locCD);
	    } else {
		ContextFile locCF = new ContextFile(file.getAbsolutePath(), this.contextManager);
		locCF.collectContext();
		this.childList.add(locCF);
	    }
	}
    }

    @Override
    public void refactorContext() throws FileProcessException {

	// TODO directory name

	// directory children
	for (File child : childList) {
	    if (child.isDirectory() && child instanceof ContextDirectory) {
		ContextDirectory locCD = (ContextDirectory) child;
		locCD.refactorContext();
	    } else if (child instanceof ContextFile) {
		ContextFile locCF = (ContextFile) child;
		locCF.refactorContext();
	    }
	}
    }

    @Override
    public boolean checkMaxNameLength() {
	boolean locValid = true;

	// TODO directory name

	// directory children
	for (File child : childList) {
	    if (child.isDirectory() && child instanceof ContextDirectory) {
		ContextDirectory locCD = (ContextDirectory) child;
		if (!locCD.checkMaxNameLength()) {
		    locValid = false;
		}
	    } else if (child instanceof ContextFile) {
		ContextFile locCF = (ContextFile) child;
		if (!locCF.checkMaxNameLength()) {
		    locValid = false;
		}
	    }
	}

	return locValid;
    }

    @Override
    public Map<String, InterfaceContext> getContextMap() {

	Map<String, InterfaceContext> locContextMap = new HashMap<String, InterfaceContext>();

	// TODO directory name

	// directory children
	for (File child : childList) {
	    if (child.isDirectory() && child instanceof ContextDirectory) {
		ContextDirectory locCD = (ContextDirectory) child;
		Map<String, InterfaceContext> locCM = locCD.getContextMap();

		locContextMap.putAll(locCM);

	    } else if (child instanceof ContextFile) {
		ContextFile locCF = (ContextFile) child;
		Map<String, InterfaceContext> locCM = locCF.getContextMap();

		locContextMap.putAll(locCM);
	    }
	}

	return locContextMap;
    }

    @Override
    public void setContextMap(Map<String, InterfaceContext> contextMap) {

	// TODO directory name

	// directory children
	for (File child : childList) {
	    if (child.isDirectory() && child instanceof ContextDirectory) {
		ContextDirectory locCD = (ContextDirectory) child;
		locCD.setContextMap(contextMap);

	    } else if (child instanceof ContextFile) {
		ContextFile locCF = (ContextFile) child;
		locCF.setContextMap(contextMap);
	    }
	}
    }

}
