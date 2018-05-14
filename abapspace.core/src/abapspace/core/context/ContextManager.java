package abapspace.core.context;

import java.util.ArrayList;
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
	this.contextRoot = new ContextDirectory(contextRootPath, this);
	this.contextList = buildContextList();
    }

    private List<InterfaceContext> buildContextList() {
	List<InterfaceContext> locContextTmpList = null;
	List<InterfaceContext> locContextList = new ArrayList<InterfaceContext>();

	// each namespace
	List<NamespaceOld> locNSOldList = this.preset.getNamespaceOldList();

	for (NamespaceOld namespaceOld : locNSOldList) {

	    // get policy objects
	    if (namespaceOld.isObjectPolicy()) {
		locContextTmpList = this.getObjectPolicy(namespaceOld.getNamespaceOld());
		locContextList.addAll(locContextTmpList);
	    } else {
		locContextTmpList = this.getObjectDefault(namespaceOld.getNamespaceOld());
		locContextList.addAll(locContextTmpList);
	    }
	}

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
