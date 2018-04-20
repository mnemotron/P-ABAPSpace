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
package abapspace.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.Logger;

import abapspace.core.context.InterfaceContext;
import abapspace.core.context.entity.ContextCheckMaxNameLength;
import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.MaxNameLengthException;
import abapspace.core.exception.PresetFileImportException;
import abapspace.core.exception.PresetFileNotFoundException;
import abapspace.core.preset.ImportXMLToPreset;
import abapspace.core.preset.entity.Preset;
import abapspace.core.process.FileProcessCollectContext;
import abapspace.core.process.FileProcessRefactorContext;
import abapspace.core.process.InterfaceFileProcess;

public class Refector {

	private static ResourceBundle messages;
	private Logger log;
	private Preset preset;
	private Map<String, Map<String, InterfaceContext>> contextMap;
	private File sourceDir;
	private File targetDir;

	public static Refector FactoryGetInstance(Logger log, Locale locale, ResourceBundle messages, String xmlPresetPath)
			throws PresetFileNotFoundException, PresetFileImportException {

		Refector.messages = messages;

		File locXMLFile = Refector.getInstanceXMLFile(xmlPresetPath);

		Preset locPreset = Refector.getInstancePreset(locXMLFile);

		return new Refector(log, locPreset);
	}

	private static File getInstanceXMLFile(String xmlPresetPath) throws PresetFileNotFoundException {
		
		File locXMLFile = new File(xmlPresetPath);

		if (!locXMLFile.exists() && !locXMLFile.isFile()) {
			throw new PresetFileNotFoundException(
					Refector.messages.getString("PresetFileNotFoundException") + xmlPresetPath);
		}

		return locXMLFile;
	}

	private static Preset getInstancePreset(File xmlFile) throws PresetFileImportException {

		Preset locPreset = new Preset();
		ImportXMLToPreset locImport = new ImportXMLToPreset(xmlFile);

		try {
			locPreset = locImport.importing();
		} catch (JAXBException e) {
			throw new PresetFileImportException(Refector.messages.getString("PresetFileImportException"), e);
		}

		return locPreset;
	}

	private Refector(Logger log, Preset preset) {
		this.log = log;
		this.preset = preset;
		this.contextMap = new HashMap<String, Map<String, InterfaceContext>>();
	}

	public void refactor() throws MaxNameLengthException, FileProcessException {

		this.sourceDir = new File(this.preset.getRefactorSourceDir());
		this.targetDir = new File(this.preset.getRefactorTargetDir());

		// collect context
		try {
			collectContext();
		} catch (Exception e) {
			this.log.error(e.getMessage(), e);
			return;
		}

		// check max name length
		if (this.preset.isCheckNameMaxLength()) {
			if (!checkMaxNameLength()) {
				throw new MaxNameLengthException(Refector.messages.getString("MaxNameLengthException"));
			}
		}

		// refactor context
		refactorContext();
	}

	private boolean checkMaxNameLength() {
		final boolean[] locValid = new boolean[] { true };

		this.contextMap.forEach((fileIdent, contextMap) -> {
			contextMap.forEach((objectIdent, iContext) -> {
				ContextCheckMaxNameLength locCheck = iContext.checkMaxNameLengthForReplacement();

				if (!locCheck.isValid()) {
					this.log.error(MessageFormat.format(Refector.messages.getString("MaxNameLengthCheckFailed"), iContext.getIdentObject(), locCheck.getMaxNameLength(), locCheck.getActualNameLength()));
					locValid[0] = false;
				}
			});
		});

		return locValid[0];
	}

	private void refactorContext() throws FileProcessException {

		File[] locFileList = this.sourceDir.listFiles();

		FileProcessRefactorContext locFPRContext = new FileProcessRefactorContext(this.log, Refector.messages,
				this.sourceDir, this.targetDir, this.contextMap);

		processFiles(locFileList, locFPRContext);
	}

	private void collectContext() throws FileProcessException {

		File[] locFileList = this.sourceDir.listFiles();

		FileProcessCollectContext locFPCContext = new FileProcessCollectContext(this.log, Refector.messages, this.preset,
				this.contextMap);

		processFiles(locFileList, locFPCContext);
	}

	private void processFiles(File[] fileList, InterfaceFileProcess iFileProcess) throws FileProcessException {

		for (File file : fileList) {

			if (file.isDirectory()) {
				processFiles(file.listFiles(), iFileProcess);
				continue;
			}

			FileReader locFR = null;
			BufferedReader locBR = null;
			StringBuffer locSB = new StringBuffer();
			int locInt;

			try {
				locFR = new FileReader(file);
				locBR = new BufferedReader(locFR);

				while ((locInt = locBR.read()) != -1) {
					locSB.append((char) locInt);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {

					if (locBR != null) {
						locBR.close();
					}

					if (locFR != null) {
						locFR.close();
					}

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			iFileProcess.processFile(file, locSB);
		}

	}
}
