package abapspace.core.context;

public interface InterfaceContext {
	
	public String getRegex(boolean asGroup);
	
	public void setIdentObject(String identObject);
	
	public String getIdentObject();
	
	public String getReplacement();
	
	public InterfaceContext clone() throws CloneNotSupportedException;
	
}
