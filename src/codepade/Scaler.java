/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 *
 * @author HASSAN
 */
public class Scaler  extends StyledEditorKit{
    
    
   public  ViewFactory  getViewFactory(){
   return new NewViewFactory();
   
   
   
   }
    
   
   
  
    
  class NewViewFactory implements ViewFactory {

        @Override
        public View create(Element elem) {
            
            
           String  kind  =  elem.getName();
           if(kind != null){
           
               if(kind.equals(AbstractDocument.ContentElementName))
                   return   new LabelView(elem);
               else if(kind.equals(AbstractDocument.ParagraphElementName))
                   return new ParagraphView(elem);
               else if(kind.equals(AbstractDocument.SectionElementName))
                return new BoxV(elem, View.Y_AXIS);
               else if(kind.equals(StyleConstants.ComponentElementName))
                   return new ComponentView(elem);
               else if(kind.equals(StyleConstants.IconElementName))
                   return new IconView(elem);
               
               
           
           }
            
            
            
            
         return new LabelView(elem);
        }//end method 
  
  
  
  
    }
    
  

   class BoxV extends  BoxView{

        public BoxV(Element e, int x) {
            super(e, x);
        }//end 
        
    public double getZoomFactor() {
        Double scale = (Double) getDocument().getProperty("ZOOM_FACTOR");
        //System.out.println("zoomFactor: "+scale);
        if (scale != null) {
            return scale;
        }

        return 1;
    }
    

        @Override
        public void paint(Graphics g, Shape allocation) {
            
            Graphics2D   g2d  =  (Graphics2D) g;   
             g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            double zoomFactor  =  getZoomFactor();
            AffineTransform  aff =   g2d.getTransform();
            g2d.scale(zoomFactor, zoomFactor);
            super.paint(g,allocation);
            g2d.setTransform(aff);
            
            
            
        }

        @Override
        public float getMaximumSpan(int axis) {
            
         float  f  =  super.getMaximumSpan(axis);
            f  *=  getZoomFactor();
         return  f;
          
        }

        @Override
        public float getMinimumSpan(int axis) {
            
            float f  =  super.getMinimumSpan(axis);
               f  *=  getZoomFactor();
            return f;
        }

        @Override
        public float getPreferredSpan(int axis) {
             float  f  =  super.getPreferredSpan(axis);

            f  *=  getZoomFactor();
   
   return f;

        }

        @Override
        protected void layout(int width, int height) {
         
            super.layout(new Double( width/ getZoomFactor()).intValue(),
                    new Double(height* getZoomFactor()).intValue()); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
            double zoomFactor =  getZoomFactor();
            Rectangle alloc;
             alloc =  a.getBounds();
            
            Shape  s  =  super.modelToView(pos, alloc, b);
            alloc =  s.getBounds();
            alloc.x  *=zoomFactor;
            alloc.y *= zoomFactor;
            alloc.width *= zoomFactor;
            alloc.height*=  zoomFactor;
           
            return alloc;
        }

        @Override
        public int viewToModel(float x, float y, Shape a, Position.Bias[] arg3) {
            
            double zoomFactor  =  getZoomFactor();
          Rectangle  alloc  =  a.getBounds();
          
          x /= zoomFactor;
          y /= zoomFactor;
          
          alloc.x /= zoomFactor;
          alloc.y /= zoomFactor;
          alloc.width /= zoomFactor;
          alloc.height /= zoomFactor;
          
          
            
            return super.viewToModel(x, y, alloc, arg3);
        }

     
    
        
        
        
        
      
 
 }
  
  
    
}//end class 
