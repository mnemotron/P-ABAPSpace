package abapspace.core.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RegexManager {

    List<Regex> regexList;

    public RegexManager() {

	this.regexList = new ArrayList<Regex>();
    }

    public void addRegex(int group, GroupType groupType, String regex) {
	this.regexList.add(new Regex(group, groupType, regex));
    }

    public String getRegex() {
	StringBuffer locSB = new StringBuffer();

	Collections.sort(regexList, new Comparator<Regex>() {

	    @Override
	    public int compare(Regex r1, Regex r2) {

		if (r1.getGroup() == r2.getGroup()) {
		    return 0;
		} else {
		    return (r1.getGroup() < r2.getGroup()) ? -1 : 1;
		}
	    }
	});

	for (Regex regex : regexList) {
	    locSB.append(regex.getRegex());
	}

	return locSB.toString();
    }

    public List<Regex> getRegexList() {
	return regexList;
    }

}
