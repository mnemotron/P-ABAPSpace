package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class NamespaceOld {

	private boolean objectPolicy;
	private String value;

	public NamespaceOld() {
		this.objectPolicy = false;
		this.value = new String();
	}

	public boolean isObjectPolicy() {
		return objectPolicy;
	}

	@XmlAttribute(namespace = "http://www.example.org/Preset")
	public void setObjectPolicy(boolean objectPolicy) {
		this.objectPolicy = objectPolicy;
	}

	public String getValue() {
		return value;
	}

	@XmlValue
	public void setValue(String value) {
		this.value = value;
	}

}
