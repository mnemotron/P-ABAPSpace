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
package abapspace.core.preset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.Logger;

import abapspace.core.exception.PresetDirNotFoundException;
import abapspace.core.preset.entity.Preset;

public class ImportXMLToPreset {

	private ResourceBundle messages;
	private Logger log;
	private File xmlPresetDir;

	public ImportXMLToPreset(ResourceBundle messages, Logger log, String xmlPresetDir)
			throws PresetDirNotFoundException {

		this.messages = messages;
		this.log = log;
		this.xmlPresetDir = getInstanceXMLFile(xmlPresetDir);

	}

	private File getInstanceXMLFile(String xmlPresetDir) throws PresetDirNotFoundException {

		File locXMLFile = new File(xmlPresetDir);

		if (!locXMLFile.exists() && !locXMLFile.isDirectory()) {
			throw new PresetDirNotFoundException(this.messages.getString("exception.presetDirNotFound") + xmlPresetDir);
		}

		return locXMLFile;
	}

	public List<Preset> importPresetList() {

		List<Preset> locPresetList = new ArrayList<Preset>();
		File[] locFiles = xmlPresetDir.listFiles();

		for (File file : locFiles) {
			try {
				Preset locPreset = this.importPreset(file);
				locPresetList.add(locPreset);
			} catch (JAXBException e) {
				this.log.error(this.messages.getString("exception.presetFileNotFound") + file.getAbsolutePath());
			}
		}

		return locPresetList;
	}

	private Preset importPreset(File xmlPresetFile) throws JAXBException {

		Preset locPreset = new Preset();

		JAXBContext locJAXBContext = JAXBContext.newInstance(Preset.class);

		Unmarshaller locJAXBUnmarshaller = locJAXBContext.createUnmarshaller();

		locPreset = (Preset) locJAXBUnmarshaller.unmarshal(xmlPresetFile);

		return locPreset;
	}

}
