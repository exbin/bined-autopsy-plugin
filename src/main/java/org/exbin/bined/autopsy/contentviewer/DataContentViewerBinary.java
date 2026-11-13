/*
 * Copyright (C) ExBin Project, https://exbin.org
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import org.exbin.bined.EditMode;
import org.exbin.bined.autopsy.BinEdAutopsyDocking;
import org.exbin.bined.autopsy.Installer;
import org.exbin.bined.autopsy.gui.BinEdFilePanel;
import org.exbin.bined.autopsy.settings.IntegrationOptions;
import org.exbin.bined.jaguif.component.BinEdDataComponent;
import org.exbin.bined.jaguif.component.gui.BinEdComponentPanel;
import org.exbin.bined.jaguif.document.BinaryFileDocument;
import org.exbin.bined.jaguif.document.BinEdFileManager;
import org.exbin.bined.jaguif.document.BinedDocumentModule;
import org.exbin.bined.jaguif.search.BinedSearchModule;
import org.exbin.bined.swing.section.SectCodeArea;
import org.exbin.bined.swing.section.SectionCodeAreaPainter;
import org.exbin.jaguif.App;
import org.exbin.jaguif.context.api.ContextStateManagement;
import org.exbin.jaguif.docking.api.ContextDocking;
import org.exbin.jaguif.frame.api.FrameModuleApi;
import org.exbin.jaguif.options.api.OptionsModuleApi;
import org.exbin.jaguif.options.settings.api.OptionsSettingsManagement;
import org.exbin.jaguif.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.jaguif.options.settings.api.SettingsOptionsProvider;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.jspecify.annotations.NullMarked;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.autopsy.corecomponents.DataContentViewerUtility;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.Content;

/**
 * Binary view of file contents.
 */
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
@ServiceProvider(service = DataContentViewer.class, position = 1)
@NullMarked
public class DataContentViewerBinary extends javax.swing.JPanel implements DataContentViewer {

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(DataContentViewerBinary.class);

    protected Content dataSource;
    protected final BinEdFilePanel filePanel = new BinEdFilePanel();
    protected boolean initialized = false;

    protected Mode mode = Mode.NO_DATA;

    protected static final Logger logger = Logger.getLogger(DataContentViewerBinary.class.getName());

    public DataContentViewerBinary() {
        initComponents();
        init();
        this.resetComponent();
    }

    private void init() {
        filePanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                BinaryFileDocument document = filePanel.getDocument();
                if (document != null) {
                    FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
                    ContextStateManagement contextManager = frameModule.getFrameStateManager();
                    BinEdAutopsyDocking docking = (BinEdAutopsyDocking) contextManager.getActiveState(ContextDocking.class);
                    docking.setActiveDocument(document);
                }
            }
        });
        ActionListener textAreaActionListener = (ActionEvent e) -> {
            JMenuItem jmi = (JMenuItem) e.getSource();
            if (jmi.equals(copyMenuItem)) {
                textArea.copy();
            } else if (jmi.equals(selectAllMenuItem)) {
                textArea.selectAll();
            }
        };
        copyMenuItem.addActionListener(textAreaActionListener);
        selectAllMenuItem.addActionListener(textAreaActionListener);

        textArea.setText(resourceBundle.getString("textArea.noDataText"));
    }

    private void switchMode(Mode mode) {
        if (this.mode != mode) {
            switch (this.mode) {
                case NO_DATA: {
                    remove(textAreaScrollPane);
                    break;
                }
                case DATA: {
                    remove(filePanel);
                    break;
                }
                case ERROR: {
                    remove(textAreaScrollPane);
                    break;
                }
            }
            this.mode = mode;
            switch (mode) {
                case NO_DATA: {
                    textArea.setText(resourceBundle.getString("textArea.noDataText"));
                    add(textAreaScrollPane, BorderLayout.CENTER);
                    break;
                }
                case DATA: {
                    add(filePanel, BorderLayout.CENTER);
                    break;
                }
                case ERROR: {
                    add(textAreaScrollPane, BorderLayout.CENTER);
                    break;
                }
            }
            revalidate();
            repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textAreaPopupMenu = new javax.swing.JPopupMenu();
        copyMenuItem = new javax.swing.JMenuItem();
        selectAllMenuItem = new javax.swing.JMenuItem();
        textAreaScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();

        copyMenuItem.setText(resourceBundle.getString("copyMenuItem.text")); // NOI18N
        textAreaPopupMenu.add(copyMenuItem);

        selectAllMenuItem.setText(resourceBundle.getString("selectAllMenuItem.text")); // NOI18N
        textAreaPopupMenu.add(selectAllMenuItem);

        setPreferredSize(new java.awt.Dimension(100, 58));
        setLayout(new java.awt.BorderLayout());

        textAreaScrollPane.setPreferredSize(new java.awt.Dimension(300, 33));

        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        textArea.setTabSize(0);
        textArea.setComponentPopupMenu(textAreaPopupMenu);
        textAreaScrollPane.setViewportView(textArea);

        add(textAreaScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem selectAllMenuItem;
    private javax.swing.JTextArea textArea;
    private javax.swing.JPopupMenu textAreaPopupMenu;
    private javax.swing.JScrollPane textAreaScrollPane;
    // End of variables declaration//GEN-END:variables

    @Messages({
        "DataContentViewerBinary_loading_text=Loading binary from file..."
    })

    @Override
    public void setNode(Node selectedNode) {
        resetComponent();

        if ((selectedNode == null)) {
            return;
        }

        Content content = DataContentViewerUtility.getDefaultContent(selectedNode);
        if (content == null) {
            switchMode(Mode.NO_DATA);
            return;
        }

        dataSource = content;
        BinaryFileDocument fileDocument;
        if (!initialized) {
            fileDocument = DataContentViewerBinary.createBinaryFileDocument();
            BinedDocumentModule binedDocumentModule = App.getModule(BinedDocumentModule.class);
            BinEdFileManager fileManager = binedDocumentModule.getFileManager();
            BinEdDataComponent dataComponent = fileDocument.getDataComponent();
            fileManager.initDataComponent(dataComponent);
            fileManager.initCommandHandler(dataComponent);
            BinedSearchModule searchModule = App.getModule(BinedSearchModule.class);
            dataComponent.setSearchController(searchModule.createBinarySearchController(dataComponent));

            SectCodeArea codeArea = (SectCodeArea) fileDocument.getCodeArea();
            codeArea.setEditMode(EditMode.READ_ONLY);
            SectionCodeAreaPainter oldPainter = (SectionCodeAreaPainter) codeArea.getPainter();
            SectionCodeAreaPainter painter = new SectionCodeAreaPainter(codeArea) {
                @Override
                public void paintComponent(Graphics g) {
                    try {
                        super.paintComponent(g);
                    } catch (ContentBinaryData.TskReadException ex) {
                        String message = String.format(resourceBundle.getString("textArea.errorText"), ex.getPosition(), ex.getPosition() + ex.getLength());
                        if (mode == Mode.ERROR) {
                            textArea.append("\n" + message);
                        } else {
                            textArea.setText(message);
                            switchMode(Mode.ERROR);
                        }
                    } catch (Exception ex) {
                        String message = String.format(resourceBundle.getString("textArea.exceptionText"), ex.getMessage());
                        if (mode == Mode.ERROR) {
                            textArea.append("\n" + message);
                        } else {
                            textArea.setText(message);
                            switchMode(Mode.ERROR);
                        }
                    }
                }
            };
            painter.setColorAssessor(oldPainter.getColorAssessor());
            painter.setCharAssessor(oldPainter.getCharAssessor());
            codeArea.setPainter(painter);
            filePanel.setDocument(fileDocument);

            OptionsSettingsModuleApi optionsSettingsModule = App.getModule(OptionsSettingsModuleApi.class);
            OptionsSettingsManagement settingsManager = optionsSettingsModule.getMainSettingsManager();
            SettingsOptionsProvider settingsOptionsProvider = settingsManager.getSettingsOptionsProvider();
            fileDocument.applySettings(settingsOptionsProvider);

            OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
            filePanel.loadFromOptions(optionsModule.getAppOptions());

            initialized = true;
        } else {
            fileDocument = filePanel.getDocument();
        }

        fileDocument.loadFrom(new DataContentDocumentSource(dataSource));
        ((SectCodeArea) filePanel.getCodeArea()).setCaretPosition(0);

        switchMode(Mode.DATA);
    }

    @Override
    public String getTitle() {
        return resourceBundle.getString("title");
    }

    @Override
    public String getToolTip() {
        return resourceBundle.getString("toolTip");
    }

    @Override
    public DataContentViewer createInstance() {
        return new DataContentViewerBinary();
    }

    @Override
    public void resetComponent() {
        // clear / reset the fields
        this.dataSource = null;
        BinaryFileDocument document = filePanel.getDocument();
        if (document != null) {
            document.clearFile();
        }
        switchMode(Mode.NO_DATA);
    }

    @Override
    public boolean isSupported(Node node) {
        if (node == null) {
            return false;
        }
        Content content = DataContentViewerUtility.getDefaultContent(node);
        return content != null && !(content instanceof BlackboardArtifact) && content.getSize() > 0;
    }

    @Override
    public int isPreferred(Node node) {
        return 1;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    public static BinaryFileDocument createBinaryFileDocument() {
        return new BinaryFileDocument(new BinEdDataComponent(new BinEdComponentPanel()));
    }

    public static void registerIntegration() {
        Installer.addIntegrationOptionsListener(new Installer.IntegrationOptionsListener() {
            @Override
            public void integrationInit(IntegrationOptions integrationOptions) {
                if (integrationOptions.isRegisterFileMenuOpenAsBinary()) {
                    install();
                } else {
                    uninstall();
                }
            }

            @Override
            public void uninstallIntegration() {
                uninstall();
            }
        });
    }

    public static void install() {
        // TODO
    }

    public static void uninstall() {
        // TODO
    }

    private enum Mode {
        NO_DATA,
        DATA,
        ERROR
    }
}
