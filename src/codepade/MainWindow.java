/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import codepade.actions.EditActions;
import codepade.actions.UndoableEditListenerx;
import com.sun.glass.events.KeyEvent;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.StyledEditorKit;
import javax.swing.tree.*;
import javax.swing.undo.UndoManager;
import org.openide.awt.*;
import org.openide.util.Exceptions;



/**
 *
 * @author HASSAN
 */
public class MainWindow extends javax.swing.JFrame implements CodePadeConstants, PropertyChangeListener{

 protected  int  fontSize = 0;
 protected int tanCount = 0;
 protected int newTabCount  = 0;
 protected  final  NumberFormat nf  =  NumberFormat.getInstance();
 protected ArrayList<String>  newAddedTabList;
 protected LinkedList<String>  newRemovedTabList;
 protected ArrayList<Boolean>   fileSaveState;
 protected BitSet  bitSetFileSaveState; 
 protected ArrayList<String > currentFiles;
 protected DefaultListModel<String> browserListModel;
 protected ArrayList<String > wordHintList;
 protected String currentFile;
 
 protected  DefaultTreeModel  treeModel;
 protected DefaultMutableTreeNode  rootNode;
 protected DefaultMutableTreeNode  leadSelection;
 protected TreePath treePath;
 protected  DefaultTreeCellRenderer  cellRenderer;
 protected ConsoleCellRenderer  consoleCellRenderer;
 protected CellRenderer cellRenderer2;
 protected DefaultTreeSelectionModel  selectionModel;
 protected DefaultListModel<String> consoleList;
 protected File treeSelectedFile;
 protected  ArrayList<String > treeDirList;
 
 protected JTextPane currentDoc;
 protected String currentPath;
 protected int currentTab;
 protected  prop  currentFilesPros,closeFilesProps,
         currentTabProps,themeProps,browserProps,
         extesionProps,defaultBrowserProps,
         userWebProps,treeDirProps,commandsProps,
         userCommandsProps,settingsProps,
         defaultProps,tabProps;
 
protected String propertiesStore;
protected String themesColor = "graySkyblue";
protected String defaultBrowser;
protected String defaultBrowserKey;
protected CodeCompiler compiler;
protected int lastEdit = 0;
protected String fileIconName;
protected String defaultFileIcon = "img/edit-new-document-icon.png";
protected boolean  isLoading =  false;
protected boolean  loadFaild =  false;
Dimension  dm;
private TabManager tabManager;
private DocumentManager documentManager;
protected FileManager  fileManager;

    public MainWindow(){
      dm=  new Dimension();
      consoleList =  new DefaultListModel<>();
      bitSetFileSaveState =  new BitSet();
     
      consoleCellRenderer =  new ConsoleCellRenderer();
      consoleCellRenderer.setCandidateVAlues("Open Succeeded","File Path:");    
     initComponents();
    createPropertyFiles();
    storeAllProps();
    loadAllpropertFiles();
    fileManager =  new FileManager(this); 
    setWindowSizeByPlattform();
   jList5.setCellRenderer(consoleCellRenderer);
   tabManager =  new TabManager(this);
   documentManager =  new DocumentManager(this);
  
     currentFiles =  new ArrayList<>();   
     newAddedTabList = new ArrayList<>();
     newRemovedTabList = new LinkedList<>();
     browserListModel =  new DefaultListModel<>();
     fileSaveState =  new ArrayList<>();
     wordHintList =  new ArrayList<>();
     
     rootNode =  new DefaultMutableTreeNode();
     treeModel =  new DefaultTreeModel(rootNode);
     directoryTree.setModel(treeModel);
     treeScroll.setViewportView(directoryTree);
     treeDirList  =  new ArrayList<>();
     fileIconName = DEFAULT_FILE_ICON;

     
     cellRenderer2 =  new CellRenderer();   
     directoryTree.setCellRenderer(cellRenderer2);
     
     selectionModel = new DefaultTreeSelectionModel();
     selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
     directoryTree.setSelectionModel(selectionModel);
     directoryTree.setSize(new Dimension(72, 75));
   
    
    findDialog.setLocationRelativeTo(this);
    findDialog.setIconImage(getIconImage());
    gotoLineBox.setLocationRelativeTo(null);
    javaSourceBox.setLocation(insertAtCenterPoint(javaSourceBox));
    javaSourceBox.setIconImage(getIconImage());
    mainDialogBox.setIconImage(getIconImage());
    
    browserList.setModel(browserListModel);
    currentDoc  =  document;
 
    processTabs();
    
    
    
     onPreLoad();
     addPoputToList();

      
   fontDialog.setPreferredSize(new Dimension(667, 488));
   fontDialog.setLocationRelativeTo(this);
   fontDialog.setIconImage(createIcont("img/TextPad.png").getImage());
   progressBar.setVisible(false);
   MainScrollPane.setRowHeaderView(new LineNumberView(currentDoc, lineNumberLable));

  
   themBox.setSelectedItem(themesColor);
   checkSelectedThem();
   currentDoc.setDocument(new TextDocument(".txt",themBox.getSelectedItem().toString()));
        
            
 addTabListener();     
   
       
        
 
 addDocListener(currentDoc);
 processDocPopupMenuActions(currentDoc);


  setEditActions();
  bindKeyStrokes();  
  bindActions();
  fileManager.openRecentFile();
  fileManager.removeNonExisitingFileFrom(currentFilesPros,"currentFileProps","value");
  fileManager.openLastOpenFiles(); 

 
loadLastTab(); 
switchToCurrentDoc();      
rememberLastDir();
webFile();
confinePopupWithTree();

SwingUtilities.invokeLater(() -> {
postLoad();
});

    }
  private void setWindowSizeByPlattform(){
  
      //make the size of this window 95% of the plattform size
      Toolkit  kit  =  Toolkit.getDefaultToolkit();
      Dimension  size  =  kit.getScreenSize();
      size.width = (int) (size.getWidth()/ 100 * 95);
      size.height = (int) (size.getHeight()/ 100 * 95);
      setPreferredSize(size);
      setSize(size);
      Dimension  winSize  =  size;
      //try to center the window 
      size  =  kit.getScreenSize();
      int x  =  (int)(size.getWidth() - winSize.getWidth())/2;
      int y  =  (int)(size.getHeight()- winSize.getHeight())/2;
      setLocation(x, y);
      setExtendedState(MAXIMIZED_BOTH);
      
      
  
  }  
    
    
    
 private void   initDefaultProps(){
     
   Set<Map.Entry<Object, Object>>  set  =  defaultBrowserProps.getProperties().entrySet();
    
     set.forEach((t) -> {
         String key  =  t.getKey().toString();
         defaultBrowserKey =  key;
         defaultBrowser = defaultBrowserProps.getPropertyValue(key);
     });
     

 }   
 private void confinePopupWithTree(){
 
directoryTree.addMouseListener(new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
        checkTrigeredEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
          checkTrigeredEvent(e);
    }
   
   
    
    private void checkTrigeredEvent(MouseEvent e){
    if(e.isPopupTrigger())
       treePopupMenu.show(e.getComponent(), e.getX(), e.getY());
    
    }
    
});
 

 }
 
 private void setInitialScale(){
  // currentDoc.setEditorKit(new Scaler());
   double  scale  = new Double((scales[scaleCounter])) / 100;
   currentDoc.getDocument().putProperty("i18n", Boolean.TRUE);
  currentDoc.getDocument().putProperty("ZOOM_FACTOR", scale);
 
 }//end method 
 private void initJavaSourceTextBox(){
 
 if(new File(currentFile).exists()){
    String location  =  currentFile.substring(0, currentFile.lastIndexOf(File.separator));
    jBoxText2.setText(location);
 
 }  else   jBoxText2.setText(System.getProperty("user.dir"));
     
 
 }
 private void rememberLastDir(){
 if(!currentFilesPros.getProperties().isEmpty())
    setTitle("CodePad - "+setTitleToFrame(tabPane.getTitleAt(tabPane.getSelectedIndex())));
  if(!currentFilesPros.getProperties().isEmpty()){
    currentPath =  extractPath(getTitle());
  } else  if(!closeFilesProps.getProperties().isEmpty()) {  
           String x  = ((JMenuItem)recent.getMenuComponent(recent.getMenuComponentCount()-1)).getText();
          currentPath = closeFilesProps.getProperties().getProperty(getFileName(x));
  } else currentPath =  System.getProperty("user.dir");
  
 } //end method  
    
 private void postLoad(){
     
int tabCount  =  tabPane.getTabCount();
for(int i = 0; i < tabCount; ++i){
 JTextPane   doc  =  findTextPane(i);
 int dot  =  Integer.valueOf(tabProps.getPropertyValue("tab"+i));
 doc.getCaret().setDot(dot);
}//end for loop 

 currentDoc     =  findTextPane(tabPane.getSelectedIndex());
 currentDoc.requestFocus();

 }   
    
    
  protected void switchToCurrentDoc() {
 
  if(currentFilesPros.getProperties().containsKey(tabPane.getTitleAt(tabPane.getSelectedIndex())))  {
      currentFile =currentFilesPros.getProperties().getProperty(tabPane.getTitleAt(tabPane.getSelectedIndex()));
     
  }else {
  currentFile =  tabPane.getTitleAt(tabPane.getSelectedIndex());
  
     
  }
      
  
  }//end method   
    
    
private void loadLastTab(){

if(tabPane.getTabCount() == 0)
   documentManager.createNewDocument(newTabCount, 0); 
    
 if(currentFilesPros.getProperties().isEmpty())
      tabPane.setSelectedIndex(0);
 
  if(currentTab > currentFilesPros.getProperties().size() - 1)
    tabPane.setSelectedIndex(0);
 else if(loadFaild)tabPane.setSelectedIndex(0);
 else if(!loadFaild && !currentFilesPros.getProperties().isEmpty())
            tabPane.setSelectedIndex(currentTab);


    
}//end method 

    
    private void onPreLoad(){
   

   webManager.setIconImage(getIconImage());
   webManager.setLocationRelativeTo(this);
   saveAllBtn.setEnabled(false);
   saveAllMenu.setEnabled(false);

   fileSaveState.ensureCapacity(currentFilesPros.getProperties().size());
     initDefaultProps();
     readProperties();
     initRecentMenu();
     initBrowserList();
     initUserWeb();
     
     Set<Map.Entry<Object, Object>>  set  =  browserProps.getProperties().entrySet();
     set.forEach((t) -> {
         String key  =  t.getKey().toString();
         renameMenuItem(key);
     });
     
     boldDefaultBrowser(webMenu.getMenuComponents());
     boldDefaultBrowser(webPopmenuList.getComponents());
     
    enableWebmenuItems(false);
    loadTheTree();
    loadSettings();
  
    
    
    }
private void loadSettings(){
   
   showTermAlway.setSelected(Boolean.valueOf(settingsProps.getPropertyValue(showTermAlway.getText())));
   showTerminalToggle.setSelected(Boolean.valueOf(settingsProps.getPropertyValue("showTerminalToggle")));
   showTerminal(showTerminalToggle.isSelected());
   
   docToolBarchk.setSelected(Boolean.valueOf(settingsProps.getPropertyValue(docToolBarchk.getText())));
   docToolBar.setVisible(docToolBarchk.isSelected());
   
   toolBarChk.setSelected(Boolean.valueOf(settingsProps.getPropertyValue(toolBarChk.getText())));
   toolBarPane.setVisible(toolBarChk.isSelected()); 


}  

private void saveAllSettings(){
saveProgramState(settingsProps,showTermAlway.getText() , Boolean.toString(showTermAlway.isSelected()),"program state properties");
saveProgramState(settingsProps,"showTerminalToggle" , Boolean.toString(showTerminalToggle.isSelected()),"program state properties");
saveAllLastEdit();
saveProgramState(settingsProps,showDPCheck.getText() , Boolean.toString(showDPCheck.isSelected()),"program state properties");
saveProgramState(settingsProps,docToolBarchk.getText() , Boolean.toString(docToolBarchk.isSelected()),"program state properties");
saveProgramState(settingsProps,toolBarChk.getText(), Boolean.toString(toolBarChk.isSelected()),"program state properties");


}
    private void loadTheTree(){
        
   fileManager.removeNonExisitingFileFrom(treeDirProps,null,"key");
   
 if(!treeDirProps.getProperties().isEmpty()){
  SwingUtilities.invokeLater(() -> {       
  Set<Map.Entry<Object, Object>>     setx =  treeDirProps.getProperties().entrySet();
   setx.forEach((t) -> {
    String key =  t.getKey().toString();
    String value  =  t.getValue().toString();
   treeDirList.add(value);
     loadFileTree(new File(value));
  });  
 }); //SwingUtilities.invokeLater 
     }//end if 
 showDPCheck.setSelected(Boolean.valueOf(settingsProps.getPropertyValue(showDPCheck.getText())));
 showTree();
 
 
 
    }

private void saveAllLastEdit(){

int tabCount  =  tabPane.getTabCount();
for(int i = 0; i < tabCount; i++){
 JTextPane   doc  =  findTextPane(i);
 int dot  =  doc.getCaret().getDot();
 tabProps.setProperty("tab"+i, String.valueOf(dot));

}//end for loop 
 tabProps.storeProperties("file last edit (tabs) Properties");
    
    


}  
boolean consoleSelectedcanCopy = false;
Action copyAction; 
JMenuItem clearAction;
 private void addPoputToList(){
 
 JPopupMenu  listPopup =  new JPopupMenu();
 
 JMenuItem  action  =  new JMenuItem();
    copyAction    =  new StyledEditorKit.CopyAction();
    copyAction.putValue(Action.NAME, "Copy Path");
    copyAction.setEnabled(consoleSelectedcanCopy);
    listPopup.add(copyAction);
 
    
 clearAction  =  new JMenuItem("Clear All");
 
 if(consoleList.isEmpty())
    clearAction.setEnabled(false);

 clearAction.addActionListener((e) -> {consoleList.clear();clearAction.setEnabled(false);});
 listPopup.add(clearAction);
 
 action  =  new JMenuItem("Hide Console");
 action.addActionListener((e) -> {showTerminal(false);});
 listPopup.add(action);
 registerPopupMenuTo(jList5, listPopup);
 }//end methpd    
 
 
 
 
 
    
    private void boldDefaultBrowser(Component[] items){
    
       
         Set<Map.Entry<Object, Object>>  set  =  defaultBrowserProps.getProperties().entrySet();
      for(Component  item :  items){
     set.forEach((t) -> {
         String key  =  t.getKey().toString();
         
        
              JMenuItem mi  =  (JMenuItem) item;
         if(key.equals(mi.getText())){
             mi.setFont(new Font(mi.getFont().getFamily(), Font.BOLD, mi.getFont().getSize()));
         }else{
         mi.setFont(new Font(mi.getFont().getFamily(),Font.PLAIN, mi.getFont().getSize()));
         
         }
              
     });
            
      }//end for loop       
            
           
        
        
        
    
    }
    
    
    
    private void initBrowserList(){
   if(browserProps.isFileFound() && !browserProps.getProperties().isEmpty()){
    Enumeration  em  =  browserProps.getProperties().elements();
    while(em.hasMoreElements())browserListModel.addElement((String) em.nextElement());
   
   }//end if 
   
    if(userWebProps.isFileFound() && !userWebProps.getProperties().isEmpty()){
    Enumeration  em  =  userWebProps.getProperties().elements();
    while(em.hasMoreElements())browserListModel.addElement((String) em.nextElement());
   
   }//end if 
   
   
    }//end method 
  
    private void initRecentMenu(){
    
     Enumeration  em  =   closeFilesProps.getProperties().elements();
     
        while (em.hasMoreElements()) 
            recent.add(new JMenuItem(em.nextElement().toString()));
            
        
    
    
    }//end method 
    
    
    

private void createPropertyFiles(){
  
   propertiesStore  =  getPropertyStore("current_files.prop",true);
  currentFilesPros =  new prop(propertiesStore,null); 
  
  propertiesStore  =  getPropertyStore("close_files_props.prop",true);
  closeFilesProps = new prop(propertiesStore,null);
  
   propertiesStore  =  getPropertyStore("current_tab_props.prop",true);
   currentTabProps = new prop(propertiesStore,null);
   
   propertiesStore  =  getPropertyStore("theme_props.prop",true);
   themeProps = new prop(propertiesStore,null);
   
   propertiesStore  =  getPropertyStore("browsers.prop",true);
   browserProps = new prop(propertiesStore,null); 
   
   propertiesStore  =  getPropertyStore("extensionProps.properties",false);
   extesionProps = new prop(propertiesStore,null); 
  
   propertiesStore  =  getPropertyStore("defaultBrowserProps.prop",true);
   defaultBrowserProps = new prop(propertiesStore,null);
    
   propertiesStore  =  getPropertyStore("userWebProps.prop",true);
   userWebProps = new prop(propertiesStore,null);
   
   propertiesStore  =  getPropertyStore("tree.properties",true);
   treeDirProps = new prop(propertiesStore,null);
   
   propertiesStore  =  getPropertyStore("commands.properties",true);
   userCommandsProps = new prop(propertiesStore,null);
   
   propertiesStore  =  getPropertyStore("commands.properties",false);
   commandsProps = new prop(propertiesStore,userCommandsProps);
   
   propertiesStore  =  getPropertyStore("defaultProps.properties",false);
   defaultProps = new prop(propertiesStore,null);
   
   propertiesStore  =  getPropertyStore("settingsProps.properties",true);
   settingsProps = new prop(propertiesStore,defaultProps);
   
  propertiesStore  =  getPropertyStore("tabProps.properties",true);
   tabProps = new prop(propertiesStore,null);

}//end emthod 

//load all properties at the start of the program
private void loadAllpropertFiles(){

 try(//this allows automatic closing of these character streams 
     BufferedReader  br1  =  new BufferedReader(new FileReader(currentFilesPros.getFile()));
     BufferedReader  br2  =  new BufferedReader(new FileReader(closeFilesProps.getFile()));
     BufferedReader  br3  =  new BufferedReader(new FileReader(currentTabProps.getFile()));
     BufferedReader  br4  =  new BufferedReader(new FileReader(themeProps.getFile())); 
     BufferedReader  br5  =  new BufferedReader(new FileReader(browserProps.getFile())); 
     InputStream  br6  =  getClass().getResourceAsStream("default_propertis/extensionProps.properties");
     BufferedReader  br7  =  new BufferedReader(new FileReader(defaultBrowserProps.getFile()));
     BufferedReader  br8  =  new BufferedReader(new FileReader(userWebProps.getFile())); 
     BufferedReader  br9  =  new BufferedReader(new FileReader(treeDirProps.getFile()));
     InputStream  br10  =  getClass().getResourceAsStream("default_propertis/commands.properties");
     BufferedReader  br11  =  new BufferedReader(new FileReader(userCommandsProps.getFile()));
     InputStream  br12  =  getClass().getResourceAsStream("default_propertis/defaultProps.properties");
     BufferedReader  br13  =  new BufferedReader(new FileReader(settingsProps.getFile()));
         BufferedReader  br14  =  new BufferedReader(new FileReader(tabProps.getFile()));
         ){
   
     if(currentFilesPros.isFileFound())
         currentFilesPros.getProperties().load(br1);
    // System.out.println("currentFilesPros loaded.");
      if(closeFilesProps.isFileFound())
        closeFilesProps.getProperties().load(br2);
  
      if(currentTabProps.isFileFound())
     currentTabProps.getProperties().load(br3);
    
      if(themeProps.isFileFound())
         themeProps.getProperties().load(br4);

      if(browserProps.isFileFound())
          browserProps.getProperties().load(br5);
   
      if(extesionProps.isFileFound())
           extesionProps.getProperties().load(br6);
      
     if(defaultBrowserProps.isFileFound())
           defaultBrowserProps.getProperties().load(br7);
    
    if(userWebProps.isFileFound())
           userWebProps.getProperties().load(br8);
      
    if(treeDirProps.isFileFound())
           treeDirProps.getProperties().load(br9);

    
     if(userCommandsProps.isFileFound())
           commandsProps.getProperties().load(br11);
   
      if(settingsProps.isFileFound())
           settingsProps.getProperties().load(br13);
    
        if(tabProps.isFileFound())
           tabProps.getProperties().load(br14);
        
 }catch(IOException ex){
     ex.printStackTrace();
 }   
    
    


}//end method 


  private void checkSelectedThem(JTextPane  docPane){
  
  switch(themBox.getSelectedItem().toString()){
      case "graySkyblue":
          docPane.setBackground(GRAY);
          
        break;
        
        case "whiteBlue":
        docPane.setBackground(WHITE);  
         
        break;
  
  }
  
  } 
  public  void checkSelectedThem(){
  
  switch(themBox.getSelectedItem().toString()){
      case "graySkyblue":
          currentDoc.setBackground(GRAY);
          
        break;
        
        case "whiteBlue":
        currentDoc.setBackground(WHITE);  
         
        break;
  
  }
  
  }  
    
 
  private void addTabListener(){
     
   
 tabPane.addMouseListener(new MouseAdapter() {
     
     
  private final    NumberFormat  nf  =  NumberFormat.getInstance();
  
  @Override
     public void mouseClicked(MouseEvent e) {
      
          
      
       int i  =  tabPane.getSelectedIndex();
       
     Thread  t  = new Thread(){
           @Override
           public void run() {
               currentDoc  =  findTextPane(i);
           }};
       
     t.start();
     try{t.join();}catch(InterruptedException ex){}
        
        
        final    String key  =  tabPane.getTitleAt(i).trim(); 
          String value =  null;
     if(currentFilesPros.getProperties().containsKey(key)){
            value  =  currentFilesPros.getPropertyValue(key);
            MainWindow.this.setTitle("CodePad - "+value);
            currentTab = i;
      }else {
         MainWindow.this.setTitle("CodePad - "+key);
        fileType.setText("Normal text file");
     }
     
          if(currentFilesPros.getProperties().containsKey(tabPane.getTitleAt(tabPane.getSelectedIndex())))
              currentFile =  currentFilesPros.getPropertyValue(tabPane.getTitleAt(tabPane.getSelectedIndex())); 
 
          webFile();
          
         currentDoc.requestFocus();
         saveBtn.setEnabled(fileSaveState.get(i));
         saveCmd.setEnabled(fileSaveState.get(i));
              checkTabTitle();
     
       
              
    if(tabPane.getTitleAt(i).endsWith(".java")){
        String className  = tabPane.getTitleAt(i);     
        className =  getFileNameOnly(className);  
        initUserCommands(userCommandsProps,"javaClass",className);     
        userCommandsProps.storeProperties("User commands ");
        processDocPopupMenuActions(currentDoc, "Insert Main Method","Println()","Insert Class Diff");
    }          
              
    
     // updateTreeOnFileCreation(new File(extractPath(getTitle())));
      
    
     }//end mwtghod 
     
 
});
 
 
 
 }   
 
 public void webFile(){
  if(isWebFile(tabPane.getTitleAt(tabPane.getSelectedIndex())))
              enableWebmenuItems(true);
          else 
              enableWebmenuItems(false);
 
 }
 
 
  protected void checkTabTitle( ){
        
         
       
     SwingUtilities.invokeLater(() -> {
       
         String path  =  extractPath(getTitle());
         Document  doc  =currentDoc.getDocument();
         if( !path.matches("New\\s+\\d+"))
           fileLen.setText(nf.format(new File(path).length()));
         else 
           fileLen.setText(nf.format(currentDoc.getText().trim().length()));    
         
         lineNumberLable.setText(nf.format(lineNumber(currentDoc.getText())));
         fileType.setText(extesionProps.getProperties().getProperty(tabPane.getTitleAt(tabPane.getSelectedIndex()),"Normal text ")+" file");        
         });
     
      
     }//end method
  

 

protected void enableWebmenuItems(boolean enable){
 
              int menuCount  =  webMenu.getMenuComponentCount();
              for(int j = 0; j < menuCount; ++j){
                JMenuItem  item  =  (JMenuItem) webMenu.getMenuComponent(j);
                if(enable){
                item.setEnabled(true);
                browserBtn.setEnabled(true);
                }
                else{ 
                    item.setEnabled(false);
                    browserBtn.setEnabled(false);
                }
              }
           
           


} 
 
 
private void addKeyListenerToDoc(JTextPane docPane){

docPane.addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
       
      
        
        
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
     
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
       
    }
    
    
    
});



}//end method 
     
protected void addDocListener(JTextPane textPane ){
        
    setRedUndoFor(textPane);    
        
    textPane.getDocument().addDocumentListener(new DocumentListener() {
        
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                int currtTab   =  tabPane.getSelectedIndex();
 
                    if (!isOpenAction) {
                    saveBtn.setEnabled(true);
                    saveCmd.setEnabled(true);
                    
                      saveAllBtn.setEnabled(true);
                      saveAllMenu.setEnabled(true);
                      
              
  
                    if(!isLoading)
                    fileSaveState.set(currtTab, true);
                    
                    
                
                }  
                
                
                
                    if( textPane.getText().length() > e.getLength()-1){
                        saveBtn.setEnabled(true);
                        saveCmd.setEnabled(true);
                        
                     

                        if(!isLoading)
                          fileSaveState.set(currtTab, true);
                        
                     }else{
                         saveBtn.setEnabled(false);
                        saveCmd.setEnabled(false);
                       
                        
                    }//end else                          
              
             
             checkInsertedText(e);
            }
            
            
            

            @Override 
            public void removeUpdate(DocumentEvent e) {
                int currtTab   =  tabPane.getSelectedIndex();
              
                    if(e.getLength() < e.getDocument().getLength()){
                        enableButtons(true);
                        fileSaveState.set(currtTab, true);
                        
                    }
             checkInsertedText(e);         
                  }//end method 
            
            
            
            @Override
   public void changedUpdate(DocumentEvent e) {
            }
  private void checkInsertedText(DocumentEvent e){
        
              SwingUtilities.invokeLater( () -> {
               File current  =  new File(extractPath(getTitle()));
               try {  
                      lineNumberLable.setText(nf.format(lineNumber(e.getDocument().getText(0, e.getDocument().getLength()))));
                      if(!current.toString().matches("New\\s+\\d+"))
                      fileLen.setText(nf.format(e.getDocument().getLength()+1));
                      else {
                          
                      fileLen.setText(nf.format(e.getDocument().getLength()+1));
                      }
                  } catch (BadLocationException ex) {
                  }
             }); 
            
        
        }    
            
        
        });
    addKeyListenerToDoc(textPane);
    
    textPane.addMouseListener( new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if(findDialogVisible) findDialog.setFocusableWindowState(false);
            
        if( !showTermAlway.isSelected()) {   
           terminal.setVisible(false);
           viewTerminal.setSelected(false);
           hintBox.setVisible(false);
            showTerminalToggle.setText("Show Console");
           showTerminalToggle.setIcon(createIcont("img/terminal.png"));
          showTerminalToggle.setSelected(false);
          
        }
        
        
     
 
        
        
        
//       
//        SwingUtilities.invokeLater(() -> {
//          painter =  new CodePadHighLighter(Color.GREEN);
//      try{ addHighlightInto(currentDoc, painter, currentSelection,false);}catch(BadLocationException ex){}
//        });
     
           
//         SwingUtilities.invokeLater(() -> {
//        if(findDialogVisible && (currentSelection != null)) {
//            findComBox.addItem(currentSelection);
//            findComBox.setSelectedIndex(findComBox.getItemCount()-1);
//        }
//        });
     
        }//end method 

      
 
        
        @Override
        public void mouseDragged(MouseEvent e) {
     

         
         
         
         
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
         currentSelection =  textPane.getSelectedText();
//           
//           SwingUtilities.invokeLater(() -> {
//          painter =  new CodePadHighLighter(Color.GREEN);
//       try{ 
//           if(currentSelection != null && currentSelection.length() > 0)
//           addHighlightInto(currentDoc, painter, currentSelection,false);
//       
//       }catch(BadLocationException ex){}
//       
//        });
//           
//           
        SwingUtilities.invokeLater(() -> {
        if((currentSelection != null)) {
            findComBox.addItem(currentSelection);
            findComBox.setSelectedIndex(findComBox.getItemCount()-1);
         
        }
        });
     
            
        }
        
        
    
        
});
    
    textPane.addMouseMotionListener(new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
            
            
               
        currentSelection =  textPane.getSelectedText();
        
        
//            try {
//                Document doc  =textPane.getDocument();  
//                Caret    c  =  textPane.getCaret();
//               currentSelection =  doc.getText(c.getDot(), c.getMark()+1);
//                System.out.println("currentSelection>>: "+currentSelection);
//            } catch (BadLocationException xp) {
//            }
            
            
    SwingUtilities.invokeLater(() -> {
               
       
       //  currentSelection =  textPane.getSelectedText();
                
         updateLineLables(textPane);
     updateSelectionLables(textPane); 
       
        });
     


//        SwingUtilities.invokeLater(() -> {
//        if(findDialogVisible && (currentSelection != null)) {
//            findComBox.addItem(currentSelection);
//            findComBox.setSelectedIndex(findComBox.getItemCount()-1);
//         
//        }
//        });
     
        }//end method 

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    });
  
       textPane.addCaretListener((e) -> {
        updateLineLables(textPane);
   
    });
    
    
        
    
    
    
    }//end method 
    
private void updateLineLables(JTextPane  textPane){
    
   Caret  e  =  textPane.getCaret(); 
 try {
            Document  doc  =  textPane.getDocument();
            String text  = doc.getText(0, e.getDot());
            int ln  =  lineNumber(text);
            int colm = 0;
            Element  elem  =  doc.getDefaultRootElement();
            Element  line  =  elem.getElement(ln-1);
            String thisLine  = text.substring(line.getStartOffset());
          
         int  x =  thisLine.length()+1;
        SwingUtilities.invokeLater(() -> {
        currentLine_lb.setText(nf.format(getLineNumbers(textPane)));
        col_lb.setText(nf.format(x));
        
        updateSelectionLables(textPane);

        });
       
         lastEdit = e.getDot(); 
        } catch (BadLocationException ex) {}


}
private void updateSelectionLables(JTextPane  textPane){
    
Caret  e  =  textPane.getCaret();
    
if(e.getDot() > e.getMark()){
            
        selLines.setText(nf.format( (e.getDot() - e.getMark()) - 1));
        sel_lb.setText(nf.format(lineNumber(textPane.getSelectedText())));
        
        }else if( e.getMark() > e.getDot()){
            
        selLines.setText(nf.format( e.getMark() - e.getDot()-1 ));
        sel_lb.setText(nf.format(lineNumber(textPane.getSelectedText())));
     
        }else { 
            selLines.setText("0");
            sel_lb.setText("0");}

}




    private JTextPane findTextPane(int tabIndex){
       
          documentPane  pane  =  (documentPane)tabPane.getComponentAt(tabIndex);
          JScrollPane     sp  =  (JScrollPane)pane.getComponent(0);
          JViewport      vp  =  (JViewport) sp.getComponent(0);
          JTextPane    textPane  = (JTextPane)vp.getComponent(0);
    return textPane;
    
    }
    

 
    private void bindKeyStrokes(){
    
        //obtain inputMap of the TextPane
//        InputMap docMap =  this.document.getInputMap();
//        
//        //obtain KeyStroke
//        int  map  =  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
//        KeyStrok e  cuts =  KeyStroke.getKeyStroke(KeyEvent.VK_X, map, false);
//        docMap.put(cuts, DefaultEditorKit.cutAction);
//        docMap.put(cuts, "Cut");
        
    
   
    
    }
   
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jOptionPane2 = new javax.swing.JOptionPane();
        fontDialog = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel4 = new javax.swing.JPanel();
        fontPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList<>();
        jTextField3 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        webManager = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        browserList = new javax.swing.JList<>();
        jPanel10 = new javax.swing.JPanel();
        addBtn_B = new javax.swing.JButton();
        removeBtn_b = new javax.swing.JButton();
        editBtn_b = new javax.swing.JButton();
        renameBtn_b = new javax.swing.JButton();
        browseBtn_B = new javax.swing.JButton();
        closeBtn_b = new javax.swing.JButton();
        defaultBrowserChbx = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        browserName = new javax.swing.JLabel();
        webPopmenuList = new javax.swing.JPopupMenu();
        firefox = new javax.swing.JMenuItem();
        chrome = new javax.swing.JMenuItem();
        opera = new javax.swing.JMenuItem();
        iexplorer = new javax.swing.JMenuItem();
        safari = new javax.swing.JMenuItem();
        webEdit = new javax.swing.JDialog();
        BrowserEditPane = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        locationField = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        hintBox = new javax.swing.JDialog();
        jScrollPane7 = new javax.swing.JScrollPane();
        hintList = new javax.swing.JList<>();
        findDialog = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        findTabpane = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jPanel19 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jCheckBox6 = new javax.swing.JCheckBox();
        jPanel20 = new javax.swing.JPanel();
        jButton21 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        findStatusLable = new javax.swing.JLabel();
        findnPane = new javax.swing.JPanel();
        findP = new javax.swing.JButton();
        findF = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        findComBox = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jPanel23 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jCheckBox11 = new javax.swing.JCheckBox();
        jPanel24 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jCheckBox12 = new javax.swing.JCheckBox();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jPanel27 = new javax.swing.JPanel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jCheckBox17 = new javax.swing.JCheckBox();
        jPanel28 = new javax.swing.JPanel();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jCheckBox18 = new javax.swing.JCheckBox();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        jPanel31 = new javax.swing.JPanel();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jCheckBox23 = new javax.swing.JCheckBox();
        jPanel32 = new javax.swing.JPanel();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jCheckBox24 = new javax.swing.JCheckBox();
        jButton37 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        gotoLineBox = new javax.swing.JDialog();
        jPanel34 = new javax.swing.JPanel();
        jRadioButton13 = new javax.swing.JRadioButton();
        jRadioButton14 = new javax.swing.JRadioButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        treePopupMenu = new javax.swing.JPopupMenu();
        addTreeDirPop = new javax.swing.JMenuItem();
        addNewTreeFilePop = new javax.swing.JMenuItem();
        removeTreeDirPop = new javax.swing.JMenuItem();
        deleteTreeDirPop = new javax.swing.JMenuItem();
        renameTreePop = new javax.swing.JMenuItem();
        openTreePop = new javax.swing.JMenuItem();
        relaodTreePop = new javax.swing.JMenuItem();
        removeAllTreePop = new javax.swing.JMenuItem();
        javaSourceBox = new javax.swing.JDialog();
        jPanel37 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jBoxText1 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jBoxText2 = new javax.swing.JTextField();
        jButton47 = new javax.swing.JButton();
        jchk1 = new javax.swing.JCheckBox();
        jchk3 = new javax.swing.JCheckBox();
        jLabel33 = new javax.swing.JLabel();
        jBoxText3 = new javax.swing.JTextField();
        jPanel38 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jradio6 = new javax.swing.JRadioButton();
        jradio7 = new javax.swing.JRadioButton();
        jradio8 = new javax.swing.JRadioButton();
        jradio1 = new javax.swing.JRadioButton();
        jradio2 = new javax.swing.JRadioButton();
        jradio3 = new javax.swing.JRadioButton();
        jradio5 = new javax.swing.JRadioButton();
        jradio4 = new javax.swing.JRadioButton();
        jFinishBtn = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jchk2 = new javax.swing.JCheckBox();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        mainDialogBox = new javax.swing.JDialog();
        mainPaneDialog = new javax.swing.JPanel();
        statusPane = new javax.swing.JPanel();
        fileType = new javax.swing.JLabel();
        fileLen = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lineNumberLable = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        currentLine_lb = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        col_lb = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        selLines = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        sel_lb = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        tabPane = new javax.swing.JTabbedPane();
        jPanel1 = new documentPane(lineNumberLable);
        MainScrollPane = new javax.swing.JScrollPane();
        document = new TextPaneDoc();
        toolBarPane = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        newBtn = new javax.swing.JButton();
        openBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        saveAllBtn = new javax.swing.JButton();
        closeTabBtn = new javax.swing.JButton();
        closeAllTabsBtn = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        zoomIn = new javax.swing.JButton();
        zoomOut = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        cutBtn = new javax.swing.JButton();
        copyBtn = new javax.swing.JButton();
        pasteBtn = new javax.swing.JButton();
        themBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jToolBar4 = new javax.swing.JToolBar();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        showTerminalToggle = new javax.swing.JToggleButton();
        jButton15 = new javax.swing.JButton();
        jToolBar6 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jToolBar7 = new javax.swing.JToolBar();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        jToolBar8 = new javax.swing.JToolBar();
        browserBtn = DropDownButtonFactory.createDropDownButton(createIcont("/codepade/img/Globe-Internet-icon.png"), webPopmenuList);
        jToolBar9 = new javax.swing.JToolBar();
        jButton9 = new javax.swing.JButton();
        jOptionPane1 = new javax.swing.JOptionPane();
        terminal = new javax.swing.JLayeredPane();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList<>();
        directoryPane = new javax.swing.JLayeredPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        treeScroll = new javax.swing.JScrollPane();
        directoryTree = new javax.swing.JTree();
        jPanel12 = new javax.swing.JPanel();
        treeToolBar = new javax.swing.JToolBar();
        browswNode = new javax.swing.JButton();
        newDirBtn = new javax.swing.JButton();
        deleteNodeBtn = new javax.swing.JButton();
        addLeafBtn = new javax.swing.JButton();
        openTreeBtn = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        docToolBar = new javax.swing.JPanel();
        jToolBar5 = new javax.swing.JToolBar();
        findPrevBtn = new javax.swing.JButton();
        findNextBtn = new javax.swing.JButton();
        hihglitToggle = new javax.swing.JToggleButton();
        jButton45 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jToolBar10 = new javax.swing.JToolBar();
        jToolBar11 = new javax.swing.JToolBar();
        commentBox = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newFileMenu = new javax.swing.JMenuItem();
        createFileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        open = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        recent = new javax.swing.JMenu();
        saveCmd = new javax.swing.JMenuItem();
        saveAsMenu = new javax.swing.JMenuItem();
        saveAllMenu = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        renameFileMenu = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        closeTabMenu = new javax.swing.JMenuItem();
        closeAllTabsMenu = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        OARFMenu = new javax.swing.JMenuItem();
        cRFLMenu = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        exitCmd = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        searchMenu = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        zoomInMenu = new javax.swing.JMenuItem();
        zoomOutMenu = new javax.swing.JMenuItem();
        viewTerminal = new javax.swing.JCheckBoxMenuItem();
        showTermAlway = new javax.swing.JCheckBoxMenuItem();
        showDPCheck = new javax.swing.JCheckBoxMenuItem();
        jMenu1 = new javax.swing.JMenu();
        toolBarChk = new javax.swing.JCheckBoxMenuItem();
        docToolBarchk = new javax.swing.JCheckBoxMenuItem();
        runMenu = new javax.swing.JMenu();
        webMenu = new javax.swing.JMenu();
        firefoxItem = new javax.swing.JMenuItem();
        chromeItem = new javax.swing.JMenuItem();
        operaItem = new javax.swing.JMenuItem();
        iexplorerItem = new javax.swing.JMenuItem();
        safariItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        settingMenu = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        showDirPaneMenu = new javax.swing.JCheckBoxMenuItem();
        helpMenu = new javax.swing.JMenu();

        fontDialog.setModal(true);
        fontDialog.setResizable(false);
        fontDialog.setSize(new java.awt.Dimension(667, 488));
        fontDialog.setType(java.awt.Window.Type.POPUP);

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Styles Settings", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 18)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N

        jList1.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Font Styles", "Background/Foreground", "Extensions", " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel1.setText("Font:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel3.setText("Font Style:");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel4.setText("Size:");

        jScrollPane3.setViewportView(jList2);

        jList3.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Plain", "Bold", "Italic", "Bold Italic" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList3);

        jScrollPane5.setViewportView(jList4);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Font Preview"));

        jLabel5.setText("Don't forget your mission");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(305, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel5)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout fontPaneLayout = new javax.swing.GroupLayout(fontPane);
        fontPane.setLayout(fontPaneLayout);
        fontPaneLayout.setHorizontalGroup(
            fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fontPaneLayout.createSequentialGroup()
                .addGroup(fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addGroup(fontPaneLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                        .addComponent(jTextField2))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fontPaneLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        fontPaneLayout.setVerticalGroup(
            fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fontPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2)
                .addGroup(fontPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane5))
                .addGap(11, 11, 11)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(fontPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fontPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jButton10.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton10.setText("OK");

        jButton11.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton11.setText("Apply");

        jButton12.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton12.setText("Cancel");

        javax.swing.GroupLayout fontDialogLayout = new javax.swing.GroupLayout(fontDialog.getContentPane());
        fontDialog.getContentPane().setLayout(fontDialogLayout);
        fontDialogLayout.setHorizontalGroup(
            fontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fontDialogLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(fontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fontDialogLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jButton10)
                        .addGap(18, 18, 18)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton12)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fontDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        fontDialogLayout.setVerticalGroup(
            fontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fontDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fontDialogLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(fontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton12)
                            .addComponent(jButton11)
                            .addComponent(jButton10))
                        .addContainerGap())
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        webManager.setTitle("Web Browser Manager");
        webManager.setIconImage(getIconImage());
        webManager.setModal(true);
        webManager.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        webManager.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        webManager.setResizable(false);
        webManager.setSize(new java.awt.Dimension(510, 380));
        webManager.setType(java.awt.Window.Type.POPUP);

        browserList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                browserListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(browserList);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
        );

        addBtn_B.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addBtn_B.setText("Add");
        addBtn_B.setEnabled(false);
        addBtn_B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtn_BActionPerformed(evt);
            }
        });

        removeBtn_b.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        removeBtn_b.setText("Remove");
        removeBtn_b.setEnabled(false);
        removeBtn_b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtn_bActionPerformed(evt);
            }
        });

        editBtn_b.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        editBtn_b.setText("Edit");
        editBtn_b.setEnabled(false);
        editBtn_b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtn_bActionPerformed(evt);
            }
        });

        renameBtn_b.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        renameBtn_b.setText("Rename");
        renameBtn_b.setEnabled(false);
        renameBtn_b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameBtn_bActionPerformed(evt);
            }
        });

        browseBtn_B.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        browseBtn_B.setText("Browse");
        browseBtn_B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBtn_BActionPerformed(evt);
            }
        });

        closeBtn_b.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        closeBtn_b.setText("Close");
        closeBtn_b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtn_bActionPerformed(evt);
            }
        });

        defaultBrowserChbx.setText("Set Default");
        defaultBrowserChbx.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                defaultBrowserChbxItemStateChanged(evt);
            }
        });
        defaultBrowserChbx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultBrowserChbxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(defaultBrowserChbx)
                    .addComponent(closeBtn_b, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addBtn_B)
                    .addComponent(removeBtn_b)
                    .addComponent(editBtn_b)
                    .addComponent(renameBtn_b)
                    .addComponent(browseBtn_B))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jPanel10Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addBtn_B, browseBtn_B, closeBtn_b, editBtn_b, removeBtn_b, renameBtn_b});

        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(browseBtn_B)
                .addGap(18, 18, 18)
                .addComponent(renameBtn_b)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addBtn_B)
                .addGap(18, 18, 18)
                .addComponent(editBtn_b)
                .addGap(18, 18, 18)
                .addComponent(removeBtn_b)
                .addGap(18, 18, 18)
                .addComponent(defaultBrowserChbx)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeBtn_b, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        jPanel10Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addBtn_B, browseBtn_B, editBtn_b, removeBtn_b, renameBtn_b});

        jLabel8.setText("Browser Name: ");

        browserName.setText("Broswer");

        javax.swing.GroupLayout webManagerLayout = new javax.swing.GroupLayout(webManager.getContentPane());
        webManager.getContentPane().setLayout(webManagerLayout);
        webManagerLayout.setHorizontalGroup(
            webManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(webManagerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(webManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(webManagerLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(browserName, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        webManagerLayout.setVerticalGroup(
            webManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(webManagerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(webManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(webManagerLayout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(webManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(browserName))
                        .addContainerGap(15, Short.MAX_VALUE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        webManager.getAccessibleContext().setAccessibleParent(this);

        firefox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Internet-firefox-icon.png"))); // NOI18N
        firefox.setText("Mozilla FireFox");
        firefox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firefoxActionPerformed(evt);
            }
        });
        webPopmenuList.add(firefox);

        chrome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Google-Chrome-icon.png"))); // NOI18N
        chrome.setText("Google Chrome");
        chrome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chromeActionPerformed(evt);
            }
        });
        webPopmenuList.add(chrome);

        opera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Opera-icon2.png"))); // NOI18N
        opera.setText("Opera");
        opera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                operaActionPerformed(evt);
            }
        });
        webPopmenuList.add(opera);

        iexplorer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Internet-Explorer-icon.png"))); // NOI18N
        iexplorer.setText("Internet Explorer");
        iexplorer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iexplorerActionPerformed(evt);
            }
        });
        webPopmenuList.add(iexplorer);

        safari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Safari-icon2.png"))); // NOI18N
        safari.setText("Safari");
        safari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                safariActionPerformed(evt);
            }
        });
        webPopmenuList.add(safari);

        webEdit.setTitle("Edit Location");
        webEdit.setModal(true);
        webEdit.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        webEdit.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        webEdit.setResizable(false);
        webEdit.setSize(new java.awt.Dimension(540, 115));
        webEdit.setType(java.awt.Window.Type.POPUP);

        jLabel10.setText("New Location:");

        jButton4.setText("Browse...");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("OK");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BrowserEditPaneLayout = new javax.swing.GroupLayout(BrowserEditPane);
        BrowserEditPane.setLayout(BrowserEditPaneLayout);
        BrowserEditPaneLayout.setHorizontalGroup(
            BrowserEditPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BrowserEditPaneLayout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationField, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
            .addGroup(BrowserEditPaneLayout.createSequentialGroup()
                .addGap(213, 213, 213)
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BrowserEditPaneLayout.setVerticalGroup(
            BrowserEditPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BrowserEditPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BrowserEditPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(BrowserEditPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(locationField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
        );

        BrowserEditPaneLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton4, jLabel10, locationField});

        javax.swing.GroupLayout webEditLayout = new javax.swing.GroupLayout(webEdit.getContentPane());
        webEdit.getContentPane().setLayout(webEditLayout);
        webEditLayout.setHorizontalGroup(
            webEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(webEditLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BrowserEditPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        webEditLayout.setVerticalGroup(
            webEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(webEditLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BrowserEditPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        webEdit.getAccessibleContext().setAccessibleParent(webManager);

        hintBox.setUndecorated(true);
        hintBox.setSize(new java.awt.Dimension(151, 195));
        hintBox.setType(java.awt.Window.Type.UTILITY);

        hintList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "java" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(hintList);

        javax.swing.GroupLayout hintBoxLayout = new javax.swing.GroupLayout(hintBox.getContentPane());
        hintBox.getContentPane().setLayout(hintBoxLayout);
        hintBoxLayout.setHorizontalGroup(
            hintBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
        );
        hintBoxLayout.setVerticalGroup(
            hintBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        hintBox.getAccessibleContext().setAccessibleParent(null);

        findDialog.setTitle("Find");
        findDialog.setAlwaysOnTop(true);
        findDialog.setFocusCycleRoot(false);
        findDialog.setFocusable(false);
        findDialog.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        findDialog.setPreferredSize(new java.awt.Dimension(620, 392));
        findDialog.setResizable(false);
        findDialog.setSize(new java.awt.Dimension(620, 392));
        findDialog.setType(java.awt.Window.Type.UTILITY);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${location}"), findDialog, org.jdesktop.beansbinding.BeanProperty.create("location"));
        bindingGroup.addBinding(binding);

        findDialog.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                findDialogFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                findDialogFocusLost(evt);
            }
        });
        findDialog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                findDialogMouseClicked(evt);
            }
        });
        findDialog.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                findDialogWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                findDialogWindowLostFocus(evt);
            }
        });
        findDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                findDialogWindowClosing(evt);
            }
        });

        findTabpane.setBackground(new java.awt.Color(213, 220, 238));
        findTabpane.setFocusable(false);
        findTabpane.setOpaque(true);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel18MouseClicked(evt);
            }
        });

        jLabel14.setText("Find What:");

        jCheckBox2.setText("Backward Direction");
        jCheckBox2.setFocusPainted(false);
        jCheckBox2.setOpaque(false);

        jCheckBox3.setText("Match whole word wnly");
        jCheckBox3.setFocusPainted(false);
        jCheckBox3.setOpaque(false);

        jCheckBox4.setText("Match case");
        jCheckBox4.setFocusPainted(false);
        jCheckBox4.setOpaque(false);

        jCheckBox5.setText("Wrap around");
        jCheckBox5.setFocusPainted(false);
        jCheckBox5.setOpaque(false);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(213, 220, 238), 2), "Search Mode"));
        jPanel19.setOpaque(false);

        jRadioButton1.setText("Normal");
        jRadioButton1.setFocusPainted(false);
        jRadioButton1.setOpaque(false);

        jRadioButton2.setText("Extended");
        jRadioButton2.setFocusPainted(false);
        jRadioButton2.setOpaque(false);

        jRadioButton3.setText("Regular Expression");
        jRadioButton3.setFocusPainted(false);
        jRadioButton3.setOpaque(false);

        jCheckBox6.setText("Matches newline");
        jCheckBox6.setFocusPainted(false);
        jCheckBox6.setOpaque(false);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jRadioButton3)
                        .addGap(47, 47, 47)
                        .addComponent(jCheckBox6)))
                .addGap(0, 95, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jCheckBox6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setOpaque(false);

        jButton21.setText("<html><center>Find All In Opened <br/>Documents</center></html>");
        jButton21.setFocusPainted(false);
        jButton21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton23.setText("<html><center>Find All In Current<br/>Document</center></html>");
        jButton23.setFocusPainted(false);
        jButton23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton23.setPreferredSize(new java.awt.Dimension(95, 23));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton20.setText("Count");
        jButton20.setFocusPainted(false);

        jButton24.setText("Close");
        jButton24.setFocusPainted(false);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton21)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20)
                    .addComponent(jButton24))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton20, jButton21, jButton23, jButton24});

        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton24)
                .addContainerGap(122, Short.MAX_VALUE))
        );

        jPanel20Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton21, jButton23});

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(findStatusLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(findStatusLable, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        findnPane.setOpaque(false);

        findP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1leftarrow.png"))); // NOI18N
        findP.setBorderPainted(false);
        findP.setFocusPainted(false);
        findnPane.add(findP);

        findF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1rightarrow.png"))); // NOI18N
        findF.setBorderPainted(false);
        findF.setFocusPainted(false);
        findnPane.add(findF);

        jCheckBox1.setFocusPainted(false);
        jCheckBox1.setOpaque(false);
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        findnPane.add(jCheckBox1);

        findComBox.setEditable(true);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(findComBox, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(findnPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox5)
                            .addComponent(jCheckBox4)
                            .addComponent(jCheckBox3)
                            .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel14)
                        .addComponent(findnPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(findComBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jCheckBox2)
                        .addGap(0, 0, 0)
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox5)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel18Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {findComBox, jLabel14});

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        findTabpane.addTab("Find", jPanel14);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel22MouseClicked(evt);
            }
        });

        jLabel16.setText("Find What:");

        jTextField5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField5MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField5MousePressed(evt);
            }
        });

        jCheckBox7.setText("Backward Direction");
        jCheckBox7.setOpaque(false);

        jCheckBox8.setText("Match whole word wnly");
        jCheckBox8.setOpaque(false);

        jCheckBox9.setText("Match case");
        jCheckBox9.setOpaque(false);

        jCheckBox10.setText("Wrap around");
        jCheckBox10.setOpaque(false);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(213, 220, 238), 2), "Search Mode"));
        jPanel23.setOpaque(false);

        jRadioButton4.setText("Normal");
        jRadioButton4.setOpaque(false);

        jRadioButton5.setText("Extended");
        jRadioButton5.setOpaque(false);

        jRadioButton6.setText("Regular Expression");
        jRadioButton6.setOpaque(false);

        jCheckBox11.setText("Matches newline");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton5)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jRadioButton6)
                        .addGap(47, 47, 47)
                        .addComponent(jCheckBox11)))
                .addGap(0, 95, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton6)
                    .addComponent(jCheckBox11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel24.setOpaque(false);

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1rightarrow.png"))); // NOI18N
        jButton19.setBorderPainted(false);
        jButton19.setFocusPainted(false);

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1leftarrow.png"))); // NOI18N
        jButton22.setBorderPainted(false);
        jButton22.setFocusPainted(false);

        jButton25.setText("<html><center>Find All In Opened <br/>Documents</center></html>");
        jButton25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton26.setText("<html><center>Find All In Current<br/>Document</center></html>");
        jButton26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton26.setPreferredSize(new java.awt.Dimension(95, 23));

        jButton27.setText("Count");

        jButton28.setText("Close");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox12))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton25)
                            .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton27)
                            .addComponent(jButton28))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox12))
                .addGap(25, 25, 25)
                .addComponent(jButton27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton28)
                .addGap(0, 102, Short.MAX_VALUE))
        );

        jPanel25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox10)
                    .addComponent(jCheckBox9)
                    .addComponent(jCheckBox8)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBox7)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(jCheckBox7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox10)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        findTabpane.addTab("Replace", jPanel15);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel26MouseClicked(evt);
            }
        });

        jLabel21.setText("Find What:");

        jTextField6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField6MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField6MousePressed(evt);
            }
        });

        jCheckBox13.setText("Backward Direction");
        jCheckBox13.setOpaque(false);

        jCheckBox14.setText("Match whole word wnly");
        jCheckBox14.setOpaque(false);

        jCheckBox15.setText("Match case");
        jCheckBox15.setOpaque(false);

        jCheckBox16.setText("Wrap around");
        jCheckBox16.setOpaque(false);

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(213, 220, 238), 2), "Search Mode"));
        jPanel27.setOpaque(false);

        jRadioButton7.setText("Normal");
        jRadioButton7.setOpaque(false);

        jRadioButton8.setText("Extended");
        jRadioButton8.setOpaque(false);

        jRadioButton9.setText("Regular Expression");
        jRadioButton9.setOpaque(false);

        jCheckBox17.setText("Matches newline");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton7)
                    .addComponent(jRadioButton8)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jRadioButton9)
                        .addGap(47, 47, 47)
                        .addComponent(jCheckBox17)))
                .addGap(0, 95, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton9)
                    .addComponent(jCheckBox17))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel28.setOpaque(false);

        jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1rightarrow.png"))); // NOI18N
        jButton29.setBorderPainted(false);
        jButton29.setFocusPainted(false);

        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1leftarrow.png"))); // NOI18N
        jButton30.setBorderPainted(false);
        jButton30.setFocusPainted(false);

        jButton31.setText("<html><center>Find All In Opened <br/>Documents</center></html>");
        jButton31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton32.setText("<html><center>Find All In Current<br/>Document</center></html>");
        jButton32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton32.setPreferredSize(new java.awt.Dimension(95, 23));

        jButton33.setText("Count");

        jButton34.setText("Close");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox18))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton31)
                            .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton33)
                            .addComponent(jButton34))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox18))
                .addGap(25, 25, 25)
                .addComponent(jButton33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton34)
                .addGap(0, 102, Short.MAX_VALUE))
        );

        jPanel29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox16)
                    .addComponent(jCheckBox15)
                    .addComponent(jCheckBox14)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBox13)
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel26Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(jCheckBox13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox16)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        findTabpane.addTab("Find in File", jPanel16);

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel30MouseClicked(evt);
            }
        });

        jLabel22.setText("Find What:");

        jTextField7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField7MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField7MousePressed(evt);
            }
        });

        jCheckBox19.setText("Backward Direction");
        jCheckBox19.setOpaque(false);

        jCheckBox20.setText("Match whole word wnly");
        jCheckBox20.setOpaque(false);

        jCheckBox21.setText("Match case");
        jCheckBox21.setOpaque(false);

        jCheckBox22.setText("Wrap around");
        jCheckBox22.setOpaque(false);

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(213, 220, 238), 2), "Search Mode"));
        jPanel31.setOpaque(false);

        jRadioButton10.setText("Normal");
        jRadioButton10.setOpaque(false);

        jRadioButton11.setText("Extended");
        jRadioButton11.setOpaque(false);

        jRadioButton12.setText("Regular Expression");
        jRadioButton12.setOpaque(false);

        jCheckBox23.setText("Matches newline");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton10)
                    .addComponent(jRadioButton11)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jRadioButton12)
                        .addGap(47, 47, 47)
                        .addComponent(jCheckBox23)))
                .addGap(0, 95, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton12)
                    .addComponent(jCheckBox23))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel32.setOpaque(false);

        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1rightarrow.png"))); // NOI18N
        jButton35.setBorderPainted(false);
        jButton35.setFocusPainted(false);

        jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1leftarrow.png"))); // NOI18N
        jButton36.setBorderPainted(false);
        jButton36.setFocusPainted(false);

        jButton37.setText("<html><center>Find All In Opened <br/>Documents</center></html>");
        jButton37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton38.setText("<html><center>Find All In Current<br/>Document</center></html>");
        jButton38.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton38.setPreferredSize(new java.awt.Dimension(95, 23));

        jButton39.setText("Count");

        jButton40.setText("Close");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox24))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton37)
                            .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton39)
                            .addComponent(jButton40))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox24))
                .addGap(25, 25, 25)
                .addComponent(jButton39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton40)
                .addGap(0, 102, Short.MAX_VALUE))
        );

        jPanel33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox22)
                    .addComponent(jCheckBox21)
                    .addComponent(jCheckBox20)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBox19)
                    .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel30Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(jCheckBox19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox22)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        findTabpane.addTab("Mark", jPanel17);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(findTabpane)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(findTabpane)
        );

        javax.swing.GroupLayout findDialogLayout = new javax.swing.GroupLayout(findDialog.getContentPane());
        findDialog.getContentPane().setLayout(findDialogLayout);
        findDialogLayout.setHorizontalGroup(
            findDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        findDialogLayout.setVerticalGroup(
            findDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        gotoLineBox.setModal(true);
        gotoLineBox.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        gotoLineBox.setResizable(false);
        gotoLineBox.setSize(new java.awt.Dimension(384, 150));
        gotoLineBox.setType(java.awt.Window.Type.UTILITY);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setPreferredSize(new java.awt.Dimension(384, 140));

        jRadioButton13.setText("Go to line");
        jRadioButton13.setFocusPainted(false);
        jRadioButton13.setOpaque(false);

        jRadioButton14.setText("Go to offset");
        jRadioButton14.setFocusPainted(false);
        jRadioButton14.setOpaque(false);

        jLabel23.setText("You are here:");

        jLabel24.setText("0");

        jLabel25.setText("You want to go to: ");

        jTextField8.setOpaque(false);

        jButton17.setText("Go");
        jButton17.setFocusPainted(false);
        jButton17.setOpaque(false);

        jLabel26.setText("Total Lines: ");

        jLabel27.setText("0");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton13)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jRadioButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton17))
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 19, Short.MAX_VALUE))))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton13)
                    .addComponent(jRadioButton14))
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout gotoLineBoxLayout = new javax.swing.GroupLayout(gotoLineBox.getContentPane());
        gotoLineBox.getContentPane().setLayout(gotoLineBoxLayout);
        gotoLineBoxLayout.setHorizontalGroup(
            gotoLineBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        gotoLineBoxLayout.setVerticalGroup(
            gotoLineBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
        );

        gotoLineBox.getAccessibleContext().setAccessibleParent(this);

        addTreeDirPop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Picture2.png"))); // NOI18N
        addTreeDirPop.setText("Add New Directory");
        treePopupMenu.add(addTreeDirPop);

        addNewTreeFilePop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/add.gif"))); // NOI18N
        addNewTreeFilePop.setText("Add New File");
        treePopupMenu.add(addNewTreeFilePop);

        removeTreeDirPop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Picture3.png"))); // NOI18N
        removeTreeDirPop.setText("Remove");
        treePopupMenu.add(removeTreeDirPop);

        deleteTreeDirPop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Delete-file-icon.png"))); // NOI18N
        deleteTreeDirPop.setText("Delete");
        treePopupMenu.add(deleteTreeDirPop);

        renameTreePop.setText("Rename");
        treePopupMenu.add(renameTreePop);

        openTreePop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/edit-new-document-icon2.png"))); // NOI18N
        openTreePop.setText("Open");
        treePopupMenu.add(openTreePop);

        relaodTreePop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/restore.png"))); // NOI18N
        relaodTreePop.setText("Reload ");
        treePopupMenu.add(relaodTreePop);

        removeAllTreePop.setText("Remove All");
        treePopupMenu.add(removeAllTreePop);

        javaSourceBox.setTitle("Create New Java Source File");
        javaSourceBox.setBackground(new java.awt.Color(255, 255, 255));
        javaSourceBox.setModal(true);
        javaSourceBox.setPreferredSize(new java.awt.Dimension(500, 370));
        javaSourceBox.setResizable(false);
        javaSourceBox.setSize(new java.awt.Dimension(530, 370));
        javaSourceBox.setType(java.awt.Window.Type.POPUP);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setPreferredSize(new java.awt.Dimension(488, 330));

        jLabel31.setText("File Name: ");

        jLabel32.setText("Save To:");

        jButton47.setText("Browse...");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        jchk1.setSelected(true);
        jchk1.setText("Use Selected File Location");
        jchk1.setOpaque(false);
        jchk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchk1ActionPerformed(evt);
            }
        });

        jchk3.setText("Add package Declaration");
        jchk3.setOpaque(false);
        jchk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchk3ActionPerformed(evt);
            }
        });

        jLabel33.setText("Package Name:");

        jBoxText3.setEnabled(false);

        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Options"));
        jPanel38.setOpaque(false);

        jComboBox2.setEditable(true);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Object", "Applet", "Application", " " }));

        buttonGroup2.add(jradio6);
        jradio6.setText("Add main method");
        jradio6.setOpaque(false);

        buttonGroup2.add(jradio7);
        jradio7.setText("Add init method");
        jradio7.setOpaque(false);

        buttonGroup2.add(jradio8);
        jradio8.setText("Add start method");
        jradio8.setOpaque(false);

        buttonGroup1.add(jradio1);
        jradio1.setSelected(true);
        jradio1.setText("Class");
        jradio1.setOpaque(false);
        jradio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jradio1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jradio2);
        jradio2.setText("Interface");
        jradio2.setOpaque(false);
        jradio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jradio2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jradio3);
        jradio3.setText("Abstrac Class");
        jradio3.setOpaque(false);
        jradio3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jradio3ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jradio5);
        jradio5.setText("implements");
        jradio5.setOpaque(false);

        buttonGroup3.add(jradio4);
        jradio4.setSelected(true);
        jradio4.setText("extends");
        jradio4.setOpaque(false);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(jradio1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jradio2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jradio3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jradio4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jradio5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(jradio6)
                .addGap(52, 52, 52)
                .addComponent(jradio7)
                .addGap(64, 64, 64)
                .addComponent(jradio8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jradio1)
                    .addComponent(jradio2)
                    .addComponent(jradio3)
                    .addComponent(jradio5)
                    .addComponent(jradio4))
                .addGap(18, 18, 18)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jradio6)
                    .addComponent(jradio7)
                    .addComponent(jradio8))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jFinishBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jFinishBtn.setText("Finish");
        jFinishBtn.setEnabled(false);

        jButton44.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton44.setText("Cancel");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jchk2.setText("Use New Folder");
        jchk2.setOpaque(false);
        jchk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchk2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jFinishBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jBoxText1))
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(18, 18, 18)
                                .addComponent(jBoxText2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton47, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jchk1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jchk2)
                                .addGap(89, 89, 89)
                                .addComponent(jchk3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jBoxText3)))
                        .addGap(10, 10, 10))))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBoxText1, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jBoxText2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jchk1)
                    .addComponent(jchk3)
                    .addComponent(jchk2))
                .addGap(18, 18, 18)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jBoxText3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFinishBtn)
                    .addComponent(jButton44))
                .addGap(39, 39, 39))
        );

        jPanel37Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBoxText1, jBoxText2, jBoxText3, jButton47, jLabel32, jLabel33});

        javax.swing.GroupLayout javaSourceBoxLayout = new javax.swing.GroupLayout(javaSourceBox.getContentPane());
        javaSourceBox.getContentPane().setLayout(javaSourceBoxLayout);
        javaSourceBoxLayout.setHorizontalGroup(
            javaSourceBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
        );
        javaSourceBoxLayout.setVerticalGroup(
            javaSourceBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
        );

        javaSourceBox.getAccessibleContext().setAccessibleParent(this);

        mainDialogBox.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        mainDialogBox.setIconImage(getIconImage());
        mainDialogBox.setModal(true);
        mainDialogBox.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        mainDialogBox.setResizable(false);
        mainDialogBox.setSize(new java.awt.Dimension(489, 360));
        mainDialogBox.setType(java.awt.Window.Type.POPUP);

        mainPaneDialog.setPreferredSize(new java.awt.Dimension(489, 330));

        javax.swing.GroupLayout mainDialogBoxLayout = new javax.swing.GroupLayout(mainDialogBox.getContentPane());
        mainDialogBox.getContentPane().setLayout(mainDialogBoxLayout);
        mainDialogBoxLayout.setHorizontalGroup(
            mainDialogBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPaneDialog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainDialogBoxLayout.setVerticalGroup(
            mainDialogBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPaneDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CodePad");
        setFocusCycleRoot(false);
        setIconImage(createIcont("img/TextPad.png").getImage()
        );
        setLocation(new java.awt.Point(0, 0));
        setName("window"); // NOI18N
        setSize((int)dm.getWidth()-100, (int)dm.getHeight()-400);
        setType(java.awt.Window.Type.POPUP);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        statusPane.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(0, 0, 0)));

        fileType.setText(" Normal text file");

        fileLen.setText("0");

        jLabel2.setText("Lenght:");

        jLabel6.setText("Line:");

        lineNumberLable.setText("1");

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel9.setText("Ln:");

        currentLine_lb.setText("1");

        jLabel11.setText("Col:");

        col_lb.setText("1");

        jLabel13.setText("Sel:");

        selLines.setText("0");

        jLabel15.setText("|");

        sel_lb.setText("0");

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel17.setText("Encoding:");

        jLabel18.setText("UTF-8");

        jLabel19.setText("INS");

        javax.swing.GroupLayout statusPaneLayout = new javax.swing.GroupLayout(statusPane);
        statusPane.setLayout(statusPaneLayout);
        statusPaneLayout.setHorizontalGroup(
            statusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileType, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(190, 190, 190)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileLen, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lineNumberLable, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentLine_lb)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(col_lb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selLines)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sel_lb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addContainerGap())
        );
        statusPaneLayout.setVerticalGroup(
            statusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPaneLayout.createSequentialGroup()
                .addGroup(statusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator8)
                        .addGroup(statusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                            .addComponent(fileType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                            .addGroup(statusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fileLen, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lineNumberLable, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                            .addGroup(statusPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(statusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(currentLine_lb)
                                    .addComponent(jLabel11)
                                    .addComponent(col_lb)
                                    .addComponent(jLabel13)
                                    .addComponent(selLines)
                                    .addComponent(jLabel15)
                                    .addComponent(sel_lb)))))
                    .addGroup(statusPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(statusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statusPaneLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fileLen, fileType, jLabel2, jLabel6, jSeparator6, jSeparator7, lineNumberLable});

        tabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPane.setFocusable(false);

        MainScrollPane.setBorder(null);
        MainScrollPane.setViewportBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        document.setBackground(new java.awt.Color(71, 71, 71));
        document.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        document.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        document.setForeground(new java.awt.Color(255, 255, 255));
        document.setCaretColor(new java.awt.Color(255, 255, 255));
        document.setDragEnabled(true);
        document.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                documentCaretUpdate(evt);
            }
        });
        document.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                documentMouseDragged(evt);
            }
        });
        document.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                documentMouseClicked(evt);
            }
        });
        MainScrollPane.setViewportView(document);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(MainScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(MainScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );

        tabPane.addTab("tab1", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        toolBarPane.setBackground(new java.awt.Color(213, 220, 238));
        toolBarPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setOpaque(false);

        newBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/new-doc-icon.png"))); // NOI18N
        newBtn.setText("New");
        newBtn.setToolTipText("Create New File");
        newBtn.setFocusable(false);
        newBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newBtn.setOpaque(false);
        newBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(newBtn);

        openBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/open-file-icon.png"))); // NOI18N
        openBtn.setText("Open");
        openBtn.setToolTipText("Open File");
        openBtn.setFocusable(false);
        openBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openBtn.setOpaque(false);
        openBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(openBtn);

        saveBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/floppy-icosn.png"))); // NOI18N
        saveBtn.setText("Save");
        saveBtn.setToolTipText("Save File");
        saveBtn.setEnabled(false);
        saveBtn.setFocusable(false);
        saveBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveBtn.setOpaque(false);
        saveBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(saveBtn);
        saveBtn.setToolTipText("Save file");

        saveAllBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/save_all.png"))); // NOI18N
        saveAllBtn.setText("Save All");
        saveAllBtn.setToolTipText("Save All Files");
        saveAllBtn.setEnabled(false);
        saveAllBtn.setFocusable(false);
        saveAllBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveAllBtn.setOpaque(false);
        saveAllBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveAllBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAllBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(saveAllBtn);

        closeTabBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Delete-file-icon.png"))); // NOI18N
        closeTabBtn.setText("Close");
        closeTabBtn.setToolTipText("Close File");
        closeTabBtn.setFocusable(false);
        closeTabBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        closeTabBtn.setOpaque(false);
        closeTabBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        closeTabBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeTabBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(closeTabBtn);

        closeAllTabsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/close.png"))); // NOI18N
        closeAllTabsBtn.setText("Close All");
        closeAllTabsBtn.setToolTipText("Close All Files");
        closeAllTabsBtn.setFocusable(false);
        closeAllTabsBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        closeAllTabsBtn.setOpaque(false);
        closeAllTabsBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        closeAllTabsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeAllTabsBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(closeAllTabsBtn);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/print.png"))); // NOI18N
        jButton6.setText("Print");
        jButton6.setToolTipText("Print File");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setOpaque(false);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jToolBar2.setRollover(true);
        jToolBar2.setOpaque(false);

        zoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Zoom-In-3icon.png"))); // NOI18N
        zoomIn.setText("Zoom IN");
        zoomIn.setToolTipText("Zoom In");
        zoomIn.setFocusable(false);
        zoomIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomIn.setOpaque(false);
        zoomIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInActionPerformed(evt);
            }
        });
        jToolBar2.add(zoomIn);

        zoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Zoom-Out3-icon.png"))); // NOI18N
        zoomOut.setText("Zoom Out");
        zoomOut.setToolTipText("Zoom Out");
        zoomOut.setFocusable(false);
        zoomOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomOut.setOpaque(false);
        zoomOut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutActionPerformed(evt);
            }
        });
        jToolBar2.add(zoomOut);

        jToolBar3.setRollover(true);
        jToolBar3.setOpaque(false);

        cutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/cuticon.png"))); // NOI18N
        cutBtn.setText("Cut");
        cutBtn.setToolTipText("Cut");
        cutBtn.setFocusable(false);
        cutBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cutBtn.setOpaque(false);
        cutBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(cutBtn);

        copyBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/copy-icon.png"))); // NOI18N
        copyBtn.setText("Copy");
        copyBtn.setToolTipText("Copy");
        copyBtn.setFocusable(false);
        copyBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        copyBtn.setOpaque(false);
        copyBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        copyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyBtnActionPerformed(evt);
            }
        });
        jToolBar3.add(copyBtn);

        pasteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Editing-Paste-icon.png"))); // NOI18N
        pasteBtn.setText("Paste");
        pasteBtn.setToolTipText("Paste");
        pasteBtn.setFocusable(false);
        pasteBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pasteBtn.setOpaque(false);
        pasteBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(pasteBtn);

        themBox.setEditable(true);
        themBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "graySkyblue", "whiteBlue", " " }));
        themBox.setFocusable(false);
        themBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                themBoxItemStateChanged(evt);
            }
        });
        themBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themBoxActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel7.setText("Theme Color:");

        jToolBar4.setRollover(true);
        jToolBar4.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jToolBar4.setOpaque(false);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/systemruncon.png"))); // NOI18N
        jButton13.setText("Compile");
        jButton13.setToolTipText("Compile");
        jButton13.setBorderPainted(false);
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setOpaque(false);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton13);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Applications-Run-icon_1.png"))); // NOI18N
        jButton14.setText("CMD");
        jButton14.setToolTipText("Run");
        jButton14.setBorderPainted(false);
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setOpaque(false);
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton14);

        showTerminalToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/terminal.png"))); // NOI18N
        showTerminalToggle.setText("Show Console");
        showTerminalToggle.setFocusable(false);
        showTerminalToggle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showTerminalToggle.setMargin(new java.awt.Insets(2, 17, 2, 17));
        showTerminalToggle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showTerminalToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTerminalToggleActionPerformed(evt);
            }
        });
        jToolBar4.add(showTerminalToggle);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/share-2-icon.png"))); // NOI18N
        jButton15.setText("Share");
        jButton15.setToolTipText("Share");
        jButton15.setBorderPainted(false);
        jButton15.setFocusable(false);
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.setOpaque(false);
        jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton15);

        jToolBar6.setRollover(true);
        jToolBar6.setOpaque(false);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/find_1.gif"))); // NOI18N
        jButton2.setText("Find");
        jButton2.setToolTipText("Find");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setOpaque(false);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/text-replace-icon.png"))); // NOI18N
        jButton3.setText("Replace");
        jButton3.setToolTipText("Replace");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setOpaque(false);
        jButton3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton3);

        jToolBar7.setRollover(true);
        jToolBar7.setOpaque(false);

        undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/undo-icon.png"))); // NOI18N
        undoButton.setText("Undo");
        undoButton.setToolTipText("Undo");
        undoButton.setFocusable(false);
        undoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undoButton.setOpaque(false);
        undoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar7.add(undoButton);

        redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/redo-icon.png"))); // NOI18N
        redoButton.setText("Redo");
        redoButton.setToolTipText("Redo");
        redoButton.setFocusable(false);
        redoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redoButton.setOpaque(false);
        redoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar7.add(redoButton);

        jToolBar8.setRollover(true);
        jToolBar8.setOpaque(false);

        browserBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Globe-Internet-icon.png"))); // NOI18N
        browserBtn.setText("Run ");
        browserBtn.setToolTipText("Run Broser");
        browserBtn.setFocusable(false);
        browserBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        browserBtn.setIconTextGap(5);
        browserBtn.setOpaque(false);
        browserBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        browserBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        browserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browserBtnActionPerformed(evt);
            }
        });
        jToolBar8.add(browserBtn);

        jToolBar9.setRollover(true);
        jToolBar9.setOpaque(false);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/setting.png"))); // NOI18N
        jButton9.setText("Settings");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setOpaque(false);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar9.add(jButton9);

        javax.swing.GroupLayout toolBarPaneLayout = new javax.swing.GroupLayout(toolBarPane);
        toolBarPane.setLayout(toolBarPaneLayout);
        toolBarPaneLayout.setHorizontalGroup(
            toolBarPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolBarPaneLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(toolBarPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themBox, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap())
        );
        toolBarPaneLayout.setVerticalGroup(
            toolBarPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(toolBarPaneLayout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(themBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jOptionPane1.setWantsInput(true);

        terminal.setBackground(new java.awt.Color(204, 204, 204));
        terminal.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Terminal"));
        terminal.setOpaque(true);

        jTabbedPane1.setBackground(new java.awt.Color(213, 220, 238));
        jTabbedPane1.setOpaque(true);

        jPanel8.setBackground(new java.awt.Color(0, 102, 255));

        jList5.setModel(consoleList);
        jList5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList5.setCellRenderer(consoleCellRenderer);
        jList5.setInheritsPopupMenu(true);
        jList5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList5MouseClicked(evt);
            }
        });
        jList5.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList5ValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(jList5);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 131, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Console", jPanel8);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        terminal.setLayer(jPanel6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout terminalLayout = new javax.swing.GroupLayout(terminal);
        terminal.setLayout(terminalLayout);
        terminalLayout.setHorizontalGroup(
            terminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, terminalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        terminalLayout.setVerticalGroup(
            terminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, terminalLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        directoryPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/delete.png"))); // NOI18N
        jButton1.setToolTipText("Close");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel12.setText("Directores Pane");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel12)
        );

        directoryTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        directoryTree.setModel(null);
        directoryTree.setCellRenderer(cellRenderer2);
        directoryTree.setDragEnabled(true);
        directoryTree.setInheritsPopupMenu(true);
        directoryTree.setLargeModel(true);
        directoryTree.setRootVisible(false);
        directoryTree.setRowHeight(25);
        directoryTree.setSelectionModel(selectionModel);
        directoryTree.setSelectionRows(null);
        directoryTree.setShowsRootHandles(true);
        directoryTree.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                directoryTreeFocusLost(evt);
            }
        });
        directoryTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                directoryTreeMouseClicked(evt);
            }
        });
        directoryTree.addTreeWillExpandListener(new javax.swing.event.TreeWillExpandListener() {
            public void treeWillCollapse(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
                directoryTreeTreeWillCollapse(evt);
            }
            public void treeWillExpand(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
            }
        });
        directoryTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                directoryTreeValueChanged(evt);
            }
        });
        treeScroll.setViewportView(directoryTree);

        jPanel12.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        treeToolBar.setBackground(new java.awt.Color(213, 220, 238));
        treeToolBar.setFloatable(false);
        treeToolBar.setRollover(true);

        browswNode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/folder-icon_1.png"))); // NOI18N
        browswNode.setToolTipText("Browse Directory");
        browswNode.setBorderPainted(false);
        browswNode.setFocusable(false);
        browswNode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        browswNode.setOpaque(false);
        browswNode.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        browswNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browswNodeActionPerformed(evt);
            }
        });
        treeToolBar.add(browswNode);

        newDirBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Picture2.png"))); // NOI18N
        newDirBtn.setToolTipText("Create Directory");
        newDirBtn.setFocusable(false);
        newDirBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newDirBtn.setOpaque(false);
        newDirBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newDirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newDirBtnActionPerformed(evt);
            }
        });
        treeToolBar.add(newDirBtn);

        deleteNodeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Picture3.png"))); // NOI18N
        deleteNodeBtn.setToolTipText("Remove Directory");
        deleteNodeBtn.setEnabled(false);
        deleteNodeBtn.setFocusable(false);
        deleteNodeBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteNodeBtn.setOpaque(false);
        deleteNodeBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteNodeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteNodeBtnActionPerformed(evt);
            }
        });
        treeToolBar.add(deleteNodeBtn);

        addLeafBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/add.gif"))); // NOI18N
        addLeafBtn.setToolTipText("Add File");
        addLeafBtn.setEnabled(false);
        addLeafBtn.setFocusable(false);
        addLeafBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addLeafBtn.setOpaque(false);
        addLeafBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addLeafBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLeafBtnActionPerformed(evt);
            }
        });
        treeToolBar.add(addLeafBtn);

        openTreeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/edit-new-document-icon2.png"))); // NOI18N
        openTreeBtn.setToolTipText("Open");
        openTreeBtn.setEnabled(false);
        openTreeBtn.setFocusable(false);
        openTreeBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openTreeBtn.setOpaque(false);
        openTreeBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openTreeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openTreeBtnActionPerformed(evt);
            }
        });
        treeToolBar.add(openTreeBtn);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/reload-icon.png"))); // NOI18N
        jButton16.setToolTipText("Reload Directories");
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setOpaque(false);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        treeToolBar.add(jButton16);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeToolBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(treeScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(treeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        directoryPane.setLayer(jPanel7, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout directoryPaneLayout = new javax.swing.GroupLayout(directoryPane);
        directoryPane.setLayout(directoryPaneLayout);
        directoryPaneLayout.setHorizontalGroup(
            directoryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        directoryPaneLayout.setVerticalGroup(
            directoryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        docToolBar.setBackground(treeToolBar.getBackground());
        docToolBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jToolBar5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 1, 0));
        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);
        jToolBar5.setOpaque(false);

        findPrevBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Arrows-Left-Round-icon.png"))); // NOI18N
        findPrevBtn.setBorderPainted(false);
        findPrevBtn.setFocusable(false);
        findPrevBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        findPrevBtn.setIconTextGap(7);
        findPrevBtn.setOpaque(false);
        findPrevBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        findPrevBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findPrevBtnActionPerformed(evt);
            }
        });
        jToolBar5.add(findPrevBtn);

        findNextBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Arrows-Right-Round-icon.png"))); // NOI18N
        findNextBtn.setBorderPainted(false);
        findNextBtn.setFocusable(false);
        findNextBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        findNextBtn.setIconTextGap(7);
        findNextBtn.setOpaque(false);
        findNextBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        findNextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findNextBtnActionPerformed(evt);
            }
        });
        jToolBar5.add(findNextBtn);

        hihglitToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Bulb-icon.png"))); // NOI18N
        hihglitToggle.setFocusable(false);
        hihglitToggle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        hihglitToggle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        hihglitToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hihglitToggleActionPerformed(evt);
            }
        });
        jToolBar5.add(hihglitToggle);

        jButton45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Pointer-icon.png"))); // NOI18N
        jButton45.setToolTipText("Go to");
        jButton45.setFocusable(false);
        jButton45.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton45.setOpaque(false);
        jButton45.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(jButton45);

        jButton42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/ok.png"))); // NOI18N
        jButton42.setBorderPainted(false);
        jButton42.setFocusable(false);
        jButton42.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton42.setIconTextGap(7);
        jButton42.setOpaque(false);
        jButton42.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(jButton42);

        jButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/comment-edit-icon.png"))); // NOI18N
        jButton43.setBorderPainted(false);
        jButton43.setFocusable(false);
        jButton43.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton43.setIconTextGap(7);
        jButton43.setOpaque(false);
        jButton43.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });
        jToolBar5.add(jButton43);

        jButton46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/comment-delete-icon.png"))); // NOI18N
        jButton46.setFocusable(false);
        jButton46.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton46.setOpaque(false);
        jButton46.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });
        jToolBar5.add(jButton46);

        jToolBar10.setFloatable(false);
        jToolBar10.setRollover(true);
        jToolBar10.setOpaque(false);

        jToolBar11.setFloatable(false);
        jToolBar11.setRollover(true);
        jToolBar11.setOpaque(false);

        commentBox.setEditable(true);
        commentBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "C IN-LINE COMMENT", "C MULTILINE COMMENT", "HTML COMMENT", "JAVADOC COMMENT" }));
        commentBox.setBorder(null);
        commentBox.setFocusable(false);
        commentBox.setOpaque(false);
        commentBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                commentBoxItemStateChanged(evt);
            }
        });
        commentBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commentBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout docToolBarLayout = new javax.swing.GroupLayout(docToolBar);
        docToolBar.setLayout(docToolBarLayout);
        docToolBarLayout.setHorizontalGroup(
            docToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(docToolBarLayout.createSequentialGroup()
                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commentBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToolBar11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(798, 798, 798))
        );
        docToolBarLayout.setVerticalGroup(
            docToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, docToolBarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(docToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commentBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(docToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jToolBar5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToolBar10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToolBar11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        fileMenu.setText("File");
        fileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });

        newFileMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newFileMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/new-doc-icon.png"))); // NOI18N
        newFileMenu.setText("New ");
        newFileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFileMenuActionPerformed(evt);
            }
        });
        fileMenu.add(newFileMenu);

        createFileMenu.setText("Create New");

        jMenuItem1.setText("Java Source File");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        createFileMenu.add(jMenuItem1);

        jMenuItem15.setText("Hypertext Markup Language File (HTML)");
        createFileMenu.add(jMenuItem15);

        jMenuItem16.setText("Web Template File (DWT)");
        createFileMenu.add(jMenuItem16);

        jMenuItem17.setText("Cascading Style sheet (CSS)");
        createFileMenu.add(jMenuItem17);

        jMenuItem18.setText("JavaScript File (JS)");
        createFileMenu.add(jMenuItem18);

        jMenuItem19.setText("Batch File (CMD)");
        createFileMenu.add(jMenuItem19);

        jMenuItem20.setText("Java Archive File (JAR)");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        createFileMenu.add(jMenuItem20);

        fileMenu.add(createFileMenu);

        open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/open-file-icon.png"))); // NOI18N
        open.setText("Open");
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        fileMenu.add(open);
        fileMenu.add(jSeparator1);

        recent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Recent-Documents-icon.png"))); // NOI18N
        recent.setText("Recent Files");
        fileMenu.add(recent);

        saveCmd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveCmd.setText("Save");
        saveCmd.setEnabled(false);
        fileMenu.add(saveCmd);

        saveAsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Save-as-icon.png"))); // NOI18N
        saveAsMenu.setText("Save AS");
        saveAsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenu);

        saveAllMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAllMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/save_all.png"))); // NOI18N
        saveAllMenu.setText("Save All");
        saveAllMenu.setEnabled(false);
        saveAllMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAllMenuActionPerformed(evt);
            }
        });
        fileMenu.add(saveAllMenu);
        fileMenu.add(jSeparator2);

        renameFileMenu.setText("Rename");
        renameFileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameFileMenuActionPerformed(evt);
            }
        });
        fileMenu.add(renameFileMenu);
        fileMenu.add(jSeparator3);

        closeTabMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        closeTabMenu.setText("Close");
        closeTabMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeTabMenuActionPerformed(evt);
            }
        });
        fileMenu.add(closeTabMenu);

        closeAllTabsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        closeAllTabsMenu.setText("Close All ");
        closeAllTabsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeAllTabsMenuActionPerformed(evt);
            }
        });
        fileMenu.add(closeAllTabsMenu);
        fileMenu.add(jSeparator4);

        OARFMenu.setText("Open All  Recent Files");
        OARFMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OARFMenuActionPerformed(evt);
            }
        });
        fileMenu.add(OARFMenu);

        cRFLMenu.setText("Clear Recent Files List");
        cRFLMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRFLMenuActionPerformed(evt);
            }
        });
        fileMenu.add(cRFLMenu);
        fileMenu.add(jSeparator5);

        exitCmd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitCmd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Exit.gif"))); // NOI18N
        exitCmd.setText("Exit");
        exitCmd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitCmdActionPerformed(evt);
            }
        });
        fileMenu.add(exitCmd);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        searchMenu.setText("Seach");

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/find_1.gif"))); // NOI18N
        jMenuItem4.setText("Find");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        searchMenu.add(jMenuItem4);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/edit-new-document-icon.png"))); // NOI18N
        jMenuItem14.setText("Find In File");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        searchMenu.add(jMenuItem14);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1rightarrow.png"))); // NOI18N
        jMenuItem5.setText("Find Next");
        searchMenu.add(jMenuItem5);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/1leftarrow_hover.png"))); // NOI18N
        jMenuItem6.setText("Find Previous");
        searchMenu.add(jMenuItem6);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/modify.png"))); // NOI18N
        jMenuItem8.setText("Replace");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        searchMenu.add(jMenuItem8);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/ok.png"))); // NOI18N
        jMenuItem9.setText("Mark");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        searchMenu.add(jMenuItem9);

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/line.png"))); // NOI18N
        jMenuItem12.setText("Go to Line");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        searchMenu.add(jMenuItem12);

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/edit-new-document-icon.png"))); // NOI18N
        jMenuItem13.setText("Go to File");
        searchMenu.add(jMenuItem13);

        jMenuBar1.add(searchMenu);

        viewMenu.setText("View");
        viewMenu.setBorderPainted(true);

        zoomInMenu.setText("Zoom In");
        zoomInMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInMenuActionPerformed(evt);
            }
        });
        viewMenu.add(zoomInMenu);

        zoomOutMenu.setText("Zoom Out");
        zoomOutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutMenuActionPerformed(evt);
            }
        });
        viewMenu.add(zoomOutMenu);

        viewTerminal.setSelected(true);
        viewTerminal.setText("Show Console");
        viewTerminal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                viewTerminalStateChanged(evt);
            }
        });
        viewTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewTerminalActionPerformed(evt);
            }
        });
        viewMenu.add(viewTerminal);

        showTermAlway.setText("Show Console Always");
        showTermAlway.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTermAlwayActionPerformed(evt);
            }
        });
        viewMenu.add(showTermAlway);

        showDPCheck.setSelected(true);
        showDPCheck.setText("Directories Pane");
        showDPCheck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/tick.png"))); // NOI18N
        showDPCheck.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/tick.png"))); // NOI18N
        showDPCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDPCheckActionPerformed(evt);
            }
        });
        viewMenu.add(showDPCheck);

        jMenu1.setText("ToolBars");

        toolBarChk.setSelected(true);
        toolBarChk.setText("Main ToolBar");
        toolBarChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarChkActionPerformed(evt);
            }
        });
        jMenu1.add(toolBarChk);

        docToolBarchk.setSelected(true);
        docToolBarchk.setText("Document ToolBar");
        docToolBarchk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docToolBarchkActionPerformed(evt);
            }
        });
        jMenu1.add(docToolBarchk);

        viewMenu.add(jMenu1);

        jMenuBar1.add(viewMenu);

        runMenu.setText("Run");

        webMenu.setText("Web");

        firefoxItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/firefox3.png"))); // NOI18N
        firefoxItem.setText("Mozilla Firefox");
        firefoxItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firefoxItemActionPerformed(evt);
            }
        });
        webMenu.add(firefoxItem);

        chromeItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Google3.png"))); // NOI18N
        chromeItem.setText("Google Chrome");
        chromeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chromeItemActionPerformed(evt);
            }
        });
        webMenu.add(chromeItem);

        operaItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Opera2.png"))); // NOI18N
        operaItem.setText("Opera");
        operaItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                operaItemActionPerformed(evt);
            }
        });
        webMenu.add(operaItem);

        iexplorerItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Internet-Explorer3.png"))); // NOI18N
        iexplorerItem.setText("Internet Explorer");
        iexplorerItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iexplorerItemActionPerformed(evt);
            }
        });
        webMenu.add(iexplorerItem);

        safariItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/codepade/img/Safari3.png"))); // NOI18N
        safariItem.setText("Safari");
        safariItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                safariItemActionPerformed(evt);
            }
        });
        webMenu.add(safariItem);

        runMenu.add(webMenu);

        jMenu2.setText("Java");

        jMenuItem7.setText("Open Command Prompt");
        jMenu2.add(jMenuItem7);

        jMenuItem10.setText("Use Terminal");
        jMenu2.add(jMenuItem10);

        runMenu.add(jMenu2);

        jMenuBar1.add(runMenu);

        settingMenu.setText("Settings");

        jMenuItem2.setText("Preferences");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        settingMenu.add(jMenuItem2);

        jMenuItem11.setText("Manage Web Browsers");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        settingMenu.add(jMenuItem11);

        jMenuBar1.add(settingMenu);

        windowMenu.setText("Window");

        jMenuItem3.setText("Show Terminal ");
        windowMenu.add(jMenuItem3);

        showDirPaneMenu.setText("Hide Directories Pane");
        showDirPaneMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDirPaneMenuActionPerformed(evt);
            }
        });
        windowMenu.add(showDirPaneMenu);

        jMenuBar1.add(windowMenu);

        helpMenu.setText("Help?");
        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(toolBarPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(docToolBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(directoryPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(terminal)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(toolBarPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(docToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(directoryPane))
                .addGap(0, 0, 0)
                .addComponent(terminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPane, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    

    private void saveQuietly(final String file,final  JTextPane doc){
    
        
    SwingWorker<Void, Void>  worker =  new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
               try(BufferedWriter br = new BufferedWriter(new FileWriter(file)) ) {
             doc.write(br);
                  saveBtn.setEnabled(false);
                  saveCmd.setEnabled(false);
          } catch (IOException ex) { }
            
            return null;
        }//end method 
    }; //end worker anonymous
      worker.execute();
           
    }
    int dot  ;
 public   void  addComments(JTextPane textPane, String type, boolean single) throws BadLocationException{
  
 Document  doc  =  textPane.getDocument();
  dot  =  textPane.getCaret().getDot();
 StringBuilder  sb  =  new StringBuilder();
 String selectedText  = null;
 String lines[] =  null;
 if(textPane.getSelectedText() != null){
 

 int start  = textPane.getSelectionStart();
 int end  =  textPane.getSelectionEnd();
      selectedText =  doc.getText(start, end-start);
       if(single){
       lines =selectedText.split("\\n");
    
         for(int i = 0; i < lines.length; i++){
           String temp  =  addCommentTo(lines[i].trim(), type);
          if(i == lines.length-1)
           lines[i] =  temp+"";
          else lines[i] =  temp+"\n";
          sb.append(lines[i]);
          }//end for loop 
     }else {
       selectedText=  addCommentTo(selectedText.trim(), type);
       sb.delete(0, sb.length());
       sb.append(selectedText);
       }//end if-else 
       
       refreshAfterComments(doc, start, end,dot, sb.toString(),true);
            
 }else {
  addCommentAt(textPane,type);
 }//end if  
       
       
 
 }  
 private void refreshAfterComments(Document doc, int start, int end,int dot, String text, boolean ...add) throws BadLocationException{
  int dotBefore  =  dot;
  doc.remove(start, end-start);
  doc.insertString(start, text, attrs);
  int dotAfter =  currentDoc.getCaret().getDot();
  
  if(add.length > 0 ){
   if(add[0]){
     dot  -=  (dotAfter -  dotBefore);  
     
 }//end test 2
  }//endn test 1
  
  
  currentDoc.getCaret().setDot(dot);
   SwingUtilities.invokeLater(() -> {
      saveQuietly(currentFile, currentDoc);
  });
 }  
  public void removeComments(JTextPane textPane, String type, boolean single) throws BadLocationException{
  
  Document  doc  =  textPane.getDocument();
  dot  =  textPane.getCaret().getDot();
  boolean hasComments =  false;
 StringBuilder  sb  =  new StringBuilder();
 String selectedText  = null;
 String lines[] =  null;
 if(textPane.getSelectedText() != null){
 

 int start  = textPane.getSelectionStart();
 int end  =  textPane.getSelectionEnd();
 String temp = "";
    
         selectedText =  doc.getText(start, end-start);
       if(single){
         lines =selectedText.split("\\n");
        for(int i = 0; i < lines.length; i++){
           
           if(lines[i].trim().startsWith("//")){
            temp  =  removeCommentsFrom(lines[i].trim(), type);
            hasComments =  true;
           }
   
          if(i == lines.length-1)
           lines[i] =  temp+"";
          else lines[i] =  temp+"\n";
          
           sb.append(lines[i]);
        }//end for looop 
      
       }else{
             if(selectedText.trim().startsWith("/*")){
             selectedText= removeCommentsFrom(selectedText.trim(), type);
             sb.delete(0, sb.length());
             sb.append(selectedText);
             hasComments =  true;
            }else if(selectedText.trim().startsWith("<!--")){
             selectedText= removeCommentsFrom(selectedText.trim(), type);
             sb.delete(0, sb.length());
             sb.append(selectedText);
             hasComments =  true;
            }
      
       }//end if 
    if(hasComments){
           refreshAfterComments(doc, start, end,dot, sb.toString());
       }
        
 }else{
     removeCommentAt(textPane, type);
 }//end if 
       
  
  
  }//end mehtod  
   
private void  addCommentAt(JTextPane  textPane, String type){
Document doc  =  textPane.getDocument();
 Caret  e  =  textPane.getCaret(); 
 int dot  =  e.getDot()+3;
 String thisLine  = "";
 try {
    
            String text  = doc.getText(0, e.getDot());
            int ln  =  lineNumber(text);     
            Element  elem  =  doc.getDefaultRootElement();
            Element  line  =  elem.getElement(ln-1);
            text  =  doc.getText(0, doc.getLength()); 
          if(type.equals("javadoc")){
           String comment  ="/**\n*\n*\n*/";
           doc.insertString(line.getStartOffset(), comment, attrs); 
             SwingUtilities.invokeLater(() -> {
             saveQuietly(currentFile, currentDoc);
             });
          }else{  
            thisLine  = text.substring(line.getStartOffset(),line.getEndOffset()-1);
            refreshAfterComments(doc, line.getStartOffset(), line.getEndOffset()-1,dot,addCommentTo(thisLine.trim(), type),true);
            textPane.getCaret().setDot(dot);
          }
            
  } catch (BadLocationException ex) {}

}    
  
private void removeCommentAt(JTextPane  textPane, String type){
Document doc  =  textPane.getDocument();
 Caret  e  =  textPane.getCaret(); 
 int dot  =  e.getDot();
 int x = 1;
 String thisLine  = "";
 try {
    
            String text  = doc.getText(0, e.getDot());
            int ln  =  lineNumber(text);     
            Element  elem  =  doc.getDefaultRootElement();
            Element  line  =  elem.getElement(ln-1);
            text  =  doc.getText(0, doc.getLength());
  
            thisLine  = text.substring(line.getStartOffset(),line.getEndOffset()-x).trim();   
            
            if(thisLine.startsWith("//")){
            refreshAfterComments(doc, line.getStartOffset(), line.getEndOffset()-x,dot,removeCommentsFrom(thisLine, type));
            
           }else 
                if(thisLine.startsWith("<!--")){
            refreshAfterComments(doc, line.getStartOffset(), line.getEndOffset()-x,dot,removeCommentsFrom(thisLine, type));
            
           }else 
                if(thisLine.startsWith("/*")){
            refreshAfterComments(doc, line.getStartOffset(), line.getEndOffset()-x,dot,removeCommentsFrom(thisLine, type));
           
           }
            
            
  } catch (BadLocationException ex) {}

}  
    private void setEditActions(){
 
    Action   action = null;
   
    JMenuItem undoAction = new JMenuItem(this.undo);
    undoAction.setIcon(this.createIcont("img/redo.gif"));
    editMenu.add(undoAction);
   
    
    JMenuItem redoAction = new JMenuItem(this.redo);
     redoAction.setIcon(createIcont("img/Undo.gif"));
     editMenu.add(redoAction);
    editMenu.addSeparator();
   
    undoButton.setAction(undo);
    undoButton.setIcon(createIcont("img/undo-icon.png"));
    
    redoButton.setAction(redo);
    redoButton.setIcon(createIcont("img/redo-icon.png"));
      
    action =  new StyledEditorKit.CutAction();
    action.putValue(Action.NAME, "Cut");
  
    this.editMenu.add(action);
   JMenuItem m =    (JMenuItem) editMenu.getMenuComponent(3);
    m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
    
    
    
    action =  new StyledEditorKit.CopyAction();
    action.putValue(Action.NAME, "Copy");
    this.editMenu.add(action);
   m =    (JMenuItem) editMenu.getMenuComponent(4);
    m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
    
    
    action =  new StyledEditorKit.PasteAction();
    action.putValue(Action.NAME, "Paste");
    this.editMenu.add(action);
    m =    (JMenuItem) editMenu.getMenuComponent(5);
    m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.ALT_MASK));
    
    
    
    JMenuItem  delete = new JMenuItem("Delete");
    editMenu.add(delete);
    editMenu.addSeparator();
  
 
  
    action =  currentDoc.getActionMap().get(DefaultEditorKit.selectAllAction);
    action.putValue(Action.NAME, "Select All");
    editMenu.add(action);
//    
//    JMenuItem selectAllEdit =  new JMenuItem(action);
//    editMenu.add(selectAllEdit);
    
   editMenu.addSeparator();
   JMenuItem  upper = new JMenuItem("UPPERCASE");
   upper.addActionListener( (e) ->{changeCase(true);});
   
   
   
    editMenu.add(upper);
    JMenuItem  lower = new JMenuItem("Lowercase");
    lower.addActionListener((e) -> {changeCase(false);});
    editMenu.add(lower);
    
   
        
    
    
    }//end method 
    
    private void changeCase(boolean upper){
    int start  =currentDoc.getSelectionStart();
        int end  = currentDoc.getSelectionEnd(); 
         String part;
        try {
           part = currentDoc.getText(start, end - start);
           
            String newChar  =  part;
             if(upper) newChar = newChar.toUpperCase();
            else newChar = newChar.toLowerCase();
                 
                 
           currentDoc.getDocument().remove(start, part.length());
           currentDoc.getDocument().insertString(start, newChar, null);
           saveQuietly(currentFile,currentDoc);
           
        } catch (BadLocationException ex) {
        }    
    
    }
    
 protected void processDocPopupMenuActions(JTextPane textPane,String...commands){
   SwingUtilities.invokeLater(() -> {
       setRedUndoFor(textPane);
   });
       
    
   JPopupMenu   popupMenu  = new JPopupMenu(); 
     popupMenu.setPopupSize(200, 200);
    Action   action = null;
    
    action =  new StyledEditorKit.CutAction();
    action.putValue(Action.NAME, "Cut");
    popupMenu.add(action);
     
     
    action =  new StyledEditorKit.CopyAction();
    action.putValue(Action.NAME, "Copy");
    popupMenu.add(action);
    
    action =  new StyledEditorKit.PasteAction();
    action.putValue(Action.NAME, "Paste");
    popupMenu.add(action);
    popupMenu.addSeparator();
    
   JMenuItem select =  new JMenuItem("Select This Line");
    popupMenu.add(select);
    JMenuItem selectAll =  new JMenuItem("Select All");
    popupMenu.add(selectAll);
    
     JMenuItem  delete = new JMenuItem("Delete");   
     popupMenu.add(delete);
     
     popupMenu.addSeparator();
      
    JMenuItem  upper = new JMenuItem("UPPERCASE");
    upper.addActionListener((e)->{changeCase(true);});
    popupMenu.add(upper);
    
    JMenuItem  lower = new JMenuItem("Lowercase");
    lower.addActionListener((e)->{changeCase(false);});
    popupMenu.add(lower);
    
   if(commands != null && commands.length > 0){
   for(int i = 0; i < commands.length; ++i){
   
      String cmd  =  commands[i]; 
      switch(cmd){
          case "Insert Main Method":
          JMenuItem  main = new JMenuItem("Insert Main Method");      
          main.addActionListener((e) -> {insertCommand(currentDoc, commandsProps, "insertMain"); });
           popupMenu.add(main);    
          break;
      
          case "Println()": 
           JMenuItem  println = new JMenuItem("Println()");      
           println.addActionListener((e) -> {insertCommand(currentDoc, commandsProps, "newLine"); });
           popupMenu.add(println);   
         break;
        case "Insert Class Diff": 
           JMenuItem  classDiff = new JMenuItem("Insert Class Diff");      
           classDiff.addActionListener((e) -> {insertCommand(currentDoc, userCommandsProps, "javaClass"); });
           popupMenu.add(classDiff);   
         break;
         
      }//end switch 
}
}//end if 
      
        registerPopupMenuTo(textPane, popupMenu);
        
    
    }
    
 
    
    
    
  
    private void bindActions(){
    bindSaveActions();
    bindPenActions();

    
    
    }
    private void bindSaveActions(){
    
    SaveAction  saveAction; 
        
   saveAction =   new SaveAction();
    //document, createIcont("img/SAVE.gif"),"Save"
    saveCmd.setAction(saveAction);
   // saveCmd.setIcon(createIcont("img/Save-as-icon.png"));
    saveCmd.setActionCommand("Save");
  //  saveCmd.getAction().putValue(Action.NAME, "Save");
     saveCmd.setText("Save");
    
   
   saveBtn.setAction(saveAction);
    saveBtn.setIcon(createIcont("img/floppy-icosn.png"));
    saveBtn.setToolTipText("Save This File");
    saveBtn.setText("Save");
    saveCmd.setIcon(createIcont("img/floppy-icosn.png"));
    
    
    saveCmd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
    saveAsMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK | InputEvent.ALT_MASK ));
   saveCmd.setEnabled(false);
   saveBtn.setEnabled(false); 
    
   browserBtn.setText("Run Browser");
    
    }//end  method 
    
    
    private void bindPenActions(){
    
Action  cut  = new   StyledEditorKit.CutAction();  
        cut.putValue(Action.NAME, null);
    cutBtn.setAction(cut);
    cutBtn.setIcon(createIcont("img/cuticon.png"));
    cutBtn.setToolTipText("Cut");
    cutBtn.setText("Cut");
    Action copy =  new StyledEditorKit.CopyAction();
    
    copyBtn.setAction(copy);
    copy.putValue(Action.NAME , null);
    copyBtn.setIcon(createIcont("img/copy-icon.png"));
    copyBtn.setText("Copy");
    copyBtn.setToolTipText("Copy");
  
    Action paste  =  new StyledEditorKit.PasteAction();
    pasteBtn.setAction(paste);
    paste.putValue(Action.NAME , null);
    pasteBtn.setIcon(createIcont("img/Editing-Paste-icon.png"));    
    pasteBtn.setToolTipText("Paste");
    pasteBtn.setText("Paste");
    open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
    
   
    
    
    
    
    }//end  method 

private EditActions.RedoAction redo;
 private EditActions.UndoAction undo;
 private final UndoManager undoManager =  new UndoManager();
  private void setRedUndoFor(JTextPane textPane){
 
redo =  new EditActions.RedoAction(undoManager);
undo = new EditActions.UndoAction(undoManager);
redo.setUndo(undo);
undo.setRedo(redo);

UndoableEditListener  listener = new UndoableEditListenerx(undoManager, redo, undo);
textPane.getDocument().addUndoableEditListener(listener);

  }//end method   
    
  

 
   private static  final int[] scales = {50, 75,100,150,200}; 
   private int scaleCounter = 0;
    final SimpleAttributeSet attrs=new SimpleAttributeSet();
    private void exitCmdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitCmdActionPerformed

System.exit(0);

        // TODO add your handling code here:
    }//GEN-LAST:event_exitCmdActionPerformed

    
    private void zoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInActionPerformed

   
    increaseFontSize();

    


    }//GEN-LAST:event_zoomInActionPerformed

 private void processTabs(){

   
     if(currentFilesPros.getProperties().isEmpty()){

           tabPane.remove(0);
           documentManager.createNewDocument(newTabCount, 0);
           tabPane.setSelectedIndex(0);
     }
     else if(!currentFilesPros.getProperties().isEmpty() && loadFaild){ 
                tabPane.setSelectedIndex(0);
         
      }else {tabPane.remove(0);}

 }   
    
 
 
 private void createNewTab(String tile){

     isOpenAction = true;
     isLoading =  true;
        documentPane   p =  new documentPane(lineNumberLable);
        currentDoc  =  p.getTextPane();
        checkSelectedThem(); 
       
        currentDoc.setDocument(new TextDocument(".txt",themBox.getSelectedItem().toString()));
        processDocPopupMenuActions(currentDoc);
         addDocListener(currentDoc);
       setDocFont(currentDoc, fontSize);
        if(tile == null)
            tile  ="New ";
   tabPane.add((tile.equals("New") ? tile+" "+tabPane.getTabCount() : tile), p); 
   tabPane.setTabComponentAt(tabPane.getTabCount()-1, new TabController(this));
   tabPane.setSelectedIndex(tabPane.getTabCount()-1);
   switchToCurrentDoc();
   setTitle("CodePad - "+tabPane.getTitleAt(tabPane.getTabCount()-1));
   tabPane.setToolTipTextAt(tabPane.getTabCount()-1, extractPath(getTitle()));
   
   fileType.setText("Normal text file");
   fileLen.setText(String.valueOf(currentDoc.getText().length()));
   fileSaveState.add(false);
   
   saveBtn.setEnabled(fileSaveState.get(tabPane.getSelectedIndex()));
   saveCmd.setEnabled(fileSaveState.get(tabPane.getSelectedIndex()));
   
   
   isOpenAction = false;
     isLoading =  false;
   
 }
 
 
private String renameFile(String fileName){

    String newName =  null;
    
String name  = getFileName(fileName); 
    
 newName =   (String) JOptionPane.showInputDialog(this,"Enter new file name:","Rename File",
           JOptionPane.QUESTION_MESSAGE, null, null, name.substring(0, name.lastIndexOf(".")));
   
    

    
    
return newName;
}    
    
private void decreaseFontSize(){

int tabs  =  tabPane.getTabCount();
  
fontSize -= 2;

if(fontSize < 11)
     return;

  for(int i = tabs-1; i > -1; --i){
     JTextPane   textDoc =  findTextPane(i);    
     textDoc.setFont(new Font(textDoc.getFont().getFamily(),textDoc.getFont().getStyle(), fontSize)  );
    }
  
  


}
    private void zoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutActionPerformed
 
     //  zoomOut();
    
decreaseFontSize();
    
    }//GEN-LAST:event_zoomOutActionPerformed

    private void newBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBtnActionPerformed

       creatNewEmptyTab();
        
    }//GEN-LAST:event_newBtnActionPerformed

   
 private void creatNewEmptyTab(){
 if(newRemovedTabList.isEmpty()){  
        createNewTab("New "+(++newTabCount));       
       }else {
        ++newTabCount;
       createNewTab(newRemovedTabList.pollLast());
       

       }
  newAddedTabList.add(tabPane.getTitleAt(tabPane.getSelectedIndex()));
  currentFile =  tabPane.getTitleAt(tabPane.getSelectedIndex());
 
 
 
 
 }   
    
    private String  setTitleToFrame(String fileName){
    
    if(!currentFilesPros.getProperties().isEmpty()){
       return   currentFilesPros.getPropertyValue(fileName);
    }
    
    return null;
    }//end method 
       
   
  
  
 
    int numOfOpenFiles = 0;
    boolean isOpenAction = false;
    
    
 
 
 
    private void openBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBtnActionPerformed
   
  
         fileManager.openFileInTab();
    
        
       
    
        // TODO add your handling code here:
    }//GEN-LAST:event_openBtnActionPerformed

  
    
    
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
 
   checkAllIfSaved();
       
    }//GEN-LAST:event_formWindowClosing

    
    
 private void checkAllIfSaved(){
     
 int uncount  =  tabManager.countUnsaved();
 
  if(uncount > 0){ 
      String msg = uncount+ (uncount > 1 ? " files have " : " file has ")+
  "been modified recently,\nAre you sure you want to close before saving"+
        (uncount > 1 ? " these files?" : " this file?");
      
      
      int  opt =    JOptionPane.showConfirmDialog((findDialog.isVisible() ? findDialog : this), msg, "Confirm ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if(opt == 1){
          setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         
      }else {
          
    storeAllProps2();
    saveAllSettings();
    System.exit(0);
          
      }//end else
  }else {
      storeAllProps2();
      saveAllSettings();
      System.exit(0);
      
      
      
  } //end if  
  

     
 
     
 
 }   
   
 
 

private void storeAllProps2(){

         currentFilesPros.storeProperties("currentFilesPros");

          browserProps.storeProperties("browserProps");
     

         closeFilesProps.storeProperties("closeFilesProps");
      
         themeProps.storeProperties("program theme color");     
         currentTabProps.setProperty("currntTab", String.valueOf(tabPane.getSelectedIndex()));
         currentTabProps.setProperty("fontSize", String.valueOf(currentDoc.getFont().getSize()));
         currentTabProps.storeProperties("last selected tab");
  
        defaultBrowserProps.storeProperties("default Browser Props");
    
        userWebProps.storeProperties("default Browser Props");
        treeDirProps.storeProperties("Tree Node properties");
}    
    
 //ensure all property files are created and stored before accessing them.   
private void storeAllProps(){

     if(!currentFilesPros.isFileFound())
         currentFilesPros.storeProperties("currentFilesPros");

       if(!browserProps.isFileFound())
          browserProps.storeProperties("browserProps");
     
     if(!closeFilesProps.isFileFound())
         closeFilesProps.storeProperties("closeFilesProps");
      
     if(!themeProps.isFileFound())
         themeProps.storeProperties("program theme color");     
      
     if(!currentTabProps.isFileFound())
         currentTabProps.storeProperties("last selected tab");
    
      
    if(!defaultBrowserProps.isFileFound())
        defaultBrowserProps.storeProperties("default Browser Props");
    
    if(!userWebProps.isFileFound())
        userWebProps.storeProperties("user web Props");
    
    if(!treeDirProps.isFileFound())
        treeDirProps.storeProperties("Tree node  Props");
    
     if(commandsProps.isFileFound() && commandsProps.getProperties().isEmpty()){
         
         initUserCommands(commandsProps);
         commandsProps.storeProperties("User commands");
   }
  if(!commandsProps.isFileFound())
        commandsProps.storeProperties("User commands");
  
  if(!userCommandsProps.isFileFound())
        userCommandsProps.storeProperties("User commands");
  
   if(!defaultProps.isFileFound())
        defaultProps.storeProperties("default Properties");
   
    if(!settingsProps.isFileFound())
        settingsProps.storeProperties("settings Properties");
    
   if(!tabProps.isFileFound())
       tabProps.storeProperties("tab Properties");
}    
    
    
    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed

    fileManager.openFileInTab();

        // TODO add your handling code here:
    }//GEN-LAST:event_openActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed



        // TODO add your handling code here:
    }//GEN-LAST:event_saveBtnActionPerformed

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuActionPerformed



        // TODO add your handling code here:
    }//GEN-LAST:event_fileMenuActionPerformed

    private void newFileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFileMenuActionPerformed
       
        creatNewEmptyTab();



        // TODO add your handling code here:
    }//GEN-LAST:event_newFileMenuActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

fontDialog.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void closeTabBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeTabBtnActionPerformed


closeOpenTab();
        // TODO add your handling code here:
    }//GEN-LAST:event_closeTabBtnActionPerformed

    private void closeAllTabsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAllTabsBtnActionPerformed

closeAllOpenTabs();


        // TODO add your handling code here:
    }//GEN-LAST:event_closeAllTabsBtnActionPerformed

    private void closeTabMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeTabMenuActionPerformed

closeOpenTab();
       

        // TODO add your handling code here:
    }//GEN-LAST:event_closeTabMenuActionPerformed

    
private   void closeAllOpenTabs(){
tabManager.closeAllTabs();
cRFLMenu.setEnabled(true);
OARFMenu.setEnabled(true);

}//end method 
private void closeOpenTab(){
 tabManager.closeTab(tabPane.getSelectedIndex(),true);
        cRFLMenu.setEnabled(true);
        OARFMenu.setEnabled(true);

}   

    private void closeAllTabsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAllTabsMenuActionPerformed

closeAllOpenTabs();

        // TODO add your handling code here:
    }//GEN-LAST:event_closeAllTabsMenuActionPerformed

    private void renameFileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameFileMenuActionPerformed
      
  String newName  =           renameFile(extractPath(getTitle()));
  String replaced  = null;
  String title  = extractPath(getTitle());  
       System.err.println(title);
  for(int i = 0; i < currentFiles.size(); ++i){
      String  f  = currentFiles.get(i); 
   if(f.equals(extractPath(getTitle()))) {
           String old =  getFileNameOnly(extractPath(f));
              replaced   =  f.replace(old, newName);
              currentFiles.set(i, replaced);
               break;
              
           }
  
  }
  this.setTitle("CodePad - "+replaced);
        changeTabTitle(getFileName(replaced));
        
            
        
       changeFile(title,replaced);
              

        // TODO add your handling code here:
    }//GEN-LAST:event_renameFileMenuActionPerformed

    private void cRFLMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRFLMenuActionPerformed

      removeAllMenuItems( recent, closeFilesProps);
      cRFLMenu.setEnabled(false);
      
        // TODO add your handling code here:
    }//GEN-LAST:event_cRFLMenuActionPerformed

    private void OARFMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OARFMenuActionPerformed

     fileManager.openAllLastClosedFiles();
     removeAllMenuItems(recent,closeFilesProps);
    OARFMenu.setEnabled(false);
    cRFLMenu.setEnabled(false);
     
    }//GEN-LAST:event_OARFMenuActionPerformed

    private void themBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themBoxActionPerformed


String them =  (String) themBox.getSelectedItem();

 themesColor =  them;
 themeProps.setProperty("current_theme",themesColor);
 settingsProps.setProperty("current_theme",themesColor);
    
    }//GEN-LAST:event_themBoxActionPerformed

    private void viewTerminalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_viewTerminalStateChanged



        // TODO add your handling code here:
    }//GEN-LAST:event_viewTerminalStateChanged

    
    
    
    
    
    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
 
       
       
    browserList.setSelectedIndex(0);
    String selected  =  browserList.getSelectedValue();
    Set<Map.Entry<Object, Object>>   set  =  browserProps.getProperties().entrySet();
      
    set.forEach((t) -> {
        if(t.getValue().toString().equals(selected)){
            browserName.setText(t.getKey().toString());
        }
         });
     webManager.setVisible(true);
    

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void closeBtn_bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtn_bActionPerformed

webManager.setVisible(false);
   
    }//GEN-LAST:event_closeBtn_bActionPerformed

    private void firefoxItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firefoxItemActionPerformed
    
     openInWebBrowser(firefoxItem.getText());
    
        


    }//GEN-LAST:event_firefoxItemActionPerformed

    
    private void openInWebBrowser(String browser){
    String selectedDoc  = getSelectedDocFile(tabPane.getTitleAt(tabPane.getSelectedIndex()));  
    if(browserProps.getProperties().containsKey(browser)) {
        Calendar   time  =  Calendar.getInstance();
        Path location  =  Paths.get(browserProps.getPropertyValue(browser));
        consoleList.addElement("["+time.getTime()+"] Opening ["+selectedDoc+"] With ["+location+"]");
           if(executeProgram(location.toString(), Paths.get(selectedDoc)))
                consoleList.addElement("Open Succeeded");
     } else if(userWebProps.getProperties().containsKey(browser)){
       Calendar   time  =  Calendar.getInstance();
        Path location  =  Paths.get(browserProps.getPropertyValue(browser));
        consoleList.addElement("["+time.getTime()+"] Opening ["+selectedDoc+"] With ["+location+"]");
           if(executeProgram(location.toString(), Paths.get(selectedDoc)))
                consoleList.addElement("Open Succeeded");
     }
    
    
        
   
      
    
    
    
    }
    
    private void firefoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firefoxActionPerformed
     
        openInWebBrowser(firefox.getText());
        
        
    }//GEN-LAST:event_firefoxActionPerformed

    private void browserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browserBtnActionPerformed

    String selectedDoc  = getSelectedDocFile(tabPane.getTitleAt(tabPane.getSelectedIndex()));  
    if(defaultBrowserProps.getProperties().containsKey(defaultBrowserKey)) {
        saveAll();
         Calendar   time  =  Calendar.getInstance();
        
        Path location  =  Paths.get(defaultBrowserProps.getPropertyValue(defaultBrowserKey));
        consoleList.addElement("["+time.getTime()+"] Opening ["+selectedDoc+"] With ["+location+"]");
        if(executeProgram(location.toString(), Paths.get(selectedDoc))){
        consoleList.addElement("Open Succeeded");
        }
      
        
        
     } 
    }//GEN-LAST:event_browserBtnActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        
        String file  = extractPath(getTitle());
        String f  =  getFileName(file);
 String path = file.substring(0, file.lastIndexOf(File.separator));
        
        path =  path.substring(path.indexOf(File.separator)+1,path.length()) ;
        
        System.out.println("succeeded: "+executeProgram("cmd.exe", Paths.get(file)));

    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed



        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void showTermAlwayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTermAlwayActionPerformed

         

        // TODO add your handling code here:
    }//GEN-LAST:event_showTermAlwayActionPerformed

    private void viewTerminalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTerminalActionPerformed
  if(viewTerminal.isSelected()){
     terminal.setVisible(true); 
     showTermAlway.setEnabled(true);
     showTerminalToggle.setText("Hide Console");
     showTerminalToggle.setIcon(createIcont("img/terminal2.png"));
     showTerminalToggle.setSelected(true);
  }else {
   terminal.setVisible(false);
   showTermAlway.setEnabled(false);
    showTerminalToggle.setText("Show Console");
     showTerminalToggle.setIcon(createIcont("img/terminal.png"));
     showTerminalToggle.setSelected(false);
  }       
        
        
        
    }//GEN-LAST:event_viewTerminalActionPerformed

    private void addBtn_BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtn_BActionPerformed

  if(browserList.getSelectedValue() != null){
     if(browserListModel.size() > 5){
   
     userWebProps.setProperty(browserName.getText(),browserList.getSelectedValue());
     JMenuItem  m1  =  new JMenuItem(browserName.getText()); 
     JMenuItem  m2  =  new JMenuItem(browserName.getText());
     webMenu.add(m1);
     webPopmenuList.add(m2);
     userWebProps.storeProperties(null);  
     addActionToUserWebMenu(m1);
      
//renameUserMenu(namef,name,webPopmenuList.getComponents());
//renameUserMenu(namef,name,webMenu.getMenuComponents());     
      
     }else{
       browserProps.setProperty(browserName.getText(),browserList.getSelectedValue());
      renameMenuItem(browserName.getText()); 
      browserProps.storeProperties(null);

     }  
      addBtn_B.setEnabled(false);
     }
   

    }//GEN-LAST:event_addBtn_BActionPerformed

    private  void initUserWeb(){
    
    //if(browserProps.getProperties().size() > 4){ 
        Set<Map.Entry<Object, Object> >  set  =  userWebProps.getProperties().entrySet();
        set.forEach((t) -> {
          JMenuItem   m1  =new JMenuItem(t.getKey().toString(),createIcont("/codepade/img/Globe-Internet-icon.png"));  
          JMenuItem   m2  =new JMenuItem(t.getKey().toString(),createIcont("/codepade/img/Globe-Internet-icon.png"));  
               addActionToUserWebMenu(m1);
            webMenu.add(m1);          
            webPopmenuList.add(m2); 
        });
 
    
    
    
    } //end method  
    
   private void addActionToUserWebMenu(final JMenuItem item){
   
       item.addActionListener((e) -> {
           openInWebBrowser(item.getText());
           
       });
       
   
   
   
   } 
    
   
    
    private void browserListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_browserListValueChanged

  
   
   if(browserList.getSelectedIndex() > -1){
    editBtn_b.setEnabled(true);
    renameBtn_b.setEnabled(true);
    removeBtn_b.setEnabled(true);
    
    String selected  =  browserList.getSelectedValue();
    Set<Map.Entry<Object, Object>>   set  =  browserProps.getProperties().entrySet();
    Set<Map.Entry<Object, Object>>   set2  =  userWebProps.getProperties().entrySet(); 
    set.forEach((t) -> {
        if(t.getValue().toString().equals(selected)){
            browserName.setText(t.getKey().toString());
        }
         });
    
     set2.forEach((t) -> {
        if(t.getValue().toString().equals(selected)){
            browserName.setText(t.getKey().toString());
        }
         });

 
    if(selected.equals(defaultBrowser)){
        defaultBrowserChbx.setSelected(true);
        defaultBrowserChbx.setEnabled(false);  
    }else {
    defaultBrowserChbx.setSelected(false);
    defaultBrowserChbx.setEnabled(true);
    
    }
    
    
   }//end if 
    

    }//GEN-LAST:event_browserListValueChanged

    
    private void removeBtn_bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtn_bActionPerformed

       String v =  browserList.getSelectedValue();
      if(browserProps.getProperties().containsValue(v)){
          browserListModel.removeElementAt(browserList.getSelectedIndex());
          browserProps.getProperties().remove(browserName.getText(), v);
          browserProps.storeProperties(null);
          
          
      }
      
          
          
    }//GEN-LAST:event_removeBtn_bActionPerformed

    private void browseBtn_BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBtn_BActionPerformed
JFileChooser  fc  =  new JFileChooser(System.getProperty("user.home"));
   int op  =  fc.showOpenDialog(webManager);
   File  file  = null;
   if(op == JFileChooser.APPROVE_OPTION){
   file = fc.getSelectedFile();
   
   browserListModel.addElement(file.toString());
   browserName.setText(getFileNameOnly(file.toString()));
   browserList.setSelectedIndex(browserListModel.size()-1);
   renameBtn_b.setEnabled(true);
   addBtn_B.setEnabled(true);
   
   
   
   
   }//end if
   

   
   






    }//GEN-LAST:event_browseBtn_BActionPerformed

    private void renameBtn_bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameBtn_bActionPerformed

        
String name;
String namef = browserName.getText();

name  =  JOptionPane.showInputDialog(webManager, "New Name:");
if(name != null && !name.equals("")) {
    
    browserName.setText(name); 
    browserList.setSelectedIndex(browserList.getSelectedIndex());
    
        String selected  =  browserList.getSelectedValue();
 
    if(browserProps.getProperties().containsValue(selected)){
         browserProps.getProperties().remove(namef, selected);
         browserProps.setProperty(name, browserList.getSelectedValue());
         browserProps.storeProperties(null);
    }else if(userWebProps.getProperties().containsValue(selected)){
         userWebProps.getProperties().remove(namef, selected);
         userWebProps.setProperty(name, browserList.getSelectedValue());
         userWebProps.storeProperties(null);
         renameUserMenu(namef,name,webPopmenuList.getComponents());
         
         renameUserMenu(namef,name,webMenu.getMenuComponents());
    
    }
    
    
//    else{
//    browserList.setSelectedIndex(browserListModel.size()-1);
//    browserName.setText(name);
//    addBtn_B.setEnabled(true);
//     
//    }
    
    editBtn_b.setEnabled(true);
    renameMenuItem(name);    
    
    
}


    }//GEN-LAST:event_renameBtn_bActionPerformed

 private void renameMenuItem(String newName){
 
     if(newName.contains("fox")){firefox.setText(newName);       firefoxItem.setText(newName);}
     if(newName.contains("rome")){chrome.setText(newName);       chromeItem.setText(newName);}
     if(newName.contains("plorer")){iexplorer.setText(newName);  iexplorerItem.setText(newName);}
     if(newName.contains("pera")){opera.setText(newName);        operaItem.setText(newName);}
     if(newName.contains("fari")){safari.setText(newName);        safariItem.setText(newName);}
     
     
     
 
 }   
  
 private void renameUserMenu(String oldName, String newName, Component...comps){
 
     
 for(Component  c : comps){
 
   JMenuItem    mi = (JMenuItem)c;
   
   if(mi.getText().equals(oldName)){
     mi.setText(newName);
   }
 
   
 }
 
 
 }
 
    private void editBtn_bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtn_bActionPerformed
webEdit.setLocationRelativeTo(webManager);
webEdit.setVisible(true);
locationField.requestFocus();



        // TODO add your handling code here:
    }//GEN-LAST:event_editBtn_bActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

JFileChooser  fc  =  new JFileChooser(System.getProperty("user.home"));
   int op  =  fc.showOpenDialog(webEdit);
   File  file  = null;
   if(op == JFileChooser.APPROVE_OPTION){
   file = fc.getSelectedFile();
   locationField.setText(file.toString());
   
   browserProps.getProperties().replace(browserName.getText(), browserList.getSelectedValue(), file.toString());
  browserProps.storeProperties(null);
  browserListModel.removeElementAt(browserList.getSelectedIndex());
  browserListModel.addElement(browserProps.getPropertyValue(browserName.getText()));
  
   }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

webEdit.setVisible(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void defaultBrowserChbxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_defaultBrowserChbxItemStateChanged

 if(defaultBrowserChbx.isSelected()){
 defaultBrowser= browserList.getSelectedValue();
 if(defaultBrowserProps.getProperties().isEmpty())
    defaultBrowserProps.setProperty(browserName.getText(), defaultBrowser);
 else{
 defaultBrowserProps.getProperties().clear();
 defaultBrowserProps.setProperty(browserName.getText(), defaultBrowser);
 }
 defaultBrowserKey =  browserName.getText();
 defaultBrowserProps.storeProperties("default Browser property");
 defaultBrowserChbx.setEnabled(false);
  boldDefaultBrowser(webMenu.getMenuComponents());
  boldDefaultBrowser(webPopmenuList.getComponents());
 
 }


        // TODO add your handling code here:
    }//GEN-LAST:event_defaultBrowserChbxItemStateChanged

    private void defaultBrowserChbxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultBrowserChbxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_defaultBrowserChbxActionPerformed

    private void chromeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chromeActionPerformed


        openInWebBrowser(chrome.getText());

        // TODO add your handling code here:
    }//GEN-LAST:event_chromeActionPerformed

    private void iexplorerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iexplorerActionPerformed

 openInWebBrowser(iexplorer.getText());

    }//GEN-LAST:event_iexplorerActionPerformed

    private void operaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_operaActionPerformed

 openInWebBrowser(opera.getText());
    }//GEN-LAST:event_operaActionPerformed

    private void safariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_safariActionPerformed
 openInWebBrowser(safari.getText());

    }//GEN-LAST:event_safariActionPerformed

    private void chromeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chromeItemActionPerformed
openInWebBrowser(chromeItem.getText());

    }//GEN-LAST:event_chromeItemActionPerformed

    private void operaItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_operaItemActionPerformed
       openInWebBrowser(operaItem.getText());


        // TODO add your handling code here:
    }//GEN-LAST:event_operaItemActionPerformed

    private void iexplorerItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iexplorerItemActionPerformed

openInWebBrowser(iexplorerItem.getText());
    }//GEN-LAST:event_iexplorerItemActionPerformed

    private void safariItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_safariItemActionPerformed
openInWebBrowser(safariItem.getText());

        // TODO add your handling code here:
    }//GEN-LAST:event_safariItemActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
 
       
    }//GEN-LAST:event_formWindowActivated

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
   
    
// TODO add your handling code here:
    }//GEN-LAST:event_formComponentResized

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

     try {
         currentDoc.print();
         
         // TODO add your handling code here:
     } catch (PrinterException ex) {
     }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void saveAllBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllBtnActionPerformed
saveAll();
     

    }//GEN-LAST:event_saveAllBtnActionPerformed

    private void saveAs(){
    
    JFileChooser  fc  =  new JFileChooser(System.getProperty("user.home"));
    fc.setDialogTitle("Save As");
    int  opt  =  fc.showSaveDialog(this);
    
    if(opt == JFileChooser.APPROVE_OPTION){
        File  file  =  fc.getSelectedFile();
        if(Files.notExists(file.toPath())){
          
          try(BufferedWriter   br  =  new BufferedWriter(new FileWriter(file))){
             currentDoc.write(br);
             String title  = tabPane.getTitleAt(tabPane.getSelectedIndex());            
             currentFilesPros.getProperties().remove(title, extractPath(getTitle()));
             title  =  getFileName(file.toString());
             setTitle("CodePad - "+file.toString());
             tabPane.setTitleAt(tabPane.getSelectedIndex(), title);
             currentFilesPros.setProperty(title, file.toString());
             fileSaveState.set(tabPane.getSelectedIndex(), false);
            
            }catch(IOException ex){JOptionPane.showMessageDialog(this,"Unable to save this file."
                    + "","Error",JOptionPane.ERROR_MESSAGE);}
            
            
        }else{
        
            {JOptionPane.showMessageDialog(this,"Unable to save this file."
                    + "\nA file with this name  already exists","Error",JOptionPane.ERROR_MESSAGE);}
        
        }//end if-else 
        
 }//end if 
   
    
    }//end method 
    
    
    
    private void saveAllMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllMenuActionPerformed

saveAll();
    }//GEN-LAST:event_saveAllMenuActionPerformed

    private void saveAsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuActionPerformed
        saveAs();
        
        
        


    }//GEN-LAST:event_saveAsMenuActionPerformed

    private void zoomInMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInMenuActionPerformed

        zoomIn();


    }//GEN-LAST:event_zoomInMenuActionPerformed

    private void zoomOutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutMenuActionPerformed

        zoomOut();

    }//GEN-LAST:event_zoomOutMenuActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        
        String file  = extractPath(getTitle());
     try {
         compiler  =  new CodeCompiler(new File(file), new FileWriter("codePadCompile.txt"));
         compiler.compileFile();
         System.out.println(compiler.getErrorHandler());
     } catch (IOException ex) {
         Exceptions.printStackTrace(ex);
     }
  

     

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

directoryPane.setVisible(false);
showDPCheck.setSelected(directoryPane.isVisible());
showDPCheck.setIcon(createIcont("img/ProdStocks.png"));
     
    }//GEN-LAST:event_jButton1ActionPerformed

    private void showDirPaneMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showDirPaneMenuActionPerformed

if(showDirPaneMenu.isSelected() && directoryPane.isVisible()){
    directoryPane.setVisible(false);
    showDirPaneMenu.setText("Show Dirctories Pane");
}else {
    if(!showDirPaneMenu.isSelected() && !directoryPane.isVisible()){
    directoryPane.setVisible(true);
showDirPaneMenu.setText("Hide Dirctories Pane");
    }
}




        // TODO add your handling code here:
    }//GEN-LAST:event_showDirPaneMenuActionPerformed

    private void newDirBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newDirBtnActionPerformed

 if(leadSelection != null)directoryTree.setSelectionPath(treePath);
 String name  = JOptionPane.showInputDialog((findDialogVisible ? findDialog : this ), "Enter directotry name:","Directory Name",JOptionPane.DEFAULT_OPTION);
 if(leadSelection ==  null){
     String message  ="If you create a folder or file without selecting a directory where to place your new folder/file,\n"
             + "Your new folder/file will be placed in your home directory by default.\n"
             + "Your home directory is: "+System.getProperty("user.home");
     
 JOptionPane.showMessageDialog((findDialogVisible ? findDialog : this ), message,"Information",JOptionPane.PLAIN_MESSAGE);
 }
 
 
 if(name != null && !name.equals("")){
DefaultMutableTreeNode   newNode  = new DefaultMutableTreeNode(name);
if(leadSelection != null){
    
     File   f  =  new File(treeSelectedFile, name);
     if(!f.exists())
        f.mkdir();
     else {
         JOptionPane.showMessageDialog(this, "File not created","Error",JOptionPane.WARNING_MESSAGE);
         return;
     }
   leadSelection.add(newNode);
   ((DefaultTreeModel)directoryTree.getModel()).reload(leadSelection);
   directoryTree.expandPath(treePath);
}else {
      File   f  =  new File(System.getProperty("user.home"), name);
     if(!f.exists())
        f.mkdir();
     else {
         JOptionPane.showMessageDialog(this, "File not created","Error",JOptionPane.WARNING_MESSAGE);
         return;
     }  
     
    
    loadFileTree(new File(System.getProperty("user.home")));
((DefaultTreeModel)directoryTree.getModel()).reload(rootNode);


}
}//end if 



        
    }//GEN-LAST:event_newDirBtnActionPerformed

    private void deleteNodeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteNodeBtnActionPerformed

if(leadSelection != null){
DefaultMutableTreeNode  parent  =  (DefaultMutableTreeNode) leadSelection.getParent();
if(parent == null)
    ;
else {

    for(int i = 0; i < treeDirList.size(); ++i) {       
      if(treeDirProps.getProperties().containsValue(treeDirList.get(i))){
         boolean removed = treeDirProps.getProperties().remove(new File(treeDirList.get(i)).getName(),treeDirList.get(i));
          if(!removed){
             treeDirProps.getProperties().remove(treeDirList.get(i));         
          }
    
     break;
      }//end loop 
 }

 parent.remove(leadSelection);
 
 leadSelection = null;
 ((DefaultTreeModel) directoryTree.getModel()).reload(parent);
 
        
        }


}

    }//GEN-LAST:event_deleteNodeBtnActionPerformed

    private void directoryTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_directoryTreeValueChanged
 
        
 if(evt.getNewLeadSelectionPath() != null){
  deleteNodeBtn.setEnabled(true);
    treePath  =  evt.getNewLeadSelectionPath();
    leadSelection  = (DefaultMutableTreeNode) treePath.getLastPathComponent();
  
    treeSelectedFile =  new File(leadSelection.getUserObject().toString());

      String c = "";
    
    for(String x : treeDirList){  
        if(getTreePath().contains(x)){
           c =getTreePath().substring(1, getTreePath().length());        
           treeSelectedFile = new File(c);
       
          consoleList.addElement("File Path: ["+treeSelectedFile.getAbsoluteFile().toString()+"]");
           break;
        }//end if 
          if(fileExists(onlyParentPath(x), getTreePath())){            
        treeSelectedFile = new File(  onlyParentPath(x)+getTreePath() );
    
          consoleList.addElement("File Path: ["+treeSelectedFile.getAbsoluteFile().toString()+"]");
      break;
      }
          
    }//end for loop 



if(leadSelection.isLeaf() && leadSelection.getUserObject().toString().matches("\\w+\\s*\\.\\w+") ){
    newDirBtn.setEnabled(false);
    openTreeBtn.setEnabled(true);
     addLeafBtn.setEnabled(false);
}else{ newDirBtn.setEnabled(true);
   openTreeBtn.setEnabled(false);
    addLeafBtn.setEnabled(true);
    newDirBtn.setEnabled(true);
}


  }//end if statement 


    }//GEN-LAST:event_directoryTreeValueChanged

    private void directoryTreeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_directoryTreeFocusLost
        
     // directoryTree.clearSelection();
      addLeafBtn.setEnabled(false);
      deleteNodeBtn.setEnabled(false);
      //newDirBtn.setEnabled(false);
      openTreeBtn.setEnabled(false);
      
    }//GEN-LAST:event_directoryTreeFocusLost
private String onlyParentPath(String x){
   
  return x.substring(0, x.lastIndexOf(File.separator));

}
 private boolean fileExists(String f1, String f2){
 
  String fullPath = f1+f2;
  
 return new File(fullPath).exists();
 }   
    
    
 private String getTreePath(){
     
 
Object[]  em =    treePath.getPath();
String path = "";
   String sep = File.separator; 
    
     for (Object object : em) 
         path +=object+sep;
     
     
     
     path = path.substring(0, path.length()-1);
   System.out.println("getTreePath:"+path);
     
return path;
}   
    
    
    private void addLeafBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLeafBtnActionPerformed


        
        
 String name  = JOptionPane.showInputDialog((findDialogVisible ? findDialog : this ), "Enter file Name:","File Name",JOptionPane.DEFAULT_OPTION);
 
  if(leadSelection ==  null){
     String message  ="If you create a folder or file without selecting a directory where to place your new folder/file,\n"
             + "Your new folder/file will be placed in your home directory by default.\n"
             + "Your home directory is: "+System.getProperty("user.home");
     
 JOptionPane.showMessageDialog((findDialogVisible ? findDialog : this ), message,"Information",JOptionPane.PLAIN_MESSAGE);
 }
 
 
 
if(name != null && !name.equals("")){
    DefaultMutableTreeNode   newNode  = new DefaultMutableTreeNode(name);
    
if(leadSelection != null){
   
     File   f  =  new File(treeSelectedFile, name);
     
     if(!f.exists())
      
       try{
     if(name.matches("\\w+\\.\\w+"))   
           f.createNewFile();
     else {
         name +=".txt"; 
         f  =  new File(treeSelectedFile, name);
         f.createNewFile(); 
     }
         
       }catch(IOException ex){
           JOptionPane.showMessageDialog((findDialogVisible ? findDialog : this ), "File not created","Error",JOptionPane.WARNING_MESSAGE);
           return;
       }
        
        
   
   leadSelection.add(newNode);
   ((DefaultTreeModel)directoryTree.getModel()).reload(leadSelection);
   directoryTree.expandPath(treePath);
   
}else {
    
     File   f  =  new File(System.getProperty("user.home"), name);
     if(!f.exists())
      
       try{
     if(name.matches("\\w+\\.\\w+"))   
           f.createNewFile();
     else {
         name +=".txt"; 
         f  =  new File(System.getProperty("user.home"), name);
         f.createNewFile();
     }
         
       }catch(IOException ex){
           JOptionPane.showMessageDialog((findDialogVisible ? findDialog : this ), "File not created","Error",JOptionPane.WARNING_MESSAGE);
           return;
       }
    
    
    loadFileTree(new File(System.getProperty("user.home")));   
     rootNode.add(newNode);
   ((DefaultTreeModel)directoryTree.getModel()).reload(rootNode);

}

}//end if 
        
    }//GEN-LAST:event_addLeafBtnActionPerformed

    private void browswNodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browswNodeActionPerformed

        
    JFileChooser  fc  =  new JFileChooser(System.getProperty("user.home"));
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    
    int val  =  fc.showOpenDialog((findDialogVisible ? findDialog : this ));
    if(val == JFileChooser.APPROVE_OPTION){
        File rootFolder  =  fc.getSelectedFile();
        
        treeDirProps.setProperty(rootFolder.getName(), rootFolder.getAbsoluteFile().toString());
        treeDirList.add(rootFolder.getAbsoluteFile().toString()); 
        
        showProgress();
        loadFileTree(rootFolder);
        getToolkit().beep();
    }//end if 
    
    
        
        
    }//GEN-LAST:event_browswNodeActionPerformed

private void showProgress(){
   
    progressBar.setIndeterminate(true);
    progressBar.setStringPainted(true);
    progressBar.setVisible(true);

}    
    
    
    
 private void loadFileTree(File dir){
  
  
     
      FileWalkTask task  =  new FileWalkTask(Paths.get(dir.getAbsoluteFile().toString()));
      task.addPropertyChangeListener(this);
      task.execute();
 
 
 }   

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
   if( "state".equals(evt.getPropertyName())){
       progressBar.setString("Scanning Directories...");
        
    
    
    }    
       
        
        
    }
 
 
 
private  class  FileWalkTask  extends  SwingWorker<Void, Void>{

private  Path startDir;
        public FileWalkTask(Path dir) {
           
            this.startDir = dir;
        }

    
    
        @Override
        protected Void doInBackground() throws Exception {
           showProgress();
            
          File  rootFolder  =  startDir.toFile();    
           int totalFile =  rootFolder.listFiles().length;       
           File[]   files  =  rootFolder.listFiles();
           
       DefaultMutableTreeNode newNode  =  new DefaultMutableTreeNode(startDir);   
        for(int i  = 0; i < totalFile; ++i){
          createFolderNode(files[i],newNode); 
          if(files[i].isFile())
              newNode.add(new DefaultMutableTreeNode(files[i].getName()));
      }
        rootNode=  (DefaultMutableTreeNode) treeModel.getRoot();
        if(rootNode != null)
              rootNode.add(newNode);
        else rootNode =  newNode;
     ((DefaultTreeModel) directoryTree.getModel()).reload(rootNode);
     
    
            
            
            return null;
        }//end method 


       @Override
        protected void done(){  
        progressBar.setVisible(false);
        progressBar.setIndeterminate(false);
        
      
        
        }//end method      
        

}
    
   
//recursive method that walks the file tree and creates nodes from 
//visited directories
   private void createFolderNode(File folder,DefaultMutableTreeNode node ){
 
   
    if( !folder.isDirectory()) return;
   
    File[]  list = null;
     if(folder.isDirectory() && !folder.isHidden())
       list = folder.listFiles();
     
   DefaultMutableTreeNode newNode =  new DefaultMutableTreeNode(folder.getName());
   if(list == null) return;
 
   for(int i  = 0; i < list.length; ++i){
        if( !list[i].isDirectory())
            newNode.add(new DefaultMutableTreeNode(list[i].getName()));
    
        
         createFolderNode(list[i],newNode); 
  } //end for loop 
   
 node.add(newNode);

   } 
    
    
    
    private void directoryTreeTreeWillCollapse(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {//GEN-FIRST:event_directoryTreeTreeWillCollapse
        
      //  if(leadSelection == rootNode)
         // throw new ExpandVetoException(evt);
        
       
    }//GEN-LAST:event_directoryTreeTreeWillCollapse

    private void directoryTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_directoryTreeMouseClicked
       
   if(evt.getClickCount() == 2){
   if(treeSelectedFile != null){
       if(treeSelectedFile.isFile())
       fileManager.openFileInTab(treeSelectedFile.toString());
   }
   
   }  
        
        
    }//GEN-LAST:event_directoryTreeMouseClicked

    private void openTreeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openTreeBtnActionPerformed
       
        fileManager.openFileInTab(treeSelectedFile.toString());
        
        
        
    }//GEN-LAST:event_openTreeBtnActionPerformed

    private void showTerminalToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTerminalToggleActionPerformed
       
        if(showTerminalToggle.isSelected()){
           showTerminalToggle.setText("Hide Console");
           showTerminalToggle.setIcon(createIcont("img/terminal2.png"));
           terminal.setVisible(true);
           viewTerminal.setSelected(true);
          
        }else{
        
           showTerminalToggle.setText("Show Console");
           showTerminalToggle.setIcon(createIcont("img/terminal.png"));
           terminal.setVisible(false);
           viewTerminal.setSelected(false);
        
        }
        
        
    }//GEN-LAST:event_showTerminalToggleActionPerformed

   
    
   private void showTerminal(boolean show){
       
          if(show){
           showTerminalToggle.setText("Hide Console");
           showTerminalToggle.setIcon(createIcont("img/terminal2.png"));
           terminal.setVisible(show);
           viewTerminal.setSelected(show);
          
        }else{
        
           showTerminalToggle.setText("Show Console");
           showTerminalToggle.setIcon(createIcont("img/terminal.png"));
           showTerminalToggle.setSelected(show);
           terminal.setVisible(show);
           viewTerminal.setSelected(show);
        
        }
   
   } 
    
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
   
        
           
   treeModel =  new DefaultTreeModel(new DefaultMutableTreeNode());
    directoryTree.setModel(treeModel);
    showProgress();
    
    treeDirList.forEach((t) -> {
    loadFileTree(new File(t));
  });
      getToolkit().beep();  
  
    
    
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
    findDialog.setVisible(true);
    findTabpane.setSelectedIndex(0);
    findDialogVisible =  true;
        
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

    
       
    }//GEN-LAST:event_formMouseClicked

    private void jPanel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel18MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel18MouseClicked

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
     if(findDialogVisible) {  
          findDialog.setVisible(true);
           findDialog.setAlwaysOnTop(true);
          
     }

     
    }//GEN-LAST:event_formWindowGainedFocus

    private void findDialogFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_findDialogFocusLost
        findDialog.setFocusableWindowState(false);
    }//GEN-LAST:event_findDialogFocusLost

    private void findDialogFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_findDialogFocusGained
       
        
      
        
    }//GEN-LAST:event_findDialogFocusGained

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
       if(findDialogVisible)
           findDialog.setVisible(false); 
      
     
     
        
    }//GEN-LAST:event_formWindowIconified
boolean findDialogVisible =  false;
    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        
       if(findDialogVisible)
           findDialog.setVisible(true);
       
       
        
    }//GEN-LAST:event_formWindowDeiconified

    private void findDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_findDialogWindowClosing
        findDialogVisible = false;
      
    }//GEN-LAST:event_findDialogWindowClosing

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        
    }//GEN-LAST:event_formWindowDeactivated

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    
    }//GEN-LAST:event_formFocusGained

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
   
    }//GEN-LAST:event_formFocusLost

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        
    }//GEN-LAST:event_formWindowStateChanged

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
      if(findDialogVisible) {  
          // findDialog.setVisible(true);
           findDialog.setAlwaysOnTop(false);
     }

    }//GEN-LAST:event_formWindowLostFocus

    private void findDialogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_findDialogMouseClicked
    if(findDialogVisible) findDialog.setFocusableWindowState(true);
    }//GEN-LAST:event_findDialogMouseClicked

    private void findDialogWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_findDialogWindowGainedFocus
          //findDialog.setFocusableWindowState(true);
    }//GEN-LAST:event_findDialogWindowGainedFocus

    private void findDialogWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_findDialogWindowLostFocus
         //  findDialog.setFocusableWindowState(false);
    }//GEN-LAST:event_findDialogWindowLostFocus

    private void jTextField5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5MouseClicked

    private void jTextField5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField5MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5MousePressed

    private void jPanel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel22MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel22MouseClicked

    private void jTextField6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6MouseClicked

    private void jTextField6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField6MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6MousePressed

    private void jPanel26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel26MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel26MouseClicked

    private void jTextField7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7MouseClicked

    private void jTextField7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField7MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7MousePressed

    private void jPanel30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel30MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel30MouseClicked

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
    if(jCheckBox1.isSelected()) {  
    findP.setVisible(false);
    findF.setText("Find Next");
    findF.setIcon(null);
    }else{
    findP.setVisible(true);
    findF.setIcon(createIcont("img/1rightarrow.png"));
    findF.setText(null);
    }
    
    }//GEN-LAST:event_jCheckBox1StateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
       findDialog.setVisible(false);
       findDialogVisible =  false;
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
    findDialog.setVisible(true);
    findTabpane.setSelectedIndex(0);
    findDialogVisible =  true;
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    findDialog.setVisible(true);
    findTabpane.setSelectedIndex(1);
    findDialogVisible =  true;  
        
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
    findDialog.setVisible(true);
    findTabpane.setSelectedIndex(2);
    findDialogVisible =  true; 
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
    findDialog.setVisible(true);
    findTabpane.setSelectedIndex(1);
    findDialogVisible =  true; 
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
    findDialog.setVisible(true);
    findTabpane.setSelectedIndex(3);
    findDialogVisible =  true;
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
 
        
        if(findDialogVisible){
            findDialog.setVisible(false); 
            findDialogVisible =  false;
            gotoLineBox.setVisible(true);
            
        }else gotoLineBox.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void showDPCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showDPCheckActionPerformed
      
            showTree();
    }//GEN-LAST:event_showDPCheckActionPerformed

    private void themBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_themBoxItemStateChanged
       
        
  String value  = themBox.getSelectedItem().toString();
      
 if(evt.getStateChange() ==  ItemEvent.SELECTED){
     SwingUtilities.invokeLater(() -> {changeTheme(value);}); 
 }
  
  
  

        
    }//GEN-LAST:event_themBoxItemStateChanged

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        hintBox.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void toolBarChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarChkActionPerformed
    
        if(!toolBarChk.isSelected()){
        toolBarPane.setVisible(false);
        }else toolBarPane.setVisible(true);
        
        
    }//GEN-LAST:event_toolBarChkActionPerformed

    private void docToolBarchkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docToolBarchkActionPerformed
       if(docToolBarchk.isSelected()) docToolBar.setVisible(true);
       else docToolBar.setVisible(false);
        
    }//GEN-LAST:event_docToolBarchkActionPerformed
Set<String> set  =  new TreeSet<>();
Highlighter.HighlightPainter   painter =  new CodePadHighLighter(Color.CYAN);
    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
   
     String   pattern    = findComBox.getSelectedItem().toString();
     highlightText =  pattern;
   set.add(pattern);
   findComBox.removeAllItems();
  set.forEach((t) -> {
      findComBox.addItem(t);
  });
    
    try {
     
         addHighlightInto(currentDoc, painter,pattern,true);
         isHighlight =  true;
         hihglitToggle.setSelected(true);
         hihglitToggle.setIcon(createIcont("img/62854-light-bulb-icon.png"));
     } catch (BadLocationException ex) {
         Exceptions.printStackTrace(ex);
     }
   
   
    }//GEN-LAST:event_jButton23ActionPerformed

    String highlightText  = null;
    boolean isHighlight =  false;
    private String currentSelection;
    private int selectionPos = 0;
    private void hihglitToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hihglitToggleActionPerformed
       
       
                if( hihglitToggle.isSelected() && !isHighlight){
                 if(highlightText != null){
                 
                     try {
                         addHighlightInto(currentDoc, painter, highlightText,true);
                         isHighlight = true;
                         
                     } catch (BadLocationException ex) {
                         Exceptions.printStackTrace(ex);
                     }
                 
                   }
                
                }else{
                    if(isHighlight ){
                    removeHighlightFrom(currentDoc);
                    isHighlight =  false;
                    }
                
                }
                
                if(hihglitToggle.isSelected())
                  hihglitToggle.setIcon(createIcont("img/62854-light-bulb-icon.png"));
                else hihglitToggle.setIcon(createIcont("img/Bulb-icon.png"));
        
    }//GEN-LAST:event_hihglitToggleActionPerformed

    private void findNextBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findNextBtnActionPerformed
        
        
       
     try {
         //obtain a highlighter from this text component
         Highlighter  highlighter =  currentDoc.getHighlighter();
         Document  doc  =  currentDoc.getDocument();
         String docText   =  doc.getText(0, doc.getLength());
        
         if(currentSelection != null)
         if(((selectionPos = docText.toUpperCase().indexOf(currentSelection.toUpperCase(), selectionPos) ) >= 0)){
             try {
                 highlighter.addHighlight(selectionPos, selectionPos+currentSelection.length(), painter);
                 currentDoc.getCaret().setDot(selectionPos+currentSelection.length());
                 currentDoc.requestFocus();
                 selectionPos += currentSelection.length();
                 
                  isHighlight =  true;
                   hihglitToggle.setSelected(true);
                   hihglitToggle.setIcon(createIcont("img/62854-light-bulb-icon.png"));
             } catch (BadLocationException ex) {
                 Exceptions.printStackTrace(ex);
             }
         }
         
         
         
         
//           try {
//               if(currentSelection != null)
//               findNextOccurence(currentDoc, painter, currentSelection,selectionPos);
//        } catch (BadLocationException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        
     } catch (BadLocationException ex) {
         Exceptions.printStackTrace(ex);
     }
        
     System.out.println("selectionPos: "+selectionPos);   
    }//GEN-LAST:event_findNextBtnActionPerformed

    private void findPrevBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findPrevBtnActionPerformed
       
  
  
        
        
    }//GEN-LAST:event_findPrevBtnActionPerformed

    private void documentCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_documentCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_documentCaretUpdate

    private void documentMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_documentMouseDragged
        System.out.println("mouse draged");
    }//GEN-LAST:event_documentMouseDragged

    private void documentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_documentMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_documentMouseClicked

    private void jList5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList5MouseClicked
       if(SwingUtilities.isRightMouseButton(evt))
        if(!consoleList.isEmpty())clearAction.setEnabled(true);
               
               
    }//GEN-LAST:event_jList5MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        addOdcListenerTo(jBoxText1);
        addOdcListenerTo(jBoxText3);
        initJavaSourceTextBox();
        jBoxText1.requestFocus();
        javaSourceBox.setLocationRelativeTo(this);
        javaSourceBox.setVisible(true);
        
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jchk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchk1ActionPerformed
    
     if(jchk1.isSelected()){
        initJavaSourceTextBox();
        jchk2.setEnabled(true);
     }
       
         
     
        
    }//GEN-LAST:event_jchk1ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
    String x =jBoxText2.getText();
      File  file  =  browseFileSystem(javaSourceBox,jBoxText2.getText(), true, true)[0];
       jBoxText2.setText(file.toString());
       if( !x.equals(jBoxText2.getText())){
           jchk1.setSelected(false);
          
       }else{
       jchk1.setSelected(true);
       
       
       }
       
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        javaSourceBox.setVisible(false);
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jchk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchk2ActionPerformed
       askFolderName();
     
        
    }//GEN-LAST:event_jchk2ActionPerformed

    private void jchk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchk3ActionPerformed
       if(jchk3.isSelected()){
             if(jBoxText1.getText().length() == 0 ||jBoxText3.getText().length() ==0 ){
                jFinishBtn.setEnabled(false);
                 }  
       jBoxText3.setEnabled(true);
       jBoxText3.requestFocus();
       }else{
         if(jBoxText1.getText().length() > 0 )  
             jFinishBtn.setEnabled(true);
       jBoxText3.setEnabled(false);
       }
       
     
       
    }//GEN-LAST:event_jchk3ActionPerformed

    private void jradio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jradio2ActionPerformed
      
        if(jradio2.isSelected()){
         jradio6.setSelected(false);
         jradio6.setEnabled(false);
         
         jradio4.setSelected(false);
         jradio4.setEnabled(false);
         
         jradio7.setSelected(false);
         jradio7.setEnabled(false);
         jradio8.setSelected(false);
         jradio8.setEnabled(false);
       }
        
    }//GEN-LAST:event_jradio2ActionPerformed

    private void jradio3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jradio3ActionPerformed
        if(jradio3.isSelected()){
         jradio6.setSelected(false);
         jradio6.setEnabled(false);
         
         jradio7.setSelected(false);
         jradio7.setEnabled(false);
         jradio8.setSelected(false);
         jradio8.setEnabled(false);
         jradio4.setSelected(true);
         jradio4.setEnabled(true);
       }
    }//GEN-LAST:event_jradio3ActionPerformed

    private void jradio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jradio1ActionPerformed
        if(jradio1.isSelected()){
         jradio6.setEnabled(true);
         jradio7.setEnabled(true);
         jradio8.setEnabled(true);
         jradio4.setEnabled(true);
       }
    }//GEN-LAST:event_jradio1ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        
    String comment  =  (String) commentBox .getSelectedItem();

     try {
         
         switch(comment){
             case "C IN-LINE COMMENT":
                 addComments(currentDoc,"java-s",true);
                 break;
         case "C MULTILINE COMMENT":
                 addComments(currentDoc,"css",false);                
                 break;
                 
                 case "HTML COMMENT":
                 addComments(currentDoc,"html",false);
                 break;
                 case "JAVADOC COMMENT":
                 addComments(currentDoc,"javadoc",true);
                
                 break;
         
         }
 
     } catch (BadLocationException ex) {
         Exceptions.printStackTrace(ex);
     }
        
        
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
       String comment  =  (String) commentBox .getSelectedItem();
     try {
         switch(comment){
             case "C IN-LINE COMMENT":
                 removeComments(currentDoc,"java-s",true);
                 break;
         case "C MULTILINE COMMENT":
                 removeComments(currentDoc,"css",false);               
                 break;
                 
                 case "HTML COMMENT":
                 removeComments(currentDoc,"html",false); 
                 break;
         
         }
   } catch (BadLocationException ex) {
         Exceptions.printStackTrace(ex);
     }
        
    }//GEN-LAST:event_jButton46ActionPerformed

    private void copyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_copyBtnActionPerformed
private String consoleSelectedVal;
    private void jList5ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList5ValueChanged
  if(!consoleList.isEmpty() ){     
       String x =   jList5.getSelectedValue();
       consoleSelectedVal =  x;
       if( x != null && x.startsWith("File Path:")){
       x =  x.substring(x.indexOf("[")+1, x.lastIndexOf("]"));
        addSelectionToSysClipboard(x);
        consoleSelectedcanCopy =  true;
       }else{
           consoleSelectedcanCopy =  false;
       }
       
       copyAction.setEnabled(consoleSelectedcanCopy);
  }else {copyAction.setEnabled(true);}
    }//GEN-LAST:event_jList5ValueChanged

    private void commentBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_commentBoxItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_commentBoxItemStateChanged

    private void commentBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commentBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_commentBoxActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        
     if(mainPaneDialog.getComponentCount() > 0)  
          mainPaneDialog.remove(0);
     
        mainPaneDialog.revalidate();
        codepade.containers.JarPaneBox  fisrtPane =   new codepade.containers.JarPaneBox(this) ;
        codepade.containers.jarPaneBox2  secondPane =   new codepade.containers.jarPaneBox2(this,null) ;
        mainPaneDialog.add(fisrtPane,0);
        secondPane.setVisible(false);
         mainPaneDialog.add(secondPane,1);
        mainDialogBox.setLocationRelativeTo(this);
        mainDialogBox.setVisible(true);
       
        
    }//GEN-LAST:event_jMenuItem20ActionPerformed

private void addOdcListenerTo(JTextField field){

    field.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            
            if(field  == jBoxText1 && (!jchk3.isSelected() || (jchk3.isSelected() && jBoxText3.getText().length() > 0 )))
                jFinishBtn.setEnabled(true);
            
             
             
             if(field  == jBoxText3  && jBoxText1.getText().length() > 0 )
                jFinishBtn.setEnabled(true);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
           
            if(field  == jBoxText1 && (jchk3.isSelected() || !jchk3.isSelected() ) && e.getDocument().getLength() < 1)
                jFinishBtn.setEnabled(false);
            
            
            
               if(field  == jBoxText3 && e.getDocument().getLength() < 1)
                jFinishBtn.setEnabled(false);
            
            
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    });


    
}    

 private   void askFolderName(){
    if(jchk2.isSelected()){
        String x  =  JOptionPane.showInputDialog(javaSourceBox, "Enter Folder Name:");
            if(x != null){
            File f =  new File(jBoxText2.getText(),x);
             f.mkdir();
             jBoxText2.setText(f.getAbsolutePath());
             jchk2.setEnabled(false);
            }
            
        }
 
 }
    
    private void changeTheme(String value){
   
    int tabCount  =  tabPane.getTabCount();
    for(int i = 0; i  < tabCount; ++i){
        
    try{    
    JTextPane  doc  =  findTextPane(i);
    String text  =  doc.getDocument().getText(0, doc.getDocument().getLength());
    int dot =  doc.getCaret().getDot();
        System.out.println("dot>>>>>>>: "+dot);
    doc.getDocument().remove(0, doc.getDocument().getLength());
 
    checkSelectedThem(doc);
    doc.setDocument(new TextDocument(getExtension(tabPane.getTitleAt(i)),value));
    doc.getDocument().insertString(0, text, null);
    
   
    doc.getCaret().setDot(dot);
    }catch(BadLocationException exp){}
    }
    
    
    
    }
 private void showTree(){
   if(showDPCheck.isSelected()) showDPCheck.setIcon(createIcont("img/tick.png"));
        else showDPCheck.setIcon(createIcont("img/ProdStocks.png"));
        if(!showDPCheck.isSelected())directoryPane.setVisible(false);
  
        if(showDPCheck.isSelected())directoryPane.setVisible(true);
 
 }   
    
    private void saveAll(){
    
       int tabCount  =  tabPane.getTabCount();
        
        for(int i = tabCount - 1; i > -1; --i){
            String title  =  tabPane.getTitleAt(i);
          if( (currentFilesPros.getProperties().containsKey(title)) && 
               (fileSaveState.get(i) ==  true)){
              JTextPane    doc = findTextPane(i);
              String file  =  currentFilesPros.getPropertyValue(title);
              saveQuietly(file,doc);
              fileSaveState.set(i, false);
              saveBtn.setEnabled(fileSaveState.get(i));
              saveCmd.setEnabled(fileSaveState.get(i));
              saveAllBtn.setEnabled(fileSaveState.get(i));
              saveAllMenu.setEnabled(fileSaveState.get(i));
          }//end if 
            
        }//end for 
    }
 
 private String getSelectedDocFile(String tabTitle){
 
     String selectedDocFile =  null;
     String exte = null;
     //check if the selected file is already open
 if(currentFilesPros.getProperties().containsKey(tabTitle)){
 
     //now check the selected file is it is an html,xml,js and css
 
 
 switch(getExtension(tabTitle)){
     
 case ".html":
     exte = ".html";
     break;
     
case ".htm":
     exte = ".htm";
     break;
case ".xml":
     exte = ".xml";
     break;
case ".js":
     exte = ".js";
     break;
case ".css":
     exte = ".css";
     break;
 }//end switch 
  
 }//end if 
 
if(currentFilesPros.getPropertyValue(tabTitle).endsWith(exte))
    selectedDocFile = currentFilesPros.getPropertyValue(tabTitle);

 return selectedDocFile;
 }   
    
  

    private void changeFile(String old, String another){
     System.err.println(old);
   File   f  =  new File(old);
   File  newF  =  new File(another);
     
   f.renameTo(newF);
        System.out.println("new Name: "+f.getAbsolutePath());   
        
     
     
        
    
    
    }
    
  
    
    
      
  
          
private void readProperties(){



 
 if(themeProps.isFileFound())
   themesColor =  themeProps.getProperties().getProperty("current_theme", "graySkyblue");
 
 if(currentTabProps.isFileFound()){
     currentTab =  Integer.parseInt( currentTabProps.getProperties().getProperty("currntTab", "0"));
      fontSize =  Integer.parseInt( currentTabProps.getProperties().getProperty("fontSize", "11"));
 }
 
 
 
}    
    
private void changeTabTitle(String title ){

int currentTab  =  tabPane.getSelectedIndex();

tabPane.setTitleAt(currentTab, title);
tabPane.setTabComponentAt(currentTab, new TabController(this));



}//end method     
    
  
private void increaseFontSize(){

int tabs  =  tabPane.getTabCount();
  
  for(int i = tabs-1; i > -1; --i){
      
      
    JTextPane   textDoc =  findTextPane(i);
     fontSize  =  textDoc.getFont().getSize();
    fontSize += 2;
  textDoc.setFont(new Font(textDoc.getFont().getFamily(),textDoc.getFont().getStyle(), fontSize)  );
    }
  
    
    

}

  private void zoomIn(){

int tabs  =  tabPane.getTabCount();
scaleCounter++;
if( (scaleCounter) < scales.length){

    for(int i = tabs-1; i > -1; --i){
    JTextPane   textDoc =  findTextPane(i);
    setZoomInOnTabSelection(textDoc);
    }
  
    
    
      
}else {scaleCounter = scales.length -1;}

 System.out.println("zoomIn: "+scaleCounter);
  }  
  
private void setZoomInOnTabSelection(JTextPane   textDoc ){
    
    if(scaleCounter < scales.length){
    
double  scale  = new Double((scales[scaleCounter])).doubleValue() / 100;
      textDoc.getDocument().putProperty("ZOOM_FACTOR", scale);
      StyledDocument  doc  =(StyledDocument)  textDoc.getDocument();
      doc.setCharacterAttributes(0, 1, attrs, true);
      try {doc.insertString(0, "", attrs);} catch (BadLocationException ex) {}
textDoc.repaint(0,0, textDoc.getWidth(), textDoc.getHeight()); 
    }
}  
  




private void setZoomOutOnTabSelection(JTextPane   textDoc){

if(scaleCounter > -1 ){
    
double  scale  = new Double((scales[scaleCounter])) / 100;
textDoc.getDocument().putProperty("ZOOM_FACTOR", scale);
StyledDocument  doc  =(StyledDocument)  textDoc.getDocument();
doc.setCharacterAttributes(0, 1, attrs, true);
try {doc.insertString(0, "", attrs);} catch (BadLocationException ex) {} 
textDoc.repaint(0,0, textDoc.getWidth(), textDoc.getHeight());    
}//end if 

}//end method 
  private void zoomOut(){
   
 int tabs  =  tabPane.getTabCount();  
 scaleCounter--;
if( (scaleCounter) > -1 ){
     
   for(int i = tabs-1; i > -1; --i){
    JTextPane   textDoc =  findTextPane(i);
    setZoomOutOnTabSelection(textDoc);
    
    }  
    System.out.println("zoomOut: "+scaleCounter);

}else scaleCounter =  0;
  
  }
    
    
    /**0 --     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
// javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BrowserEditPane;
    private javax.swing.JScrollPane MainScrollPane;
    protected javax.swing.JMenuItem OARFMenu;
    private javax.swing.JButton addBtn_B;
    private javax.swing.JButton addLeafBtn;
    private javax.swing.JMenuItem addNewTreeFilePop;
    private javax.swing.JMenuItem addTreeDirPop;
    private javax.swing.JButton browseBtn_B;
    private javax.swing.JButton browserBtn;
    private javax.swing.JList<String> browserList;
    private javax.swing.JLabel browserName;
    private javax.swing.JButton browswNode;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    protected javax.swing.JMenuItem cRFLMenu;
    private javax.swing.JMenuItem chrome;
    private javax.swing.JMenuItem chromeItem;
    private javax.swing.JButton closeAllTabsBtn;
    private javax.swing.JMenuItem closeAllTabsMenu;
    private javax.swing.JButton closeBtn_b;
    private javax.swing.JButton closeTabBtn;
    private javax.swing.JMenuItem closeTabMenu;
    private javax.swing.JLabel col_lb;
    private javax.swing.JComboBox<String> commentBox;
    private javax.swing.JButton copyBtn;
    private javax.swing.JMenu createFileMenu;
    private javax.swing.JLabel currentLine_lb;
    private javax.swing.JButton cutBtn;
    private javax.swing.JCheckBox defaultBrowserChbx;
    private javax.swing.JButton deleteNodeBtn;
    private javax.swing.JMenuItem deleteTreeDirPop;
    private javax.swing.JLayeredPane directoryPane;
    private javax.swing.JTree directoryTree;
    private javax.swing.JPanel docToolBar;
    private javax.swing.JCheckBoxMenuItem docToolBarchk;
    private javax.swing.JTextPane document;
    private javax.swing.JButton editBtn_b;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitCmd;
    protected javax.swing.JLabel fileLen;
    private javax.swing.JMenu fileMenu;
    protected javax.swing.JLabel fileType;
    private javax.swing.JComboBox<String> findComBox;
    protected javax.swing.JDialog findDialog;
    private javax.swing.JButton findF;
    private javax.swing.JButton findNextBtn;
    private javax.swing.JButton findP;
    private javax.swing.JButton findPrevBtn;
    private javax.swing.JLabel findStatusLable;
    private javax.swing.JTabbedPane findTabpane;
    private javax.swing.JPanel findnPane;
    private javax.swing.JMenuItem firefox;
    private javax.swing.JMenuItem firefoxItem;
    private javax.swing.JDialog fontDialog;
    private javax.swing.JPanel fontPane;
    private javax.swing.JDialog gotoLineBox;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JToggleButton hihglitToggle;
    private javax.swing.JDialog hintBox;
    private javax.swing.JList<String> hintList;
    private javax.swing.JMenuItem iexplorer;
    private javax.swing.JMenuItem iexplorerItem;
    private javax.swing.JTextField jBoxText1;
    private javax.swing.JTextField jBoxText2;
    private javax.swing.JTextField jBoxText3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JButton jFinishBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JList<String> jList4;
    private javax.swing.JList<String> jList5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JOptionPane jOptionPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton13;
    private javax.swing.JRadioButton jRadioButton14;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar10;
    private javax.swing.JToolBar jToolBar11;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JToolBar jToolBar9;
    private javax.swing.JDialog javaSourceBox;
    private javax.swing.JCheckBox jchk1;
    private javax.swing.JCheckBox jchk2;
    private javax.swing.JCheckBox jchk3;
    private javax.swing.JRadioButton jradio1;
    private javax.swing.JRadioButton jradio2;
    private javax.swing.JRadioButton jradio3;
    private javax.swing.JRadioButton jradio4;
    private javax.swing.JRadioButton jradio5;
    private javax.swing.JRadioButton jradio6;
    private javax.swing.JRadioButton jradio7;
    private javax.swing.JRadioButton jradio8;
    protected javax.swing.JLabel lineNumberLable;
    private javax.swing.JTextField locationField;
    public javax.swing.JDialog mainDialogBox;
    public javax.swing.JPanel mainPaneDialog;
    private javax.swing.JButton newBtn;
    private javax.swing.JButton newDirBtn;
    private javax.swing.JMenuItem newFileMenu;
    private javax.swing.JMenuItem open;
    private javax.swing.JButton openBtn;
    private javax.swing.JButton openTreeBtn;
    private javax.swing.JMenuItem openTreePop;
    private javax.swing.JMenuItem opera;
    private javax.swing.JMenuItem operaItem;
    private javax.swing.JButton pasteBtn;
    private javax.swing.JProgressBar progressBar;
    protected javax.swing.JMenu recent;
    private javax.swing.JButton redoButton;
    private javax.swing.JMenuItem relaodTreePop;
    private javax.swing.JMenuItem removeAllTreePop;
    private javax.swing.JButton removeBtn_b;
    private javax.swing.JMenuItem removeTreeDirPop;
    private javax.swing.JButton renameBtn_b;
    private javax.swing.JMenuItem renameFileMenu;
    private javax.swing.JMenuItem renameTreePop;
    private javax.swing.JMenu runMenu;
    private javax.swing.JMenuItem safari;
    private javax.swing.JMenuItem safariItem;
    protected javax.swing.JButton saveAllBtn;
    protected javax.swing.JMenuItem saveAllMenu;
    private javax.swing.JMenuItem saveAsMenu;
    protected javax.swing.JButton saveBtn;
    protected javax.swing.JMenuItem saveCmd;
    private javax.swing.JMenu searchMenu;
    private javax.swing.JLabel selLines;
    private javax.swing.JLabel sel_lb;
    private javax.swing.JMenu settingMenu;
    private javax.swing.JCheckBoxMenuItem showDPCheck;
    private javax.swing.JCheckBoxMenuItem showDirPaneMenu;
    private javax.swing.JCheckBoxMenuItem showTermAlway;
    private javax.swing.JToggleButton showTerminalToggle;
    private javax.swing.JPanel statusPane;
    protected javax.swing.JTabbedPane tabPane;
    private javax.swing.JLayeredPane terminal;
    protected javax.swing.JComboBox<String> themBox;
    private javax.swing.JCheckBoxMenuItem toolBarChk;
    private javax.swing.JPanel toolBarPane;
    private javax.swing.JPopupMenu treePopupMenu;
    private javax.swing.JScrollPane treeScroll;
    private javax.swing.JToolBar treeToolBar;
    private javax.swing.JButton undoButton;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JCheckBoxMenuItem viewTerminal;
    private javax.swing.JDialog webEdit;
    private javax.swing.JDialog webManager;
    private javax.swing.JMenu webMenu;
    private javax.swing.JPopupMenu webPopmenuList;
    private javax.swing.JMenu windowMenu;
    private javax.swing.JButton zoomIn;
    private javax.swing.JMenuItem zoomInMenu;
    private javax.swing.JButton zoomOut;
    private javax.swing.JMenuItem zoomOutMenu;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables


    
    
    

  
   
 public  class SaveAction extends AbstractAction{   
  
String vaiableFile;
private boolean saveFirstTime = false;
private File file;
    public SaveAction(Icon icon, String command){
        
          super(command, icon);
      
    
    }
    

public SaveAction( ){
    super();
   
    
}    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
 
        createFileChooser();
      
         
    }//end method 
    
  
    
    
    
 private void createFileChooser(){

 String text  = null;
   int currtTab =  tabPane.getSelectedIndex();
   
 //currentFile   = extractPath(getTitle());
if(currentFile.matches("New\\s+\\d+") && !currentFilesPros.getProperties().containsValue(currentFile)){
    JFileChooser  fc  =  new JFileChooser();
    fc.setSelectedFile(new File(currentFile));
    
     int returnVal  =  fc.showSaveDialog((findDialogVisible ? findDialog : MainWindow.this ));
   if(returnVal == JFileChooser.APPROVE_OPTION){
       file  =  fc.getSelectedFile();
       
        //check if the new file name matches any existing file name in the list 
        if(currentFilesPros.getProperties().containsValue(file.toString())  || !Files.notExists(file.toPath()) ){   
         JOptionPane.showMessageDialog((findDialogVisible ? findDialog : MainWindow.this ), 
           "A file with this file name already exits. please choose a diffrent name.","Save Error",JOptionPane.ERROR_MESSAGE); 
         file = new File(currentFile);
         return; //stop and don't do any thing but just return 
        }//end if 
    
     text =  currentDoc.getText();
      saveFirstTime =  true;
      
   }else { return;}//end if 
   

}else {

     file  = new File(currentFile);
     saveFirstTime =  false;
 
}//end if  
       
  

      if(!isEnabled())
       setEnabled(true); 
   
   try(BufferedWriter br = new BufferedWriter(new FileWriter(file)) ) {
            final JTextPane  doc  =  currentDoc;
            final String textx =  text;
            Thread  t =  new Thread(){
                @Override
                public void run() {
                    try {
                        doc.write(br);
                     
                       if(saveFirstTime){
                            changeTabTitle(getFileName(file.toString()));
                             MainWindow.this.setTitle("Codepad - "+file.toString());
                             currentFile  =  extractPath(getTitle());
                             currentFilesPros.setProperty(getFileName(file.toString()), file.toString());
                             documentManager.changeFileIcon(getExtension(file.toString()));
                          doc.setDocument(new TextDocument(getExtension(file.toString()),themBox.getSelectedItem().toString()));
                          processDocPopupMenuActions(doc);
                          addDocListener(doc);
                          setDocFont(currentDoc, fontSize);
                          doc.getDocument().insertString(0, textx, attrs);
                          tabPane.setTabComponentAt(tabPane.getSelectedIndex(), new TabController(MainWindow.this));
                          
                          
                           
                          
                       }//end if 
                      
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (BadLocationException ex) {
                    }//end catch
                }//end run
            
            
            };//end thread 
            t.start();
            t.join();
 
            updateFileInfo(true,extesionProps,getExtension( MainWindow.this.getTitle()), fileType, fileLen,lineNumberLable,currentDoc, MainWindow.this); 
            webFile();
            setEnabled(false);
            
                
              } catch (IOException ex) {
               
               ex.printStackTrace();
               JOptionPane.showMessageDialog((findDialogVisible ? findDialog : MainWindow.this ), 
                       "Error while saving file, file has not been saved","Save Error",JOptionPane.ERROR_MESSAGE);
             } catch (InterruptedException ex) { }
    
  fileSaveState.set(currtTab,false);

 }//end method 
 
 
 
 
 
 
 }//end inner-class//end inner-class
 

    

 class CodePadHighLighter extends  DefaultHighlighter.DefaultHighlightPainter{

        public CodePadHighLighter(Color color) {
            super(color);
        }
 
 
 
 }






}//end top-level class 


