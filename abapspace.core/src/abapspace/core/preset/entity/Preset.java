/*
 * MIT License
 *
 * Copyright (c) 2018 mnemotron
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package abapspace.core.preset.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import abapspace.core.exception.SourceDirectoryNotFoundException;
import abapspace.core.exception.TargetDirectoryNotFoundException;
import abapspace.core.messages.MessageManager;

@XmlRootElement(namespace = "http://www.abapspace.com/Preset")
public class Preset {

    private String description;
    private String refactorSourceDir;
    private String refactorTargetDir;
    private FileStructure fileStructure;
    private boolean checkNameMaxLength;
    private String namespaceNew;
    private String supplement;
    private List<NamespaceOld> namespaceOldList;
    private ObjectPolicyGeneral objectPolicyGeneral;
    private ObjectPolicy objectPolicy;

    public Preset() {
	this.description = new String();
	this.refactorSourceDir = new String();
	this.refactorTargetDir = new String();
	this.namespaceNew = new String();
	this.supplement = new String();
	this.namespaceOldList = new ArrayList<NamespaceOld>();
	this.fileStructure = new FileStructure();
	this.checkNameMaxLength = false;
	this.objectPolicyGeneral = new ObjectPolicyGeneral();
	this.objectPolicy = new ObjectPolicy();
    }

    public String getDescription() {
	return description;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setDescription(String description) {
	this.description = description;
    }

    public String getSupplement() {
	return supplement;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setSupplement(String supplement) {
	this.supplement = supplement;
    }

    public String getRefactorSourceDir() {
	return refactorSourceDir;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setRefactorSourceDir(String refactorSourceDir) {
	this.refactorSourceDir = refactorSourceDir;
    }

    public String getRefactorTargetDir() {
	return refactorTargetDir;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setRefactorTargetDir(String refactorTargetDir) {
	this.refactorTargetDir = refactorTargetDir;
    }

    public FileStructure getFileStructure() {
	return fileStructure;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setFileStructure(FileStructure fileStructure) {
	this.fileStructure = fileStructure;
    }

    public boolean isCheckNameMaxLength() {
	return checkNameMaxLength;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setCheckNameMaxLength(boolean checkNameMaxLength) {
	this.checkNameMaxLength = checkNameMaxLength;
    }

    public String getNamespaceNew() {
	return namespaceNew;
    }

    public List<NamespaceOld> getNamespaceOldList() {
	return namespaceOldList;
    }

    @XmlElementWrapper(namespace = "http://www.abapspace.com/Preset", name = "namespaceOldList")
    @XmlElement(namespace = "http://www.abapspace.com/Preset", name = "namespaceOld")
    public void setNamespaceOldList(List<NamespaceOld> namespaceOldList) {
	this.namespaceOldList = namespaceOldList;
    }

    public ObjectPolicyGeneral getObjectPolicyGeneral() {
	return objectPolicyGeneral;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectPolicyGeneral(ObjectPolicyGeneral objectPolicyGeneral) {
	this.objectPolicyGeneral = objectPolicyGeneral;
    }

    public ObjectPolicy getObjectPolicy() {
	return objectPolicy;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectPolicy(ObjectPolicy objectPolicy) {
	this.objectPolicy = objectPolicy;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setNamespaceNew(String namespaceNew) {
	this.namespaceNew = namespaceNew;
    }

    public File getFileSourceDir() throws SourceDirectoryNotFoundException {
	File locSourceDir = new File(this.refactorSourceDir);

	if (!locSourceDir.exists() || !locSourceDir.isDirectory()) {
	    throw new SourceDirectoryNotFoundException(
		    MessageManager.getMessage("exception.sourceDirNotFound") + locSourceDir.getAbsolutePath());
	}

	return locSourceDir;
    }

    public File getFileTargetDir() throws TargetDirectoryNotFoundException {
	File locTargetDir = new File(this.refactorTargetDir);

	if (!locTargetDir.exists() || !locTargetDir.isDirectory()) {
	    throw new TargetDirectoryNotFoundException(
		    MessageManager.getMessage("exception.targetDirNotFound") + locTargetDir.getAbsolutePath());
	}

	return locTargetDir;
    }

    @Override
    public String toString() {
	return this.getDescription();
    }
}
