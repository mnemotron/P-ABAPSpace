package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Preset {

	private String refactorRootDir;

	private ObjectClass objectClass;
	private ObjectInterface objectInterface;

	public Preset() {

	}

	public String getRefactorRootDir() {
		return refactorRootDir;
	}

	@XmlElement
	public void setRefactorRootDir(String refactorRootDir) {
		this.refactorRootDir = refactorRootDir;
	}

	public ObjectClass getObjectClass() {
		return objectClass;
	}

	@XmlElement
	public void setObjectClass(ObjectClass objectClass) {
		this.objectClass = objectClass;
	}

	public ObjectInterface getObjectInterface() {
		return objectInterface;
	}

	@XmlElement
	public void setObjectInterface(ObjectInterface objectInterface) {
		this.objectInterface = objectInterface;
	}

}
