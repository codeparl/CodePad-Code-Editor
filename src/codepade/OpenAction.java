/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author HASSAN
 */
public class OpenAction {

    
    
    
    
    
 public static String[] openFile(Container frame, String currentDir){
 String fileNames[] =  null;
     JFileChooser  fc  =  new JFileChooser();
      fc.setMultiSelectionEnabled(true);
 
      
     
     if(currentDir !=  null)
        fc.setCurrentDirectory(new File(currentDir));
     
     
     
     
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
 
        
      int returnVal  =  fc.showOpenDialog(frame);
      
      if(returnVal == JFileChooser.APPROVE_OPTION){
       File file[]  =   fc.getSelectedFiles();
       fileNames = new String[file.length];
      for(int i = 0; i < file.length;   ++i)
          fileNames[i] =  file[i].getAbsoluteFile().toString();
       //here the file already create so just save without 
       //asking the user to suply the file name 
    
       centralizeFileChooser(fc);
       
      
      }
 
 
 return fileNames;
 
 }   
 
 
 
 
 
 

  private  static void centralizeFileChooser(JFileChooser fc){
    
    Dimension  screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
        Dimension chooserZise =  fc.getSize();
       
      
         fc.setLocation((screenSize.width -chooserZise.width / 2),
                          (screenSize.height -chooserZise.height / 2));
         
    
    
    }//end method 
    
    }//end class 
          
    

