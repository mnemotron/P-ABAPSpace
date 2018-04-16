package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;

public class ObjectGen {

	private String identRegex;
	private Integer nameMaxLength;

	public ObjectGen() {

	}

	public String getIdentRegex() {
		return identRegex;
	}

	@XmlElement
	public void setIdentRegex(String identRegex) {
		this.identRegex = identRegex;
	}

	public Integer getNameMaxLength() {
		return nameMaxLength;
	}

	@XmlElement
	public void setNameMaxLength(Integer nameMaxLength) {
		this.nameMaxLength = nameMaxLength;
	}

}
