package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlValue;

public class Keyword {

    private String keyword;

    public Keyword() {
	this.keyword = new String();
    }

    public String getKeyword() {
	return keyword;
    }

    @XmlValue
    public void setKeyword(String keyword) {
	this.keyword = keyword;
    }
}
