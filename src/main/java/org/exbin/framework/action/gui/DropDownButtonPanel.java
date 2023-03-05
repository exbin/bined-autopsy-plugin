/*
 * Copyright (C) ExBin Project
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
package org.exbin.framework.action.gui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicArrowButton;
import org.exbin.framework.utils.WindowUtils;

/**
 * Drop down button panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class DropDownButtonPanel extends javax.swing.JPanel {

    public DropDownButtonPanel() {
        initComponents();

        actionLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passMouseEventToParent(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                passMouseEventToParent(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                passMouseEventToParent(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                passMouseEventToParent(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                passMouseEventToParent(e);
            }

            private void passMouseEventToParent(MouseEvent me) {
                Component parent = DropDownButtonPanel.this.getParent();
                //dispatch it to the parent component
                parent.dispatchEvent(SwingUtilities.convertMouseEvent(actionLabel, me, parent));
            }
        });
    }

    @Nonnull
    public JLabel getActionButton() {
        return actionLabel;
    }

    @Nonnull
    public JButton getMenuButton() {
        return menuButton;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuButton = new BasicArrowButton(SwingConstants.SOUTH);
        actionLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setFocusable(false);
        setOpaque(false);

        menuButton.setBorderPainted(false);
        menuButton.setFocusable(false);
        menuButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        menuButton.setMaximumSize(new java.awt.Dimension(15, 100));
        menuButton.setOpaque(false);

        actionLabel.setFocusable(false);
        actionLabel.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(actionLabel)
                .addGap(0, 0, 0)
                .addComponent(menuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(actionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new DropDownButtonPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actionLabel;
    private javax.swing.JButton menuButton;
    // End of variables declaration//GEN-END:variables
}
