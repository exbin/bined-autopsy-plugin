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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.annotation.Nonnull;
import org.sleuthkit.autopsy.coreutils.Logger;
import javax.swing.JMenuItem;
import org.exbin.bined.EditMode;
import org.exbin.bined.autopsy.gui.BinEdComponentFileApi;
import org.exbin.bined.autopsy.gui.BinEdComponentPanel;
import org.exbin.bined.highlight.swing.extended.ExtendedHighlightNonAsciiCodeAreaPainter;
import org.exbin.bined.swing.extended.ExtCodeArea;
import org.exbin.framework.bined.FileHandlingMode;
import org.exbin.framework.utils.LanguageUtils;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.autopsy.corecomponents.DataContentViewerUtility;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.Content;

/**
 * Binary view of file contents.
 *
 * @author ExBin Project (https://exbin.org)
 */
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
@ServiceProvider(service = DataContentViewer.class, position = 1)
public class DataContentViewerBinary extends javax.swing.JPanel implements DataContentViewer {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(DataContentViewerBinary.class);

    private Content dataSource;
    private BinEdComponentPanel componentPanel = new BinEdComponentPanel();

    private Mode mode = Mode.NO_DATA;

    private static final Logger logger = Logger.getLogger(DataContentViewerBinary.class.getName());

    public DataContentViewerBinary() {
        initComponents();
        init();
        this.resetComponent();
    }

    private void init() {
        componentPanel.setFileApi(new BinEdComponentFileApi() {
            @Override
            public boolean isSaveSupported() {
                return false;
            }

            @Override
            public void saveDocument() {
                throw new IllegalStateException();
            }

            @Override
            public void switchFileHandlingMode(FileHandlingMode newHandlingMode) {
                // ignore
            }

            @Override
            public void closeData() {
                // ignore
            }
        });
        ExtCodeArea codeArea = componentPanel.getCodeArea();
        codeArea.setEditMode(EditMode.READ_ONLY);
        codeArea.setPainter(new ExtendedHighlightNonAsciiCodeAreaPainter(codeArea) {
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
                    remove(componentPanel);
                    break;
                }
                case ERROR: {
//                    remove(toolbarPanel);
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
                    break;
                }
                case ERROR: {
//                    add(toolbarPanel, BorderLayout.NORTH);
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
        componentPanel.getCodeArea().setCaretPosition(0);
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

    @Nonnull
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
