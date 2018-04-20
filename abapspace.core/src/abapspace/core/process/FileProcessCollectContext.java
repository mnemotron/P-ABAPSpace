/*
 * MIT License
 *
 * Copyright (c) 2018 mnemotron
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package abapspace.core.process;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import abapspace.core.context.InterfaceContext;
import abapspace.core.context.entity.Context;
import abapspace.core.exception.FileProcessException;
import abapspace.core.preset.entity.Preset;

public class FileProcessCollectContext implements InterfaceFileProcess {

    private Logger log;
    private ResourceBundle messages;
    private Map<String, Map<String, InterfaceContext>> contextMap;
    private Preset preset;

    public FileProcessCollectContext(Logger log, ResourceBundle messages, Preset preset,
	    Map<String, Map<String, InterfaceContext>> contextMap) {
	this.log = log;
	this.messages = messages;
	this.preset = preset;
	this.contextMap = contextMap;
    }

    @Override
    public void processFile(File sourceFile, StringBuffer contextBuffer) throws FileProcessException {

	this.log.info(this.messages.getString("collect.context.file") + sourceFile.getAbsolutePath());

	Map<String, InterfaceContext> locIContextMap = new HashMap<String, InterfaceContext>();

	// object class
	if (this.preset.getObjectClass() != null) {
	    Context locContext = new Context();
	    locContext.setIdentRegex(this.preset.getObjectClass().getIdentRegex());
	    locContext.setNameMaxLength(this.preset.getObjectClass().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(this.preset.getNamespaceOld());

	    try {
		locIContextMap = processFileSearch(contextBuffer.toString(), locIContextMap, locContext);
	    } catch (CloneNotSupportedException e) {
		throw new FileProcessException(
			this.messages.getString("FileProcessCollectContextException_CloneNotSupported"), e);
	    }
	}

	// object interface
	if (this.preset.getObjectInterface() != null) {
	    Context locContext = new Context();
	    locContext.setIdentRegex(this.preset.getObjectInterface().getIdentRegex());
	    locContext.setNameMaxLength(this.preset.getObjectInterface().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(this.preset.getNamespaceOld());

	    try {
		locIContextMap = processFileSearch(contextBuffer.toString(), locIContextMap, locContext);
	    } catch (CloneNotSupportedException e) {
		throw new FileProcessException(
			this.messages.getString("FileProcessCollectContextException_CloneNotSupported"), e);
	    }
	}

	this.contextMap.put(sourceFile.getAbsolutePath(), (HashMap<String, InterfaceContext>) locIContextMap);
    }

    private Map<String, InterfaceContext> processFileSearch(String fileContextString,
	    Map<String, InterfaceContext> iContextMap, InterfaceContext iContext) throws CloneNotSupportedException {

	for (Matcher m = Pattern.compile(iContext.getRegex(true), Pattern.CASE_INSENSITIVE)
		.matcher(fileContextString); m.find();) {

	    String locGroup1 = m.group(1);

	    InterfaceContext locIContext = iContext.clone();

	    this.log.info(MessageFormat.format(this.messages.getString("collect.context.object"), locGroup1,
		    m.start(), m.end()));

	    if (iContextMap.containsKey(locGroup1)) {
		continue;
	    }

	    locIContext.setIdentObject(locGroup1);

	    iContextMap.put(locGroup1, locIContext);
	}

	return iContextMap;
    }
}
