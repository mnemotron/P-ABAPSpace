/*
 * MIT License
 *
 * Copyright (c) 2018 mnemotron
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package abapspace.gui.main;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import abapspace.core.preset.entity.Preset;
import abapspace.gui.messages.GUIMessageManager;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class PanelMain extends JPanel {

	private static final long serialVersionUID = 7382875965875337426L;

	private JPanel panelPreset;
	private JTextField txfPresetDir;
	private JLabel lblPresetDir;
	private JButton btnPresetDir;
	private JComboBox<Preset> cbPreset;
	private JLabel lblPreset;
	private JPanel panelST;
	private JTextField txtSourceDir;
	private JLabel lblSourceDir;
	private JButton btnSourceDir;
	private JLabel lblTargetDir;
	private JTextField txtTargetDir;
	private JButton btnTargetDir;
	private GUICMain guicmain;

	public PanelMain(GUICMain guicmain) {

		this.guicmain = guicmain;

		initialize();
	}

	private void initialize() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(getPanelST(), GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE).addComponent(
								getPanelPreset(), Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(getPanelPreset(), GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
						.addGap(7)
						.addComponent(getPanelST(), GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(25, Short.MAX_VALUE)));
		setLayout(groupLayout);
	}

	private JPanel getPanelPreset() {
		if (panelPreset == null) {
			panelPreset = new JPanel();
			panelPreset.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255)),
					GUIMessageManager.getMessage("border.title.preset"), TitledBorder.LEADING, TitledBorder.TOP, null,
					Color.BLUE));
			GroupLayout gl_panelPreset = new GroupLayout(panelPreset);
			gl_panelPreset.setHorizontalGroup(gl_panelPreset.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panelPreset.createSequentialGroup().addContainerGap().addGroup(gl_panelPreset
							.createParallelGroup(Alignment.LEADING)
							.addGroup(Alignment.TRAILING, gl_panelPreset.createSequentialGroup()
									.addComponent(getTxfPresetDir(), GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED).addComponent(getBtnPresetDir())
									.addGap(2))
							.addComponent(getLblPresetDir())
							.addComponent(getCbPreset(), Alignment.TRAILING, 0, 356, Short.MAX_VALUE)
							.addComponent(getLblPreset())).addContainerGap()));
			gl_panelPreset
					.setVerticalGroup(
							gl_panelPreset.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panelPreset.createSequentialGroup().addGap(14)
											.addComponent(getLblPresetDir()).addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(gl_panelPreset.createParallelGroup(Alignment.BASELINE)
													.addComponent(getTxfPresetDir(), GroupLayout.PREFERRED_SIZE,
															GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
													.addComponent(getBtnPresetDir()))
											.addGap(12).addComponent(getLblPreset())
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(getCbPreset(), GroupLayout.PREFERRED_SIZE,
													GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addContainerGap(48, Short.MAX_VALUE)));
			panelPreset.setLayout(gl_panelPreset);
		}
		return panelPreset;
	}

	public JTextField getTxfPresetDir() {
		if (txfPresetDir == null) {
			txfPresetDir = new JTextField();
			txfPresetDir.setEditable(false);
			txfPresetDir.setColumns(10);
		}
		return txfPresetDir;
	}

	private JLabel getLblPresetDir() {
		if (lblPresetDir == null) {
			lblPresetDir = new JLabel(GUIMessageManager.getMessage("label.presetDir"));
			lblPresetDir.setForeground(Color.BLUE);
			lblPresetDir.setLabelFor(getTxfPresetDir());
		}
		return lblPresetDir;
	}

	private JButton getBtnPresetDir() {
		if (btnPresetDir == null) {
			btnPresetDir = new JButton("");
			btnPresetDir.setIcon(new ImageIcon(PanelMain.class.getResource("/abapspace/gui/res/folderopen.png")));
			btnPresetDir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					guicmain.chooseDirPreset();
					guicmain.visualController();
				}
			});
		}
		return btnPresetDir;
	}

	public JComboBox<Preset> getCbPreset() {
		if (cbPreset == null) {
			cbPreset = new JComboBox<Preset>();
			cbPreset.setBackground(Color.WHITE);
			cbPreset.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					guicmain.choosePreset((Preset) cbPreset.getSelectedItem());
					guicmain.visualController();
				}
			});
		}
		return cbPreset;
	}

	private JLabel getLblPreset() {
		if (lblPreset == null) {
			lblPreset = new JLabel(GUIMessageManager.getMessage("label.preset"));
			lblPreset.setForeground(Color.BLUE);
		}
		return lblPreset;
	}

	private JPanel getPanelST() {
		if (panelST == null) {
			panelST = new JPanel();
			panelST.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255)),
					GUIMessageManager.getMessage("border.title.targetSourceDir"), TitledBorder.LEADING,
					TitledBorder.TOP, null, Color.BLUE));
			GroupLayout gl_panelST = new GroupLayout(panelST);
			gl_panelST.setHorizontalGroup(gl_panelST.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panelST.createSequentialGroup().addContainerGap()
							.addGroup(gl_panelST.createParallelGroup(Alignment.LEADING).addComponent(getLblSourceDir())
									.addComponent(getLblTargetDir())
									.addGroup(Alignment.TRAILING, gl_panelST.createSequentialGroup()
											.addGroup(gl_panelST.createParallelGroup(Alignment.TRAILING)
													.addComponent(getTxtTargetDir(), GroupLayout.DEFAULT_SIZE, 801,
															Short.MAX_VALUE)
													.addComponent(getTxtSourceDir(), GroupLayout.DEFAULT_SIZE, 801,
															Short.MAX_VALUE))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(gl_panelST.createParallelGroup(Alignment.LEADING)
													.addComponent(getBtnSourceDir(), Alignment.TRAILING)
													.addComponent(getBtnTargetDir(), Alignment.TRAILING))))
							.addContainerGap()));
			gl_panelST.setVerticalGroup(gl_panelST.createParallelGroup(Alignment.LEADING).addGroup(gl_panelST
					.createSequentialGroup().addGap(14).addComponent(getLblSourceDir())
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelST.createParallelGroup(Alignment.BASELINE)
							.addComponent(getTxtSourceDir(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
							.addComponent(getBtnSourceDir()))
					.addGap(14).addComponent(getLblTargetDir()).addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelST.createParallelGroup(Alignment.BASELINE)
							.addComponent(getTxtTargetDir(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
							.addComponent(getBtnTargetDir()))
					.addContainerGap(39, Short.MAX_VALUE)));
			panelST.setLayout(gl_panelST);
		}
		return panelST;
	}

	public JTextField getTxtSourceDir() {
		if (txtSourceDir == null) {
			txtSourceDir = new JTextField();
			txtSourceDir.setEditable(false);
			txtSourceDir.setColumns(10);
		}
		return txtSourceDir;
	}

	private JLabel getLblSourceDir() {
		if (lblSourceDir == null) {
			lblSourceDir = new JLabel(GUIMessageManager.getMessage("label.sourceDir"));
			lblSourceDir.setForeground(Color.BLUE);
			lblSourceDir.setLabelFor(getTxtSourceDir());
		}
		return lblSourceDir;
	}

	public JButton getBtnSourceDir() {
		if (btnSourceDir == null) {
			btnSourceDir = new JButton("");
			btnSourceDir.setIcon(new ImageIcon(PanelMain.class.getResource("/abapspace/gui/res/folderopen.png")));
			btnSourceDir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guicmain.chooseDirSource();
					guicmain.visualController();
				}
			});
		}
		return btnSourceDir;
	}

	private JLabel getLblTargetDir() {
		if (lblTargetDir == null) {
			lblTargetDir = new JLabel(GUIMessageManager.getMessage("label.targetDir"));
			lblTargetDir.setLabelFor(getTxtTargetDir());
			lblTargetDir.setForeground(Color.BLUE);
		}
		return lblTargetDir;
	}

	public JTextField getTxtTargetDir() {
		if (txtTargetDir == null) {
			txtTargetDir = new JTextField();
			txtTargetDir.setEditable(false);
			txtTargetDir.setColumns(10);
		}
		return txtTargetDir;
	}

	public JButton getBtnTargetDir() {
		if (btnTargetDir == null) {
			btnTargetDir = new JButton("");
			btnTargetDir.setIcon(new ImageIcon(PanelMain.class.getResource("/abapspace/gui/res/folderopen.png")));
			btnTargetDir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guicmain.chooseDirTarget();
					guicmain.visualController();
				}
			});
		}
		return btnTargetDir;
	}
}
