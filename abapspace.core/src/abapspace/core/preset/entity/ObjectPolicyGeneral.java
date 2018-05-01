package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;

public class ObjectPolicyGeneral {

	private String regexIdent;

	public ObjectPolicyGeneral() {
		this.regexIdent = new String();
	}

	public String getRegexIdent() {
		return regexIdent;
	}

	@XmlElement
	public void setRegexIdent(String regexIdent) {
		this.regexIdent = regexIdent;
	}

}
