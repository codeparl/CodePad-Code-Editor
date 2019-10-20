/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import org.openide.util.Exceptions;

/**
 *
 * @author HASSAN
 */
public class TextPaneDoc extends  JTextPane implements  CodePadeConstants{

    
    private int mouseX, mouseY;
    private Point p;
    public TextPaneDoc() {
        super();
        
    
       
        
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);

            FontMetrics  fm  =  getFontMetrics(getFont());
            
            int lineHeight  =  fm.getHeight();
            int lineWidth  =  getSize().width -20;
            int leftMargin  =  getInsets().left-1;
            int posY = 0;

Rectangle rec  =  this.modelToView(getCaretPosition());

Color   c  =  new Color(189,189,189);

if(getBackground() == WHITE){
     c  =  new Color(117,117,117);
     setCaretColor(Color.BLACK);
}else if(getBackground() == GRAY){
    c  =  new Color(245,245,245);
    setCaretColor(Color.WHITE);
}
if(rec  != null){
    posY = rec.y;
g.setColor(c); 
g.drawRect(leftMargin, posY, lineWidth, lineHeight);

}
repaint(0, getX(), getY(), getWidth(), getHeight());

        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        
 
    }

    

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        super.repaint(tm, 0, 0, width, height);
    }

  
    
    
    
}
