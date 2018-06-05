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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;
import abapspace.core.preset.entity.Keyword;

public class Context implements Cloneable, InterfaceContext {

    protected String namespaceOld;
    protected String namespaceNew;
    protected String preIdent;
    protected String postIdent;
    protected String objectID;
    protected String objectNameIdent;
    protected Map<GroupType, String> objectMap;
    protected String supplement;
    protected Integer nameMaxLength;
    protected String replacement;
    protected boolean ignore;
    protected boolean enhancedObject;

    public Context() {
	this.namespaceOld = new String();
	this.namespaceNew = new String();
	this.preIdent = new String();
	this.postIdent = new String();
	this.objectID = new String();
	this.objectNameIdent = new String();
	this.supplement = new String();
	this.objectMap = new HashMap<GroupType, String>();
	this.nameMaxLength = new Integer(0);
	this.replacement = new String();
	this.ignore = false;
	this.enhancedObject = false;
    }

    @Override
    public String getObject() {

	StringBuffer locSB = new StringBuffer();

	if (this.objectMap.containsKey(GroupType.NAMESPACEOLD_OBJID)) {
	    locSB.append(this.objectMap.get(GroupType.NAMESPACEOLD_OBJID));
	}

	if (this.objectMap.containsKey(GroupType.OBJNAMEID)) {
	    locSB.append(this.objectMap.get(GroupType.OBJNAMEID));
	}

	return locSB.toString();
    }

    @Override
    public void setObject(Map<GroupType, String> objectMap) {
	this.objectMap = objectMap;
    }

    public String getObjectID() {
	return objectID;
    }

    public void setObjectID(String objectID) {
	this.objectID = objectID;
    }

    public String getSupplement() {
	return supplement;
    }

    public void setSupplement(String supplement) {

	String locSupplement = supplement;

	locSupplement = locSupplement.replaceAll(" *", "");

	if (!locSupplement.isEmpty()) {
	    this.supplement = supplement;
	}
    }

    public String getNamespaceOld() {
	return namespaceOld;
    }

    public void setNamespaceOld(String namespaceOld) {
	this.namespaceOld = namespaceOld;
    }

    public String getNamespaceNew() {
	return namespaceNew;
    }

    public void setNamespaceNew(String namespaceNew) {
	this.namespaceNew = namespaceNew;
    }

    public String getObjectNameIdent() {
	return objectNameIdent;
    }

    public void setObjectNameIdent(String objectNameIdent) {
	this.objectNameIdent = objectNameIdent;
    }

    public Integer getNameMaxLength() {
	return nameMaxLength;
    }

    public void setNameMaxLength(Integer nameMaxLength) {
	this.nameMaxLength = nameMaxLength;
    }

    public void setReplacement(String replacement) {
	this.replacement = replacement;
    }

    public String getPreIdent() {
	return preIdent;
    }

    public void setPreIdent(String preIdent) {
	this.preIdent = preIdent;
    }

    public String getPostIdent() {
	return postIdent;
    }

    public void setPostIdent(String postIdent) {
	this.postIdent = postIdent;
    }

    public boolean isIgnore() {
	return ignore;
    }

    public void setIgnore(boolean ignore) {
	this.ignore = ignore;
    }

    public boolean isEnhancedObject() {
	return enhancedObject;
    }

    public void setEnhancedObject(boolean enhancedObject) {
	this.enhancedObject = enhancedObject;
    }

    protected boolean isStringUpperCase(String string) {
	boolean locUpperCase = true;

	char[] locChars = string.toCharArray();

	for (int i = 0, l = locChars.length; i < l; ++i) {

	    char locChar = locChars[i];

	    if (Character.isLetter(locChar) && !Character.isUpperCase(locChar)) {
		locUpperCase = false;
		break;
	    }
	}

	return locUpperCase;
    }

    @Override
    public InterfaceContext clone() throws CloneNotSupportedException {
	return (InterfaceContext) super.clone();
    }

    @Override
    public RegexManager getRegex() {
	return this.getRegex(true, true);
    }

    @Override
    public RegexManager getRegex(boolean preIdent, boolean postIdent) {

	RegexManager locRegexManager = new RegexManager();
	int locGroupCounter = 0;

	if (preIdent) {
	    locGroupCounter++;
	    locRegexManager.addRegex(locGroupCounter, GroupType.PREIDENT, "(" + this.preIdent + ")");
	}

	locGroupCounter++;
	locRegexManager.addRegex(locGroupCounter, GroupType.NAMESPACEOLD_OBJID,
		"(" + this.namespaceOld + this.objectID + ")");

	locGroupCounter++;
	locRegexManager.addRegex(locGroupCounter, GroupType.OBJNAMEID, "(" + this.objectNameIdent + ")");

	if (postIdent) {
	    locGroupCounter++;
	    locRegexManager.addRegex(locGroupCounter, GroupType.POSTIDENT, "(" + this.postIdent + ")");
	}

	return locRegexManager;
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

	    locReplacement = locGroup1 + this.supplement + this.objectMap.get(GroupType.OBJNAMEID);

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
    public ContextCheckMaxNameLength checkMaxNameLengthForReplacement() {

	ContextCheckMaxNameLength locCheck = new ContextCheckMaxNameLength();

	Integer locActualLength = new Integer(this.getReplacement().length());
	Integer locOffset = new Integer(locActualLength - this.nameMaxLength);

	locCheck.setMaxNameLength(this.nameMaxLength);
	locCheck.setActualNameLength(locActualLength);
	locCheck.setOffset(locOffset);

	if (locOffset > 0) {
	    locCheck.setValid(false);
	} else {
	    locCheck.setValid(true);
	}

	return locCheck;
    }

    @Override
    public Map<String, InterfaceContext> processContextSearch(String contextString, List<Keyword> keywordList,
	    Map<String, InterfaceContext> iContextMap) throws CloneNotSupportedException {

	RegexManager locRegexManager = this.getRegex();

	for (Matcher m = Pattern.compile(locRegexManager.getRegex(), Pattern.CASE_INSENSITIVE).matcher(contextString); m
		.find();) {

	    Map<GroupType, String> locObjectMap = new HashMap<GroupType, String>();

	    ArrayList<Regex> locRegexList = (ArrayList<Regex>) locRegexManager.getRegexList();

	    for (Regex regex : locRegexList) {
		locObjectMap.put(regex.getGroupType(), m.group(regex.getGroup()));
	    }

	    InterfaceContext locIContext = this.clone();

	    locIContext.setObject(locObjectMap);

	    String locObject = locIContext.getObject();

	    if (this.excludeKeyword(keywordList, locObject)) {
		continue;
	    }

	    if (this.isEnhancedObject()) {
		LogEventManager.fireLog(LogType.INFO, MessageManager.getMessageFormat("collect.context.object.enhanced",
			locObject, m.start(), m.end()));
	    } else {
		LogEventManager.fireLog(LogType.INFO,
			MessageManager.getMessageFormat("collect.context.object", locObject, m.start(), m.end()));
	    }

	    if (iContextMap.containsKey(locObject)) {
		continue;
	    }

	    iContextMap.put(locObject, locIContext);
	}

	return iContextMap;
    }

    private boolean excludeKeyword(List<Keyword> keywordList, String object) {

	boolean locResult = false;

	for (Keyword keyword : keywordList) {
	    if (keyword.getKeyword().toLowerCase().equals(object.toLowerCase())) {
		locResult = true;
		break;
	    }
	}

	return locResult;
    }

    @Override
    public Map<String, InterfaceContext> processNameSearch(NameSearchType nameSearchType, boolean searchPreIdent,
	    boolean searchPostIdent, String nameString, Map<String, InterfaceContext> fileNameContextMap)
	    throws CloneNotSupportedException {

	RegexManager locRegexManager = this.getRegex(searchPreIdent, searchPostIdent);

	for (Matcher m = Pattern.compile(locRegexManager.getRegex(), Pattern.CASE_INSENSITIVE).matcher(nameString); m
		.find();) {

	    Map<GroupType, String> locObjectMap = new HashMap<GroupType, String>();

	    ArrayList<Regex> locRegexList = (ArrayList<Regex>) locRegexManager.getRegexList();

	    for (Regex regex : locRegexList) {
		locObjectMap.put(regex.getGroupType(), m.group(regex.getGroup()));
	    }

	    InterfaceContext locIContext = this.clone();

	    locIContext.setObject(locObjectMap);
	    String locObject = locIContext.getObject();

	    if (this.isEnhancedObject()) {
		switch (nameSearchType) {
		case FILE_NAME:
		    LogEventManager.fireLog(LogType.INFO, MessageManager.getMessageFormat(
			    "collect.context.object.fileName.enhanced", locObject, m.start(), m.end()));
		    break;

		case DIRECTORY_NAME:
		    LogEventManager.fireLog(LogType.INFO, MessageManager.getMessageFormat(
			    "collect.context.object.DirName.enhanced", locObject, m.start(), m.end()));
		    break;
		}

	    } else {

		switch (nameSearchType) {
		case FILE_NAME:
		    LogEventManager.fireLog(LogType.INFO, MessageManager
			    .getMessageFormat("collect.context.object.fileName", locObject, m.start(), m.end()));
		    break;
		case DIRECTORY_NAME:
		    LogEventManager.fireLog(LogType.INFO, MessageManager
			    .getMessageFormat("collect.context.object.dirName", locObject, m.start(), m.end()));
		    break;
		}

	    }

	    if (!fileNameContextMap.containsKey(locObject)) {
		fileNameContextMap.put(locObject, locIContext);
	    }
	}

	return fileNameContextMap;

    }

}
