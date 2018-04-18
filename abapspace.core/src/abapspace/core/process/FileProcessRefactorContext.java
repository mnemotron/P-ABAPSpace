package abapspace.core.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

import abapspace.core.context.InterfaceContext;
import abapspace.core.preset.entity.Preset;

public class FileProcessRefactorContext implements InterfaceFileProcess {

	private Map<String, Map<String, InterfaceContext>> contextMap;
	private Preset preset;
	private File targetDir;
	private File sourceDir;

	public FileProcessRefactorContext(Preset preset, File sourceDir, File targetDir,
			Map<String, Map<String, InterfaceContext>> contextMap) {
		this.preset = preset;
		this.contextMap = contextMap;
		this.targetDir = targetDir;
		this.sourceDir = sourceDir;
	}

	@Override
	public void processFile(File sourceFile, StringBuffer contextBuffer) throws Exception {

		String locContext = contextBuffer.toString();

		Map<String, InterfaceContext> locIContextMap = this.contextMap.get(sourceFile.getAbsolutePath());

		Iterator<Map.Entry<String, InterfaceContext>> locICMIterator = locIContextMap.entrySet().iterator();

		while (locICMIterator.hasNext()) {
			Map.Entry<String, InterfaceContext> locV = locICMIterator.next();

			locContext = locContext.replaceAll(locV.getValue().getIdentObject(), locV.getValue().getReplacement());
		}

		// locIContextMap.forEach((objectIdent, iContext) ->
		// {
		// locContext = locContext.replaceAll(iContext.getIdentObject(),
		// iContext.getReplacement());
		// });

		saveTargetFile(this.sourceDir, sourceFile, this.targetDir, locContext);
	}

	private void saveTargetFile(File sourceDir, File sourceFile, File targetDir, String context) throws IOException {
		String locTargetPath = sourceFile.getAbsolutePath();

		locTargetPath = locTargetPath.replaceAll(sourceDir.getAbsolutePath(), targetDir.getAbsolutePath());

		File locTargetFile = new File(locTargetPath);
	
		Files.createDirectories(locTargetFile.toPath().getParent());

		BufferedWriter locBW = new BufferedWriter(new FileWriter(locTargetFile));
		locBW.write(context);
		locBW.close();
	}
}
