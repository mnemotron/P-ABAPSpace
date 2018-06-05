package abapspace.core.context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.TargetDirectoryNotCreatedException;
import abapspace.core.exception.TargetDirectoryNotFoundException;
import abapspace.core.exception.TargetFileContentNotWrittenException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.preset.entity.Keyword;
import abapspace.core.process.InterfaceFileProcess;

public class ContextFile extends File implements InterfaceFileProcess {

    private static final long serialVersionUID = 6780539753747307909L;

    private ContextManager contextManager;
    private Map<String, InterfaceContext> contextMap;
    private Map<String, InterfaceContext> fileNameContextMap;

    public ContextFile(String pathname, ContextManager contextManager) {
	super(pathname);
	this.contextManager = contextManager;
	this.contextMap = new HashMap<String, InterfaceContext>();
	this.fileNameContextMap = new HashMap<String, InterfaceContext>();
    }

    @Override
    public void collectContext() throws FileProcessException {

	LogEventManager.fireLog(LogType.INFO,
		MessageManager.getMessage("collect.context.file") + this.getAbsolutePath());

	try {

	    // file name
	    if (this.contextManager.getPreset().getFileStructure().isUpdate()) {
		String locName = this.removeNamespacePlaceholder(this.getName());
		this.processFileNameSearch(locName, this.contextManager.getContextList());
	    }

	    // file content
	    StringBuffer locSB = this.getContextBuffer();

	    this.contextMap = processFileContextSearch(locSB.toString(), this.contextMap,
		    this.contextManager.getContextList());
	} catch (CloneNotSupportedException e) {
	    throw new FileProcessException(
		    MessageManager.getMessage("exception.fileProcessCollectContext.cloneNotSupported"), e);
	} catch (IOException e) {
	    throw new FileProcessException(MessageManager.getMessageFormat(
		    "exception.fileProcessCollectContext.FileNotReachable" + this.getAbsolutePath()), e);
	}
    }

    public void refactorContext(String parentDirPath) throws FileProcessException {

	LogEventManager.fireLog(LogType.INFO,
		MessageFormat.format(MessageManager.getMessage("refactor.file.source"), this.getAbsolutePath()));

	try {
	    // refactor context
	    StringBuffer locSB = this.getContextBuffer();
	    String locContext = locSB.toString();

	    Iterator<Map.Entry<String, InterfaceContext>> locICMIterator = this.contextMap.entrySet().iterator();

	    while (locICMIterator.hasNext()) {
		Map.Entry<String, InterfaceContext> locV = locICMIterator.next();

		LogEventManager.fireLog(LogType.INFO, MessageManager.getMessageFormat("refactor.object",
			locV.getValue().getObject(), locV.getValue().getReplacement()));

		locContext = locContext.replaceAll(locV.getValue().getObject(), locV.getValue().getReplacement());
	    }

	    // refactor file name
	    String locFileNameNew = this.refactorFileName();

	    String locTargetPath = this.contextManager.getPreset().getFileTargetDir().getAbsolutePath() + File.separator
		    + parentDirPath + File.separator + locFileNameNew;

	    // save target file
	    this.saveTargetFile(locTargetPath, locContext);

	} catch (IOException e) {
	    throw new FileProcessException(MessageManager.getMessageFormat(
		    "exception.fileProcessRefactorContext.fileNotReachable" + this.getAbsolutePath()), e);
	} catch (TargetDirectoryNotCreatedException e) {
	    throw new FileProcessException(e.getMessage(), e);
	} catch (TargetFileContentNotWrittenException e) {
	    throw new FileProcessException(e.getMessage(), e);
	} catch (TargetDirectoryNotFoundException e) {
	    throw new FileProcessException(e.getMessage(), e);
	}
    }

    @Override
    public void refactorContext() throws FileProcessException {
	this.refactorContext(new String());
    }

    private String replaceNamespacePlaceholder(String fileName) {
	String locResult = fileName;

	locResult = locResult.replace(this.contextManager.getPreset().getFileStructure().getNamespaceReplacement(),
		this.contextManager.getPreset().getFileStructure().getNamespacePlaceholder());

	return locResult;
    }

    private String removeNamespacePlaceholder(String fileName) {
	String locResult = fileName;

	locResult = locResult.replace(this.contextManager.getPreset().getFileStructure().getNamespacePlaceholder(),
		this.contextManager.getPreset().getFileStructure().getNamespaceReplacement());

	return locResult;
    }

    private void processFileNameSearch(String fileNameString, List<InterfaceContext> contextList)
	    throws CloneNotSupportedException {

	for (InterfaceContext iContext : contextList) {

	    this.fileNameContextMap = iContext.processNameSearch(NameSearchType.FILE_NAME, false, false, fileNameString,
		    this.fileNameContextMap);
	}
    }

    private Map<String, InterfaceContext> processFileContextSearch(String fileContextString,
	    Map<String, InterfaceContext> iContextMap, List<InterfaceContext> contextList)
	    throws CloneNotSupportedException {

	Map<String, InterfaceContext> locIContextMap = iContextMap;
	List<Keyword> locKeywordList = this.contextManager.getPreset().getKeywordsExclude();

	for (InterfaceContext iContext : contextList) {

	    locIContextMap = iContext.processContextSearch(fileContextString, locKeywordList, locIContextMap);
	}

	return locIContextMap;
    }

    private StringBuffer getContextBuffer() throws IOException {

	FileReader locFR = null;
	BufferedReader locBR = null;
	StringBuffer locSB = new StringBuffer();
	int locInt;

	try {
	    locFR = new FileReader(this);
	    locBR = new BufferedReader(locFR);

	    while ((locInt = locBR.read()) != -1) {
		locSB.append((char) locInt);

	    }
	} finally {

	    if (locBR != null) {
		locBR.close();
	    }

	    if (locFR != null) {
		locFR.close();
	    }
	}

	return locSB;
    }

    private void saveTargetFile(String targetPath, String context)
	    throws TargetDirectoryNotCreatedException, TargetFileContentNotWrittenException {

	BufferedWriter locBW = null;

	File locTargetFile = new File(targetPath);

	LogEventManager.fireLog(LogType.INFO,
		MessageManager.getMessageFormat("refactor.file.target", locTargetFile.getAbsolutePath()));

	try {
	    Files.createDirectories(locTargetFile.toPath().getParent());
	} catch (IOException e) {
	    throw new TargetDirectoryNotCreatedException(MessageManager.getMessage("TargetDirectoryNotCreatedException")
		    + locTargetFile.toPath().getParent(), e);
	}

	try {
	    locBW = new BufferedWriter(new FileWriter(locTargetFile));
	    locBW.write(context);
	} catch (IOException e) {
	    throw new TargetFileContentNotWrittenException(
		    MessageManager.getMessage("TargetFileContentNotWrittenException") + locTargetFile.getAbsolutePath(),
		    e);
	} finally {
	    if (locBW != null) {
		try {
		    locBW.close();
		} catch (IOException e) {
		    LogEventManager.fireLog(LogType.ERROR,
			    MessageManager.getMessage("FileProcessRefactorContext_BW_NotClosed"), e);
		}
	    }
	}
    }

    private String refactorFileName() {

	String[] locResult = new String[1];
	locResult[0] = this.getName();

	if (this.isFileNameObject()) {
	    locResult[0] = this.removeNamespacePlaceholder(locResult[0]);

	    this.fileNameContextMap.forEach((key, value) -> {
		locResult[0] = locResult[0].replaceAll(value.getObject(), value.getReplacement());
	    });

	    locResult[0] = this.replaceNamespacePlaceholder(locResult[0]);
	}

	return locResult[0];
    }

    private boolean isFileNameObject() {
	boolean locResult = false;

	if (this.fileNameContextMap.isEmpty()) {
	    locResult = false;
	} else {
	    locResult = true;
	}

	return locResult;
    }

    @Override
    public boolean checkMaxNameLength() {

	final boolean[] locValid = new boolean[] { true };

	// check file name
	if (this.isFileNameObject()) {

	    ContextCheckMaxNameLength locCheck = null;

	    Iterator<Map.Entry<String, InterfaceContext>> locFNCMIterator = this.fileNameContextMap.entrySet()
		    .iterator();

	    while (locFNCMIterator.hasNext()) {
		Map.Entry<String, InterfaceContext> locPair = locFNCMIterator.next();

		locCheck = locPair.getValue().checkMaxNameLengthForReplacement();

		if (!locCheck.isValid()) {
		    LogEventManager.fireLog(LogType.WARNING,
			    MessageManager.getMessageFormat("check.maxNameLength", locPair.getValue().getObject(),
				    locPair.getValue().getReplacement(), locCheck.getMaxNameLength(),
				    locCheck.getActualNameLength()));

		    locValid[0] = false;
		    break;
		}
	    }

	    // ContextCheckMaxNameLength locCheck =
	    // this.iContext.checkMaxNameLengthForReplacement();
	    //
	    // if (!locCheck.isValid()) {
	    //
	    // LogEventManager.fireLog(LogType.WARNING,
	    // MessageManager.getMessageFormat("check.maxNameLength",
	    // iContext.getObject(),
	    // iContext.getReplacement(), locCheck.getMaxNameLength(),
	    // locCheck.getActualNameLength()));
	    //
	    // locValid[0] = false;
	    // }
	}

	// check file objects
	this.getContextMap().forEach((objectIdent, iContext) -> {
	    ContextCheckMaxNameLength locCheck = iContext.checkMaxNameLengthForReplacement();

	    if (!locCheck.isValid()) {
		LogEventManager.fireLog(LogType.WARNING,
			MessageManager.getMessageFormat("check.maxNameLength", iContext.getObject(),
				iContext.getReplacement(), locCheck.getMaxNameLength(),
				locCheck.getActualNameLength()));
		locValid[0] = false;
	    }
	});

	return locValid[0];
    }

    @Override
    public Map<String, InterfaceContext> getContextMap() {

	Map<String, InterfaceContext> locCM = new HashMap<String, InterfaceContext>();

	locCM.putAll(this.contextMap);

	this.fileNameContextMap.forEach((key, value) -> {
	    if (!locCM.containsKey(key)) {
		locCM.put(key, value);
	    }
	});

	return locCM;
    }

    @Override
    public void setContextMap(Map<String, InterfaceContext> contextMap) {

	List<String> locDelObject = new ArrayList<String>();

	// file name
	if (contextMap.containsKey(this.object)) {
	    InterfaceContext locIC = contextMap.get(this.object);

	    if (!locIC.isIgnore()) {
		this.iContext.setReplacement(locIC.getReplacement());
	    } else {
		LogEventManager.fireLog(LogType.INFO,
			MessageManager.getMessageFormat("refactor.object.ignore", this.object));
		this.removeFileNameObject();
	    }
	}

	// file context
	this.contextMap.forEach((objectIdent, iContext) -> {

	    if (contextMap.containsKey(objectIdent)) {
		InterfaceContext locIC = contextMap.get(objectIdent);
		if (!locIC.isIgnore()) {
		    iContext.setReplacement(contextMap.get(objectIdent).getReplacement());
		} else {
		    LogEventManager.fireLog(LogType.INFO,
			    MessageManager.getMessageFormat("refactor.object.ignore", objectIdent));
		    locDelObject.add(objectIdent);
		}
	    }

	});

	for (String object : locDelObject) {
	    this.contextMap.remove(object);
	}

    }

    private void removeFileNameObject() {
	this.fileNameContextMap.clear();
    }

}
