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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapspace.core.context.Context;
import abapspace.core.context.InterfaceContext;
import abapspace.core.exception.FileProcessException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.preset.entity.NamespaceOld;
import abapspace.core.preset.entity.Preset;

public class FileProcessCollectContext implements InterfaceFileProcess {

	private Map<String, Map<String, InterfaceContext>> contextMap;
	private Preset preset;
	private List<InterfaceContext> contextList;

	public FileProcessCollectContext(Preset preset, Map<String, Map<String, InterfaceContext>> contextMap) {
		this.preset = preset;
		this.contextMap = contextMap;

		this.contextList = getContextList();
	}

	@Override
	public void processFile(File sourceFile, StringBuffer contextBuffer) throws FileProcessException {

		LogEventManager.fireLog(LogType.INFO,
				MessageManager.getMessage("collect.context.file") + sourceFile.getAbsolutePath());

		Map<String, InterfaceContext> locIContextMap = new HashMap<String, InterfaceContext>();

		try {
			locIContextMap = processFileSearch(contextBuffer.toString(), locIContextMap);
		} catch (CloneNotSupportedException e) {
			throw new FileProcessException(
					MessageManager.getMessage("exception.fileProcessCollectContext.cloneNotSupported"), e);
		}

		this.contextMap.put(sourceFile.getAbsolutePath(), (HashMap<String, InterfaceContext>) locIContextMap);
	}

	private Map<String, InterfaceContext> processFileSearch(String fileContextString,
			Map<String, InterfaceContext> iContextMap) throws CloneNotSupportedException {

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

	private List<InterfaceContext> getContextList() {
		List<InterfaceContext> locContextTmpList = null;
		List<InterfaceContext> locContextList = new ArrayList<InterfaceContext>();

		// each namespace
		List<NamespaceOld> locNSOldList = this.preset.getNamespaceOldList();

		for (NamespaceOld namespaceOld : locNSOldList) {

			// get policy objects
			if (namespaceOld.isObjectPolicy()) {
				locContextTmpList = this.getObjectPolicy(namespaceOld.getNamespaceOld());
				locContextList.addAll(locContextTmpList);
			} else {
				locContextTmpList = this.getObjectDefault(namespaceOld.getNamespaceOld());
				locContextList.addAll(locContextTmpList);
			}
		}

		return locContextList;
	}

	private List<InterfaceContext> getObjectDefault(String namespaceOld) {

		List<InterfaceContext> locContextList = new ArrayList<InterfaceContext>();

		if (this.preset.getObjectPolicyGeneral() != null) {
			Context locContext = new Context();
			locContext.setPreIdent(this.preset.getObjectPolicyGeneral().getPreIdent());
			locContext.setObjectNameIdent(this.preset.getObjectPolicyGeneral().getObjectNameIdent());
			locContext.setSupplement(this.preset.getSupplement());
			locContext.setPostIdent(this.preset.getObjectPolicyGeneral().getPostIdent());
			locContext.setNamespaceNew(this.preset.getNamespaceNew());
			locContext.setNamespaceOld(namespaceOld);
			locContextList.add(locContext);
		}

		return locContextList;
	}

	private List<InterfaceContext> getObjectPolicy(String namespaceOld) {

		List<InterfaceContext> locContextList = new ArrayList<InterfaceContext>();

		// object: package
		if (this.preset.getObjectPolicy().getObjectPackage() != null) {
			Context locContext = new Context();
			locContext.setPreIdent(this.preset.getObjectPolicy().getObjectPackage().getPreIdent());
			locContext.setObjectID(this.preset.getObjectPolicy().getObjectPackage().getObjectIdent());
			locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectPackage().getObjectNameIdent());
			locContext.setSupplement(this.preset.getSupplement());
			locContext.setPostIdent(this.preset.getObjectPolicy().getObjectPackage().getPostIdent());
			locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectPackage().getNameMaxLength());
			locContext.setNamespaceNew(this.preset.getNamespaceNew());
			locContext.setNamespaceOld(namespaceOld);
			locContextList.add(locContext);
		}

		// object: class
		if (this.preset.getObjectPolicy().getObjectClass() != null) {
			Context locContext = new Context();
			locContext.setPreIdent(this.preset.getObjectPolicy().getObjectClass().getPreIdent());
			locContext.setObjectID(this.preset.getObjectPolicy().getObjectClass().getObjectIdent());
			locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectClass().getObjectNameIdent());
			locContext.setSupplement(this.preset.getSupplement());
			locContext.setPostIdent(this.preset.getObjectPolicy().getObjectClass().getPostIdent());
			locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectClass().getNameMaxLength());
			locContext.setNamespaceNew(this.preset.getNamespaceNew());
			locContext.setNamespaceOld(namespaceOld);
			locContextList.add(locContext);
		}

		// object: interface
		if (this.preset.getObjectPolicy().getObjectInterface() != null) {
			Context locContext = new Context();
			locContext.setPreIdent(this.preset.getObjectPolicy().getObjectInterface().getPreIdent());
			locContext.setObjectID(this.preset.getObjectPolicy().getObjectInterface().getObjectIdent());
			locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectInterface().getObjectNameIdent());
			locContext.setSupplement(this.preset.getSupplement());
			locContext.setPostIdent(this.preset.getObjectPolicy().getObjectInterface().getPostIdent());
			locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectInterface().getNameMaxLength());
			locContext.setNamespaceNew(this.preset.getNamespaceNew());
			locContext.setNamespaceOld(namespaceOld);
			locContextList.add(locContext);
		}

		return locContextList;
	}
}
