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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import abapspace.core.exception.SourceDirectoryNotFoundException;
import abapspace.core.messages.MessageManager;

@XmlRootElement
public class Preset {

    private String description;
    private String refactorSourceDir;
    private String refactorTargetDir;
    private boolean checkNameMaxLength;
    private String namespaceOld;
    private String namespaceNew;
    private String supplement;
    private ObjectClass objectClass;
    private ObjectInterface objectInterface;

    public Preset() {

    }

    public String getDescription() {
	return description;
    }

    @XmlElement
    public void setDescription(String description) {
	this.description = description;
    }

    public String getSupplement() {
	return supplement;
    }

    @XmlElement
    public void setSupplement(String supplement) {
	this.supplement = supplement;
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

    public File getFileSourceDir() throws SourceDirectoryNotFoundException {
	File locSourceDir = new File(this.refactorSourceDir);

	if (!locSourceDir.exists() || !locSourceDir.isDirectory()) {
	    throw new SourceDirectoryNotFoundException(
		    MessageManager.getMessage("exception.sourceDirNotFound") + locSourceDir.getAbsolutePath());
	}

	return locSourceDir;
    }
}
