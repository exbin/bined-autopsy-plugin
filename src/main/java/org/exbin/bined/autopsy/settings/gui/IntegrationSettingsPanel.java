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
package org.exbin.bined.autopsy.settings.gui;

import java.awt.Component;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.exbin.bined.autopsy.settings.IntegrationOptions;
import org.exbin.jaguif.App;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.options.settings.api.SettingsComponent;
import org.exbin.jaguif.options.settings.api.SettingsModifiedListener;
import org.exbin.jaguif.options.settings.api.SettingsOptionsProvider;
import org.exbin.jaguif.utils.WindowUtils;
import org.exbin.jaguif.ui.LanguageRecord;
import org.exbin.jaguif.utils.TestApplication;

/**
 * Integration preference parameters panel.
 */
@NullMarked
public class IntegrationSettingsPanel extends javax.swing.JPanel implements SettingsComponent {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(IntegrationSettingsPanel.class);
    private SettingsModifiedListener settingsModifiedListener;
    private String defaultLocaleName = "";

    public IntegrationSettingsPanel() {
        initComponents();
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public void saveToOptions(SettingsOptionsProvider settingsOptionsProvider) {
        IntegrationOptions options = settingsOptionsProvider.getSettingsOptions(IntegrationOptions.class);
        options.setLanguageLocale(((LanguageRecord) languageComboBox.getSelectedItem()).getLocale());
        options.setIconSet((String) iconSetComboBox.getSelectedItem());
        options.setRegisterFileMenuOpenAsBinary(openFileAsBinaryCheckBox.isSelected());
        options.setRegisterDataContentTab(dataContentBinaryCheckBox.isSelected());
    }

    @Override
    public void loadFromOptions(SettingsOptionsProvider settingsOptionsProvider) {
        IntegrationOptions options = settingsOptionsProvider.getSettingsOptions(IntegrationOptions.class);
            Locale languageLocale = options.getLanguageLocale();
        ComboBoxModel<LanguageRecord> languageComboBoxModel = languageComboBox.getModel();
        for (int i = 0; i < languageComboBoxModel.getSize(); i++) {
            LanguageRecord languageRecord = languageComboBoxModel.getElementAt(i);
            if (languageLocale.equals(languageRecord.getLocale())) {
                languageComboBox.setSelectedIndex(i);
                break;
            }
        }
        iconSetComboBox.setSelectedIndex(findMatchingElement(iconSetComboBox.getModel(), options.getIconSet()));
        openFileAsBinaryCheckBox.setSelected(options.isRegisterFileMenuOpenAsBinary());
        dataContentBinaryCheckBox.setSelected(options.isRegisterDataContentTab());
    }

    private static int findMatchingElement(ComboBoxModel<String> model, String value) {
        for (int i = 0; i < model.getSize(); i++) {
            if (value.equals(model.getElementAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public void setLanguageLocales(List<LanguageRecord> languageLocales) {
        DefaultComboBoxModel<LanguageRecord> languageComboBoxModel = new DefaultComboBoxModel<>();
        languageLocales.forEach(languageComboBoxModel::addElement);
        languageComboBox.setModel(languageComboBoxModel);
        languageComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                DefaultListCellRenderer renderer = (DefaultListCellRenderer) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                LanguageRecord record = (LanguageRecord) value;
                String languageText = record.getText();
                if ("".equals(languageText)) {
                    languageText = defaultLocaleName;
                }
                renderer.setText(languageText);
                ImageIcon flag = record.getFlag().orElse(null);
                if (flag != null) {
                    renderer.setIcon(flag);
                }
                return renderer;
            }
        });
    }

    public void setDefaultLocaleName(String defaultLocaleName) {
        this.defaultLocaleName = defaultLocaleName;
    }

    public void setIconSets(List<String> iconSetKeys, List<String> iconSetNames) {
        DefaultComboBoxModel<String> iconSetComboBoxModel = new DefaultComboBoxModel<>();
        iconSetKeys.forEach((iconSetKey) -> {
            iconSetComboBoxModel.addElement(iconSetKey);
        });
        iconSetComboBox.setModel(iconSetComboBoxModel);
        iconSetComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (index >= 0) {
                    return super.getListCellRendererComponent(list, iconSetNames.get(index), index, isSelected, cellHasFocus);
                }
                int selectedIndex = iconSetComboBox.getSelectedIndex();
                return super.getListCellRendererComponent(list, selectedIndex >= 0 ? iconSetNames.get(selectedIndex) : value, index, isSelected, cellHasFocus);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        languageLabel = new javax.swing.JLabel();
        languageComboBox = new javax.swing.JComboBox<>();
        iconSetLabel = new javax.swing.JLabel();
        iconSetComboBox = new javax.swing.JComboBox<>();
        openFileAsBinaryCheckBox = new javax.swing.JCheckBox();
        dataContentBinaryCheckBox = new javax.swing.JCheckBox();

        languageLabel.setText(resourceBundle.getString("languageLabel.text") + " *"); // NOI18N

        languageComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                languageComboBoxItemStateChanged(evt);
            }
        });

        iconSetLabel.setText(resourceBundle.getString("iconSetLabel.text")); // NOI18N

        iconSetComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                iconSetComboBoxItemStateChanged(evt);
            }
        });

        openFileAsBinaryCheckBox.setSelected(true);
        openFileAsBinaryCheckBox.setText(resourceBundle.getString("openFileAsBinaryCheckBox.text")); // NOI18N
        openFileAsBinaryCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                openFileAsBinaryCheckBoxStateChanged(evt);
            }
        });

        dataContentBinaryCheckBox.setSelected(true);
        dataContentBinaryCheckBox.setText(resourceBundle.getString("dataContentBinaryCheckBox.text")); // NOI18N
        dataContentBinaryCheckBox.setEnabled(false);
        dataContentBinaryCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dataContentBinaryCheckBoxStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(languageLabel)
                            .addComponent(iconSetLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(iconSetComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataContentBinaryCheckBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(languageComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(openFileAsBinaryCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(languageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(languageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iconSetLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iconSetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(openFileAsBinaryCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataContentBinaryCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void openFileAsBinaryCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_openFileAsBinaryCheckBoxStateChanged
        notifyModified();
    }//GEN-LAST:event_openFileAsBinaryCheckBoxStateChanged

    private void dataContentBinaryCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dataContentBinaryCheckBoxStateChanged
        notifyModified();
    }//GEN-LAST:event_dataContentBinaryCheckBoxStateChanged

    private void languageComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_languageComboBoxItemStateChanged
        notifyModified();
    }//GEN-LAST:event_languageComboBoxItemStateChanged

    private void iconSetComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_iconSetComboBoxItemStateChanged
        notifyModified();
    }//GEN-LAST:event_iconSetComboBoxItemStateChanged

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication.run(() -> WindowUtils.invokeWindow(new IntegrationSettingsPanel()));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox dataContentBinaryCheckBox;
    private javax.swing.JComboBox<String> iconSetComboBox;
    private javax.swing.JLabel iconSetLabel;
    private javax.swing.JComboBox<LanguageRecord> languageComboBox;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JCheckBox openFileAsBinaryCheckBox;
    // End of variables declaration//GEN-END:variables

    private void notifyModified() {
        if (settingsModifiedListener != null) {
            settingsModifiedListener.notifyModified();
        }
    }

    @Override
    public void setSettingsModifiedListener(SettingsModifiedListener listener) {
        settingsModifiedListener = listener;
    }
}
