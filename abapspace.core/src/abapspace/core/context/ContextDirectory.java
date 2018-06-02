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
package abapspace.core.context;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.SourceDirectoryNotFoundException;
import abapspace.core.exception.TargetDirectoryNotFoundException;
import abapspace.core.exception.UnzipException;
import abapspace.core.exception.ZipException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.process.InterfaceFileProcess;
import abapspace.core.zip.ZipManager;

public class ContextDirectory extends File implements InterfaceFileProcess {

	private static final long serialVersionUID = -3077572976733503870L;
	private static final String FILE_SUFFIX_ZIP = ".zip";
	private static final String DIR_PREFIX_ZIP = "[zip]";

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

		// unpack archives
		try {
			this.unpackArchives();
		} catch (UnzipException ue) {
			throw new FileProcessException(ue.getMessage(), ue);
		} catch (SourceDirectoryNotFoundException ue) {
			throw new FileProcessException(ue.getMessage(), ue);
		}

		// directory name
		if (this.contextManager.getPreset().getFileStructure().isUpdate()
				&& !this.getName().startsWith(DIR_PREFIX_ZIP)) {
			try {
				String locName = this.removeNamespacePlaceholder(this.getName());
				this.processDirNameSearch(locName, this.contextManager.getContextList());
			} catch (CloneNotSupportedException e) {
				throw new FileProcessException(
						MessageManager.getMessage("exception.fileProcessCollectContext.cloneNotSupported"), e);
			}
		}

		// directory children
		File[] locFileList = this.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				boolean locAccept = true;

				if (pathname.isFile()) {
					locAccept = !pathname.getName().toLowerCase().endsWith(FILE_SUFFIX_ZIP);
				}

				return locAccept;
			}
		});

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

		// pack archives
		try {
			this.packArchives();
		} catch (ZipException e) {
			throw new FileProcessException(e.getMessage(), e);
		} catch (TargetDirectoryNotFoundException e) {
			throw new FileProcessException(e.getMessage(), e);
		}
	}

	@Override
	public void refactorContext() throws FileProcessException {
		this.refactorContext(new String());
	}

	@Override
	public boolean checkMaxNameLength() {

		boolean locValid = true;

		// directory name
		if (this.isDirNameObject()) {
			ContextCheckMaxNameLength locCheck = this.iContext.checkMaxNameLengthForReplacement();

			if (!locCheck.isValid()) {

				LogEventManager.fireLog(LogType.WARNING,
						MessageManager.getMessageFormat("check.maxNameLength", iContext.getObject(),
								iContext.getReplacement(), locCheck.getMaxNameLength(),
								locCheck.getActualNameLength()));

				locValid = false;
			}
		}

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

			if (!locIC.isIgnore()) {
				this.iContext.setReplacement(locIC.getReplacement());
			} else {
				LogEventManager.fireLog(LogType.INFO,
						MessageManager.getMessageFormat("refactor.object.ignore", this.object));
				this.removeDirNameObject();
			}
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

	private void removeDirNameObject() {
		this.iContext = null;
		this.object = new String();
	}

	private String refactorDirName() {
		String locResult = this.getName();

		if (this.isDirNameObject() && !this.getName().startsWith(DIR_PREFIX_ZIP)) {
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

				if (iContext.isEnhancedObject()) {
					LogEventManager.fireLog(LogType.INFO, MessageManager.getMessageFormat(
							"collect.context.object.DirName.enhanced", locObject, m.start(), m.end()));
				} else {
					LogEventManager.fireLog(LogType.INFO, MessageManager
							.getMessageFormat("collect.context.object.dirName", locObject, m.start(), m.end()));
				}

				locIContext.setObject(new String[] { locGroup1, locGroup2 });

				this.object = locObject;
				this.iContext = locIContext;

				return;
			}

		}
	}

	private void packArchives() throws ZipException, TargetDirectoryNotFoundException {

		File[] locZipDir = this.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				boolean locAccept = false;

				if (pathname.isDirectory()) {
					locAccept = pathname.getName().startsWith(DIR_PREFIX_ZIP);
				}

				return locAccept;
			}
		});

		for (File file : locZipDir) {

			File locZipTargetFile = new File(this.contextManager.getPreset().getFileTargetDir().getAbsolutePath()
					+ File.separator + (file.getName().replace(DIR_PREFIX_ZIP, "")));

			File locTargetDir = new File(this.contextManager.getPreset().getFileTargetDir().getAbsolutePath()
					+ File.separator + file.getName());

			ZipManager.zipArchive(locZipTargetFile, locTargetDir);
		}

	}

	private void unpackArchives() throws UnzipException, SourceDirectoryNotFoundException {

		File[] locZipFiles = this.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				boolean locAccept = false;

				if (pathname.isFile()) {
					locAccept = pathname.getName().toLowerCase().endsWith(FILE_SUFFIX_ZIP);
				}

				return locAccept;
			}
		});

		for (File file : locZipFiles) {

			File locFileZipSourceDir = new File(this.contextManager.getPreset().getFileSourceDir().getAbsolutePath()
					+ File.separator + DIR_PREFIX_ZIP + file.getName());
			locFileZipSourceDir.mkdir();

			ZipManager.unZip(file, locFileZipSourceDir);
		}

	}

}
