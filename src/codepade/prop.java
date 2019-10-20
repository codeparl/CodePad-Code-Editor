/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author HASSAN
 */
public class prop extends  Properties{

    private final  Properties  properties;
   private  String file;
 
    private boolean   fileFound  =  true;
    
    /**
     * 
     */
    public prop(String  file,Properties defaultProps) {
        super(defaultProps);
           this.file =  file;
         if(!new File(file).exists())
             fileFound = false;
        properties =  new Properties(defaultProps);
        
        
        
        
    }
   
    
 public  Object  setProperty(String key, String value ){
 

      
 return  properties.setProperty(key, value);
 }//end emtood 

    public boolean isFileFound() {
        return fileFound;
    }
   


 
public  void storeProperties(String comment){

    
  try(BufferedWriter out   = new BufferedWriter(new FileWriter(file))) {
            properties.store(out, comment);
        } catch (IOException ex) {
        }
  

}

public String getPropertyValue(String key){


return  properties.getProperty(key);
}

    public Properties getProperties() {
        return properties;
    }
 


 public void setFile(String file){
 
     this.file =  file;
 
 }

    public String getFile() {
        return file;
    }
 
 
 
 
    public  Object  loadProp(String key ){
 
        if(file == null)
           return  null;
        
         
        try (BufferedReader   input  =new  BufferedReader(new FileReader(file))){
           
            properties.load(input);
        } catch (IOException ex) {
        }
     
      return properties.getProperty(key);
 
 }//end emtood  
    
    

    
     
    public  Set<Entry<Object, Object>>  loadAllProp(){
 
        
         if(file == null)
           return  null;

        try(BufferedReader   input  =new  BufferedReader(new FileReader(file))  ){
            properties.load(input);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
      return properties.entrySet();
 
 }//end emtood  
    
    
}
