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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import abapspace.core.exception.PresetDirNotFoundException;
import abapspace.core.exception.PresetSchemaException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.preset.entity.Preset;

public class PresetManager {

	private static String FILE_SUFFIX_XPX = "xpx";
	private static String PRESET_XML_SCHEMA_PATH = "/abapspace/core/preset/entity/Preset.xsd";

	private File presetDir;
	private List<Preset> presetList;
	private Schema presetSchema;

	public static PresetManager getInstance(String presetDir) throws PresetDirNotFoundException, PresetSchemaException {

		Schema locPresetSchema = PresetManager.getInstanceXMLSchemaFile();
		File locPresetDir = PresetManager.getInstanceXMLDir(presetDir);
		PresetManager locPresetManager = new PresetManager(locPresetDir, locPresetSchema);

		return locPresetManager;
	}

	private static File getInstanceXMLDir(String presetDir) throws PresetDirNotFoundException {

		File locPresetDir = new File(presetDir);

		if (!locPresetDir.exists() && !locPresetDir.isDirectory()) {
			throw new PresetDirNotFoundException(
					MessageManager.getMessage("exception.presetDirNotFound") + locPresetDir.getAbsolutePath());
		}

		return locPresetDir;
	}

	private static Schema getInstanceXMLSchemaFile() throws PresetSchemaException {

		InputStream locPresetSchemaFile = PresetManager.class.getResourceAsStream(PRESET_XML_SCHEMA_PATH);
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema locSchema = null;

		try {
			locSchema = factory.newSchema(new StreamSource(locPresetSchemaFile));
		} catch (SAXException e) {
			throw new PresetSchemaException(MessageManager.getMessage("exception.presetSchema" + PRESET_XML_SCHEMA_PATH), e);
		}

		return locSchema;
	}

	private PresetManager(File presetDir, Schema presetSchema) {
		this.presetDir = presetDir;
		this.presetList = new ArrayList<Preset>();
		this.presetSchema = presetSchema;
		this.importPresetList();
	}

	public void importPresetList() {

		List<Preset> locPresetList = new ArrayList<Preset>();

		FilenameFilter locFileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("." + FILE_SUFFIX_XPX);
			}
		};

		File[] locFiles = this.presetDir.listFiles(locFileNameFilter);

		for (File file : locFiles) {
			try {

				if (!this.isPresetXMLFileValid(file)) {
					LogEventManager.fireLog(LogType.WARNING,
							MessageManager.getMessage("check.presetFile") + file.getAbsolutePath());
				} else {
					Preset locPreset = this.importPreset(file);
					locPresetList.add(locPreset);
				}

			} catch (JAXBException e) {
				LogEventManager.fireLog(LogType.ERROR, MessageManager.getMessage("exception.presetFileImport"), e);
			}
		}

		this.presetList = locPresetList;
	}

	private Preset importPreset(File xmlPresetFile) throws JAXBException {

		Preset locPreset = new Preset();

		JAXBContext locJAXBContext = JAXBContext.newInstance(Preset.class);

		Unmarshaller locJAXBUnmarshaller = locJAXBContext.createUnmarshaller();

		locPreset = (Preset) locJAXBUnmarshaller.unmarshal(xmlPresetFile);

		return locPreset;
	}

	private boolean isPresetXMLFileValid(File presetFile) {

		boolean locValid = false;

		try {
			Validator validator = this.presetSchema.newValidator();
			validator.validate(new StreamSource(presetFile));
			locValid = true;
		} catch (SAXException | IOException e) {
			LogEventManager.fireLog(LogType.ERROR, e.getMessage(), e);
			locValid = false;
		}

		return locValid;
	}

	public void exportPreset(File xmlFile, Preset preset) throws JAXBException {

		JAXBContext locJAXBContext = JAXBContext.newInstance(Preset.class);

		Marshaller locJAXBMarshaller = locJAXBContext.createMarshaller();
		locJAXBMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		locJAXBMarshaller.marshal(preset, xmlFile);
	}

	public List<Preset> getPresetList() {
		return presetList;
	}

	public File getPresetDir() {
		return presetDir;
	}

}
