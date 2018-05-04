package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;

public class ObjectPolicyGeneral {

	private String preIdent;
	private String objectNameIdent;
	private String postIdent;

	public ObjectPolicyGeneral() {
		this.preIdent = new String();
		this.objectNameIdent = new String();
		this.postIdent = new String();
	}

	public String getPreIdent() {
		return preIdent;
	}

	@XmlElement(namespace = "http://www.example.org/Preset")
	public void setPreIdent(String preIdent) {
		this.preIdent = preIdent;
	}

	public String getObjectNameIdent() {
		return objectNameIdent;
	}

	@XmlElement(namespace = "http://www.example.org/Preset")
	public void setObjectNameIdent(String objectNameIdent) {
		this.objectNameIdent = objectNameIdent;
	}

	public String getPostIdent() {
		return postIdent;
	}

	@XmlElement(namespace = "http://www.example.org/Preset")
	public void setPostIdent(String postIdent) {
		this.postIdent = postIdent;
	}

}
