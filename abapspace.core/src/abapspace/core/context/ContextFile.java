package abapspace.core.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapspace.core.exception.FileProcessException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.preset.entity.Preset;
import abapspace.core.process.InterfaceFileProcess;

public class ContextFile extends File implements InterfaceFileProcess {

    private static final long serialVersionUID = 6780539753747307909L;

    private Preset preset;
    private ContextManager contextManager;
    private Map<String, InterfaceContext> contextMap;

    public ContextFile(String pathname, Preset preset, ContextManager contextManager) {
	super(pathname);
	this.preset = preset;
	this.contextManager = contextManager;
	this.contextMap = new HashMap<String, InterfaceContext>();
    }

    @Override
    public void collectContext() throws FileProcessException {

	LogEventManager.fireLog(LogType.INFO,
		MessageManager.getMessage("collect.context.file") + this.getAbsolutePath());

	//file name
	
	//file content
	try {
	    StringBuffer locSB = this.getContextBuffer();

	    this.contextMap = processFileSearch(locSB.toString(), this.contextMap,
		    this.contextManager.getContextList());
	} catch (CloneNotSupportedException e) {
	    throw new FileProcessException(
		    MessageManager.getMessage("exception.fileProcessCollectContext.cloneNotSupported"), e);
	} catch (IOException e) {
	    throw new FileProcessException(MessageManager.getMessageFormat(
		    "exception.fileProcessCollectContext.FileNotReachable" + this.getAbsolutePath()), e);
	}
    }

    @Override
    public void RefactorContext() {

    }

    private Map<String, InterfaceContext> processFileSearch(String fileContextString,
	    Map<String, InterfaceContext> iContextMap, List<InterfaceContext> contextList)
	    throws CloneNotSupportedException {

	for (InterfaceContext iContext : contextList) {

	    for (Matcher m = Pattern.compile(iContext.getRegex(), Pattern.CASE_INSENSITIVE)
		    .matcher(fileContextString); m.find();) {

		String locGroup1 = m.group(1); // group 1: namespace + object ID
		String locGroup2 = m.group(2); // group 2: object name
		String locObject = locGroup1 + locGroup2;

		InterfaceContext locIContext = iContext.clone();

		LogEventManager.fireLog(LogType.INFO,
			MessageManager.getMessageFormat("collect.context.object", locObject, m.start(), m.end()));

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

}
