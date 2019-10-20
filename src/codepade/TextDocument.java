/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.openide.util.Exceptions;

/**
 *
 * @author HASSAN
 */
public class TextDocument  extends DefaultStyledDocument implements  CodePadeConstants{
    
    
public    AttributeSet  KEY_ATTR_COLOR; 
public    AttributeSet   NORMAL_ATTR;
public    AttributeSet   COMMENT_ATTR;
private MutableAttributeSet   attributeSet;
public    String COMMENT =  "//";
public String FILE_NAME;
private final String THEME_COLOR;
 //set blue foreground color fro java keywords
  String keywords[];
 public TextDocument(String fileNale, String theme){
 
  keywords =  JAVA_KEYWORDS.split("\\|");
keywords[0] = keywords[0].substring(1);
 int i  =  keywords[keywords.length-1].length()-1;
  keywords[keywords.length-1] = keywords[keywords.length-1].substring(0, i);
      
     
attributeSet =  new SimpleAttributeSet();
StyleConstants.setLineSpacing(attributeSet, 0.2f);
     setParagraphAttributes(0, getLength(), attributeSet, false);
     
this.FILE_NAME = fileNale; 
THEME_COLOR =  theme;
 //set attribute colors fro the keywords 
   if(fileNale.endsWith(".java")){
    if(THEME_COLOR.equals("graySkyblue")){
     KEY_ATTR_COLOR =  SC.addAttribute(SC.getEmptySet(),StyleConstants.Foreground, GRAY_SKYBLUE);
     
    }
    
    
    
    if(THEME_COLOR.equals("whiteBlue")){
        KEY_ATTR_COLOR =  SC.addAttribute(SC.getEmptySet(),StyleConstants.Foreground, WHITE_BLUE);
    }
    
    
    
   }else if(THEME_COLOR.equals("graySkyblue"))
         KEY_ATTR_COLOR =  SC.addAttribute(SC.getEmptySet(),StyleConstants.Foreground, WHITE);
   else KEY_ATTR_COLOR =  SC.addAttribute(SC.getEmptySet(),StyleConstants.Foreground, BLACK);
   
   
 
   if(THEME_COLOR.equals("graySkyblue"))
 NORMAL_ATTR  =  SC.addAttribute(SC.getEmptySet(), StyleConstants.Foreground, WHITE);
   else 
       NORMAL_ATTR  =  SC.addAttribute(SC.getEmptySet(), StyleConstants.Foreground, BLACK);
 
 COMMENT_ATTR = SC.addAttribute(SC.getEmptySet(), StyleConstants.Foreground,COMMENT_COLOR );
 
 
 
 }

 
 

 
 
 
 private int findLast(String text, int position){
 
     while(position < text.length()){
     if(String.valueOf(text.charAt(position)).matches("\\W"))
         break;
      position++;
     }//end loop
     
     
 return position;
 }
 
 private int findFirst(String text, int position ){
 while(--position >= 0){
 if(String.valueOf(text.charAt(position)).matches("\\W"))
     break;
 
 }
return position; 
 }

 
    
 //called when any insertion of characters into the document occurs    
 public void insertString( int offset,String str, AttributeSet  a)throws BadLocationException{
       
      super.insertString(offset, str, a); 
 hilightKeywords(offset, str);
   //hilightComments(offset, str);

  // String text  =  getText(0, getLength());

                
         
        


 }//end method    
 
 Pattern  p;
private int   countNext(String text, String key){
  int count = 0;
  String[] x  =  text.split("\\s");
  for(String y: x){
      if(y.equals(key))
         ++count;
  }
  
//  
//      while( (pos = text.indexOf(s,pos)) >= 0){
//        String sub  =  text.substring(pos, pos+s.length());
//            System.out.println("Sub: "+sub);
//            pos += s.length();
//        }//end while loop 
    
return count;
}

 

 
public void hilightKeywords(int offset,String str) throws BadLocationException{
       String text = getText(0, getLength());
                    
                int before = findLastNonWordChar(text, offset,"\\W");
              
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length(),"\\W");
     
                int wordL = before;
                int wordR = before;
                
               while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                         if (text.substring(wordL, wordR).matches("(\\W)*("+JAVA_KEYWORDS+")")){
                             String x = text.substring(wordL, wordR);                              
                           if(String.valueOf(x.charAt(0)).matches("\\W"))
                             setCharacterAttributes(wordL+1, ((wordR) - wordL), KEY_ATTR_COLOR, false);
                            //else  setCharacterAttributes(wordL, wordR - wordL, NORMAL_ATTR, false);
                         
                         } 
                        else
                            setCharacterAttributes(wordL+1, (wordR - wordL), NORMAL_ATTR, true);
                              wordL = wordR;
                    }
                    wordR++;
                }//end while loop 

         
        
    

} 


public void hilightComments(int offset,String str) throws BadLocationException{
       String text = getText(0, getLength());
                    
                int before = findLastNonWordChar(text, offset,"//");
              
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length(),"//");
     
                int wordL = before;
                int wordR = before;

           while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("//")) {
                         if (text.substring(wordL, wordR).matches("//"))
                              setCharacterAttributes(wordL, wordR - wordL, COMMENT_ATTR, false);                       
                        else
                            setCharacterAttributes(wordL, wordR - wordL, NORMAL_ATTR, false);
                              wordL = wordR;
                    }
                    wordR++;
                }//end while loop 

         
        
    

} 



 
 private int findSpaceIn(String text, int i){
 
   while(--i >= 0){
   if(String.valueOf(text.charAt(i)).matches("\\W"))
      break;
   }
     
     
    return i;
 }
 
private void hilightKeywords(int offset, int len){
   String  regex  =  "(\\w)*("+JAVA_KEYWORDS+")\\b";
Pattern  p  = checkPattern(regex);
if(p == null)
return;   
    
    
 String text =  "";
 
        try {
            text = getText(0, getLength());
        } catch (BadLocationException ex) {
        }
 
    StyleContext  sc =  StyleContext.getDefaultStyleContext();
    AttributeSet  bgColor =  sc.addAttribute(sc.getEmptySet(),StyleConstants.Background, Color.LIGHT_GRAY);
   
    Matcher  m = p.matcher(text);  
    
  
 while(m.find()){
   setCharacterAttributes(m.start(), m.end()-m.start(),KEY_ATTR_COLOR, false);
    
  
 }//end  loop     
    

}//end method 


  private int findLastNonWordChar (String text, int index, String ch) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches(ch)) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index,String ch) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches(ch)) {
                break;
            }
            index++;
        }
        return index;
    }

    
    


 
 //called when any removal of characters from the document occurs
 public void remove(int offset,int len)throws BadLocationException{
 
     super.remove(offset, len);
// hilightKeywords(offset, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset,"\\W");
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset,"\\W");

                if (text.substring(before, after).matches("(\\W)*("+JAVA_KEYWORDS+")")) {
                    String x = text.substring(before, after);  
                           if(String.valueOf(x.charAt(0)).matches("\\W"))
                            setCharacterAttributes(before+1, after - before, KEY_ATTR_COLOR, false);
                           
                           else  setCharacterAttributes(before, after - before, KEY_ATTR_COLOR, false);
                    
                    
                } else {
                    setCharacterAttributes(before, after - before, NORMAL_ATTR, false);
                }
     
     
 }



    private void hilightAllOccurrenceOf(String expre){

String  regex  =  expre.trim();
Pattern  p  = checkPattern(regex);
if(p == null)
return;   
    


// String[]  occurrence =  matchFindSb.toString().split("\\s");
 
 String text =  "";
 
        try {
            text = getText(0, getLength());
        } catch (BadLocationException ex) {
        }
 
    StyleContext  sc =  StyleContext.getDefaultStyleContext();
    AttributeSet  bgColor =  sc.addAttribute(sc.getEmptySet(),StyleConstants.Background, Color.LIGHT_GRAY);
   
    Matcher  m = p.matcher(text);  
    
  
 while(m.find()){
   setCharacterAttributes(m.start(), m.end()-m.start(), bgColor, false);
  
 }//end  loop 

    
}//end method  

     
    private Pattern checkPattern( String regex){
    
        Pattern p  =  null;
        try{
p =  Pattern.compile(regex,Pattern.DOTALL);
    }catch(PatternSyntaxException  exp){
  
    }
        

    return p;
    }
    

    
    private int readCharSpapeAfter(String str, int offset){

    return 0;
    }
    
}//end class 
