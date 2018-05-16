package abapspace.core.context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapspace.core.exception.FileProcessException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.process.InterfaceFileProcess;

public class ContextDirectory extends File implements InterfaceFileProcess {

    private static final long serialVersionUID = -3077572976733503870L;

    private boolean root;
    private ContextManager contextManager;
    private List<File> childList;
    private InterfaceContext iContext;
    private String object;

    public ContextDirectory(boolean root, String pathname, ContextManager contextManager) {
	super(pathname);
	this.root = root;
	this.contextManager = contextManager;
	this.iContext = null;
	this.object = new String();
	this.childList = new ArrayList<File>();
    }

    @Override
    public void collectContext() throws FileProcessException {
	File[] locFileList = this.listFiles();

	// directory name
	try {
	    String locName = this.removeNamespacePlaceholder(this.getName());
	    this.processDirNameSearch(locName, this.contextManager.getContextList());
	} catch (CloneNotSupportedException e) {
	    throw new FileProcessException(
		    MessageManager.getMessage("exception.fileProcessCollectContext.cloneNotSupported"), e);
	}

	// directory children
	for (File file : locFileList) {
	    if (file.isDirectory()) {
		ContextDirectory locCD = new ContextDirectory(false, file.getAbsolutePath(), this.contextManager);
		locCD.collectContext();
		this.childList.add(locCD);
	    } else {
		ContextFile locCF = new ContextFile(file.getAbsolutePath(), this.contextManager);
		locCF.collectContext();
		this.childList.add(locCF);
	    }
	}
    }

    public void refactorContext(String parentDirPath) throws FileProcessException {

	// directory name
	String locDirName = new String();
	if (!this.root) {
	    if (parentDirPath.isEmpty()) {
		locDirName = this.refactorDirName();
	    } else {
		locDirName = parentDirPath + File.separator + this.refactorDirName();
	    }
	}

	// directory children
	for (File child : childList) {
	    if (child.isDirectory() && child instanceof ContextDirectory) {
		ContextDirectory locCD = (ContextDirectory) child;
		locCD.refactorContext(locDirName);
	    } else if (child instanceof ContextFile) {
		ContextFile locCF = (ContextFile) child;
		locCF.refactorContext(locDirName);
	    }
	}
    }

    @Override
    public void refactorContext() throws FileProcessException {
	this.refactorContext(new String());
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

	// directory name
	if (this.isDirNameObject()) {
	    locContextMap.put(this.object, this.iContext);
	}

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

	// directory name
	if (contextMap.containsKey(this.object)) {
	    InterfaceContext locIC = contextMap.get(this.object);
	    this.iContext.setReplacement(locIC.getReplacement());
	}

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

    private String refactorDirName() {
	String locResult = this.getName();

	if (this.isDirNameObject()) {
	    locResult = this.removeNamespacePlaceholder(locResult);
	    locResult = locResult.replaceAll(this.iContext.getObject(), this.iContext.getReplacement());
	    locResult = this.replaceNamespacePlaceholder(locResult);
	}

	return locResult;
    }

    private String replaceNamespacePlaceholder(String dirName) {
	String locResult = dirName;

	locResult = locResult.replace(this.contextManager.getPreset().getFileStructure().getNamespaceReplacement(),
		this.contextManager.getPreset().getFileStructure().getNamespacePlaceholder());

	return locResult;
    }

    private String removeNamespacePlaceholder(String dirName) {
	String locResult = dirName;

	locResult = locResult.replace(this.contextManager.getPreset().getFileStructure().getNamespacePlaceholder(),
		this.contextManager.getPreset().getFileStructure().getNamespaceReplacement());

	return locResult;
    }

    private boolean isDirNameObject() {
	boolean locResult = false;

	if (this.object != null && this.iContext != null) {
	    locResult = true;
	} else {
	    locResult = false;
	}

	return locResult;
    }

    private void processDirNameSearch(String dirName, List<InterfaceContext> contextList)
	    throws CloneNotSupportedException {

	for (InterfaceContext iContext : contextList) {

	    for (Matcher m = Pattern.compile(iContext.getRegex(false, false), Pattern.CASE_INSENSITIVE)
		    .matcher(dirName); m.find();) {

		String locGroup1 = m.group(1); // group 1: namespace + object ID
		String locGroup2 = m.group(2); // group 2: object name
		String locObject = locGroup1 + locGroup2;

		InterfaceContext locIContext = iContext.clone();

		LogEventManager.fireLog(LogType.INFO, MessageManager.getMessageFormat("collect.context.object.fileName",
			locObject, m.start(), m.end()));

		locIContext.setObject(new String[] { locGroup1, locGroup2 });

		this.object = locObject;
		this.iContext = locIContext;

		return;
	    }

	}
    }

}
