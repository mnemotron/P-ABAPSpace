package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;

public class ObjectGen {

	private String identifier;
	private String namespaceOld;
	private String namespaceNew;
	private Integer classNameMaxLength;

	public ObjectGen() {

	}

	public String getIdentifier() {
		return identifier;
	}

	@XmlElement
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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

	public Integer getClassNameMaxLength() {
		return classNameMaxLength;
	}

	@XmlElement
	public void setClassNameMaxLength(Integer classNameMaxLength) {
		this.classNameMaxLength = classNameMaxLength;
	}

}
