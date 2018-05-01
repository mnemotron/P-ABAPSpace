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
package abapspace.core.preset.entity;

import javax.xml.bind.annotation.XmlElement;

public class ObjectGen {

	private String objectIdent;
	private String regexIdent;
	private Integer nameMaxLength;

	public ObjectGen() {
		this.objectIdent = new String();
		this.regexIdent = new String();
		this.nameMaxLength = 0;
	}

	public String getObjectIdent() {
		return objectIdent;
	}

	@XmlElement(namespace="http://www.example.org/Preset")
	public void setObjectIdent(String objectIdent) {
		this.objectIdent = objectIdent;
	}

	public String getRegexIdent() {
		return regexIdent;
	}

	@XmlElement(namespace="http://www.example.org/Preset")
	public void setRegexIdent(String regexIdent) {
		this.regexIdent = regexIdent;
	}

	public Integer getNameMaxLength() {
		return nameMaxLength;
	}

	@XmlElement(namespace="http://www.example.org/Preset")
	public void setNameMaxLength(Integer nameMaxLength) {
		this.nameMaxLength = nameMaxLength;
	}

}
