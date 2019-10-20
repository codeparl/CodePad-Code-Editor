/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 *
 * @author HASSAN
 */
public class CodeCompiler {
 private File sourceFile;
 private String errorHandler;
private FileWriter  out;
private ArrayList<String>  messageList;
    public CodeCompiler(File sourceFile,FileWriter  out) {
        this.sourceFile = sourceFile;
        this.out =  out;
        messageList =  new ArrayList<>();
        
    }

    public ArrayList getMessageList() {
        return messageList;
    }

    
    
    
    
    public String getErrorHandler() {
        return errorHandler;
    }
 
 
    
    
public boolean compileFile(){

    //used to collect error might occur while compiling the file 
    DiagnosticCollector<JavaFileObject>  errorCollection  =  new DiagnosticCollector<>();
    //obtain the compile to compile java sorce file
    JavaCompiler    compiler =  ToolProvider.getSystemJavaCompiler();
    //this allows us to abtain this class files
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(errorCollection,null, null);
    //will hold some options 
    List<String> optionList =  new ArrayList<>();
    
    optionList.add("-classpath");
    optionList.add(System.getProperty("java.class.path;"+"dist/MainWindow.jar"));
    
    Iterable<? extends  JavaFileObject> compilatioUnit  =  fileManager.getJavaFileObjectsFromFiles(Arrays.asList(this.sourceFile));

    JavaCompiler.CompilationTask  task  =  compiler.getTask(null, fileManager, errorCollection, optionList, null, compilatioUnit);
    
if(task.call()){
   messageList.add("File compiled succesfully.");
   return true;
}else {
      StringBuilder sb  =  new StringBuilder();
    for (Diagnostic<? extends  JavaFileObject> dg : errorCollection.getDiagnostics()) {
      
        sb.append("Error occured at: ").append(dg.getLineNumber()).append("\n");
        sb.append("Error Source: ").append(dg.getSource().toUri());
    }
    errorHandler =  sb.toString();
}//end if 
return false;
}  
    
    
    
    
    
    
}
