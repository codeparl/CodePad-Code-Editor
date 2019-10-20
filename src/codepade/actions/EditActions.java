/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author HASSAN
 */
public class EditActions {
    
  public static class UndoAction extends  AbstractAction{
     private RedoAction redo;
     private UndoManager manager;
        public UndoAction(UndoManager m) {
         super("Undo");  
         manager =  m;
         setEnabled(false);
        }//end method 

   @Override
  public void actionPerformed(ActionEvent e) {
      
      
       try {
         manager.undo();  
       } catch (CannotUndoException exp) {
           exp.printStackTrace();
       }//end catch 
       
       updateUndoAction();
       redo.updateRedoAction();
      
        }
  protected  void updateUndoAction(){
    if(manager.canUndo()){
        setEnabled(true);
        putValue(Action.NAME, "Undo");
    
    }else {
        setEnabled(false);
        putValue(Action.NAME, "Undo");
    }//end else 
  }//end method 

        public void setRedo(RedoAction redo) {
            this.redo = redo;
        }
  
  
  }//end nested class UndoAction
    
 
  public static class RedoAction extends  AbstractAction{
  
      private UndoAction  undo;
      private final UndoManager manager;

        public RedoAction( UndoManager manager) {
           super("Redo");
            this.manager = manager;
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
          try {
             manager.redo();   
            } catch (CannotRedoException exp) {
                exp.printStackTrace();
            }//end catch 
  
           updateRedoAction();
           undo.updateUndoAction();
        }//end method  
      
        protected  void updateRedoAction(){
        
        if(manager.canRedo()){
            setEnabled(true);
            putValue(Action.NAME, "Redo");
        }else {
            setEnabled(false);
            putValue(Action.NAME, "Redo");
        }//end else 
        }//end method 

        public void setUndo(UndoAction undo) {
            this.undo = undo;
        }
  
  
  }//end nested class UndoAction
  
  
  
}//end outer class 
