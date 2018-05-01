package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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

	@XmlElement(namespace = "http://www.example.org/Preset")
	public void setObjectPolicy(boolean objectPolicy) {
		this.objectPolicy = objectPolicy;
	}

	public String getNamespaceOld() {
		return namespaceOld;
	}

	@XmlElement(namespace = "http://www.example.org/Preset")
	public void setNamespaceOld(String namespaceOld) {
		this.namespaceOld = namespaceOld;
	}

}
