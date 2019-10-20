/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import org.openide.util.Exceptions;

/**
 *
 * @author HASSAN
 */
public interface CommandsManager {
    
  public  default  void registerPopupMenuTo(Component invoker, JPopupMenu popupMenu){
  
  invoker.addMouseListener(new MouseAdapter() {
     @Override
     public void mousePressed(MouseEvent e) {
         checkTrigeredEvent(e);
     }

     @Override
     public void mouseReleased(MouseEvent e) {
         checkTrigeredEvent(e);
     }
     
     private void checkTrigeredEvent(MouseEvent e){
     if(e.isPopupTrigger()){
       popupMenu.show(invoker, e.getX(), e.getY());
     }
     
    if(invoker instanceof  JList){
        JList list  = (JList) invoker;
        DefaultListModel  m  =  (DefaultListModel) list.getModel();
        JMenuItem  mi  =  (JMenuItem) popupMenu.getComponent(1);
        JMenuItem  mi2  =  (JMenuItem) popupMenu.getComponent(0);
        if(m.isEmpty()){
          mi.setEnabled(false);
          mi2.setEnabled(false);
        }else mi.setEnabled(true);
    } 
     
     }
     
});
 
 
  
  
  }  
    
  public  default  void initUserCommands(prop  props,String...keyValue){
  

  
  String value   = "\tpublic static void main(String[] args){\n\n\t}//END METHOD main\n";
    props.setProperty("insertMain", value);
    value  = "System.out.println();";
    props.setProperty("newLine", value);  
    

  if(keyValue != null && keyValue.length > 1){
    String key =  keyValue[0];
    String name =  keyValue[1];
    name  =  (name.substring(0, 1).toUpperCase())+ name.substring(1, name.length());
    value = "public class "+name+"{\n\n}//END CLASS "+name;
    props.setProperty(key, value);  
    
    }
    
    
  
  
  
  }//end mehtod  
    
    
  public  default  void saveProgramState(prop property, String state, String value,String commment){
   property.setProperty(state, value);
   property.storeProperties(commment);
      
   
    
    
 }//end method 
  

public  default boolean addSelectionToSysClipboard(String selectedData){
    boolean  added  =  false;
    StringSelection   selection =  new StringSelection(selectedData);
    Clipboard   clipboard  =  Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(selection, selection);
    
      try {
          String x  =  (String) clipboard.getData(DataFlavor.stringFlavor);
          if(x.equals(selectedData)) added =  true;
      } catch (UnsupportedFlavorException | IOException ex) {
          Exceptions.printStackTrace(ex);
      }
    
return added;
}  
  
 /**
  * 
  * @param line
  * @param symbol
  * @return 
  */ 
   public  default  String   addCommentTo(String line, String symbol){
   
       String lineText  = line;
       
     String  commentSymbol =  "// ";
     switch(symbol){
         case "java-s":
         case "js":
         case "php":
         case "c-s":
         commentSymbol =  "// "; 
          lineText =  commentSymbol+lineText;
          break;
        case "java-m":
         case "c-m":
         case "css": 
         case "c++":
         commentSymbol ="/* ";
         lineText =  commentSymbol+lineText+" */";
         
         break;
         case "html":
         case "xml":
         commentSymbol =  "<!-- ";  
        lineText =  commentSymbol+lineText+" -->";
         break;
         
        default:
        commentSymbol ="// "; 
        lineText =  commentSymbol+lineText;
     }
   
   
    return lineText; 
   
   } 
   
   
   
  public  default String  removeCommentsFrom(String line, String symbol){
 
         String lineText  = line;
 
     switch(symbol){
         case "java-s":
         case "js":
         case "php":
         case "c-s":
         
          lineText =  lineText.substring(lineText.lastIndexOf("/")+1, lineText.length());
          break;
         case "java-m":
         case "c-m":
         case "css": 
         case "c++":
          lineText =  lineText.substring(lineText.indexOf("*")+1, lineText.lastIndexOf("*"));
         break;
         case "html":
         case "xml":
          lineText =  lineText.substring(lineText.indexOf("-")+2, lineText.lastIndexOf("-")-1);
         break;
         
       case "javadoc":
          lineText =  lineText.substring(lineText.indexOf("*")+2, lineText.lastIndexOf("*")-1);
         break;
      
                 
     }
    
  return lineText;
  }
  
  

  
 public  default  void addHighlightInto(JTextPane docPane,Highlighter.HighlightPainter painter, String text,boolean moveDot) throws BadLocationException{
 
     removeHighlightFrom(docPane);
 //obtain a highlighter from this text component
 Highlighter  highlighter =  docPane.getHighlighter();
 Document  doc  =  docPane.getDocument();
 String docText   =  doc.getText(0, doc.getLength());
 
 int pos = 0; //we will start with this value to find the next match.
 
 if(text != null){
 while((pos = docText.toUpperCase().indexOf(text.toUpperCase(), pos) ) >= 0){
   String  x = docText.substring(pos,pos+text.length()+1 );
   
  //if(x != null &&   x.matches("(\\w)+")  ){
       System.out.println("y: "+x);
   highlighter.addHighlight(pos, pos+text.length(), painter);
   
   if(moveDot){
   docPane.getCaret().setDot(pos+text.length());
   docPane.requestFocus();
  }
  
//}//end inner if 
   pos += text.length();
 }//end while loop 

 }//end if 
     
     
 }//end method 
    
  public  default  void findNextOccurence(JTextPane docPane,Highlighter.HighlightPainter painter, String text, int pos) throws BadLocationException{
  
   //obtain a highlighter from this text component
 Highlighter  highlighter =  docPane.getHighlighter();
 Document  doc  =  docPane.getDocument();
 String docText   =  doc.getText(0, doc.getLength());

 if(((pos = docText.toUpperCase().indexOf(text.toUpperCase(), pos) ) >= 0)){
   highlighter.addHighlight(pos, pos+text.length(), painter);
   docPane.getCaret().setDot(pos+text.length());
   docPane.requestFocus();
   pos += text.length();
 }
 
 
  
  }//end method 
 

public default int countOcurrenceOf(String text, char operator, int offset){

int x =0;
for(int i = offset; i < text.length(); i++)
    if(text.charAt(i) ==  operator)
        ++x;

return x;
}
 
    
     public  default  void removeHighlightFrom(JTextPane docPane){
 
       Highlighter    h  =  docPane.getHighlighter();
       Highlighter.Highlight[]  highlighters = h.getHighlights();
       
         for (Highlighter.Highlight x : highlighters) 
             if(x.getPainter() instanceof  MainWindow.CodePadHighLighter)
                 h.removeHighlight(x);
         
         
 
 }//end mehtod 
    
    
}
