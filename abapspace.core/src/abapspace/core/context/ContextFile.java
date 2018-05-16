package abapspace.core.context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private InterfaceContext iContext;
    private String object;

    public ContextFile(String pathname, ContextManager contextManager) {
	super(pathname);
	this.contextManager = contextManager;
	this.contextMap = new HashMap<String, InterfaceContext>();
	this.iContext = null;
	this.object = new String();
    }

    @Override
    public void collectContext() throws FileProcessException {

	LogEventManager.fireLog(LogType.INFO,
		MessageManager.getMessage("collect.context.file") + this.getAbsolutePath());

	try {

	    // file name
	    String locName = this.removeNamespacePlaceholder(this.getName());
	    this.processFileNameSearch(locName, this.contextManager.getContextList());

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

	    for (Matcher m = Pattern.compile(iContext.getRegex(false, false), Pattern.CASE_INSENSITIVE)
		    .matcher(fileNameString); m.find();) {

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

    private Map<String, InterfaceContext> processFileContextSearch(String fileContextString,
	    Map<String, InterfaceContext> iContextMap, List<InterfaceContext> contextList)
	    throws CloneNotSupportedException {

	for (InterfaceContext iContext : contextList) {

	    for (Matcher m = Pattern.compile(iContext.getRegex(), Pattern.CASE_INSENSITIVE)
		    .matcher(fileContextString); m.find();) {

		String locGroup1 = m.group(1); // group 1: namespace + object ID
		String locGroup2 = m.group(2); // group 2: object name
		String locObject = locGroup1 + locGroup2;

		if (this.excludeKeyword(locObject)) {
		    continue;
		}

		LogEventManager.fireLog(LogType.INFO,
			MessageManager.getMessageFormat("collect.context.object", locObject, m.start(), m.end()));

		InterfaceContext locIContext = iContext.clone();

		if (iContextMap.containsKey(locObject)) {
		    continue;
		}

		locIContext.setObject(new String[] { locGroup1, locGroup2 });

		iContextMap.put(locObject, locIContext);
	    }

	}

	return iContextMap;
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

    private boolean excludeKeyword(String object) {
	boolean locResult = false;
	List<Keyword> locKeywordList = this.contextManager.getPreset().getKeywordsExclude();

	for (Keyword keyword : locKeywordList) {
	    if (keyword.getKeyword().toLowerCase().equals(object.toLowerCase())) {
		locResult = true;
		break;
	    }
	}

	return locResult;
    }

    private String refactorFileName() {

	String locResult = this.getName();

	if (this.isFileNameObject()) {
	    locResult = this.removeNamespacePlaceholder(locResult);
	    locResult = locResult.replaceAll(this.iContext.getObject(), this.iContext.getReplacement());
	    locResult = this.replaceNamespacePlaceholder(locResult);
	}

	return locResult;
    }

    private boolean isFileNameObject() {
	boolean locResult = false;

	if (this.object != null && this.iContext != null) {
	    locResult = true;
	} else {
	    locResult = false;
	}

	return locResult;
    }

    @Override
    public boolean checkMaxNameLength() {

	final boolean[] locValid = new boolean[] { true };

	this.getContextMap().forEach((objectIdent, iContext) -> {
	    ContextCheckMaxNameLength locCheck = iContext.checkMaxNameLengthForReplacement();

	    if (!locCheck.isValid()) {
		LogEventManager.fireLog(LogType.ERROR,
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

	if (this.object != null && this.iContext != null && !locCM.containsKey(this.object)) {
	    locCM.put(this.object, this.iContext);
	}

	return locCM;
    }

    @Override
    public void setContextMap(Map<String, InterfaceContext> contextMap) {

	// file name
	if (contextMap.containsKey(this.object)) {
	    InterfaceContext locIC = contextMap.get(this.object);
	    this.iContext.setReplacement(locIC.getReplacement());
	}

	// file context
	this.contextMap.forEach((objectIdent, iContext) -> {

	    if (contextMap.containsKey(objectIdent)) {
		iContext.setReplacement(contextMap.get(objectIdent).getReplacement());
	    }

	});

    }

}
