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

import javax.xml.bind.annotation.XmlElement;

public class ObjectPolicy {

    private ObjectPackage objectPackage;
    private ObjectClass objectClass;
    private ObjectInterface objectInterface;
    private ObjectExceptionClass objectExceptionClass;
    private ObjectInclude objectInclude;
    private ObjectFunctionGroup objectFunctionGroup;
    private ObjectFunctionModule objectFunctionModule;
    private ObjectDatabaseTable objectDatabaseTable;
    private ObjectTableType objectTableType;
    private ObjectStructure objectStructure;
    private ObjectDomain objectDomain;
    private ObjectDataElement objectDataElement;
    private ObjectMessageClass objectMessageClass;

    public ObjectPolicy() {
	this.objectPackage = new ObjectPackage();
	this.objectClass = new ObjectClass();
	this.objectInterface = new ObjectInterface();
	this.objectExceptionClass = new ObjectExceptionClass();
	this.objectInclude = new ObjectInclude();
	this.objectFunctionGroup = new ObjectFunctionGroup();
	this.objectFunctionModule = new ObjectFunctionModule();
	this.objectDatabaseTable = new ObjectDatabaseTable();
	this.objectTableType = new ObjectTableType();
	this.objectStructure = new ObjectStructure();
	this.objectDomain = new ObjectDomain();
	this.objectDataElement = new ObjectDataElement();
	this.objectMessageClass = new ObjectMessageClass();
    }

    public ObjectPackage getObjectPackage() {
	return objectPackage;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectPackage(ObjectPackage objectPackage) {
	this.objectPackage = objectPackage;
    }

    public ObjectClass getObjectClass() {
	return objectClass;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectClass(ObjectClass objectClass) {
	this.objectClass = objectClass;
    }

    public ObjectInterface getObjectInterface() {
	return objectInterface;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectInterface(ObjectInterface objectInterface) {
	this.objectInterface = objectInterface;
    }

    public ObjectExceptionClass getObjectExceptionClass() {
	return objectExceptionClass;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectExceptionClass(ObjectExceptionClass objectExceptionClass) {
	this.objectExceptionClass = objectExceptionClass;
    }

    public ObjectInclude getObjectInclude() {
	return objectInclude;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectInclude(ObjectInclude objectInclude) {
	this.objectInclude = objectInclude;
    }

    public ObjectFunctionGroup getObjectFunctionGroup() {
	return objectFunctionGroup;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectFunctionGroup(ObjectFunctionGroup objectFunctionGroup) {
	this.objectFunctionGroup = objectFunctionGroup;
    }

    public ObjectFunctionModule getObjectFunctionModule() {
	return objectFunctionModule;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectFunctionModule(ObjectFunctionModule objectFunctionModule) {
	this.objectFunctionModule = objectFunctionModule;
    }

    public ObjectDatabaseTable getObjectDatabaseTable() {
	return objectDatabaseTable;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectDatabaseTable(ObjectDatabaseTable objectDatabaseTable) {
	this.objectDatabaseTable = objectDatabaseTable;
    }

    public ObjectTableType getObjectTableType() {
	return objectTableType;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectTableType(ObjectTableType objectTableType) {
	this.objectTableType = objectTableType;
    }

    public ObjectStructure getObjectStructure() {
	return objectStructure;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectStructure(ObjectStructure objectStructure) {
	this.objectStructure = objectStructure;
    }

    public ObjectDomain getObjectDomain() {
	return objectDomain;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectDomain(ObjectDomain objectDomain) {
	this.objectDomain = objectDomain;
    }

    public ObjectDataElement getObjectDataElement() {
	return objectDataElement;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectDataElement(ObjectDataElement objectDataElement) {
	this.objectDataElement = objectDataElement;
    }

    public ObjectMessageClass getObjectMessageClass() {
	return objectMessageClass;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setObjectMessageClass(ObjectMessageClass objectMessageClass) {
	this.objectMessageClass = objectMessageClass;
    }

}
