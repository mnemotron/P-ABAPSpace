package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Preset {

	private String refactorSourceDir;
	private String refactorTargetDir;
	private boolean checkNameMaxLength;
	private String namespaceOld;
	private String namespaceNew;

	private ObjectClass objectClass;
	private ObjectInterface objectInterface;

	public Preset() {

	}

	public String getRefactorSourceDir() {
		return refactorSourceDir;
	}

	@XmlElement
	public void setRefactorSourceDir(String refactorSourceDir) {
		this.refactorSourceDir = refactorSourceDir;
	}

	public String getRefactorTargetDir() {
		return refactorTargetDir;
	}

	@XmlElement
	public void setRefactorTargetDir(String refactorTargetDir) {
		this.refactorTargetDir = refactorTargetDir;
	}

	public boolean isCheckNameMaxLength() {
		return checkNameMaxLength;
	}

	@XmlElement
	public void setCheckNameMaxLength(boolean checkNameMaxLength) {
		this.checkNameMaxLength = checkNameMaxLength;
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
