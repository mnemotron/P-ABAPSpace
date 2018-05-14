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
package abapspace.core;

import abapspace.core.context.ContextDirectory;
import abapspace.core.context.ContextManager;
import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.SourceDirectoryNotFoundException;
import abapspace.core.preset.entity.Preset;

/**
 * Refector
 * 
 * @author mnemotron
 * @version 1.0.0
 * @since 2018-05-04
 */
public class Refector {

    private Preset preset;
    private ContextManager contextManager;

    public Refector() {
	this.preset = new Preset();
	this.contextManager = new ContextManager(this.preset, null);
    }

    public Refector(Preset preset) throws SourceDirectoryNotFoundException {
	this.preset = preset;
	this.contextManager = new ContextManager(this.preset,
		new ContextDirectory(this.preset.getFileSourceDir().getAbsolutePath(), this.contextManager));
    }

    public void refactorContext() throws FileProcessException {
	this.contextManager.refactorContext();
    }

    public void collectContext() throws FileProcessException {
	this.contextManager.collectContext();
    }
}
