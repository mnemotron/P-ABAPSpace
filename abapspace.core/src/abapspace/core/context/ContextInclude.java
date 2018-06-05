package abapspace.core.context;

public class ContextInclude extends Context {

    @Override
    public String getObject() {

	StringBuffer locSB = new StringBuffer();

	if (this.objectMap.containsKey(GroupType.PREIDENT)) {
	    locSB.append(this.objectMap.get(GroupType.PREIDENT));
	}

	if (this.objectMap.containsKey(GroupType.NAMESPACEOLD_OBJID)) {
	    locSB.append(this.objectMap.get(GroupType.NAMESPACEOLD_OBJID));
	}

	if (this.objectMap.containsKey(GroupType.OBJNAMEID)) {
	    locSB.append(this.objectMap.get(GroupType.OBJNAMEID));
	}

	return locSB.toString();
    }
    
    @Override
    public String getReplacement() {
	String locReplacement = new String();

	if (!this.replacement.isEmpty()) {

	    locReplacement = this.replacement;

	} else {

	    // change namespace
	    Object locGroup1 = this.objectMap.get(GroupType.NAMESPACEOLD_OBJID).toLowerCase().replaceAll("^" + this.namespaceOld.toLowerCase(),
		    this.namespaceNew.toLowerCase());

	    locReplacement = this.objectMap.get(GroupType.PREIDENT) + locGroup1 + this.supplement + this.objectMap.get(GroupType.OBJNAMEID);

	    locReplacement = locReplacement.replaceAll("__", "_");
	}

	// check object name is upper case or lower case
	if (this.isStringUpperCase(this.getObject())) {
	    locReplacement = locReplacement.toUpperCase();
	} else {
	    locReplacement = locReplacement.toLowerCase();
	}

	return locReplacement;
    }

    @Override
    public InterfaceContext processNameSearch(NameSearchType nameSearchType, boolean searchPreIdent,
	    boolean searchPostIdent, String nameString) throws CloneNotSupportedException {
	return super.processNameSearch(nameSearchType, true, searchPostIdent, nameString);
    }

}
