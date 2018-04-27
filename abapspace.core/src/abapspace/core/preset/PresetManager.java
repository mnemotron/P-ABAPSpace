package abapspace.core.preset;

import java.io.File;
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
		File[] locFiles = this.presetDir.listFiles();

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
