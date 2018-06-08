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
package abapspace.core.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import abapspace.core.exception.FileProcessException;
import abapspace.core.preset.entity.NamespaceOld;
import abapspace.core.preset.entity.Preset;

public class ContextManager {

    private Preset preset;
    private List<InterfaceContext> contextList;
    private ContextDirectory contextRoot;

    public ContextManager(Preset preset, String contextRootPath) {
	this.preset = preset;
	this.contextRoot = new ContextDirectory(true, contextRootPath, this);
	this.contextList = buildContextList();
    }

    private List<InterfaceContext> buildContextList() {
	List<InterfaceContext> locContextTmpList = null;
	List<InterfaceContext> locContextList = new ArrayList<InterfaceContext>();

	// each namespace
	List<NamespaceOld> locNSOldList = this.preset.getNamespaceOldList();

	for (NamespaceOld namespaceOld : locNSOldList) {

	    // get enhanced policy objects
	    locContextTmpList = this.getObjectPolicy(namespaceOld.getNamespaceOld());
	    locContextList.addAll(locContextTmpList);

	    // get default policy objects
	    locContextTmpList = this.getObjectDefault(namespaceOld.getNamespaceOld());
	    locContextList.addAll(locContextTmpList);
	}

	// first enhanced objects
	Collections.sort(locContextList, new Comparator<InterfaceContext>() {
	    @Override
	    public int compare(InterfaceContext o1, InterfaceContext o2) {
		if (o1.isEnhancedObject() == o2.isEnhancedObject()) {
		    return 0;
		} else {
		    return o1.isEnhancedObject() ? -1 : 1;
		}
	    }
	});

	return locContextList;
    }

    private List<InterfaceContext> getObjectDefault(String namespaceOld) {

	List<InterfaceContext> locContextList = new ArrayList<InterfaceContext>();

	if (this.preset.getObjectPolicyGeneral() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicyGeneral().getPreIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicyGeneral().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicyGeneral().getPostIdent());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(false);
	    locContextList.add(locContext);
	}

	return locContextList;
    }

    private List<InterfaceContext> getObjectPolicy(String namespaceOld) {

	List<InterfaceContext> locContextList = new ArrayList<InterfaceContext>();

	// object: package
	if (this.preset.getObjectPolicy().getObjectPackage() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectPackage().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectPackage().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectPackage().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectPackage().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectPackage().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: class
	if (this.preset.getObjectPolicy().getObjectClass() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectClass().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectClass().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectClass().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectClass().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectClass().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: interface
	if (this.preset.getObjectPolicy().getObjectInterface() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectInterface().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectInterface().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectInterface().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectInterface().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectInterface().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: exception class
	if (this.preset.getObjectPolicy().getObjectExceptionClass() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectExceptionClass().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectExceptionClass().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectExceptionClass().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectExceptionClass().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectExceptionClass().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: include
	if (this.preset.getObjectPolicy().getObjectInclude() != null) {
	    ContextInclude locContext = new ContextInclude();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectInclude().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectInclude().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectInclude().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectInclude().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectInclude().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: function group
	if (this.preset.getObjectPolicy().getObjectFunctionGroup() != null) {
	    ContextFunctionGroup locContext = new ContextFunctionGroup();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectFunctionGroup().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectFunctionGroup().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectFunctionGroup().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectFunctionGroup().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectFunctionGroup().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: function module
	if (this.preset.getObjectPolicy().getObjectFunctionModule() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectFunctionModule().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectFunctionModule().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectFunctionModule().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectFunctionModule().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectFunctionModule().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: database table
	if (this.preset.getObjectPolicy().getObjectDatabaseTable() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectDatabaseTable().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectDatabaseTable().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectDatabaseTable().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectDatabaseTable().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectDatabaseTable().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: table type
	if (this.preset.getObjectPolicy().getObjectTableType() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectTableType().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectTableType().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectTableType().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectTableType().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectTableType().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: structure
	if (this.preset.getObjectPolicy().getObjectStructure() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectStructure().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectStructure().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectStructure().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectStructure().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectStructure().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: domain
	if (this.preset.getObjectPolicy().getObjectDomain() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectDomain().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectDomain().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectDomain().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectDomain().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectDomain().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: data element
	if (this.preset.getObjectPolicy().getObjectDataElement() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectDataElement().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectDataElement().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectDataElement().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectDataElement().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectDataElement().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	// object: message class
	if (this.preset.getObjectPolicy().getObjectMessageClass() != null) {
	    Context locContext = new Context();
	    locContext.setPreIdent(this.preset.getObjectPolicy().getObjectMessageClass().getPreIdent());
	    locContext.setObjectID(this.preset.getObjectPolicy().getObjectMessageClass().getObjectIdent());
	    locContext.setObjectNameIdent(this.preset.getObjectPolicy().getObjectMessageClass().getObjectNameIdent());
	    locContext.setSupplement(this.preset.getSupplement());
	    locContext.setPostIdent(this.preset.getObjectPolicy().getObjectMessageClass().getPostIdent());
	    locContext.setNameMaxLength(this.preset.getObjectPolicy().getObjectMessageClass().getNameMaxLength());
	    locContext.setNamespaceNew(this.preset.getNamespaceNew());
	    locContext.setNamespaceOld(namespaceOld);
	    locContext.setEnhancedObject(true);
	    locContextList.add(locContext);
	}

	return locContextList;
    }

    public List<InterfaceContext> getContextList() {
	return contextList;
    }

    public Preset getPreset() {
	return preset;
    }

    public void collectContext() throws FileProcessException {
	this.contextRoot.collectContext();
    }

    public void refactorContext() throws FileProcessException {
	this.contextRoot.refactorContext();
    }

    public boolean checkMaxNameLength() {
	return this.contextRoot.checkMaxNameLength();
    }

    public void setContextMap(Map<String, InterfaceContext> contextMap) {
	this.contextRoot.setContextMap(contextMap);
    }

    public Map<String, InterfaceContext> getContextMap() {
	return this.contextRoot.getContextMap();
    }

}
