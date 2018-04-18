package abapspace.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import abapspace.core.context.InterfaceContext;
import abapspace.core.preset.ImportXMLToPreset;
import abapspace.core.preset.entity.Preset;
import abapspace.core.process.FileProcessCollectContext;
import abapspace.core.process.FileProcessRefactorContext;
import abapspace.core.process.InterfaceFileProcess;

public class Refector {

	private static ResourceBundle messages;
	private Preset preset;
	private Map<String, Map<String, InterfaceContext>> contextMap;
	private File sourceDir;
	private File targetDir;

	public static Refector FactoryGetInstance(Locale locale, ResourceBundle messages, String xmlPresetPath)
			throws MissingResourceException, FileNotFoundException, JAXBException {

		Refector.messages = messages;

		File locXMLFile = Refector.getInstanceXMLFile(xmlPresetPath);

		Preset locPreset = Refector.getInstancePreset(locXMLFile);

		return new Refector(locPreset);
	}

	private static File getInstanceXMLFile(String xmlPresetPath) throws FileNotFoundException {
		File locXMLFile = new File(xmlPresetPath);

		if (!locXMLFile.exists() && !locXMLFile.isFile()) {
			throw new FileNotFoundException(Refector.messages.getString("refector_xmlFileNotFound") + xmlPresetPath);
		}

		return locXMLFile;
	}

	private static Preset getInstancePreset(File xmlFile) throws JAXBException {
		ImportXMLToPreset locImport = new ImportXMLToPreset(xmlFile);

		Preset locPreset = locImport.importing();

		return locPreset;
	}

	private Refector(Preset preset) {
		this.preset = preset;
		this.contextMap = new HashMap<String, Map<String, InterfaceContext>>();
	}

	public void refactor() {

		this.sourceDir = new File(this.preset.getRefactorSourceDir());
		this.targetDir = new File(this.preset.getRefactorTargetDir());

		// collect context
		try {
			collectContext();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check max name length

		// refactor context
		try {
			refactorContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refactorContext() throws Exception {
		File[] locFileList = this.sourceDir.listFiles();

		FileProcessRefactorContext locFPRContext = new FileProcessRefactorContext(this.preset, this.sourceDir,
				this.targetDir, this.contextMap);

		processFiles(locFileList, locFPRContext);
	}

	private void collectContext() throws Exception {
		File[] locFileList = this.sourceDir.listFiles();

		FileProcessCollectContext locFPCContext = new FileProcessCollectContext(this.preset, this.contextMap);

		processFiles(locFileList, locFPCContext);
	}

	private void processFiles(File[] fileList, InterfaceFileProcess iFileProcess) throws Exception {

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
