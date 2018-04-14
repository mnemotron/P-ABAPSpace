package abapspace;

import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import abapspace.core.Refector;

public class ABAPSpace {

	private static final String PROPERTY_RESOURCE_BUNDLE_MESSAGES = "abapspace.core.messages.messages";
	private static final String OPTION_XML_PRESET_FILE = "p";
	private static final String OPTION_HELP = "h";
	private static final String OPTION_HELP_USAGE = "ABAPSpace";

	private ResourceBundle messages;
	private Locale locale;
	private CommandLine cmd;
	private Options options;

	public static void main(String[] args) {

		try {
			ABAPSpace locABAPSpace = new ABAPSpace(args);

			locABAPSpace.refactor();

		} catch (MissingResourceException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public ABAPSpace(String[] args) throws MissingResourceException, ParseException {
		this.locale = Locale.getDefault();
		this.messages = getMessages(this.locale);
		this.options = getOptions();
		this.cmd = getCommandLine(this.options, args);
	}

	public void refactor() throws MissingResourceException, FileNotFoundException, JAXBException {
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

		Refector locRefector = Refector.FactoryGetInstance(this.locale, this.messages, locXMLPresetPath);
		
		locRefector.refactor();
	}

	private ResourceBundle getMessages(Locale locale) throws MissingResourceException {
		ResourceBundle locResourceBundle = ResourceBundle.getBundle(PROPERTY_RESOURCE_BUNDLE_MESSAGES, locale);
		return locResourceBundle;
	}

	private Options getOptions() {
		Options locOptions = new Options();
		locOptions.addOption(OPTION_XML_PRESET_FILE, true, this.messages.getString("cmdLine_p"));
		locOptions.addOption(OPTION_HELP, false, this.messages.getString("cmdLine_h"));
		return locOptions;
	}

	private CommandLine getCommandLine(Options options, String[] args) throws ParseException {
		CommandLineParser locCmdLineParser = new DefaultParser();

		return locCmdLineParser.parse(options, args);
	}

}
