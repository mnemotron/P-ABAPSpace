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
import java.util.HashMap;
import java.util.Map;

import abapspace.core.context.ContextCheckMaxNameLength;
import abapspace.core.context.InterfaceContext;
import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.SourceDirectoryNotFoundException;
import abapspace.core.exception.TargetDirectoryNotFoundException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.preset.entity.Preset;
import abapspace.core.process.FileProcessCollectContext;
import abapspace.core.process.FileProcessRefactorContext;
import abapspace.core.process.InterfaceFileProcess;

public class Refector {

	private Preset preset;
	private Map<String, Map<String, InterfaceContext>> contextMap;

	public Refector()
	{
		this.preset = new Preset();
		this.contextMap = new HashMap<String, Map<String, InterfaceContext>>();
	}
	
	public Refector(Preset preset) {
		this.preset = preset;
		this.contextMap = new HashMap<String, Map<String, InterfaceContext>>();
	}

	public boolean checkMaxNameLength() {

		final boolean[] locValid = new boolean[] { true };

		this.contextMap.forEach((fileIdent, contextMap) -> {
			contextMap.forEach((objectIdent, iContext) -> {
				ContextCheckMaxNameLength locCheck = iContext.checkMaxNameLengthForReplacement();

				if (!locCheck.isValid()) {
					LogEventManager.fireLog(LogType.ERROR, MessageManager.getMessageFormat("check.maxNameLength",
							iContext.getObject(), iContext.getReplacement(), locCheck.getMaxNameLength(), locCheck.getActualNameLength()));
					locValid[0] = false;
				}
			});
		});

		return locValid[0];
	}

	public void refactorContext()
			throws FileProcessException, SourceDirectoryNotFoundException, TargetDirectoryNotFoundException {

		File[] locFileList = this.preset.getFileSourceDir().listFiles();

		FileProcessRefactorContext locFPRContext = new FileProcessRefactorContext(this.preset.getFileSourceDir(),
				this.preset.getFileTargetDir(), this.contextMap);

		processFiles(locFileList, locFPRContext);
	}

	public void collectContext() throws FileProcessException, SourceDirectoryNotFoundException {

		File[] locFileList = this.preset.getFileSourceDir().listFiles();

		FileProcessCollectContext locFPCContext = new FileProcessCollectContext(this.preset, this.contextMap);

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

	public Map<String, Map<String, InterfaceContext>> getContextMap() {
		return contextMap;
	}
}
