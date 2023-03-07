/*
 * Copyright (C) ExBin Project
 *
 * Copyright 2011-2019 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.bined.autopsy.contentviewer;

import java.awt.event.ActionEvent;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.sleuthkit.autopsy.coreutils.Logger;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.exbin.bined.CodeType;
import org.exbin.bined.highlight.swing.extended.ExtendedHighlightNonAsciiCodeAreaPainter;
import org.exbin.bined.swing.extended.ExtCodeArea;
import org.exbin.framework.action.gui.DropDownButton;
import org.exbin.framework.bined.preferences.BinaryEditorPreferences;
import org.exbin.framework.utils.LanguageUtils;

/**
 * Toolbar panel for binary data content viewer.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ViewerToolbarPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(ViewerToolbarPanel.class);

    private final BinaryEditorPreferences preferences;
    private final ExtCodeArea codeArea;
    private final AbstractAction refreshAction;
    private final AbstractAction goToPositionAction;
    private final AbstractAction optionsAction;
    private final AbstractAction onlineHelpAction;

    private final AbstractAction cycleCodeTypesAction;
    private final JRadioButtonMenuItem binaryCodeTypeAction;
    private final JRadioButtonMenuItem octalCodeTypeAction;
    private final JRadioButtonMenuItem decimalCodeTypeAction;
    private final JRadioButtonMenuItem hexadecimalCodeTypeAction;
    private final ButtonGroup codeTypeButtonGroup;
    private DropDownButton codeTypeDropDown;

    private static final Logger logger = Logger.getLogger(ViewerToolbarPanel.class.getName());

    public ViewerToolbarPanel(BinaryEditorPreferences preferences, ExtCodeArea codeArea, AbstractAction refreshAction, AbstractAction goToPositionAction, AbstractAction optionsAction, AbstractAction onlineHelpAction) {
        this.preferences = preferences;
        this.codeArea = codeArea;
        this.refreshAction = refreshAction;
        this.goToPositionAction = goToPositionAction;
        this.optionsAction = optionsAction;
        this.onlineHelpAction = onlineHelpAction;

        codeTypeButtonGroup = new ButtonGroup();
        binaryCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(resourceBundle.getString("codeType.binary")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setCodeType(CodeType.BINARY);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(binaryCodeTypeAction);
        octalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(resourceBundle.getString("codeType.octal")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setCodeType(CodeType.OCTAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(octalCodeTypeAction);
        decimalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(resourceBundle.getString("codeType.decimal")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setCodeType(CodeType.DECIMAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(decimalCodeTypeAction);
        hexadecimalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(resourceBundle.getString("codeType.hexadecimal")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setCodeType(CodeType.HEXADECIMAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(hexadecimalCodeTypeAction);
        cycleCodeTypesAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codeTypePos = codeArea.getCodeType().ordinal();
                CodeType[] values = CodeType.values();
                CodeType next = codeTypePos + 1 >= values.length ? values[0] : values[codeTypePos + 1];
                codeArea.setCodeType(next);
                updateCycleButtonState();
            }
        };

        initComponents();
        init();
    }

    private void init() {
        cycleCodeTypesAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("cycleCodeTypesAction.text"));
        JPopupMenu cycleCodeTypesPopupMenu = new JPopupMenu();
        cycleCodeTypesPopupMenu.add(binaryCodeTypeAction);
        cycleCodeTypesPopupMenu.add(octalCodeTypeAction);
        cycleCodeTypesPopupMenu.add(decimalCodeTypeAction);
        cycleCodeTypesPopupMenu.add(hexadecimalCodeTypeAction);
        codeTypeDropDown = new DropDownButton(cycleCodeTypesAction, cycleCodeTypesPopupMenu);
        updateCycleButtonState();
        controlToolBar.add(codeTypeDropDown, 0);

        codeColorizationToggleButton.setSelected(((ExtendedHighlightNonAsciiCodeAreaPainter) codeArea.getPainter()).isNonAsciiHighlightingEnabled());

        controlToolBar.addSeparator();
        JButton optionsButton = new JButton();
        optionsButton.setAction(optionsAction);
        optionsButton.setToolTipText("Options");
        optionsButton.setIcon(new ImageIcon(getClass().getResource("/org/exbin/framework/options/gui/resources/icons/Preferences16.gif")));
        controlToolBar.add(optionsButton);

        JButton onlineHelpButton = new JButton();
        onlineHelpButton.setAction(onlineHelpAction);
        onlineHelpButton.setToolTipText("Online Help");
        onlineHelpButton.setIcon(new ImageIcon(getClass().getResource("/org/exbin/framework/bined/resources/icons/open_icon_library/icons/png/16x16/actions/help.png")));
        controlToolBar.add(onlineHelpButton);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlToolBar = new javax.swing.JToolBar();
        refreshButton = new javax.swing.JButton();
        codeColorizationToggleButton = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        goToButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(100, 58));

        controlToolBar.setBorderPainted(false);
        controlToolBar.setFocusable(false);

        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/bined/autopsy/resources/icons/arrow_refresh.png"))); // NOI18N
        refreshButton.setToolTipText(resourceBundle.getString("refreshButton.toolTipText"));
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(refreshButton);

        codeColorizationToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/bined/autopsy/resources/icons/color_swatch.png"))); // NOI18N
        codeColorizationToggleButton.setToolTipText(resourceBundle.getString("codeColorizationToggleButton.toolTipText"));
        codeColorizationToggleButton.setFocusable(false);
        codeColorizationToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        codeColorizationToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        codeColorizationToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeColorizationToggleButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(codeColorizationToggleButton);
        controlToolBar.add(jSeparator1);

        goToButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/bined/autopsy/resources/icons/bullet_go.png"))); // NOI18N
        goToButton.setToolTipText(resourceBundle.getString("goToButton.toolTipText"));
        goToButton.setFocusable(false);
        goToButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goToButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        goToButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(goToButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void updateCycleButtonState() {
        CodeType codeType = codeArea.getCodeType();
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
        updateUnprintables();
    }

    public void loadFromPreferences() {
        codeArea.setCodeType(preferences.getCodeAreaPreferences().getCodeType());
        updateCycleButtonState();
        updateUnprintables();
    }

    public void updateUnprintables() {
        // TODO showUnprintablesToggleButton.setSelected(codeArea.isShowUnprintables());
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (codeTypeDropDown != null) {
            codeTypeDropDown.updateUI();
        }
    }

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        refreshAction.actionPerformed(evt);
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void codeColorizationToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeColorizationToggleButtonActionPerformed
        ((ExtendedHighlightNonAsciiCodeAreaPainter) codeArea.getPainter()).setNonAsciiHighlightingEnabled(codeColorizationToggleButton.isSelected());
        codeArea.repaint();
    }//GEN-LAST:event_codeColorizationToggleButtonActionPerformed

    private void goToButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToButtonActionPerformed
        goToPositionAction.actionPerformed(evt);
    }//GEN-LAST:event_goToButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton codeColorizationToggleButton;
    private javax.swing.JToolBar controlToolBar;
    private javax.swing.JButton goToButton;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables

}
