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

import java.util.Map;

public class ContextFunctionGroup extends Context {

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
	    Object locGroup1 = this.objectMap.get(GroupType.NAMESPACEOLD_OBJID).toLowerCase()
		    .replaceAll("^" + this.namespaceOld.toLowerCase(), this.namespaceNew.toLowerCase());

	    locReplacement = this.objectMap.get(GroupType.PREIDENT) + locGroup1 + this.supplement
		    + this.objectMap.get(GroupType.OBJNAMEID);

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
    public Map<String, InterfaceContext> processNameSearch(NameSearchType nameSearchType, boolean searchPreIdent,
	    boolean searchPostIdent, String nameString, Map<String, InterfaceContext> fileNameContextMap) throws CloneNotSupportedException {
	return super.processNameSearch(nameSearchType, true, searchPostIdent, nameString, fileNameContextMap);
    }

}
