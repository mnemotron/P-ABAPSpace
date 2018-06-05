package abapspace.core.context;

public enum NameSearchType {
    FILE_NAME("FILE_NAME"), DIRECTORY_NAME("DIR_NAME");

    private final String nameSearchType;

    NameSearchType(String nameSearchType) {
	this.nameSearchType = nameSearchType;
    }

    public String getGroupType() {
	return this.nameSearchType;
    }
}
