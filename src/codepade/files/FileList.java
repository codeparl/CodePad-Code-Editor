/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade.files;

import java.util.List;

/**
 *
 * @author HASSAN
 */

public class FileList {
  private List<String> fileList;  
public List<String> getFileList(){

return fileList;
} 

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
    
    public void printList(){
    fileList.forEach((t) -> {
        System.out.println("File: "+t);
    });
    
    
    }
}
