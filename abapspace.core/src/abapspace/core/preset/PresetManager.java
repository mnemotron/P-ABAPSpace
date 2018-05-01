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
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.preset.entity.Preset;

public class PresetManager {

	private static String FILE_SUFFIX_XPX = "xpx";
	private static String PRESET_XML_SCHEMA_PATH = "/abapspace/core/preset/entity/Preset.xsd";

	private File presetDir;
	private InputStream presetSchemaFile;
	private List<Preset> presetList;

	public static PresetManager getInstance(String presetDir) throws PresetDirNotFoundException {

		InputStream locPresetSchemaFile = PresetManager.getInstanceXMLSchemaFile();
		File locPresetDir = PresetManager.getInstanceXMLDir(presetDir);
		PresetManager locPresetManager = new PresetManager(locPresetDir, locPresetSchemaFile);

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

	private static InputStream getInstanceXMLSchemaFile() {
		InputStream locPresetSchemaFile = PresetManager.class.getResourceAsStream(PRESET_XML_SCHEMA_PATH);
		return locPresetSchemaFile;
	}

	private PresetManager(File presetDir, InputStream presetSchemaFile) {
		this.presetDir = presetDir;
		this.presetList = new ArrayList<Preset>();
		this.presetSchemaFile = presetSchemaFile;
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

//				if (!this.isPresetXMLFileValid(file)) {
//					LogEventManager.fireLog(LogType.WARNING,
//							MessageManager.getMessage("check.presetFile") + file.getAbsolutePath());
//				} else {
					Preset locPreset = this.importPreset(file);
					locPresetList.add(locPreset);
//				}

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
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new StreamSource(this.presetSchemaFile));
			Validator validator = schema.newValidator();
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
