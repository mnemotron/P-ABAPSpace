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
package abapspace.core.context.entity;

import abapspace.core.context.InterfaceContext;

public class Context implements Cloneable, InterfaceContext {

	private String namespaceOld;
	private String namespaceNew;
	private String identRegex;
	private String identObject;
	private Integer nameMaxLength;

	public Context() {

	}

	@Override
	public String getIdentObject() {
		return identObject;
	}

	@Override
	public void setIdentObject(String identObject) {
		this.identObject = identObject;
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

	public String getIdentRegex() {
		return identRegex;
	}

	public void setIdentRegex(String identRegex) {
		this.identRegex = identRegex;
	}

	public Integer getNameMaxLength() {
		return nameMaxLength;
	}

	public void setNameMaxLength(Integer nameMaxLength) {
		this.nameMaxLength = nameMaxLength;
	}

	@Override
	public InterfaceContext clone() throws CloneNotSupportedException {
		return (InterfaceContext) super.clone();
	}

	@Override
	public String getRegex(boolean asGroup) {

		String locRegex = new String();

		locRegex = this.namespaceOld + this.identRegex;

		if (asGroup = true) {
			locRegex = "(" + locRegex + ")";
		}

		return locRegex;
	}

	@Override
	public String getReplacement() {
		String locReplacement = new String();
		String locIdentObject = new String(this.identObject);

		locReplacement = locIdentObject.toLowerCase().replaceAll("^" + this.namespaceOld.toLowerCase(),
				this.namespaceNew.toLowerCase());

		return locReplacement;
	}

	@Override
	public ContextCheckMaxNameLength checkMaxNameLengthForReplacement() {

		ContextCheckMaxNameLength locCheck = new ContextCheckMaxNameLength();

		Integer locActualLength = new Integer(this.getIdentObject().length());
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
