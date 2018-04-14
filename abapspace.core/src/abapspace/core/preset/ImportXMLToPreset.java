package abapspace.core.preset;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import abapspace.core.preset.entity.Preset;

public class ImportXMLToPreset {

	private File xmlFile;

	public ImportXMLToPreset(File xmlFile) {
		this.xmlFile = xmlFile;
	}

	public Preset importing() throws JAXBException {

		Preset locPreset = new Preset();

		JAXBContext locJAXBContext = JAXBContext.newInstance(Preset.class);

		Unmarshaller locJAXBUnmarshaller = locJAXBContext.createUnmarshaller();

		locPreset = (Preset) locJAXBUnmarshaller.unmarshal(this.xmlFile);

		return locPreset;
	}

}
