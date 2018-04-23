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
package abapspace;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import abapspace.core.Refector;
import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.MaxNameLengthException;

public class ABAPSpace {

	private static final String PROPERTY_RESOURCE_BUNDLE_MESSAGES = "abapspace.core.messages.messages";
	private static final String OPTION_XML_PRESET_FILE = "p";
	private static final String OPTION_HELP = "h";
	private static final String OPTION_HELP_USAGE = "ABAPSpace";

	private static final String SYSTEM_PROPERTY_KEY_LOG4J2_CONFIG_FILE = "log4j2.configurationFile";
	private static final String SYSTEM_PROPERTY_VALUE_LOG4J2_CONFIG_FILE = "abapspace/core/log/log4j2.xml";

	private Logger log;
	private ResourceBundle messages;
	private Locale locale;
	private CommandLine cmd;
	private Options options;

	public static void main(String[] args) {

		ABAPSpace.setSystemProperties();
		
		Logger locLog = LogManager.getLogger();

		ABAPSpace locABAPSpace;
		try {
			locABAPSpace = new ABAPSpace(args, locLog);
			locABAPSpace.refactor();
		} catch (ParseException e) {
			locLog.error(e.getMessage(), e);
			return;
		}
	}

	public ABAPSpace(String[] args, Logger log) throws ParseException {
		this.log = log;
		this.locale = Locale.getDefault();
		this.messages = getMessages(this.locale);
		this.options = getOptions();
		this.cmd = getCommandLine(this.options, args);
	}

	public void refactor() {

		Refector locRefector = null;
		String locXMLPresetPath = new String();

		if (this.cmd.hasOption(OPTION_HELP)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(OPTION_HELP_USAGE, this.options);
			return;
		}

		if (this.cmd.hasOption(OPTION_XML_PRESET_FILE)) {
			locXMLPresetPath = this.cmd.getOptionValue(OPTION_XML_PRESET_FILE);

			if (locXMLPresetPath.isEmpty()) {
				return;
			}

		} else {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(OPTION_HELP_USAGE, this.options);
			return;
		}

		try {
			locRefector = Refector.FactoryGetInstance(this.log, this.locale, this.messages, locXMLPresetPath);
			locRefector.refactor();
		} catch (PresetFileNotFoundException | PresetFileImportException | MaxNameLengthException
				| FileProcessException e) {
			this.log.error(e.getMessage(), e);
			return;
		}
	}

	private ResourceBundle getMessages(Locale locale) throws MissingResourceException {
		ResourceBundle locResourceBundle = ResourceBundle.getBundle(PROPERTY_RESOURCE_BUNDLE_MESSAGES, locale);
		return locResourceBundle;
	}

	private Options getOptions() {
		Options locOptions = new Options();
		locOptions.addOption(OPTION_XML_PRESET_FILE, true, this.messages.getString("cmdline.p"));
		locOptions.addOption(OPTION_HELP, false, this.messages.getString("cmdline.h"));
		return locOptions;
	}

	private CommandLine getCommandLine(Options options, String[] args) throws ParseException {
		CommandLineParser locCmdLineParser = new DefaultParser();

		return locCmdLineParser.parse(options, args);
	}

	private static void setSystemProperties() {
		Properties locSystemProperties = System.getProperties();
		locSystemProperties.setProperty(SYSTEM_PROPERTY_KEY_LOG4J2_CONFIG_FILE,
				SYSTEM_PROPERTY_VALUE_LOG4J2_CONFIG_FILE);
	}

}
