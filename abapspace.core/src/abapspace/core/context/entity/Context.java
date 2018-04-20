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
