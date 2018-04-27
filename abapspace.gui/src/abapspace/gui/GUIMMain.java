package abapspace.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import abapspace.core.exception.PresetDirNotFoundException;
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

    public GUIMMain() {
	this.presetManager = null;
	this.log = LogManager.getLogger();
	this.initLogEventManager();
    }

    public void setPresetManager(String presetDir) throws PresetDirNotFoundException {
	this.presetManager = PresetManager.getInstance(presetDir);
    }

    public void setPresetManagerDefault() throws PresetDirNotFoundException {
	this.setPresetManager(GUIMMain.PRESET_DIR_DEFAULT);
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

    public Preset getPreset() {
	return preset;
    }

    public void setPreset(Preset preset) {
	this.preset = preset;
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
