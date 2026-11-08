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
import org.sleuthkit.autopsy.coreutils.Logger;
import javax.swing.JMenuItem;
import org.exbin.bined.CodeAreaUtils;
import org.exbin.bined.CodeType;
import org.exbin.bined.EditMode;
import org.exbin.bined.autopsy.BinEdAutopsyDocking;
import org.exbin.bined.autopsy.gui.BinEdToolbarPanel;
import org.exbin.bined.highlight.swing.NonprintablesCodeAreaAssessor;
import org.exbin.bined.highlight.swing.NonAsciiCodeAreaColorAssessor;
import org.exbin.bined.jaguif.component.BinEdDataComponent;
import org.exbin.bined.jaguif.component.BinedComponentModule;
import org.exbin.bined.jaguif.component.gui.BinEdComponentPanel;
import org.exbin.bined.jaguif.document.BinEdFileManager;
import org.exbin.bined.jaguif.document.BinedDocumentModule;
import org.exbin.bined.jaguif.viewer.BinedViewerModule;
import org.exbin.bined.jaguif.viewer.settings.BinaryEncodingSettingsApplier;
import org.exbin.bined.jaguif.viewer.status.gui.BinaryDataSizeComponent;
import org.exbin.bined.swing.CodeAreaSwingUtils;
import org.exbin.bined.swing.capability.ColorAssessorPainterCapable;
import org.exbin.bined.swing.section.SectCodeArea;
import org.exbin.bined.swing.section.SectionCodeAreaPainter;
import org.exbin.jaguif.App;
import org.exbin.jaguif.context.ContextStateManager;
import org.exbin.jaguif.context.api.ContextStateManagement;
import org.exbin.jaguif.context.api.ContextChangeListener;
import org.exbin.jaguif.context.api.ContextComponent;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextMonitoringRegistration;
import org.exbin.jaguif.context.api.ContextMonitoringManagement;
import org.exbin.jaguif.context.api.StateUpdateType;
import org.exbin.jaguif.docking.api.ContextDocking;
import org.exbin.jaguif.document.api.ContextDocument;
import org.exbin.jaguif.frame.api.FrameModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.options.api.OptionsModuleApi;
import org.exbin.jaguif.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.jaguif.search.api.ContextSearch;
import org.exbin.jaguif.statusbar.api.StatusBar;
import org.exbin.jaguif.statusbar.api.StatusBarComponent;
import org.exbin.jaguif.statusbar.api.StatusBarModuleApi;
import org.exbin.jaguif.text.encoding.ContextEncoding;
import org.exbin.jaguif.utils.DesktopUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
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
    protected final BinEdComponentPanel componentPanel;
    protected final BinEdDataComponent dataComponent;
    protected BinEdToolbarPanel toolbarPanel = new BinEdToolbarPanel();
    protected ContextStateManagement statusContextManager;
    protected ContextChangeListener contextChangeListener;
    protected StatusBar statusBar;

    protected Mode mode = Mode.NO_DATA;

    protected static final Logger logger = Logger.getLogger(DataContentViewerBinary.class.getName());

    public DataContentViewerBinary() {
        componentPanel = new BinEdComponentPanel();
        dataComponent = new BinEdDataComponent(componentPanel);
        BinedDocumentModule binedDocumentModule = App.getModule(BinedDocumentModule.class);
        BinedViewerModule binedViewerModule = App.getModule(BinedViewerModule.class);
        BinEdFileManager fileManager = binedDocumentModule.getFileManager();
        fileManager.initDataComponent(dataComponent);

        OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);

        SectCodeArea codeArea = (SectCodeArea) dataComponent.getCodeArea();
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

        toolbarPanel.setTargetComponent(componentPanel);
        toolbarPanel.setCodeAreaControl(new BinEdToolbarPanel.Control() {
            @Override
            public CodeType getCodeType() {
                return codeArea.getCodeType();
            }

            @Override
            public void setCodeType(CodeType codeType) {
                codeArea.setCodeType(codeType);
            }

            @Override
            public boolean isShowNonprintables() {
                ColorAssessorPainterCapable painter = (ColorAssessorPainterCapable) codeArea.getPainter();
                NonprintablesCodeAreaAssessor nonprintablesCodeAreaAssessor = CodeAreaSwingUtils.findColorAssessor(painter, NonprintablesCodeAreaAssessor.class);
                return CodeAreaUtils.requireNonNull(nonprintablesCodeAreaAssessor).isShowNonprintables();
            }

            @Override
            public void setShowNonprintables(boolean showNonprintables) {
                ColorAssessorPainterCapable painter = (ColorAssessorPainterCapable) codeArea.getPainter();
                NonprintablesCodeAreaAssessor nonprintablesCodeAreaAssessor = CodeAreaSwingUtils.findColorAssessor(painter, NonprintablesCodeAreaAssessor.class);
                CodeAreaUtils.requireNonNull(nonprintablesCodeAreaAssessor).setShowNonprintables(showNonprintables);
            }

            @Override
            public void repaint() {
                codeArea.repaint();
            }

            public boolean isCodeColorization() {
                ColorAssessorPainterCapable painter = (ColorAssessorPainterCapable) codeArea.getPainter();
                NonAsciiCodeAreaColorAssessor assessor = CodeAreaSwingUtils.findColorAssessor(painter, NonAsciiCodeAreaColorAssessor.class);
                return CodeAreaUtils.requireNonNull(assessor).isNonAsciiHighlightingEnabled();
            }

            public void setCodeColorization(boolean codeColorization) {
                ColorAssessorPainterCapable painter = (ColorAssessorPainterCapable) codeArea.getPainter();
                NonAsciiCodeAreaColorAssessor assessor = CodeAreaSwingUtils.findColorAssessor(painter, NonAsciiCodeAreaColorAssessor.class);
                CodeAreaUtils.requireNonNull(assessor).setNonAsciiHighlightingEnabled(codeColorization);
            }

            public void openSettings() {
                throw new UnsupportedOperationException();
            }

            public void onlineHelp() {
                LanguageModuleApi languageModuleApi = App.getModule(LanguageModuleApi.class);
                DesktopUtils.openDesktopURL(languageModuleApi.getAppBundle().getString("online_help_url"));
            }

            public void refresh() {
                throw new UnsupportedOperationException();
            }

            public void goToPosition() {
                org.exbin.bined.jaguif.component.action.GoToPositionAction action = new org.exbin.bined.jaguif.component.action.GoToPositionAction();
                action.setCodeArea(codeArea);
                action.actionPerformed();
            }
        });

        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        StatusBarModuleApi statusBarModule = App.getModule(StatusBarModuleApi.class);
        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        ContextStateManagement contextManager = frameModule.getFrameStateManager();
        statusContextManager = contextModule.createChildStateManager(contextManager);
        contextChangeListener = new ContextChangeListener() {
            @Override
            public <T> void notifyStateChanged(Class<T> stateClass, @Nullable T activeState) {
                if (dataComponent == null) {
                    return;
                }

                if (stateClass == ContextComponent.class && activeState != dataComponent) {
                    return;
                }

                if (stateClass == ContextEncoding.class && activeState != dataComponent) {
                    return;
                }

                statusContextManager.changeActiveState(stateClass, activeState);
            }

            @Override
            public <T> void notifyStateUpdated(Class<T> stateClass, T activeState, StateUpdateType stateUpdateType) {
                if (dataComponent == null) {
                    return;
                }

                if (stateClass == ContextComponent.class && activeState != dataComponent) {
                    return;
                }

                if (stateClass == ContextEncoding.class && activeState != dataComponent) {
                    return;
                }

                statusContextManager.updateActiveState(stateClass, activeState, stateUpdateType);
            }
        };
        contextManager.addChangeListener(contextChangeListener);
        ContextMonitoringManagement statusUpdateManager = contextModule.createMonitoringManager(statusContextManager);
        ContextMonitoringRegistration statusContextRegistrator = contextModule.createMonitoringRegistrator(statusUpdateManager, statusContextManager);
        statusBar = statusBarModule.createStatusBar(BinedComponentModule.BINARY_STATUS_BAR_ID, statusContextRegistrator);

        initComponents();
        init();
        this.resetComponent();
    }

    private void init() {
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

    public void detach() {
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ContextStateManagement contextManager = frameModule.getFrameStateManager();
        contextManager.removeChangeListener(contextChangeListener);
    }

    private void switchMode(Mode mode) {
        if (this.mode != mode) {
            switch (this.mode) {
                case NO_DATA: {
                    remove(textAreaScrollPane);
                    break;
                }
                case DATA: {
                    remove(toolbarPanel);
                    remove(componentPanel);
                    remove(statusBar.getComponent());

                    statusContextManager.changeActiveState(ContextComponent.class, null);
                    statusContextManager.changeActiveState(ContextEncoding.class, null);
                    statusContextManager.changeActiveState(ContextSearch.class, null);
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
                    add(componentPanel, BorderLayout.CENTER);
                    add(toolbarPanel, BorderLayout.NORTH);
                    add(statusBar.getComponent(), BorderLayout.SOUTH);

                    statusContextManager.changeActiveState(ContextComponent.class, dataComponent);
                    statusContextManager.changeActiveState(ContextEncoding.class, dataComponent);
                    statusContextManager.changeActiveState(ContextSearch.class, dataComponent.getSearchController().orElse(null));
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
        dataComponent.getCodeArea().setCaretPosition(0);
        componentPanel.setContentData(new ContentBinaryData(dataSource));
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
        componentPanel.setContentData(null);
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

    private enum Mode {
        NO_DATA,
        DATA,
        ERROR
    }
}
