package abapspace.core.context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import abapspace.core.exception.FileProcessException;
import abapspace.core.preset.entity.Preset;
import abapspace.core.process.InterfaceFileProcess;

public class ContextDirectory extends File implements InterfaceFileProcess {

    private static final long serialVersionUID = -3077572976733503870L;

    private Preset preset;
    private ContextManager contextManager;
    private List<File> childList;
    private InterfaceContext iContext;
    private String object;
  
    public ContextDirectory(String pathname, Preset preset, ContextManager contextManager) {
	super(pathname);
	this.preset = preset;
	this.contextManager = contextManager;
	this.iContext = null;
	this.object = new String();
	this.childList = new ArrayList<File>();
    }

    @Override
    public void collectContext() throws FileProcessException {
	File[] locFileList = this.listFiles();

	//directory name
	
	//directory children
	for (File file : locFileList) {
	    if (file.isDirectory()) {
		ContextDirectory locCD = new ContextDirectory(file.getAbsolutePath(), this.preset, this.contextManager);
		locCD.collectContext();
		this.childList.add(locCD);
	    } else {
		ContextFile locCF = new ContextFile(file.getAbsolutePath(), this.preset, this.contextManager);
		locCF.collectContext();
		this.childList.add(locCF);
	    }
	}
    }

    @Override
    public void RefactorContext() {

    }

}
