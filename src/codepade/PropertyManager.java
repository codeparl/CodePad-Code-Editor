/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

/**
 *
 * @author HASSAN
 */
public class PropertyManager  implements  CodePadeConstants{

    private final MainWindow mainWindow;
    public PropertyManager(MainWindow win ) {
        this.mainWindow = win;
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
  
    
    
}
