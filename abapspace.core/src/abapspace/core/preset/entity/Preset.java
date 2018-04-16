package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Preset {

	private String refactorRootDir;
	private String namespaceOld;
	private String namespaceNew;

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

}
