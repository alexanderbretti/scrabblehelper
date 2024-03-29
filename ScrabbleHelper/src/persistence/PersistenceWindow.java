/*
 * PersistenceWindow.java
 *
 * Created on January 4, 2008, 7:23 PM
 */

package persistence;

import gui.ScrabbleWindow;
import java.io.File;

/**
 *
 * @author  Nick
 */
public class PersistenceWindow extends javax.swing.JFrame {
    ScrabbleWindow sw;
    
    /** Creates new form PersistenceWindow */
    public PersistenceWindow(ScrabbleWindow sw) {
        this.sw = sw;
        initComponents();
        if (savedComboBox.getModel().getSize() > 0) {
            savedComboBox.setSelectedIndex(0);
        }
    }
    
    public void save(String saveName) {
        
    }
    
    public void load(String saveName) {
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        savedComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        loadJButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        saveAsField = new javax.swing.JTextField();
        saveJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        savedComboBox.setModel(new BoardFileListModel());

        jLabel1.setText("Saved Boards:");

        loadJButton.setText("Load");
        loadJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadJButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Save As:");

        saveAsField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsFieldActionPerformed(evt);
            }
        });

        saveJButton.setText("Save");
        saveJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(savedComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadJButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(45, 45, 45)
                        .addComponent(saveAsField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveJButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(savedComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadJButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(saveJButton)
                    .addComponent(saveAsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadJButtonActionPerformed
        File f = (File)((BoardFileListModel)savedComboBox.getModel()).getSelectedItem();
        SavedBoard sb = BoardLoaderAndSaver.loadBoard(f);
        sw.applyBoard(sb);
}//GEN-LAST:event_loadJButtonActionPerformed

    private void saveJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJButtonActionPerformed
        SavedBoard sb = sw.getBoard();
        BoardLoaderAndSaver.saveBoard(saveAsField.getText(), sb);
}//GEN-LAST:event_saveJButtonActionPerformed

    private void saveAsFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsFieldActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_saveAsFieldActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loadJButton;
    private javax.swing.JTextField saveAsField;
    private javax.swing.JButton saveJButton;
    private javax.swing.JComboBox savedComboBox;
    // End of variables declaration//GEN-END:variables
    
}
