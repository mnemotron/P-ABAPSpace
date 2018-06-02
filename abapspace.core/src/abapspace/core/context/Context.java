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

public class Context implements Cloneable, InterfaceContext {

	private String namespaceOld;
	private String namespaceNew;
	private String preIdent;
	private String postIdent;
	private String objectID;
	private String objectNameIdent;
	private String[] object;
	private String supplement;
	private Integer nameMaxLength;
	private String replacement;
	private boolean ignore;
	private boolean enhancedObject;

	public Context() {
		this.namespaceOld = new String();
		this.namespaceNew = new String();
		this.preIdent = new String();
		this.postIdent = new String();
		this.objectID = new String();
		this.objectNameIdent = new String();
		this.supplement = new String();
		this.object = new String[] {};
		this.nameMaxLength = new Integer(0);
		this.replacement = new String();
		this.ignore = false;
		this.enhancedObject = false;
	}

	@Override
	public String getObject() {

		StringBuffer locSB = new StringBuffer();

		for (String object : object) {
			locSB.append(object);
		}

		return locSB.toString();
	}

	@Override
	public void setObject(String[] object) {

		for (String string : object) {
			string = string.trim();
		}

		this.object = object;
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

	private boolean isStringUpperCase(String string) {
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
	public String getRegex() {
		return this.getRegex(true, true);
	}

	@Override
	public String getRegex(boolean preIdent, boolean postIdent) {

		StringBuffer locRegex = new StringBuffer();

		if (preIdent) {
			locRegex.append(this.preIdent);
		}

		// group 1, 2
		locRegex.append("(" + this.namespaceOld + this.objectID + ")" + "(" + this.objectNameIdent + ")");

		if (postIdent) {
			locRegex.append(this.postIdent);
		}

		return locRegex.toString();
	}

	@Override
	public String getReplacement() {

		String locReplacement = new String();

		if (!this.replacement.isEmpty()) {

			locReplacement = this.replacement;

		} else {

			// change namespace
			Object locGroup1 = this.object[0].toLowerCase().replaceAll("^" + this.namespaceOld.toLowerCase(),
					this.namespaceNew.toLowerCase());

			// group1 = namespace + object ID
			// group2 = object name
			locReplacement = locGroup1 + this.supplement + object[1];

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

}
