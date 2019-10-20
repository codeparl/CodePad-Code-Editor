/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;


import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import org.openide.util.Exceptions;

/**
 *
 * @author HASSAN
 */

public class FileManager implements  CodePadeConstants{
private final MainWindow  mainWindow;
private final DocumentManager documentManager;
private JTextPane currentDoc;
private final JTabbedPane tabPane;
private final PropertyManager propertyManager;
   private boolean isLoading;
   private boolean isOpenAction;
   private final JComboBox themBox;
   private final prop currentFilesPros;
   private final JLabel lineNumberLable;
    public FileManager(MainWindow mw) {
        this.mainWindow = mw;
        documentManager =  new DocumentManager(mainWindow);
        currentDoc = mainWindow.currentDoc;
        tabPane =  mainWindow.tabPane;
        isLoading = mainWindow.isLoading;  
       isOpenAction =mainWindow.isOpenAction;
       themBox =  mainWindow.themBox; 
       currentFilesPros=  mainWindow.currentFilesPros;
      lineNumberLable =  mainWindow.lineNumberLable; 
      propertyManager =  new PropertyManager(mainWindow);
    }
  
    
    
    public  void openFileInTab(String file  ){
    
  documentPane   p =  new documentPane(mainWindow.lineNumberLable);
     currentDoc = p.getTextPane();
     mainWindow.checkSelectedThem();   
 
      
      
     String filePath = file;
        if(filePath == null) return;
     

  //Do not open the same already opened file  
  if(fileAlreadyOpen(filePath)){
      JOptionPane.showMessageDialog(mainWindow,"The file ["+filePath+"] is already open in CodePad.","Error", JOptionPane.ERROR_MESSAGE);
      return;
}   
mainWindow.currentFilesPros.setProperty(getFileName(filePath), filePath);
    
     
      String  toolTip  =  filePath;
   
     removeMenuItemIfFileOpen(filePath, mainWindow.recent);
     if(mainWindow.recent.getMenuComponentCount() == 0){
       mainWindow.OARFMenu.setEnabled(false);
        mainWindow.cRFLMenu.setEnabled(false);
           }
    
        mainWindow.currentPath =  filePath;
        
         
      String ext  = getExtension(filePath);
      documentManager.changeFileIcon(ext);
      currentDoc.setDocument(new TextDocument(ext,mainWindow.themBox.getSelectedItem().toString()));
      mainWindow.processDocPopupMenuActions(currentDoc);
      mainWindow.addDocListener(currentDoc);
      setDocFont(currentDoc, mainWindow.fontSize);
 

  
  readFileFirst(currentDoc, filePath, false);
 
 
      mainWindow.setTitle("CodePad - "+filePath);
      
      filePath  =  getFileName(filePath);
   tabPane.add(filePath, p); 
   tabPane.setTabComponentAt(tabPane.getTabCount()-1, new TabController(mainWindow));
   tabPane.setSelectedIndex(tabPane.getTabCount()-1);
   mainWindow.fileSaveState.add(false); 
   tabPane.setToolTipTextAt(tabPane.getTabCount()-1, toolTip);
   mainWindow.tanCount =  tabPane.getTabCount();
   
    System.out.println(mainWindow.fileSaveState.get(tabPane.getSelectedIndex()));
 //saveBtn.setEnabled(fileSaveState.get(tabPane.getSelectedIndex()));
 //saveCmd.setEnabled(fileSaveState.get(tabPane.getSelectedIndex()));
  enableButtons(mainWindow.fileSaveState.get(tabPane.getSelectedIndex()),
          mainWindow.saveBtn,mainWindow.saveCmd);
    
 
 
   if(mainWindow.currentFilesPros.getProperties().containsKey(tabPane.getTitleAt(tabPane.getSelectedIndex())))
       mainWindow.currentFile =  mainWindow.currentFilesPros.getPropertyValue(tabPane.getTitleAt(tabPane.getSelectedIndex()));
 

 
   

   
  mainWindow.checkTabTitle();
  mainWindow.webFile();
  mainWindow.isOpenAction = false;
  mainWindow.isLoading =  false;
  

}

public void  openLastOpenFiles( ){
 
  
Set<Map.Entry<Object, Object>> em  =  currentFilesPros.getProperties().entrySet(); 


int c = 0;

for(Map.Entry<Object, Object>  x: em){
    
 String file  =  x.getValue().toString();
 File  f  =  new File(file);
 
 if(Files.notExists(f.toPath())){
 } else{
   mainWindow.fileSaveState.add(false);   
    openLastOpenFiles(file,c++);

} 

}//end loop 
  }//end method  
  
  public  void openFileInTab( ){
 
 
     Container  con =  null;
     if(mainWindow.findDialog.isVisible()) con  =  mainWindow.findDialog;
     else con  =  mainWindow;

     String[] filePath =  OpenAction.openFile(con, mainWindow.currentPath);
        if(filePath == null) return;
     
for(int i = 0; i < filePath.length; ++i){
  documentPane   p =  new documentPane(mainWindow.lineNumberLable);
    mainWindow.currentDoc = p.getTextPane();
  
  documentManager.checkSelectedThem();   
  //Do not open the same already opened file  
  if(fileAlreadyOpen(filePath[i])){
      JOptionPane.showMessageDialog(mainWindow,"The file ["+ filePath[i]+"] is already open in CodePad.","Error", JOptionPane.ERROR_MESSAGE);
      continue;
}   


  mainWindow.currentFilesPros.setProperty(getFileName( filePath[i]),  filePath[i]);
     
     
      String  toolTip  =  filePath[i];
   
     removeMenuItemIfFileOpen( filePath[i], mainWindow.recent);
     if(mainWindow.recent.getMenuComponentCount() == 0){
       mainWindow.OARFMenu.setEnabled(false);
        mainWindow.cRFLMenu.setEnabled(false);
           }
    
        mainWindow.currentPath =   filePath[i];
        
         
      String ext  = getExtension( filePath[i]);
      documentManager.changeFileIcon(ext);
      mainWindow.currentDoc.setDocument(new TextDocument(ext,mainWindow.themBox.getSelectedItem().toString()));
      mainWindow.processDocPopupMenuActions(mainWindow.currentDoc);
      mainWindow.addDocListener(mainWindow.currentDoc);
      mainWindow.setDocFont(mainWindow.currentDoc, mainWindow.fontSize);
 

  
  readFileFirst(mainWindow.currentDoc,  filePath[i], false);
 
 
      mainWindow.setTitle("CodePad - "+filePath[i]);
      
       filePath[i]  =  getFileName( filePath[i]);
   mainWindow.tabPane.add( filePath[i], p); 
   mainWindow.tabPane.setTabComponentAt(mainWindow.tabPane.getTabCount()-1, new TabController(mainWindow));
   mainWindow.tabPane.setSelectedIndex(mainWindow.tabPane.getTabCount()-1);
   mainWindow.fileSaveState.add(false);
   mainWindow.tabPane.setToolTipTextAt(mainWindow.tabPane.getTabCount()-1, toolTip);
   mainWindow.tanCount =  mainWindow.tabPane.getTabCount();

 
   if(mainWindow.currentFilesPros.getProperties().containsKey(
           mainWindow.tabPane.getTitleAt(mainWindow.tabPane.getSelectedIndex())))
       mainWindow.currentFile =  mainWindow.currentFilesPros.getPropertyValue(
               mainWindow.tabPane.getTitleAt(mainWindow.tabPane.getSelectedIndex()));
 
}//end for loop 


  mainWindow.checkTabTitle();
  mainWindow.webFile();
  mainWindow.isOpenAction = false;
  mainWindow.isLoading =  false;
 enableButtons(false);
 }   
   
  public void webFile(){
  if(isWebFile(mainWindow.tabPane.getTitleAt(mainWindow.tabPane.getSelectedIndex())))
              mainWindow.enableWebmenuItems(true);
          else 
              mainWindow.enableWebmenuItems(false);
 
 } 
  
 public  boolean fileAlreadyOpen(String path){
 
  //Do not open the same already opened file          
 return mainWindow.currentFilesPros.getProperties().containsValue(path);
 }    
    
 public void removeNonExisitingFileFrom(prop property, String...op){
  Collection<Object> c =  property.getProperties().values();
if(!property.getProperties().isEmpty()){
 ArrayList<Object> x  = new ArrayList<>(c);
    printList(x);
    
 int size  =property.getProperties().size();  
  for(int i = 0; i < size; ++i){
      String y =  getFileName(x.get(i).toString());
     
    if(y == null || y.equals(""))
         y = x.get(i).toString();
   
     String  f  = property.getPropertyValue(y);

     if(Files.notExists(Paths.get(f))){      
        if(op != null  && op.length > 1 && op[1]!= null){
          if(op[1].equals("value"))
         y =     propertyManager.removePropertyFrom(property, getFileName(f), f);
       
          mainWindow.loadFaild = true;
           System.out.println(y+" was removed"); 
        } else{
          y =   propertyManager.removePropertyFrom(property, f, null);
          mainWindow.loadFaild = true;
        }
  
       if(op != null && op[0] != null && op[0].equals("currentFileProps"))
               mainWindow.currentTab = 0;
     }//end inner if 
      
  }//end loop 
  
}
  
        
  
  }
  
    
  public void readFileFirst(final JTextPane textPane, final  String titlex, boolean  freezButtons){
            
             Thread t  =      new Thread(){
                    @Override
                    public void run() {
                         try { 
                        readFile(new File(titlex),textPane); 
                        if(freezButtons){
                        mainWindow.cRFLMenu.setEnabled(false);
                        mainWindow.OARFMenu.setEnabled(false);
                        }else {
                        mainWindow.cRFLMenu.setEnabled(true);
                        mainWindow.OARFMenu.setEnabled(true);
                        }
                         } catch (BadLocationException | IOException ex) {} 
               }
               };
                  
    try {
        t.start();
        t.join();
    } catch (InterruptedException ex) { }
 
 }
   
  
   public   void readFile(File file, JTextPane textDoc) throws FileNotFoundException, BadLocationException, IOException{


// List<String> text  =  Files.readAllLines(file.toPath());

            
    

   SwingUtilities.invokeLater(() -> {
     try {
         byte[]    data  =  Files.readAllBytes(file.toPath());
         String content  =  new String(data, StandardCharsets.UTF_8);
         try {
             textDoc.getDocument().insertString(0, content, null);
             mainWindow.lineNumberLable.setText(NumberFormat.getNumberInstance().format(lineNumber(content)));
         } catch (BadLocationException ex) {
             Exceptions.printStackTrace(ex);
         }
     } catch (IOException ex) {
         Exceptions.printStackTrace(ex);
     }
      });
     

   
   

    }//end method 

   

    public void openLastOpenFiles(String file, int index){
        
     
        
isLoading =  true;
isOpenAction =  true;

   String ext2 =  null;   
 

documentPane   p =  new documentPane(mainWindow.lineNumberLable);
     
//set current docuemnt to this new textPane
currentDoc = p.getTextPane(); 

 String filex  = file;
  



if(themBox.getSelectedItem().toString().equals("whiteBlue"))
    currentDoc.setBackground(WHITE);
else 
   currentDoc.setBackground(GRAY);

      String ext  = getExtension(filex);
      ext2= ext;
      currentDoc.setDocument(new TextDocument(ext,themBox.getSelectedItem().toString()));
     
      mainWindow.processDocPopupMenuActions(currentDoc);
      mainWindow.addDocListener(currentDoc);
      setDocFont(currentDoc, mainWindow.fontSize);
      readFileFirst(currentDoc, filex,false);
    
 
   filex  = new File(filex).getName();
     documentManager.changeFileIcon(ext);
   tabPane.add(filex, p); 
   tabPane.setTabComponentAt(tabPane.getTabCount()-1, new TabController(mainWindow));  
   tabPane.setSelectedIndex(tabPane.getTabCount()-1);
   mainWindow.tanCount =  tabPane.getTabCount();

if(currentFilesPros.getProperties().containsKey(tabPane.getTitleAt(tabPane.getSelectedIndex())))
   mainWindow.setTitle("CodePad - "+currentFilesPros.getPropertyValue(tabPane.getTitleAt(tabPane.getSelectedIndex())));

  tabPane.setToolTipTextAt(tabPane.getTabCount()-1, extractPath(mainWindow.getTitle()));
  mainWindow.updateFileInfo(true,mainWindow.extesionProps,
          getExtension(mainWindow.getTitle()),mainWindow.fileType, 
          mainWindow.fileLen,lineNumberLable,currentDoc, mainWindow);  

isLoading =  false;
isOpenAction =  false;
 

 }//end method 

    
public void openAllLastClosedFiles(){
isLoading = true;
  int tabCounter = 0;
 if(!mainWindow.closeFilesProps.getProperties().isEmpty()) { 
     
Enumeration em  = mainWindow.closeFilesProps.getProperties().elements();
while(em.hasMoreElements()){     

documentPane   p =  new documentPane(lineNumberLable);
     
//set current docuemnt to this new textPane
currentDoc = p.getTextPane(); 
mainWindow.checkSelectedThem();
String title  = (String) em.nextElement();

if(currentFilesPros.getProperties().containsKey(getFileName(title))){
    return;
}

             

if(!Files.exists(Paths.get(title)) ){
//   JOptionPane.showMessageDialog(currentDoc, 
//   "Error while opening: "+title,"Open Error",JOptionPane.ERROR_MESSAGE);
   mainWindow.closeFilesProps.getProperties().remove(getFileName(title), title);
   continue;
}
        



      String ext  = getExtension(title);
      documentManager.changeFileIcon(ext);
      currentDoc.setDocument(new TextDocument(ext,themBox.getSelectedItem().toString()));
      mainWindow.processDocPopupMenuActions(currentDoc);
      mainWindow.addDocListener(currentDoc);
      setDocFont(currentDoc, mainWindow.fontSize);
 
  currentFilesPros.setProperty(getFileName(title), title);
  mainWindow.fileSaveState.add(false);
    

      
  
  
  String titlex =  title; 
  tabCounter++; 
    final   int xc  =    tabCounter;
Thread  t  =  new Thread(){    
     
  public void run(){
      
      
    readFileFirst(currentDoc, title,true);

    mainWindow.setTitle("CodePad - "+title);
    String titlex2  = new File(titlex).getName();
 
   tabPane.add(titlex2, p); 
   tabPane.setTabComponentAt(tabPane.getTabCount()-1, new TabController(mainWindow));   
   tabPane.setToolTipTextAt(tabPane.getTabCount()-1, title);
   mainWindow.tanCount =  tabPane.getTabCount();
 
   enableButtons(mainWindow.fileSaveState.get(tabPane.getSelectedIndex()));
   mainWindow.switchToCurrentDoc();
   
    
}//end method run  
  };

t.start();
    try {
        t.join();
    } catch (InterruptedException ex) {
    }

 }

tabPane.setSelectedIndex(tabPane.getTabCount()-1);

 }//end if 
 
 isOpenAction = false;
isLoading = false;
   
 
 }//end method 

 
 public void openClosedFile(final JMenuItem  m){
 
 isLoading = true;
String text  =  m.getText();
String title =  text;
      String  toolTip  =  title;

   documentPane   p =  new documentPane(lineNumberLable);
     
          currentDoc    = p.getTextPane();
          mainWindow.checkSelectedThem();
   
      
      String ext  = getExtension(title);
      documentManager.changeFileIcon(ext);
      currentDoc.setDocument(new TextDocument(ext,themBox.getSelectedItem().toString()));      
      mainWindow.addDocListener(currentDoc);
      mainWindow.processDocPopupMenuActions(currentDoc);    
      propertyManager.removePropertyFrom(mainWindow.closeFilesProps,getFileName(text), null);
     
     
      if(mainWindow.closeFilesProps.getProperties().isEmpty()){
         mainWindow.cRFLMenu.setEnabled(false);
         mainWindow.OARFMenu.setEnabled(false);
     }
     currentFilesPros.setProperty(getFileName(text), text);
     currentFilesPros.storeProperties("currentFilesPros");
    mainWindow.fileSaveState.add(false);
    
     readFileFirst(currentDoc, title, true);

     mainWindow.setTitle("CodePad - "+text);
     
    updateFileInfo(true,mainWindow.extesionProps,
            getExtension(text),mainWindow.fileType,
            mainWindow.fileLen,lineNumberLable,
            currentDoc, mainWindow);
    
  
    
   tabPane.add(getFileName(title), p); 
   tabPane.setTabComponentAt(tabPane.getTabCount()-1, new TabController(mainWindow));
   tabPane.setSelectedIndex(tabPane.getTabCount()-1);
   tabPane.setToolTipTextAt(tabPane.getTabCount()-1, toolTip);
   mainWindow.tanCount =  tabPane.getTabCount();
   
   mainWindow.saveBtn.setEnabled(mainWindow.fileSaveState.get(tabPane.getSelectedIndex()));
  mainWindow.saveCmd.setEnabled(mainWindow.fileSaveState.get(tabPane.getSelectedIndex()));
    
   
   if(currentFilesPros.getProperties().containsKey(tabPane.getTitleAt(tabPane.getSelectedIndex())))
      mainWindow.currentFile =  currentFilesPros.getPropertyValue(tabPane.getTitleAt(tabPane.getSelectedIndex()));
   
    webFile();
   mainWindow.recent.remove(m);

   isOpenAction = false;
     isLoading = false;
 
 }


 
  public void openRecentFile(){
  
 int x = mainWindow.recent.getMenuComponentCount();
 
     for (int i = 0; i < x; i++) {
         
      JMenuItem   m  =   (JMenuItem) mainWindow.recent.getMenuComponent(i);
      m.addActionListener( (e) -> {
      openClosedFile(m); 
       });
      
         
      
         
     }
 
 
     
 
 }//end method  

}//end class 
