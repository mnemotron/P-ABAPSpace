package abapspace.core.context;

public class Regex {

    private int group;
    private GroupType groupType;
    private String regex;

    public Regex() {
	this.group = 0;
	this.groupType = null;
	this.regex = new String();
    }

    public Regex(int group, GroupType groupType, String regex) {
	this.group = group;
	this.groupType = groupType;
	this.regex = regex;
    }

    public int getGroup() {
	return group;
    }

    public void setGroup(int group) {
	this.group = group;
    }

    public GroupType getGroupType() {
	return groupType;
    }

    public void setGroupType(GroupType groupType) {
	this.groupType = groupType;
    }

    public String getRegex() {
	return regex;
    }

    public void setRegex(String regex) {
	this.regex = regex;
    }

}
