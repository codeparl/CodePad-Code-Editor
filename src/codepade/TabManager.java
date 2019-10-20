


package codepade;

import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author HASSAN
 */
public class TabManager    implements  CodePadeConstants{
    
 private final MainWindow MainWindow;
 private final DocumentManager documentManager;
private FileManager fileManager; 
    public TabManager(MainWindow win) {
        MainWindow =  win;
        documentManager =  new DocumentManager(MainWindow);
        fileManager =  MainWindow.fileManager;
    }
 
public  void removeTab(String remove, String key, int i){
    
  if (i != -1 ) { 
  key =  MainWindow.tabPane.getTitleAt(i);   
  remove =  removePropertyFrom(MainWindow.currentFilesPros, key,null);
             removeNewFileTabs(i);
             addClosedFileToRecentFiles(remove);
             removeFileSaveState(i);
             MainWindow.tabPane.remove(i);
  }//end if 
            
     createTabIfNoTabs(i);
     MainWindow.switchToCurrentDoc();
   enableButtons(MainWindow.fileSaveState.get(MainWindow.tabPane.getSelectedIndex()),
           MainWindow.saveBtn,MainWindow.saveCmd);
   
   if(MainWindow.tabPane.getSelectedIndex() == 0)
    enableButtons(MainWindow.fileSaveState.get(MainWindow.tabPane.getSelectedIndex()),
            MainWindow.saveBtn,MainWindow.saveCmd,
            MainWindow.saveAllBtn,
            MainWindow.saveAllMenu);
   

}
   
 public void createTabIfNoTabs(int index){
     
  if(MainWindow.tabPane.getTabCount() == 0){
               MainWindow.newTabCount =  0;
               documentManager.createNewDocument(MainWindow.newTabCount,index);
              }//end if  
  
      if( !MainWindow.currentFilesPros.getProperties().isEmpty()) {       
           MainWindow.setTitle("CodePad - "+MainWindow.currentFilesPros.getPropertyValue(
           MainWindow.tabPane.getTitleAt(MainWindow.tabPane.getSelectedIndex())));
             
      }else {
      MainWindow.setTitle("CodePad - "+MainWindow.tabPane.getTitleAt(MainWindow.tabPane.getSelectedIndex()));  
             
      }
 
 }
   
 public void confirmBeforeRemovingTab(String remove, String key, int i){
    
String file  =extractPath(MainWindow.getTitle()); 
if(MainWindow.fileSaveState.get(i) == true){
    String msg =  "The file "+file+" has been modified.\nAre you sure you want to close this tab before saving this file?";   
      int  opt =    JOptionPane.showConfirmDialog(MainWindow, msg, "Confirm ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      
         if(opt == 1){
              
        }else {
             removeTab(remove, key, i);
      }
            
}else{ removeTab(remove, key, i); 
        }
    JLayeredPane  p =  new JLayeredPane();
    
}
public  void closeAllTabs(){
  int tabCount  =  MainWindow.tabPane.getTabCount();
    
    if(tabCount > 0){
    for(int i = tabCount - 1; i > -1; --i){
        
       closeTab(i,false);
        
    }
    
    }

}   
 protected void closeTab(int index, boolean current){

int i  =  index;
String remove =  null;
String key  = null;
 
if(current){
  if(MainWindow.saveBtn.isEnabled()){
   confirmBeforeRemovingTab(remove, key, i);
   
 }else{ 
      removeTab(remove, key, i); 
  
  }
} else{

    int unsaved  =  countUnsaved();
    if(unsaved > 0){
   confirmBeforeRemovingTab(remove, key, i);
     }else {
       removeTab(remove, key, i);
    
    }


} 



 }
public int countUnsaved(){

    int total = 0;
    for(int i = 0; i < MainWindow.fileSaveState.size(); ++i)
      if(MainWindow.fileSaveState.get(i) == true){
      
         ++total;
      }
    
return total;
} 

 public void addClosedFileToRecentFiles(String menuText){

JMenuItem   menu =  new JMenuItem(menuText);
menu.addActionListener((e) -> {
    fileManager.openClosedFile (menu);
});
    
MainWindow.recent.add(menu);
if(menuText != null){
    MainWindow.closeFilesProps.setProperty(getFileName(menuText), menuText);
    MainWindow.closeFilesProps.storeProperties(null);
}
    

}

 
public void removeNewFileTabs(int i){
   
           if(MainWindow.tabPane.getTitleAt(i).matches("New\\s+\\d+") &&
              !MainWindow.currentFilesPros.getProperties().containsValue(MainWindow.tabPane.getTitleAt(i))){
              --MainWindow.newTabCount;
              String  j =  MainWindow.tabPane.getTitleAt(i);           
             MainWindow.newAddedTabList.remove(j);
             MainWindow.newRemovedTabList.add(j);
               
                 
             }
   
   
   } 

 
 public String removePropertyFrom(prop propList, String key, String value){
 
     String retrunValue = null;
     if(value != null){
      if(propList.getProperties().containsKey(key)){
       boolean t =   propList.getProperties().remove(key,value);
       if(t)
        retrunValue =  value;
       else retrunValue = null;
      }
     }else {
          if(propList.getProperties().containsKey(key)){
          
          retrunValue = (String) propList.getProperties().remove(key);
          }
       
     
     }
     
 return retrunValue;
 }

 

public void removeFileSaveState(int index){
       MainWindow.OARFMenu.setEnabled(true);
       MainWindow.cRFLMenu.setEnabled(true);
       MainWindow.fileSaveState.remove(index);

}//end metod 


}//end class 
