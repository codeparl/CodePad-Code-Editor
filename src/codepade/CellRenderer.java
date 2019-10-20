/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author HASSAN
 */
public class CellRenderer extends  DefaultTreeCellRenderer implements TreeCellRenderer, CodePadeConstants  {

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
         super.getTreeCellRendererComponent(tree, value, leaf, expanded, leaf, row, hasFocus);
      DefaultMutableTreeNode  node  = null;     
        
    if(value instanceof  DefaultMutableTreeNode)
        node  =  (DefaultMutableTreeNode)value;
    
    
    setText(value.toString());

    if(selected){
        setOpaque(false);
        setBackground(tree.getBackground());
   }
    else {setOpaque(true);
    setForeground(tree.getForeground());
    }
  
      String userObject  = "";
      userObject  = getText();
   
  
     switch(getExtension(userObject)){
     
         case ".jpg":
         case ".png":
         case ".gif":
         case ".GIF":    
         case ".icon":
         case ".JPEG":
         case ".JPG":
         setIcon(createIcont("img/pic.png"));
         break;
         case ".doc":
         case ".docx":
         setIcon(createIcont("img/wrd.png"));    
         break;
         
         case ".mp4":
         case ".mp3":
         case ".flv":
         case ".aiff":
         case ".mpeg":
         case ".wmv":
             
         setIcon(createIcont("img/vd.png"));    
         break;
         
         case ".xml":
         setIcon(createIcont("img/xml.png"));    
         break;
  
         case ".java":
         setIcon(createIcont("img/java.png"));    
         break;
         
         case ".zip":
         case ".rar":
         case ".gzip":
         setIcon(createIcont("img/zip.png"));    
         break;
         
        case ".html":
        case ".htm":
         setIcon(createIcont("img/Document-html-icon.png"));    
         break;
         
         case ".pdf":
         setIcon(createIcont("img/Adobe-PDF-Document-icon.png"));    
         break;
         
         default:
         setIcon(createIcont("img/File-icon.png"));   
         break;
     }
    
 

      
      if(leaf && userObject.matches("New\\s*folder\\s*\\([0-9]\\)")){
      setIcon(createIcont("img/folder.png"));
      }
         //check if it is a file 
      if(!leaf){
      setIcon(createIcont("img/folder.png"));
      }
      
     if(expanded && !leaf){
       setIcon(createIcont("img/openFolder.png"));
     
     }
      
      return this;
    }

    
    
    
    
    
    
    
}
