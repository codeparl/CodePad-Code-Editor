/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author HASSAN
 */
public class ConsoleCellRenderer  extends JLabel  implements  ListCellRenderer<Object>, CodePadeConstants{
private  String[] candidateVAlues;
    
    public ConsoleCellRenderer() {
     setOpaque(true);
        
    }

    public void setCandidateVAlues(String...arrys){
    
    candidateVAlues = arrys;
    
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
       
        String val  =  value.toString();
       setText(val);
        if(isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(Color.WHITE);
        
        }else{
          setBackground(list.getBackground());
          setForeground(list.getForeground());
        
        }
   
        if(candidateVAlues != null && candidateVAlues.length > 0 && !isSelected){
            String cand =  candidateVAlues[0];
            
             String treeSlectedVAl  =  null;
             
            if(val.equals(cand)){
                setForeground(rgb(0,150,136));
                
                setFont(new Font(getFont().getFamily(), Font.BOLD, getFont().getSize()));
            }
                  
            
               if(candidateVAlues.length > 1&&   candidateVAlues[1] != null )
                 treeSlectedVAl =  candidateVAlues[1];
               if(treeSlectedVAl != null && val.startsWith("File Path:")){
                 setForeground(rgb(25,118,210));
            
            }//end method 
        
   
            
            
        }//end method 
  return this;  
        
    }
    
    
    
    
 
    
}
