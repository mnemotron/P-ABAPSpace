package abapspace.core.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import abapspace.core.exception.UnzipException;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogType;
import abapspace.core.messages.MessageManager;

public class ZipManager {

	public static void unZip(File zipFile, File targetDir) throws UnzipException {

		byte[] buffer = new byte[1024];
		ZipInputStream locZipIS = null;
		File locNewFile = null;
		
		LogEventManager.fireLog(LogType.INFO,
				MessageManager.getMessage("unzip.file") + zipFile.getAbsoluteFile());

		try {
			locZipIS = new ZipInputStream(new FileInputStream(zipFile));

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
			throw new UnzipException(MessageManager.getMessageFormat(
					"exception.unzip.fileNotFound") + locNewFile.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new UnzipException(MessageManager.getMessageFormat(
					"exception.fileProcessCollectContext.FileNotReachable" + locNewFile.getAbsolutePath()), e);
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

}
