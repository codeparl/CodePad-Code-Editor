/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade.actions;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 *
 * @author HASSAN
 */
public class UndoableEditListenerx implements UndoableEditListener{

private final UndoManager manager;
private final EditActions.RedoAction redo;
private final EditActions.UndoAction  undo;
int x = 0;
    public UndoableEditListenerx(UndoManager manager, EditActions.RedoAction redo, EditActions.UndoAction undo) {
        this.manager = manager;
        this.redo = redo;
        this.undo = undo;
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
     manager.addEdit(e.getEdit());
     redo.updateRedoAction();
     undo.updateUndoAction();
        
    }//end method

    
    
    
}//end class 
