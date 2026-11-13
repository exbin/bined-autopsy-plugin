/*
 * Copyright (C) ExBin Project, https://exbin.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.bined.autopsy.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.exbin.auxiliary.dropdownbutton.DropDownButton;
import org.exbin.bined.CodeType;
import org.exbin.bined.operation.command.BinaryDataUndoRedo;
import org.exbin.jaguif.App;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.options.api.OptionsStorage;
import org.exbin.bined.jaguif.viewer.settings.CodeAreaOptions;

/**
 * Binary editor toolbar panel.
 */
@NullMarked
public class BinEdToolbarPanel extends javax.swing.JPanel {

    protected final java.util.ResourceBundle componentResourceBundle;
    protected final java.util.ResourceBundle viewerResourceBundle;
    protected final java.util.ResourceBundle optionsSettingsResourceBundle;
    protected final java.util.ResourceBundle onlineHelpResourceBundle;
    protected final java.util.ResourceBundle operationUndoResourceBundle;

    protected BinaryDataUndoRedo undoRedo = null;
    protected @Nullable Control control;

    protected final AbstractAction cycleCodeTypesAction;
    protected final JRadioButtonMenuItem binaryCodeTypeAction;
    protected final JRadioButtonMenuItem octalCodeTypeAction;
    protected final JRadioButtonMenuItem decimalCodeTypeAction;
    protected final JRadioButtonMenuItem hexadecimalCodeTypeAction;
    protected final ButtonGroup codeTypeButtonGroup;
    protected DropDownButton codeTypeDropDown;

    protected JButton saveButton = null;
    protected JButton undoEditButton = null;
    protected JButton redoEditButton = null;

    public BinEdToolbarPanel() {
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        componentResourceBundle = languageModule.getBundle(org.exbin.bined.jaguif.component.BinedComponentModule.class);
        viewerResourceBundle = languageModule.getBundle(org.exbin.bined.jaguif.viewer.BinedViewerModule.class);
        optionsSettingsResourceBundle = languageModule.getBundle(org.exbin.jaguif.options.settings.OptionsSettingsModule.class);
        onlineHelpResourceBundle = languageModule.getBundle(org.exbin.jaguif.help.online.action.OnlineHelpAction.class);
        operationUndoResourceBundle = languageModule.getBundle(org.exbin.jaguif.operation.undo.OperationUndoModule.class);

        codeTypeButtonGroup = new ButtonGroup();
        binaryCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(viewerResourceBundle.getString("binaryCodeTypeAction.text")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.setCodeType(CodeType.BINARY);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(binaryCodeTypeAction);
        octalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(viewerResourceBundle.getString("octalCodeTypeAction.text")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.setCodeType(CodeType.OCTAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(octalCodeTypeAction);
        decimalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(viewerResourceBundle.getString("decimalCodeTypeAction.text")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.setCodeType(CodeType.DECIMAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(decimalCodeTypeAction);
        hexadecimalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(viewerResourceBundle.getString("hexadecimalCodeTypeAction.text")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.setCodeType(CodeType.HEXADECIMAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(hexadecimalCodeTypeAction);
        cycleCodeTypesAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codeTypePos = control.getCodeType().ordinal();
                CodeType[] values = CodeType.values();
                CodeType next = codeTypePos + 1 >= values.length ? values[0] : values[codeTypePos + 1];
                control.setCodeType(next);
                updateCycleButtonState();
            }
        };

        initComponents();
        init();
    }

    private void init() {
        cycleCodeTypesAction.putValue(Action.SHORT_DESCRIPTION, viewerResourceBundle.getString("cycleCodeTypesAction.text"));
        JPopupMenu cycleCodeTypesPopupMenu = new JPopupMenu();
        cycleCodeTypesPopupMenu.add(binaryCodeTypeAction);
        cycleCodeTypesPopupMenu.add(octalCodeTypeAction);
        cycleCodeTypesPopupMenu.add(decimalCodeTypeAction);
        cycleCodeTypesPopupMenu.add(hexadecimalCodeTypeAction);
        codeTypeDropDown = new DropDownButton(cycleCodeTypesAction, cycleCodeTypesPopupMenu);
        controlToolBar.add(codeTypeDropDown, 0);

        JButton optionsButton = new JButton();
        optionsButton.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                control.openSettings();
            }
        });
        optionsButton.setToolTipText(optionsSettingsResourceBundle.getString("settingsAction.text"));
        optionsButton.setIcon(new ImageIcon(getClass().getResource(optionsSettingsResourceBundle.getString("settingsAction.smallIcon"))));
        controlToolBar.add(optionsButton);

        JButton onlineHelpButton = new JButton();
        onlineHelpButton.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                control.onlineHelp();
            }
        });
        onlineHelpButton.setToolTipText(onlineHelpResourceBundle.getString("onlineHelpAction.text"));
        onlineHelpButton.setIcon(new ImageIcon(getClass().getResource("/org/exbin/bined/autopsy/resources/icons/help.png")));
        controlToolBar.add(onlineHelpButton);
    }

    public void setTargetComponent(JComponent targetComponent) {
        // controlToolBar.setTargetComponent(targetComponent);
    }

    public void setCodeAreaControl(Control codeAreaControl) {
        this.control = codeAreaControl;
        updateNonprintables();
        updateCodeColorization();
        updateCycleButtonState();
    }

    private void updateCycleButtonState() {
        CodeType codeType = control.getCodeType();
        codeTypeDropDown.setActionText(codeType.name().substring(0, 3));
        switch (codeType) {
            case BINARY: {
                if (!binaryCodeTypeAction.isSelected()) {
                    binaryCodeTypeAction.setSelected(true);
                }
                break;
            }
            case OCTAL: {
                if (!octalCodeTypeAction.isSelected()) {
                    octalCodeTypeAction.setSelected(true);
                }
                break;
            }
            case DECIMAL: {
                if (!decimalCodeTypeAction.isSelected()) {
                    decimalCodeTypeAction.setSelected(true);
                }
                break;
            }
            case HEXADECIMAL: {
                if (!hexadecimalCodeTypeAction.isSelected()) {
                    hexadecimalCodeTypeAction.setSelected(true);
                }
                break;
            }
        }
    }

    public void applyFromCodeArea() {
        updateCycleButtonState();
        updateNonprintables();
    }

    public void loadFromOptions(OptionsStorage options) {
        control.setCodeType(new CodeAreaOptions(options).getCodeType());
        updateCycleButtonState();
        updateNonprintables();
    }

    public void updateNonprintables() {
        showNonprintablesToggleButton.setSelected(control.isShowNonprintables());
    }

    public void updateCodeColorization() {
        codeColorizationToggleButton.setSelected(control.isCodeColorization());
    }

    public void updateUndoState() {
        if (undoRedo != null) {
            undoEditButton.setEnabled(undoRedo.canUndo());
            redoEditButton.setEnabled(undoRedo.canRedo());
        }
    }

    public void updateModified(boolean modified) {
        if (saveButton != null) {
            saveButton.setEnabled(modified);
        }
    }

    public void setUndoHandler(BinaryDataUndoRedo undoRedo) {
        this.undoRedo = undoRedo;

        undoEditButton = new JButton();
        undoEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(operationUndoResourceBundle.getString("editUndoAction.smallIcon"))));
        undoEditButton.setToolTipText(operationUndoResourceBundle.getString("editUndoAction.text"));
        undoEditButton.addActionListener((evt) -> {
            try {
                undoRedo.performUndo();
                updateUndoState();
            } catch (Exception ex) {
                Logger.getLogger(BinEdToolbarPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        redoEditButton = new JButton();
        redoEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(operationUndoResourceBundle.getString("editRedoAction.smallIcon"))));
        redoEditButton.setToolTipText(operationUndoResourceBundle.getString("editRedoAction.text"));
        redoEditButton.addActionListener((evt) -> {
            try {
                undoRedo.performRedo();
                updateUndoState();
            } catch (Exception ex) {
                Logger.getLogger(BinEdToolbarPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        updateUndoState();
        int position = saveButton != null ? 1 : 0;
        controlToolBar.add(undoEditButton, position);
        controlToolBar.add(redoEditButton, position + 1);
        controlToolBar.add(new javax.swing.JSeparator(), position + 2);
    }

    public void setSaveAction(ActionListener saveAction) {
        saveButton = new JButton();
        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/bined/autopsy/resources/icons/document-save.png")));
        saveButton.setToolTipText("Save current file");
        saveButton.setEnabled(false);
        saveButton.addActionListener((evt) -> {
            saveAction.actionPerformed(new ActionEvent(BinEdToolbarPanel.this, 0, ""));
        });
        controlToolBar.add(saveButton, 0);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (codeTypeDropDown != null) {
            codeTypeDropDown.updateUI();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlToolBar = new javax.swing.JToolBar();
        refreshButton = new javax.swing.JButton();
        goToButton = new javax.swing.JButton();
        separator1 = new javax.swing.JToolBar.Separator();
        showNonprintablesToggleButton = new javax.swing.JToggleButton();
        codeColorizationToggleButton = new javax.swing.JToggleButton();
        separator2 = new javax.swing.JToolBar.Separator();

        controlToolBar.setBorder(null);
        controlToolBar.setRollover(true);

        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/bined/autopsy/resources/icons/arrow_refresh.png"))); // NOI18N
        refreshButton.setToolTipText("Refresh content");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(refreshButton);

        goToButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/bined/autopsy/resources/icons/bullet_go.png"))); // NOI18N
        goToButton.setToolTipText(componentResourceBundle.getString("goToPositionAction.shortDescription"));
        goToButton.setFocusable(false);
        goToButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goToButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        goToButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(goToButton);
        controlToolBar.add(separator1);

        showNonprintablesToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(componentResourceBundle.getString("toggleNonprintablesAction.smallIcon"))));
        showNonprintablesToggleButton.setToolTipText(componentResourceBundle.getString("viewNonprintablesAction.shortDescription"));
        showNonprintablesToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNonprintablesToggleButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(showNonprintablesToggleButton);

        codeColorizationToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/bined/autopsy/resources/icons/color_swatch.png")));
        codeColorizationToggleButton.setToolTipText("Toggle colorization of the hex codes");
        codeColorizationToggleButton.setFocusable(false);
        codeColorizationToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        codeColorizationToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        codeColorizationToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeColorizationToggleButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(codeColorizationToggleButton);
        controlToolBar.add(separator2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(controlToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 285, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(controlToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void showNonprintablesToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNonprintablesToggleButtonActionPerformed
        control.setShowNonprintables(showNonprintablesToggleButton.isSelected());
    }//GEN-LAST:event_showNonprintablesToggleButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        control.refresh();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void codeColorizationToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeColorizationToggleButtonActionPerformed
        control.setCodeColorization(codeColorizationToggleButton.isSelected());
    }//GEN-LAST:event_codeColorizationToggleButtonActionPerformed

    private void goToButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToButtonActionPerformed
        control.goToPosition();
    }//GEN-LAST:event_goToButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton codeColorizationToggleButton;
    private javax.swing.JToolBar controlToolBar;
    private javax.swing.JButton goToButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JToolBar.Separator separator1;
    private javax.swing.JToolBar.Separator separator2;
    private javax.swing.JToggleButton showNonprintablesToggleButton;
    // End of variables declaration//GEN-END:variables

    @NullMarked
    public interface Control {

        CodeType getCodeType();

        void setCodeType(CodeType codeType);

        boolean isShowNonprintables();

        void setShowNonprintables(boolean showNonprintables);

        boolean isCodeColorization();

        void setCodeColorization(boolean codeColorization);

        void repaint();
        
        void openSettings();
        
        void onlineHelp();

        void refresh();

        void goToPosition();
    }
}
