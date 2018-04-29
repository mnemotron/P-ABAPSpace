package abapspace.core.preset;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import abapspace.core.exception.PresetDirNotFoundException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.preset.entity.Preset;

public class PresetManager {

	private static String FILE_SUFFIX_XML = "xml";

	private File presetDir;
	private List<Preset> presetList;

	public static PresetManager getInstance(String presetDir) throws PresetDirNotFoundException {

		File locPresetDir = PresetManager.getInstanceXMLDir(presetDir);
		PresetManager locPresetManager = new PresetManager(locPresetDir);

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

	private PresetManager(File presetDir) {
		this.presetDir = presetDir;
		this.presetList = new ArrayList<Preset>();
		this.importPresetList();
	}

	public void importPresetList() {

		List<Preset> locPresetList = new ArrayList<Preset>();

		FilenameFilter locFileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("." + FILE_SUFFIX_XML);
			}
		};

		File[] locFiles = this.presetDir.listFiles(locFileNameFilter);

		// TODO only xml dateien, valid nach xsd schema
		// isPresetXMLFileValid()

		for (File file : locFiles) {
			try {
				Preset locPreset = this.importPreset(file);
				locPresetList.add(locPreset);
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

	// TODO XML Preset Validation With XSD
	private boolean isPresetXMLFileValid()
	{
//		static boolean validateAgainstXSD(InputStream xml, InputStream xsd)
//		{
//		    try
//		    {
//		        SchemaFactory factory = 
//		            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		        Schema schema = factory.newSchema(new StreamSource(xsd));
//		        Validator validator = schema.newValidator();
//		        validator.validate(new StreamSource(xml));
//		        return true;
//		    }
//		    catch(Exception ex)
//		    {
//		        return false;
//		    }
//
//	}

	return false;}

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
