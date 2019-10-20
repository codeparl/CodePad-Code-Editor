/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleContext;
import org.openide.util.Exceptions;

/**
 *
 * @author HASSAN
 */
public interface CodePadeConstants extends CommandsManager{
    
     String DEFAULT_FILE_ICON ="img/edit-new-document-icon.png";
     String[] TAB_CLOSE_ICONS =  {"img/Button-Close-icon2.png","img/Button-Close-icon.png"};
     public   final String[] FILE_FORMATS = {"Hypertext Markup Language",
            "Java Source","Cascading Style sheet","JavaScript",
            "Extensible Markup Language","Hypertext pre-processor","Normal text","Extensible Markup Language"};    
    
     public final    String EXTENSIONS[] ={".html",".java",".css",".js",".xml",".php",".txt",".ste"};
    
 
  public  final static String JAVA_KEYWORDS = "(public|private|protected|int|long|short|float|double|byte|"
         + "for|while|do|switch|case|break|default|continue|class|abstract|implements|"
         + "extends|native|interface|char|catch|try|const|goto|void|return|transiet|super|"
         + "synchronized|package|import|static|final|finally|if|else|instaceof|new)";  
  
 public    Color  GRAY_SKYBLUE = new Color(144,202,249);
 public    Color  WHITE_BLUE = Color.BLUE;
 public  Color GRAY = new Color(66,66,66);
 //rgb(66,66,66)
 //104,104,104
 public  Color WHITE = Color.WHITE;
  public  Color BLACK = Color.BLACK;
         
 public   Color  NON_KEY_COLOR = Color.WHITE;
 public   Color  COMMENT_COLOR = Color.RED;
 public   StyleContext  SC =  StyleContext.getDefaultStyleContext();


    
 public default  String getPropertyStore(String fileName, boolean  external){
 
     Path p = null;
     String appData = null;
 if(external) {  
     try {
       if(System.getenv("AppData") != null){     
            if(!Files.exists(Paths.get(System.getenv("AppData")+"\\codepad\\properties")))
             p =      Files.createDirectories(Paths.get(System.getenv("AppData")+"\\codepad\\properties"));
       }else if(System.getProperty("user.home") != null)
             if(!Files.exists(Paths.get(System.getProperty("user.home")+"\\codepad\\properties")))
                p =  Files.createDirectories(Paths.get(System.getProperty("user.home")+"\\codepad\\properties"));
       
     } catch (IOException ex) {}
     
     
      
       appData =  System.getenv("AppData")+"\\codepad\\\\properties\\"+fileName;
     if(appData== null)
      appData = System.getProperty("user.home")+"\\codepad\\\\properties\\"+fileName;     
 }//end if  for external 
 else {
 appData = "src\\codepade\\default_propertis\\"+fileName;
     System.out.println("enternal: "+appData);
 }
 
     
     
     
 
 return appData;
 }   
 
 
 public default  Color rgb(int r,int g, int b){
 
 
 return new Color(r,g,b);
         
 }
 
 
public default Point insertAtCenterPoint(Object obj ){
Container con  = null;
JComponent  comp = null;
    if(obj instanceof  Container)
    con =  (Container) obj;
    else if(obj instanceof  JComponent) comp =  (JComponent) obj;
    else return null;

    
Dimension  screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
    //System.out.println("screen "+screenSize);
        Dimension odsn = null;
     
        if(con != null)
         odsn =  con.getSize();
        else if(comp != null)odsn =  comp.getSize();
        
      Point  p =  new Point((screenSize.width  / 3),(screenSize.height  / 3));
       
return  p;
}
 
 public default File[] browseFileSystem(Component parent,String currentDir, boolean open, boolean dirOnly){
 File[] files = null;
     JFileChooser  fc  =  new JFileChooser((currentDir == null ? System.getProperty("user.home") : currentDir));
    if(dirOnly) 
      fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    
    fc.setMultiSelectionEnabled(true);
 int val  =  0;
 if(open )
     val  =  fc.showOpenDialog(parent);
 else val  =  fc.showSaveDialog(parent);
 
 if(val == JFileChooser.APPROVE_OPTION){
  files  =  fc.getSelectedFiles();
 
 }
 return files;
 }
 
  public  default  void setDocFont(JTextPane doc, int size){
   doc.setFont(new Font(doc.getFont().getFamily(), doc.getFont().getStyle(), size));
      
 
 
 }
 
  public  default boolean areButtonsEnabled(JComponent...b){
  boolean e = false;
  for(JComponent x : b)
    if(x.isEnabled()) e = true;
  return e;
  }
  
   public  default void enableButtons(boolean value,JComponent...b){   
 for(JComponent x : b)
     if(value) x.setEnabled(value);
     else x.setEnabled(value);
       
       
       
}

  
  public  default  void insertCommand(JTextPane doc,prop commandsProps,String...commands){
 
      String cmd  = "";
      
     for(String cm :commands ){
       if(commandsProps.getProperties().containsKey(cm)){
           cmd  =  commandsProps.getPropertyValue(cm);
           insertTextIntoDoc(doc,cmd);
           }//end if 
    }//end loop  

  }//end method 
  
  public  default  void  insertTextIntoDoc(JTextPane doc,String text,String...opt){
  
  int caretPos  =  doc.getCaretPosition();
     try {
          doc.getDocument().insertString(caretPos, text, null);     
         } catch (BadLocationException ex) {
             Exceptions.printStackTrace(ex);
         }
      
  
  }//end method 
  
 
 public default  void displayMessage(JComponent com, String msg, String type){
 
 int t =  0;
 
    
 
 
 
 }
 
public default  void setExtensionProperties(prop  props){
props.setProperty(".html","Hypertext Markup Language" );
props.setProperty(".java","Java Source");
props.setProperty( ".css","Cascading Style sheet");
props.setProperty(".xml","Extensible Markup Language");
props.setProperty(".js","JavaScript");
props.setProperty(".php","Hypertext pre-processor");
props.setProperty(".txt","Normal text");

props.storeProperties("File Extensions properties");
}//end method 
 
 
 
 public default  void updateFileInfo(boolean readLine,prop extensionProps,String file, final Object...comp){
     
     NumberFormat  nf  =  NumberFormat.getInstance();
     JLabel fileType = (JLabel) comp[0]; 
     JLabel fileLen = (JLabel) comp[1];
     JLabel lineN = (JLabel) comp[2]; 
     JTextPane doc = (JTextPane) comp[3]; 
     JFrame  frame = (JFrame) comp[4];
  
//
//SwingUtilities.invokeLater(() -> {
         
      
        if(file != null &&  extensionProps.getProperties().containsKey(file)){
            
         fileType.setText(extensionProps.getPropertyValue(file)+" file");
            
        
          String  len = nf.format(doc.getText().trim().length());
          fileLen.setText(len);
          String path  = extractPath(frame.getTitle());
          
          if(readLine && (!path.matches("New\\s+\\d+")) ){
             lineN.setText(nf.format(howManyLinesOf(new File(path))));
              
          }else 
              lineN.setText(nf.format(lineNumber(doc.getText())));
          
          }else{
           fileType.setText(extensionProps.getPropertyValue(".txt")+" file");
     
        
           
        }//end if    
         
 //});// end thread 
    
          
         
 
 }//end method 
 
 
 public default  int  lineNumber(String text ){
 
     int  count  = 1;
     
    for(char x : text.toCharArray())
       if(x == '\n')
           ++count;

 return count;
 }
 
 
    
public default int getLineNumbers(JTextPane textPane ){
    int line  = 0;
     try {
         Document  doc  =  textPane.getDocument();
         String text  = doc.getText(0, textPane.getCaret().getDot());
         line  =  lineNumber(text);
      
     } catch (BadLocationException ex) {
         Exceptions.printStackTrace(ex);
     }
     
return line;
}
    
    
 
 
 
public default  long howManyLinesOf(File file ){
long nl  = 0;

try {
  List<String>   lines =  Files.readAllLines(file.toPath());
  nl  =  lines.size();
 
 } catch (IOException ex) {
             }
return nl+1;
} 
 
 
 
 /**
  * This method removes all menuItem components from a JMenu component. 
  * @param menu the menu that has menu items to be removed.
  * throws NullPointerException if the argument supplied is null.
  * throws IllegalArgumentException if the argument supplied is not a javax.swing.JMenu.
  */
 public default void removeAllMenuItems(JMenu  menu, prop closeList){
 
     if(menu == null )
         throw new NullPointerException();
     if( (menu instanceof JMenu) == false)
           throw new IllegalArgumentException();
     
     
     int compCount =  menu.getMenuComponentCount();
     
     if(compCount > 0){
     Component[]   menuItems =   menu.getMenuComponents();
     
         for (Component menuItem : menuItems) {
             menu.remove((JMenuItem)menuItem);
             
         }//end for loop 
        closeList.getProperties().clear();
     }//end if 
 
 
 }//end method 
 
 
 public default boolean executeProgram(String app,Path location){
 
 Runtime   runtime =  Runtime.getRuntime();
 boolean  succeeded = false;
         try {
             runtime.exec(app+" "+location.toString());
             succeeded=  true;
         } catch (IOException ex) {
             ex.printStackTrace();
         }
 
 return succeeded;
 }
 
 
 
 
 public default boolean isWebFile(String tabTitle ){
 if(tabTitle.matches("New\\s+\\d+"))
     return false;
 
 boolean webFile  =  false;

 switch(getExtension(tabTitle)){
     
 case ".html":
     webFile =  true;
     break;
     
case ".htm":
     webFile =  true;
     break;
case ".xml":
    webFile =  true;
     break;
case ".js":
   webFile =  true;
     break;
case ".css":
    webFile =  true;
     break;
 }//end switch 
      

    
 
     
     
     return webFile;
 
 }
 
 public default   void removeMenuItemIfFileOpen(String  path, JMenu  menu){
 
  
     int compCount =  menu.getMenuComponentCount();
     
     if(compCount > 0){
     Component[]   menuItems =   menu.getMenuComponents();
     
         for (Component menuItem : menuItems) {
             JMenuItem  mi = (JMenuItem)menuItem;
             if(path.equals(mi.getText())){
                 menu.remove(mi);
                 break;
             }//end  if 
         }//end for loop 
     
         
 
     }//end if 
      
 }//end metod 
 
 
 
 
  public default String extractPath(String title){
  
      return title.substring(title.lastIndexOf("-")+1).trim();
  
  }
 
  public default String getExtension(String file){
    
      if(file.contains("."))
       return file.substring(file.lastIndexOf("."), file.length());
    return ".txt";
    }
  
  
    public default ArrayDeque<String>    reverseList(Object[] list){
    
    int i = 0;
      for(; i < (list.length - (i+1)) ; ++i){
	Object temp = list[i];	
	list[i] = list[list.length - (i+1)];
	list[list.length - (i+1)] =  temp;	

   }  
        
    ArrayDeque<String> l2  =  new ArrayDeque<>();
        for (Object object : list) {
            l2.add(object.toString());
        }
        
        
        return  l2;
    
    }

  
 public default ArrayDeque<String> sliceArray(ArrayDeque<String> list, int offset){
 
 ArrayList<String>  array =  new ArrayList<>(); 
 array.addAll(list);
 ArrayList<String>  arrayList =  new ArrayList<>();
 int c = 0;
 if(array.size() > offset){
 for(int i  = list.size() - 1; i > 0; --i ){
  
     arrayList.add(array.get(i));
     if( arrayList.size() == c )
         break;
    c++; 
 }
 

 }//end if 
 

 
 list.clear();
 list.addAll(arrayList);
 
 
 return  list;
 } 
  
public default String getFileName(String file){  
 return new File(file).getName().trim();
 }

public default String openFile(final JFrame frame,JTextPane textDoc, String currentDir){
 String fileName = null;
   JFileChooser  fc  =  new JFileChooser();
    
     if(currentDir !=  null)
        fc.setCurrentDirectory(new File(currentDir));
     
   FileNameExtensionFilter  filter =  new FileNameExtensionFilter("Java Files[.java]",".java");
      
  fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Text Files[.txt]","txt");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Hypertext Markup Language Files[.html]",".html");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Cascading Style sheet Files[.css]",".css");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter("Javascript Files[.js]","js");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter(" Hypertext pre-processor Files[.php]",".php");
        fc.addChoosableFileFilter(filter);
        filter =  new FileNameExtensionFilter(" Extensible Markup Language Files[.xml]",".xml");
        fc.addChoosableFileFilter(filter); 
 
        
      int returnVal  =  fc.showOpenDialog(frame);
      
      if(returnVal == JFileChooser.APPROVE_OPTION){
       File file  =   fc.getSelectedFile();
       fileName =  file.getAbsolutePath();
      
       //here the file already create so just save without 
       //asking the user to suply the file name 
    
      // centralizeFileChooser(fc);
       
      
      }
 
 
 return fileName;
 
 }   

public default void printList(Collection<? extends Object> list){
  System.out.println("------------------------------");   
 System.out.println("CurrentList Interface:");
    for (Object s: list) {
        System.out.println(s);
    }
    System.out.println("------------------------------");  

}
     
public default ImageIcon createIcont(String name){
 
  java.net.URL  url =  getClass().getResource(name);
  
  if(url != null)
      return new ImageIcon(url);
  else{
     System.err.println("Url not font");
     return null;
  }
 
 }

public default String getFileNameOnly(String file){
 
     if(file.contains("\\") )
  file  =  file.substring(file.lastIndexOf("\\")+1,file.lastIndexOf(".") );
     else if(file.contains("/"))
   file  =  file.substring(file.lastIndexOf("/")+1,file.lastIndexOf(".") );
     else if (file.matches("\\w+\\.\\w+"))
        file =  file.substring(0,file.lastIndexOf(".") );
     
     
 return file;
 }

}//end interface