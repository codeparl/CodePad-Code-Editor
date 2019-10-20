/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import static codepade.CodePadeConstants.GRAY;
import static codepade.CodePadeConstants.WHITE;


/**
 *
 * @author HASSAN
 */
public class DocumentManager  implements CodePadeConstants{
    

private MainWindow MainWindow;

 
    public DocumentManager(MainWindow MainWindow) {
        this.MainWindow = MainWindow;
    }


  public void createNewDocument(int newTabCount,  int index){
  documentPane   p  = new documentPane(MainWindow.lineNumberLable); 
               MainWindow.currentDoc = p.getTextPane();
               MainWindow.currentDoc.requestFocus();
               checkSelectedThem(); 
               changeFileIcon(".txt");
               MainWindow.currentDoc.setDocument(new 
            TextDocument(".txt",MainWindow.themBox.getSelectedItem().toString()));
               
               MainWindow.addDocListener(MainWindow.currentDoc);
               MainWindow.processDocPopupMenuActions(MainWindow.currentDoc);
              MainWindow.tabPane.add("New "+(++newTabCount),p );
              MainWindow.tabPane.setTabComponentAt(index, new TabController(MainWindow));
              MainWindow.tabPane.setSelectedIndex(index);
              MainWindow.fileSaveState.add(false);
              MainWindow.saveBtn.setEnabled(MainWindow.fileSaveState.get(
                      MainWindow.tabPane.getSelectedIndex()));
              MainWindow.saveCmd.setEnabled(MainWindow.fileSaveState.get(
                      MainWindow.tabPane.getSelectedIndex()));
  
  }
  public void checkSelectedThem(){
  
  switch(MainWindow.themBox.getSelectedItem().toString()){
      case "graySkyblue":
          MainWindow.currentDoc.setBackground(GRAY);
          
        break;
        
        case "whiteBlue":
        MainWindow.currentDoc.setBackground(WHITE);  
         
        break;
  
  }
  
  }  
  public void changeFileIcon(String extention){
    
    switch(extention){
        case ".html":           
        case ".xhtml":
        case ".htm":
         MainWindow.fileIconName ="img/Document-html-icon.png";   
       break;
        case ".css": 
         MainWindow.fileIconName ="img/text-css-icon.png";    
         break;
            
        case ".xml":
         MainWindow.fileIconName ="img/Web-XML-icon.png";     
       break;
       case ".java":
       MainWindow.fileIconName ="img/java-icon.png"; 
       break;
       
      case ".c":
       MainWindow.fileIconName ="img/text-x-c-icon.png"; 
       break;
      
        case ".cpp":
       MainWindow.fileIconName ="img/Mimetype-source-cpp-icon.png"; 
       break;
       
      case ".php":
       MainWindow.fileIconName ="img/Mimetype-php-icon.png"; 
       break;
       
       case ".js":
       MainWindow.fileIconName ="img/text-x-javascript-icon.png"; 
       break;
       
       default:
      MainWindow.fileIconName =DEFAULT_FILE_ICON;
      break;
  
    }//end switch 
    
    }

    
}
