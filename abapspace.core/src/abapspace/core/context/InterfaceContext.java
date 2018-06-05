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

import java.util.List;
import java.util.Map;

import abapspace.core.preset.entity.Keyword;

public interface InterfaceContext {

    public RegexManager getRegex();

    public RegexManager getRegex(boolean preIdent, boolean postIdent);

    // public void setObject(String[] object);
    public void setObject(Map<GroupType, String> objectMap);

    public String getObject();

    public String getReplacement();

    public void setReplacement(String replacement);

    public ContextCheckMaxNameLength checkMaxNameLengthForReplacement();

    public InterfaceContext clone() throws CloneNotSupportedException;

    public void setIgnore(boolean ignore);

    public boolean isIgnore();

    public boolean isEnhancedObject();

    public Map<String, InterfaceContext> processContextSearch(String contextString, List<Keyword> keywordList,
	    Map<String, InterfaceContext> iContextMap) throws CloneNotSupportedException;

    public Map<String, InterfaceContext> processNameSearch(NameSearchType nameSearchType, boolean searchPreIdent, boolean searchPostIdent, String nameString,  Map<String, InterfaceContext> fileNameContextMap) throws CloneNotSupportedException;
}
