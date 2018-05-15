package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;

public class FileStructure {

    private boolean update;
    private String namespacePlaceholder;
    private String namespaceReplacement;

    public FileStructure() {
	this.update = false;
	this.namespacePlaceholder = new String();
	this.namespaceReplacement = new String();
    }

    public boolean isUpdate() {
	return update;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setUpdate(boolean update) {
	this.update = update;
    }

    public String getNamespacePlaceholder() {
	return namespacePlaceholder;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setNamespacePlaceholder(String namespacePlaceholder) {
	this.namespacePlaceholder = namespacePlaceholder;
    }

    public String getNamespaceReplacement() {
	return namespaceReplacement;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setNamespaceReplacement(String namespaceReplacement) {
	this.namespaceReplacement = namespaceReplacement;
    }
}
