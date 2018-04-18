package abapspace.core.process;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapspace.core.context.InterfaceContext;
import abapspace.core.context.entity.Context;
import abapspace.core.preset.entity.Preset;

public class FileProcessCollectContext implements InterfaceFileProcess {

	private Map<String, Map<String, InterfaceContext>> contextMap;
	private Preset preset;

	public FileProcessCollectContext(Preset preset, Map<String, Map<String, InterfaceContext>> contextMap) {
		this.preset = preset;
		this.contextMap = contextMap;
	}

	@Override
	public void processFile(File sourceFile, StringBuffer contextBuffer) throws Exception {

		Map<String, InterfaceContext> locIContextMap = new HashMap<String, InterfaceContext>();

		// object class
		if (this.preset.getObjectClass() != null) {
			Context locContext = new Context();
			locContext.setIdentRegex(this.preset.getObjectClass().getIdentRegex());
			locContext.setNameMaxLength(this.preset.getObjectClass().getNameMaxLength());
			locContext.setNamespaceNew(this.preset.getNamespaceNew());
			locContext.setNamespaceOld(this.preset.getNamespaceOld());

			locIContextMap = processFileSearch(contextBuffer.toString(), locIContextMap, locContext);
		}

		// object interface
		if (this.preset.getObjectInterface() != null) {
			Context locContext = new Context();
			locContext.setIdentRegex(this.preset.getObjectInterface().getIdentRegex());
			locContext.setNameMaxLength(this.preset.getObjectInterface().getNameMaxLength());
			locContext.setNamespaceNew(this.preset.getNamespaceNew());
			locContext.setNamespaceOld(this.preset.getNamespaceOld());

			locIContextMap = processFileSearch(contextBuffer.toString(), locIContextMap, locContext);
		}

		this.contextMap.put(sourceFile.getAbsolutePath(), (HashMap<String, InterfaceContext>) locIContextMap);
	}

	private Map<String, InterfaceContext> processFileSearch(String fileContextString, Map<String, InterfaceContext> iContextMap, InterfaceContext iContext)
			throws CloneNotSupportedException {

		for (Matcher m = Pattern.compile(iContext.getRegex(true), Pattern.CASE_INSENSITIVE).matcher(fileContextString); m.find();) {

			String locGroup1 = m.group(1);
			
			InterfaceContext locIContext = iContext.clone();

			if (iContextMap.containsKey(locGroup1)) {
				continue;
			}

			locIContext.setIdentObject(locGroup1);

			iContextMap.put(locGroup1, locIContext);
		}

		return iContextMap;
	}
}
