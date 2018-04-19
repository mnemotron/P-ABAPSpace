package abapspace.core.context;

import abapspace.core.context.entity.ContextCheckMaxNameLength;

public interface InterfaceContext {
	
	public String getRegex(boolean asGroup);
	
	public void setIdentObject(String identObject);
	
	public String getIdentObject();
	
	public String getReplacement();
	
	public ContextCheckMaxNameLength checkMaxNameLengthForReplacement();
	
	public InterfaceContext clone() throws CloneNotSupportedException;
	
}
