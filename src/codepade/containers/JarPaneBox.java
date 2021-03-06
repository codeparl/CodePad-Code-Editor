/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade.containers;

import codepade.MainWindow;
import codepade.files.FileList;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;

/**
 *
 * @author HASSAN
 */
public class JarPaneBox extends javax.swing.JPanel {

    /**
     * Creates new form JarPaneBox
     */
    private final MainWindow  mainWindow;
   protected jarPaneBox2 nextPane;
    protected DefaultListModel<String> listModel;
    protected  ArrayList<String> str;
    private final FileList fileList;
    public JarPaneBox(MainWindow  mw) {
        this.mainWindow=  mw;
       
        listModel=  new DefaultListModel<>();
        str =  new ArrayList<>();
        fileList =  new FileList();
        fileList.setFileList(str);
                
        initComponents();
        entryList.setModel(listModel);
    
      //nextPane.setPreviousPane(this);
      
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        destDir = new javax.swing.JButton();
        destText = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        archiveName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        entryList = new javax.swing.JList<>();
        addEntry = new javax.swing.JButton();

        destDir.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(destDir, org.openide.util.NbBundle.getMessage(JarPaneBox.class, "JarPaneBox.destDir.text")); // NOI18N
        destDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destDirActionPerformed(evt);
            }
        });

        destText.setEditable(false);
        destText.setBackground(new java.awt.Color(255, 255, 255));
        destText.setText(org.openide.util.NbBundle.getMessage(JarPaneBox.class, "JarPaneBox.destText.text")); // NOI18N
        destText.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(JarPaneBox.class, "JarPaneBox.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton4, org.openide.util.NbBundle.getMessage(JarPaneBox.class, "JarPaneBox.jButton4.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(JarPaneBox.class, "JarPaneBox.jLabel2.text")); // NOI18N

        archiveName.setText(org.openide.util.NbBundle.getMessage(JarPaneBox.class, "JarPaneBox.archiveName.text")); // NOI18N
        archiveName.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6)));

        entryList.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(JarPaneBox.class, "JarPaneBox.entryList.border.insideBorder.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION))); // NOI18N
        jScrollPane1.setViewportView(entryList);

        addEntry.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addEntry, org.openide.util.NbBundle.getMessage(JarPaneBox.class, "JarPaneBox.addEntry.text")); // NOI18N
        addEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(38, 38, 38)
                .addComponent(jButton4)
                .addGap(142, 142, 142))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(archiveName))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(addEntry)
                .addContainerGap(56, Short.MAX_VALUE))
            .addComponent(destDir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(destText)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(archiveName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(addEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(destDir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(destText, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
     mainWindow.mainPaneDialog.getComponent(0).setVisible(false);
    // nextPane =  new jarPaneBox2(mainWindow,fileList);
    mainWindow.mainPaneDialog.getComponent(1).setVisible(true);
     //mainWindow.mainPaneDialog.add(nextPane,0);
     mainWindow.mainPaneDialog.revalidate();
    mainWindow. mainDialogBox.revalidate(); 
    }//GEN-LAST:event_jButton3ActionPerformed
 
    public void setNextPane(jarPaneBox2 nextPane) {
        this.nextPane = nextPane;
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        mainWindow. mainDialogBox.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void addEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryActionPerformed
   
         for (File file : browseFiles(this, true,JFileChooser.FILES_AND_DIRECTORIES)) {
             listModel.addElement(file.getPath());
             str.add(file.getPath());
         }
    
      
      
    }//GEN-LAST:event_addEntryActionPerformed

 public static File[] browseFiles(Component parent,boolean open, int mode){
 JFileChooser fc  =  new JFileChooser("user.dir");
 fc.setMultiSelectionEnabled(true);
  fc.setFileSelectionMode(mode);
  File[]  files = new File[0];
  int val =0;
  if(open  == true)
    val  =  fc.showOpenDialog(parent);
  else val  =  fc.showOpenDialog(parent);
     if(val  == JFileChooser.APPROVE_OPTION)
        files =  fc.getSelectedFiles(); 
  
  return files;
 } 
    private void destDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destDirActionPerformed
     
          File[] files  = browseFiles(this, true,JFileChooser.DIRECTORIES_ONLY);
            destText.setText(files[files.length-1].getPath());
            
        
    }//GEN-LAST:event_destDirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEntry;
    private javax.swing.JTextField archiveName;
    private javax.swing.JButton destDir;
    private javax.swing.JTextField destText;
    protected javax.swing.JList<String> entryList;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
