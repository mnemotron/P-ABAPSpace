package abapspace.core.context;

public enum GroupType {
    PREIDENT("PREIDENT"), NAMESPACEOLD_OBJID("NOOBJID"), OBJNAMEID("OBJNAMEID"), POSTIDENT("POSTIDENT");
    private final String groupType;

    GroupType(String groupType) {
	this.groupType = groupType;
    }

    public String getGroupType() {
	return this.groupType;
    }
}
