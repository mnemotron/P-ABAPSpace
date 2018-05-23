package abapspace.core.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipManager {

    public static void unZip(File zipFile, File targetDir) {

	byte[] buffer = new byte[1024];
	ZipInputStream locZipIS = null;

	try {
	    locZipIS = new ZipInputStream(new FileInputStream(zipFile));

	    ZipEntry locZE = locZipIS.getNextEntry();

	    while (locZE != null) {

		String locFileName = locZE.getName();
		File locNewFile = new File(targetDir + File.separator + locFileName);

		// TODO Auto-generated catch block
		// LogEventManager.fireLog(LogType.INFO, File Unzip:
		// locNewFile.getAbsoluteFile());

		// create all non exists directories
		Files.createDirectories(locNewFile.toPath().getParent());

		FileOutputStream fos = new FileOutputStream(locNewFile);

		int len;
		while ((len = locZipIS.read(buffer)) > 0) {
		    fos.write(buffer, 0, len);
		}

		fos.close();
		locZE = locZipIS.getNextEntry();
	    }
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	} finally {

	    if (locZipIS != null) {
		try {
		    locZipIS.closeEntry();
		    locZipIS.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    // LogEventManager.fireLog(LogType.ERROR,
		    // MessageManager.getMessage("FileProcessRefactorContext_BW_NotClosed"),
		    // e);
		}
	    }

	}

    }

}
