/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author HASSAN
 */




public class FilesVisitor   extends SimpleFileVisitor<Path> {

    private final  DefaultMutableTreeNode parentNode;

    public FilesVisitor(DefaultMutableTreeNode parentNode) {
        this.parentNode = parentNode;
    }
    
    private void find(Path dir){
    DefaultMutableTreeNode node= null;
    if(Files.isDirectory(dir)){
        
    File[]    list  =  dir.toFile().listFiles();
    
    
    for(File f : list){
         node =  new DefaultMutableTreeNode(dir.getFileName());
       node.add(new DefaultMutableTreeNode(f.getName()));
    }
    }
     
   
    
    }
    
    


    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        
      return FileVisitResult.CONTINUE;  
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        
        
        
          find(file);
           
           
          
          
           
      return FileVisitResult.CONTINUE;   
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        
        find(dir);
      return FileVisitResult.CONTINUE; 
        
        
    }
    
    
    
    
    
    
    
}
