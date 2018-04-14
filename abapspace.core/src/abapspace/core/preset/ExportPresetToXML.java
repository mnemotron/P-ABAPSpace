package abapspace.core.preset;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import abapspace.core.preset.entity.Preset;

public class ExportPresetToXML {

	private File xmlFile;
	private Preset preset;

	public ExportPresetToXML(File xmlFile, Preset preset) {
		this.xmlFile = xmlFile;
		this.preset = preset;
	}

	public void exporting() throws JAXBException {

		JAXBContext locJAXBContext = JAXBContext.newInstance(Preset.class);

		Marshaller locJAXBMarshaller = locJAXBContext.createMarshaller();
		locJAXBMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		locJAXBMarshaller.marshal(this.preset, xmlFile);
	}

}
