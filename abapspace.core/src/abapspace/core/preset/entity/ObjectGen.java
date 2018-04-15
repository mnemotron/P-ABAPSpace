package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;

public class ObjectGen {

	private String regex;
	private String namespaceOld;
	private String namespaceNew;
	private Integer nameMaxLength;

	public ObjectGen() {

	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getNamespaceOld() {
		return namespaceOld;
	}

	@XmlElement
	public void setNamespaceOld(String namespaceOld) {
		this.namespaceOld = namespaceOld;
	}

	public String getNamespaceNew() {
		return namespaceNew;
	}

	@XmlElement
	public void setNamespaceNew(String namespaceNew) {
		this.namespaceNew = namespaceNew;
	}

	public Integer getNameMaxLength() {
		return nameMaxLength;
	}

	@XmlElement
	public void setNameMaxLength(Integer nameMaxLength) {
		this.nameMaxLength = nameMaxLength;
	}

}
