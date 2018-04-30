package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class NamespaceOld {

	private boolean objectPolicy;
	private String namespaceOld;

	public NamespaceOld() {
		this.objectPolicy = false;
		this.namespaceOld = new String();
	}

	public boolean isObjectPolicy() {
		return objectPolicy;
	}

	@XmlAttribute
	public void setObjectPolicy(boolean objectPolicy) {
		this.objectPolicy = objectPolicy;
	}

	public String getNamespaceOld() {
		return namespaceOld;
	}

	@XmlValue
	public void setNamespaceOld(String namespaceOld) {
		this.namespaceOld = namespaceOld;
	}
}
