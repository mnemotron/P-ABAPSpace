package abapspace.core.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;

import abapspace.core.context.InterfaceContext;
import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.TargetDirectoryNotCreatedException;
import abapspace.core.exception.TargetFileContentNotWrittenException;

public class FileProcessRefactorContext implements InterfaceFileProcess {

	private Map<String, Map<String, InterfaceContext>> contextMap;
	private Logger log;
	private ResourceBundle messages;
	private File targetDir;
	private File sourceDir;

	public FileProcessRefactorContext(Logger log, ResourceBundle messages, File sourceDir, File targetDir,
			Map<String, Map<String, InterfaceContext>> contextMap) {
		this.log = log;
		this.messages = messages;
		this.contextMap = contextMap;
		this.targetDir = targetDir;
		this.sourceDir = sourceDir;
	}

	@Override
	public void processFile(File sourceFile, StringBuffer contextBuffer) throws FileProcessException {

		String locContext = contextBuffer.toString();

		Map<String, InterfaceContext> locIContextMap = this.contextMap.get(sourceFile.getAbsolutePath());

		Iterator<Map.Entry<String, InterfaceContext>> locICMIterator = locIContextMap.entrySet().iterator();

		while (locICMIterator.hasNext()) {
			Map.Entry<String, InterfaceContext> locV = locICMIterator.next();

			locContext = locContext.replaceAll(locV.getValue().getIdentObject(), locV.getValue().getReplacement());
		}

		try {
			this.saveTargetFile(this.sourceDir, sourceFile, this.targetDir, locContext);
		} catch (TargetDirectoryNotCreatedException | TargetFileContentNotWrittenException e) {
			throw new FileProcessException(e.getMessage(), e);
		}
	}

	private void saveTargetFile(File sourceDir, File sourceFile, File targetDir, String context)
			throws TargetDirectoryNotCreatedException, TargetFileContentNotWrittenException {

		BufferedWriter locBW = null;
		File locTargetFile = null;
		String locTargetPath = sourceFile.getAbsolutePath();

		locTargetPath = locTargetPath.replaceAll(sourceDir.getAbsolutePath(), targetDir.getAbsolutePath());

		locTargetFile = new File(locTargetPath);

		try {
			Files.createDirectories(locTargetFile.toPath().getParent());
		} catch (IOException e) {
			throw new TargetDirectoryNotCreatedException(
					this.messages.getString("TargetDirectoryNotCreatedException") + locTargetFile.toPath().getParent(),
					e);
		}

		try {
			locBW = new BufferedWriter(new FileWriter(locTargetFile));
			locBW.write(context);
		} catch (IOException e) {
			throw new TargetFileContentNotWrittenException(
					this.messages.getString("TargetFileContentNotWrittenException") + locTargetFile.getAbsolutePath(),
					e);
		} finally {
			if (locBW != null) {
				try {
					locBW.close();
				} catch (IOException e) {
					this.log.error(this.messages.getString("FileProcessRefactorContext_BW_NotClosed"), e);
				}
			}
		}

	}
}
