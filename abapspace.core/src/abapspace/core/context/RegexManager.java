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
