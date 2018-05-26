package abapspace.core.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import abapspace.core.exception.UnzipException;
import abapspace.core.exception.ZipException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;

public class ZipManager {

    public static void unZip(File zipSourceFile, File targetDir) throws UnzipException {

	byte[] buffer = new byte[1024];
	ZipInputStream locZipIS = null;
	File locNewFile = null;

	LogEventManager.fireLog(LogType.INFO,
		MessageManager.getMessage("unzip.file") + zipSourceFile.getAbsoluteFile());

	try {
	    locZipIS = new ZipInputStream(new FileInputStream(zipSourceFile));

	    ZipEntry locZE = locZipIS.getNextEntry();

	    while (locZE != null) {

		String locFileName = locZE.getName();

		locNewFile = new File(targetDir + File.separator + locFileName);

		// create all non-existent directories
		Files.createDirectories(locNewFile.toPath().getParent());

		FileOutputStream locFOS = new FileOutputStream(locNewFile);

		int len;
		while ((len = locZipIS.read(buffer)) > 0) {
		    locFOS.write(buffer, 0, len);
		}

		locFOS.close();

		LogEventManager.fireLog(LogType.INFO,
			MessageManager.getMessage("unzip.file.child") + locNewFile.getAbsoluteFile());

		locZE = locZipIS.getNextEntry();
	    }
	} catch (FileNotFoundException e) {
	    throw new UnzipException(
		    MessageManager.getMessage("exception.zip.fileNotFound") + locNewFile.getAbsolutePath(), e);
	} catch (IOException e) {
	    throw new UnzipException(
		    MessageManager.getMessage("exception.zip.fileNotReachable" + locNewFile.getAbsolutePath()), e);
	} finally {

	    if (locZipIS != null) {
		try {
		    locZipIS.closeEntry();
		    locZipIS.close();
		} catch (IOException e) {
		    LogEventManager.fireLog(LogType.ERROR,
			    MessageManager.getMessage("exception.zipInputStreamNotClosed"), e);
		}
	    }

	}

    }

    public static void zipArchive(File zipTargetFile, File srcDir) throws ZipException {

	FileOutputStream locFOS = null;
	ZipOutputStream locZOS = null;

	try {

	    locFOS = new FileOutputStream(zipTargetFile);

	    locZOS = new ZipOutputStream(locFOS);

	    ZipManager.addDirToArchive(locZOS, srcDir);

	} catch (FileNotFoundException e) {
	    throw new ZipException(
		    MessageManager.getMessage("exception.zip.fileNotFound") + zipTargetFile.getAbsolutePath(), e);
	} finally {
	    if (locZOS != null) {
		try {
		    locZOS.close();
		} catch (IOException e) {
		    LogEventManager.fireLog(LogType.ERROR,
			    MessageManager.getMessage("exception.zipInputStreamNotClosed"), e);
		}
	    }
	}
    }

    private static void addDirToArchive(ZipOutputStream zipOS, File srcFile) throws ZipException {

	FileInputStream locFIS = null;
	File[] files = srcFile.listFiles();

	LogEventManager.fireLog(LogType.INFO, MessageManager.getMessage("zip.dir") + srcFile.getAbsolutePath());

	for (File file : files) {

	    if (file.isDirectory()) {
		addDirToArchive(zipOS, file);
		continue;
	    }

	    try {

		LogEventManager.fireLog(LogType.INFO, MessageManager.getMessage("zip.file") + file.getAbsolutePath());

		byte[] buffer = new byte[1024];

		locFIS = new FileInputStream(file);

		zipOS.putNextEntry(new ZipEntry(file.getName()));

		int length;

		while ((length = locFIS.read(buffer)) > 0) {
		    zipOS.write(buffer, 0, length);
		}

	    } catch (FileNotFoundException e) {
		throw new ZipException(MessageManager.getMessage("exception.zip.fileNotFound") + file.getAbsolutePath(),
			e);
	    } catch (IOException e) {
		throw new ZipException(
			MessageManager.getMessage("exception.zip.fileNotReachable" + file.getAbsolutePath()), e);
	    } finally {

		if (locFIS != null) {
		    try {
			locFIS.close();
		    } catch (IOException e) {
			LogEventManager.fireLog(LogType.ERROR,
				MessageManager.getMessage("exception.fileInputStreamNotClosed"), e);
		    }
		}

		if (zipOS != null) {
		    try {
			zipOS.closeEntry();
		    } catch (IOException e) {
			LogEventManager.fireLog(LogType.ERROR,
				MessageManager.getMessage("exception.zipInputStreamNotClosed"), e);
		    }
		}
	    }
	}
    }
}
