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

public class FileStructure {

    private boolean update;
    private String namespacePlaceholder;
    private String namespaceReplacement;

    public FileStructure() {
	this.update = false;
	this.namespacePlaceholder = new String();
	this.namespaceReplacement = new String();
    }

    public boolean isUpdate() {
	return update;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setUpdate(boolean update) {
	this.update = update;
    }

    public String getNamespacePlaceholder() {
	return namespacePlaceholder;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setNamespacePlaceholder(String namespacePlaceholder) {
	this.namespacePlaceholder = namespacePlaceholder;
    }

    public String getNamespaceReplacement() {
	return namespaceReplacement;
    }

    @XmlElement(namespace = "http://www.abapspace.com/Preset")
    public void setNamespaceReplacement(String namespaceReplacement) {
	this.namespaceReplacement = namespaceReplacement;
    }
}
