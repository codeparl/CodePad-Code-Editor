/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

/**
 *
 * @author HASSAN
 */
public class FileActions  {

   
    
 public static class SaveAction extends AbstractAction{   
    
    private   JTextPane textDoc;
    private JFrame  frame;
    private ArrayList<String> currentFiles;

    public SaveAction(final JFrame  frame, JTextPane doc,Icon icon, String command){
        
          super(command, icon);
        textDoc = doc;
        this.frame =  frame;
    
    }
    

public SaveAction( ArrayList<String> currentFiles,final JFrame frame, JTextPane doc){
    super();
    textDoc = doc;
    this.frame =  frame;
    this.currentFiles =  currentFiles;
}    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
 
        createFileChooser();
      
         
    }//end method 
    
 private void createFileChooser(){
     System.out.println("TextSav: "+textDoc.getText());
     JFileChooser  fc  =  new JFileChooser();
   FileNameExtensionFilter  filter =  new FileNameExtensionFilter("Java Files[.java]","java");
      
  fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Text Files[.txt]","txt");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Hypertext Markup Language Files[.html]","html");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Cascading Style sheet Files[.css]","css");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Javascript Files[.js]","js");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter(" Hypertext pre-processor Files[.php]","php");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter(" Extensible Markup Language Files[.xml]","xml");
        fc.addChoosableFileFilter(filter); 
        
        
      int returnVal  =  fc.showSaveDialog(frame);
      
      if(returnVal == JFileChooser.APPROVE_OPTION){
       File file  =  fc.getSelectedFile();
       if( !file.exists()){
           try {
               textDoc.write(new BufferedWriter(new FileWriter(file)));
              SaveAction.this.setEnabled(false);
               
           } catch (IOException ex) {
               
               
               JOptionPane.showMessageDialog(frame, 
                       "Error while saving file, file has not been saved","Save Error",JOptionPane.ERROR_MESSAGE);
           }
                   
    
       
       }//end inner if 
       //here the file already create so just save without 
       //asking the user to suply the file name 
       else{
       fc.setVisible(false);
       try {
               textDoc.write(new BufferedWriter(new FileWriter(file)));
           } catch (IOException ex) {
               
               
               JOptionPane.showMessageDialog(null, 
                       "Error while saving file, file has not been saved","Save Error",JOptionPane.ERROR_MESSAGE);
           }
       }//end else
       
       
      
      }//end outer if 
    
 
 
 
 
 
 
 }//end method 
 
    
    
 }
    
 
public static class OpenAction  extends AbstractAction{
private static  JTextPane textDoc;
    private JFrame  frame;
    private  String  fileName;
        public OpenAction(final JTextPane doc,Icon icon, String command) {
            super(command, icon);
            textDoc = doc;
        }

    
    
        @Override
        public void actionPerformed(ActionEvent e) {
            new  MyfileChooser();
            
        }

public String getFileName(){return fileName;}
private class MyfileChooser extends JFileChooser{
    
    
    public MyfileChooser(){
    
    super();
    
   // setAcceptAllFileFilterUsed(false);
       FileNameExtensionFilter  filter =  new FileNameExtensionFilter("Java Files[.java]","java");
        this.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Text Files[.txt]","txt");
        addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Hypertext Markup Language Files[.html]","html");
        addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Cascading Style sheet Files[.css]","css");
        addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Javascript Files[.js]","js");
        addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter(" Hypertext pre-processor Files[.php]","php");
        addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter(" Extensible Markup Language Files[.xml]","xml");
        addChoosableFileFilter(filter); 
        
        
      int returnVal  = showOpenDialog(null);
      
      if(returnVal == JFileChooser.APPROVE_OPTION){
       File file  =  getSelectedFile();
       fileName =  file.getAbsolutePath();
      if(file.exists() && file.canRead()){
           try {      
          readFile(file);       
           } catch (IOException ex) {
               
                
               JOptionPane.showMessageDialog(this, 
                       "Error while opening file, ","Open Error",JOptionPane.ERROR_MESSAGE);
           } catch (BadLocationException ex) {
           }
                   
    
       
       }//end inner if 
       //here the file already create so just save without 
       //asking the user to suply the file name 
       
       
       
      
      }
    
     centralizeFileChooser();   
    }//end constructor 
    
    private void readFile(File file) throws FileNotFoundException, BadLocationException, IOException{
         BufferedReader  reader =  new BufferedReader(new FileReader(file));
         
               String line =  null;
           StringBuilder   text  = new StringBuilder();
         while(  (line =  reader.readLine()) != null ){
           text.append(line).append("\n");
           }//end while loop 
               
               textDoc.getDocument().insertString(0, text.toString(), null);
            
               
    
    }


    
    public String openFile(){
    
    
        
        
        
    return  fileName;
    
    }
    
        @Override
        protected JDialog createDialog(Component parent) throws HeadlessException {
            
          JDialog  dialog  = super.createDialog(parent);    
            
          Image image =  new ImageIcon(getClass().getResource("img/OPEN.gif")).getImage();
          dialog.setIconImage(image );
          
          
          
          
            
            return dialog ; //To change body of generated methods, choose Tools | Templates.
          
        }
    
    
    
    
    private void centralizeFileChooser(){
    
    Dimension  screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
        Dimension chooserZise =  getSize();
       
      
         setLocation((screenSize.width -chooserZise.width / 2),
                          (screenSize.height -chooserZise.height / 2));
         
    
    
    }//end method 
    
    }//end inner-class 
        

}//nested class 
 
 







   
  //  setAcceptAllFileFilterUsed(false);
     
}//end class 
