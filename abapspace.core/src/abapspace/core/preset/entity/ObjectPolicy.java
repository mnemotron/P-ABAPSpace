package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;

public class ObjectPolicy {

	private ObjectPackage objectPackage;
	private ObjectClass objectClass;
	private ObjectInterface objectInterface;

	public ObjectPolicy() {
		this.objectPackage = new ObjectPackage();
		this.objectClass = new ObjectClass();
		this.objectInterface = new ObjectInterface();
	}

	public ObjectPackage getObjectPackage() {
		return objectPackage;
	}

	@XmlElement
	public void setObjectPackage(ObjectPackage objectPackage) {
		this.objectPackage = objectPackage;
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
