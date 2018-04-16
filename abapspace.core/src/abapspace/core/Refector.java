package abapspace.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import abapspace.core.preset.ImportXMLToPreset;
import abapspace.core.preset.entity.Preset;

public class Refector {

	private static ResourceBundle messages;
	private Preset preset;

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
	}

	public void refactor() {
		File locRootDir = new File(this.preset.getRefactorRootDir());

		File[] locFileList = locRootDir.listFiles();

		processFiles(locFileList);
	}

	private void processFiles(File[] fileList) {

		for (File file : fileList) {

			if (file.isDirectory()) {
				processFiles(file.listFiles());
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

			processFile(file, locSB);
		}

	}

	private void processFile(File file, StringBuffer fileContextSB) {

		String regex = new String();
		String locNamespaceOld = this.preset.getNamespaceOld().toLowerCase();
		
		System.out.println(file.getAbsolutePath().toString());

		// object class
		if (this.preset.getObjectClass() != null) {
		regex = locNamespaceOld + this.preset.getObjectClass().getIdentRegex().toLowerCase();
		processFileReplace(fileContextSB.toString().toLowerCase(), regex);
		}

		// object interface
		if (this.preset.getObjectInterface() != null) {
			regex = locNamespaceOld + this.preset.getObjectInterface().getIdentRegex().toLowerCase();
			processFileReplace(fileContextSB.toString().toLowerCase(), regex);
		}
		
		System.out.println();

	}

	private void processFileReplace(String fileContextString, String regex) {
		for (Matcher m = Pattern.compile(regex).matcher(fileContextString); m.find();) {
		
			System.out.println(m.group(1) + "[Start:"+m.start()+" End:"+m.end()+"]");
			
			fileContextString.replace(target, replacement)
		}
	}

}
