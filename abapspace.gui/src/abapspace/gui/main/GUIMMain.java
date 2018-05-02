package abapspace.gui.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import abapspace.core.Refector;
import abapspace.core.context.InterfaceContext;
import abapspace.core.context.ContextCheckMaxNameLength;
import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.PresetDirNotFoundException;
import abapspace.core.exception.SourceDirectoryNotFoundException;
import abapspace.core.exception.TargetDirectoryNotFoundException;
import abapspace.core.log.LogEvent;
import abapspace.core.log.LogEventManager;
import abapspace.core.log.LogListener;
import abapspace.core.preset.PresetManager;
import abapspace.core.preset.entity.Preset;

public class GUIMMain {

	private static final String PRESET_DIR_DEFAULT = "preset";

	private Logger log;
	private PresetManager presetManager;
	private Preset preset;
	private Refector refector;

	public GUIMMain() {
		this.presetManager = null;
		this.log = LogManager.getLogger();
		this.initLogEventManager();
		this.refector = new Refector();
		this.preset = new Preset();
	}

	public void setPresetManager(String presetDir) throws PresetDirNotFoundException {
		this.presetManager = PresetManager.getInstance(presetDir);

		this.preset = new Preset();
	}

	public void setPresetManagerDefault() throws PresetDirNotFoundException {
		this.setPresetManager(GUIMMain.PRESET_DIR_DEFAULT);

		this.preset = new Preset();
	}

	public String getPresetDir() {

		String locPresetDir = new String();

		if (this.presetManager != null) {
			locPresetDir = this.presetManager.getPresetDir().getAbsolutePath();
		}

		return locPresetDir;
	}

	public List<Preset> getPresetList() {
		List<Preset> locPresetList = new ArrayList<Preset>();

		if (this.presetManager != null) {
			locPresetList = this.presetManager.getPresetList();
		}

		return locPresetList;
	}

	public boolean isPresetListEmpty() {
		if (this.presetManager != null) {
			return this.presetManager.getPresetList().isEmpty();
		} else {
			return true;
		}
	}

	public Preset getPreset() {
		return preset;
	}

	public void setPreset(Preset preset) {
		this.preset = preset;
	}

	public String getSourceDir() throws SourceDirectoryNotFoundException {
		File locFile = this.getPreset().getFileSourceDir();

		if (locFile != null) {
			return locFile.getAbsolutePath();
		} else {
			return new String();
		}
	}

	public String getTargetDir() throws TargetDirectoryNotFoundException {
		File locFile = this.getPreset().getFileTargetDir();

		if (locFile != null) {
			return locFile.getAbsolutePath();
		} else {
			return new String();
		}
	}

	public boolean startPreRefactor()
			throws FileProcessException, SourceDirectoryNotFoundException, TargetDirectoryNotFoundException {
		boolean locValid = true;

		this.refector = new Refector(this.getPreset());

		this.refector.collectContext();

		locValid = this.refector.checkMaxNameLength();

		if (locValid) {
			this.startRefactor();
		}

		return locValid;
	}

	public void setSourceDir(String sourceDir) {
		this.preset.setRefactorSourceDir(sourceDir);
	}

	public void setTargetDir(String targetDir) {
		this.preset.setRefactorTargetDir(targetDir);
	}

	public void startRefactor()
			throws FileProcessException, SourceDirectoryNotFoundException, TargetDirectoryNotFoundException {
		this.refector.refactorContext();
	}

	public Object[][] getEditData() {
		Map<String, InterfaceContext> locEditMap = new HashMap<String, InterfaceContext>();
		Map<String, Map<String, InterfaceContext>> locContextMap = this.refector.getContextMap();

		locContextMap.forEach((fileIdent, contextMap) -> {
			contextMap.forEach((objectIdent, iContext) -> {
				if (!locEditMap.containsKey(iContext.getObject())) {
					locEditMap.put(iContext.getObject(), iContext);
				}
			});
		});

		Object[][] locObjectArray = new Object[locEditMap.size()][4]; // 1 row, 2 col

		int locRow[] = { 0 };

		locEditMap.forEach((objectID, iContext) -> {

			ContextCheckMaxNameLength locMaxNameLength = iContext.checkMaxNameLengthForReplacement();

			for (int j = 0, lj = locObjectArray[locRow[0]].length; j < lj; j++) {

				switch (j) {
				case 0: // found object
					locObjectArray[locRow[0]][j] = iContext.getObject();
					break;
				case 1: // replacement
					locObjectArray[locRow[0]][j] = iContext.getReplacement();
					break;
				case 2: // maximum length
					locObjectArray[locRow[0]][j] = locMaxNameLength.getMaxNameLength();
					break;
				case 3: // length
					locObjectArray[locRow[0]][j] = locMaxNameLength.getActualNameLength();
					break;
				}

			}

			locRow[0]++;

		});

		return locObjectArray;
	}

	public void setEditData(Object[][] data) {

		Map<String, Map<String, InterfaceContext>> locContextMap = this.refector.getContextMap();

		locContextMap.forEach((fileIdent, contextMap) -> {
			contextMap.forEach((objectIdent, iContext) -> {

				for (int i = 0, li = data.length; i < li; i++) { // row
					if (objectIdent.equals(data[i][0])) {
						iContext.setReplacement((String) data[i][1]);
					}
				}

			});
		});

		this.refector.setContextMap(locContextMap);
	}

	private void initLogEventManager() {
		LogEventManager locLogEventManager = LogEventManager.getInstance();
		locLogEventManager.addLogListener(new LogListener() {

			@Override
			public void log(LogEvent event) {

				switch (event.getLogType()) {
				case ERROR:
					log.error(event.getMessage(), event.getException());
					break;
				case INFO:
					log.info(event.getMessage(), event.getException());
					break;
				case WARNING:
					log.warn(event.getMessage(), event.getException());
					break;
				default:
					log.info(event.getMessage(), event.getException());
					break;
				}
			}
		});
	}

}
