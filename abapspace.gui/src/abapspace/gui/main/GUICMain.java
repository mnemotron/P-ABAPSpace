package abapspace.gui.main;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import abapspace.core.exception.FileProcessException;
import abapspace.core.exception.PresetDirNotFoundException;
import abapspace.core.exception.SourceDirectoryNotFoundException;
import abapspace.core.exception.TargetDirectoryNotFoundException;
import abapspace.core.preset.entity.Preset;
import abapspace.gui.edit.GUICEdit;
import abapspace.gui.messages.GUIMessageManager;

public class GUICMain {

	private GUIMMain guimmain;
	private GUIMain guimain;

	public GUICMain() {

		// init model
		this.guimmain = new GUIMMain();

		// init GUI
		this.guimain = new GUIMain(this);

		this.setPresetManager(null);
		this.addPresetsToComboBox();

		this.visualController();
	}

	public void startGUI() {
		this.guimain.getFrameMain().setVisible(true);
		this.guimain.getFrameMain().toFront();
		this.guimain.getFrameMain().requestFocus();
	}

	public void stopGUI() {

		if (this.guimain.getFrameMain().isEnabled()) {
			this.guimain.getFrameMain().setVisible(false);
			System.exit(0);
		}
	}

	public void chooseDirPreset() {
		File locPresetDir = this.guimain.showDirectoryChooser(this.guimmain.getPresetDir());

		if (locPresetDir == null) {
			return;
		}

		this.setPresetManager(locPresetDir);

		this.addPresetsToComboBox();
	}

	public void chooseDirSource() {

		File locSourceDir = this.guimain.showDirectoryChooser(this.guimmain.getPreset().getRefactorSourceDir());

		if (locSourceDir != null) {
			this.guimmain.setSourceDir(locSourceDir.getAbsolutePath());

			this.setSourceDirToTxf();
		}
	}

	public void chooseDirTarget() {
		File locTargetDir = this.guimain.showDirectoryChooser(this.guimmain.getPreset().getRefactorTargetDir());

		if (locTargetDir != null) {
			this.guimmain.setTargetDir(locTargetDir.getAbsolutePath());

			this.setTargetDirToTxf();
		}
	}

	public void choosePreset(Preset preset) {

		if (preset != null) {
			this.guimmain.setPreset(preset);
		}

		this.setSourceDirToTxf();

		this.setTargetDirToTxf();
	}

	public void setTargetDirToTxf() {
		try {
			this.guimain.getPanelMain().getTxtTargetDir().setText(this.guimmain.getTargetDir());
		} catch (TargetDirectoryNotFoundException e) {
			this.guimain.getPanelMain().getTxtTargetDir().setText(null);
			// this.guimain.showMessage(e.getMessage(),
			// GUIMessageManager.getMessage("dialog.title.targetDir"),
			// JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setSourceDirToTxf() {
		try {
			this.guimain.getPanelMain().getTxtSourceDir().setText(this.guimmain.getSourceDir());
		} catch (SourceDirectoryNotFoundException e) {
			this.guimain.getPanelMain().getTxtSourceDir().setText(null);
			// this.guimain.showMessage(e.getMessage(),
			// GUIMessageManager.getMessage("dialog.title.sourceDir"),
			// JOptionPane.ERROR_MESSAGE);
		}
	}

	public void startPreRefactor() {
		try {
			boolean locValid = this.guimmain.startPreRefactor();

			if (!locValid) {
				this.showEdit(this.guimmain.getEditData());
			}

		} catch (FileProcessException | SourceDirectoryNotFoundException | TargetDirectoryNotFoundException e) {
			this.guimain.showMessage(e.getMessage(), GUIMessageManager.getMessage("dialog.title.exception"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public GUIMain getGuimain() {
		return guimain;
	}

	public void showEdit(Object[][] data) {
		GUICEdit guicedit = new GUICEdit(this, data);

		guicedit.startGUI();
	}

	public void responseEdit(Object[][] data, boolean cancel) {
		if (!cancel) {
			this.guimmain.setEditData(data);

			try {
				this.guimmain.startRefactor();
			} catch (FileProcessException | SourceDirectoryNotFoundException | TargetDirectoryNotFoundException e) {
				this.guimain.showMessage(e.getMessage(), GUIMessageManager.getMessage("dialog.title.exception"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void visualController() {
		PanelMain locPanelMain = this.guimain.getPanelMain();

		if ((this.guimmain.getPresetDir().isEmpty()) || (this.guimmain.isPresetListEmpty())) {
			locPanelMain.getBtnSourceDir().setEnabled(false);
			locPanelMain.getBtnTargetDir().setEnabled(false);
			locPanelMain.getCbPreset().setEnabled(false);

			locPanelMain.getTxtSourceDir().setText(null);
			locPanelMain.getTxtTargetDir().setText(null);
		} else {
			locPanelMain.getBtnSourceDir().setEnabled(true);
			locPanelMain.getBtnTargetDir().setEnabled(true);
			locPanelMain.getCbPreset().setEnabled(true);
		}

		try {
			if (this.guimmain.getPreset().getFileSourceDir() != null
					&& this.guimmain.getPreset().getFileTargetDir() != null) {
				this.guimain.getBtnRefactor().setEnabled(true);
			} else {
				this.guimain.getBtnRefactor().setEnabled(false);
			}
		} catch (SourceDirectoryNotFoundException | TargetDirectoryNotFoundException e) {
			this.guimain.getBtnRefactor().setEnabled(false);
		}
	}

	private void setPresetManager(File preset) {

		try {
			if (preset != null) {
				this.guimmain.setPresetManager(preset.getAbsolutePath());
			} else {
				this.guimmain.setPresetManagerDefault();
			}

		} catch (PresetDirNotFoundException e) {
			this.guimain.showMessage(e.getMessage(), GUIMessageManager.getMessage("dialog.title.presetDir"),
					JOptionPane.ERROR_MESSAGE);
		}

		this.setPresetDirToTxf();
	}

	private void setPresetDirToTxf() {
		this.guimain.getPanelMain().getTxfPresetDir().setText(this.guimmain.getPresetDir());
	}

	private void addPresetsToComboBox() {
		List<Preset> locPresetList = this.guimmain.getPresetList();

		this.guimain.getPanelMain().getCbPreset().removeAllItems();

		for (Preset preset : locPresetList) {
			this.guimain.getPanelMain().getCbPreset().addItem(preset);
		}
	}
}
