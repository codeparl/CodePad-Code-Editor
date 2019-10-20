/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codepade;

import static codepade.CodePadeConstants.TAB_CLOSE_ICONS;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author HASSAN
 */




public class TabController extends JPanel implements CodePadeConstants {
   
private final  MainWindow mainWindow;
private TabManager  tabManager;    
    public TabController(MainWindow win) {
     //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
         mainWindow = win; 
         tabManager=  new TabManager(mainWindow);
        
        setBackground(Color.BLACK);
        setOpaque(false);
    
     
       add(new JLabel(createIcont(DEFAULT_FILE_ICON)));
 
       //make JLabel read titles from JTabbedPane
        JLabel label = new JLabel() {
            public String getText() {
                int i = mainWindow.tabPane.indexOfTabComponent(TabController.this);
                if (i != -1) {
                    return mainWindow.tabPane.getTitleAt(i);
                }
                return null;
            }
        };
        
        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        label.setOpaque(false);
        //tab button
        JButton button = new TabButton();
        button.setOpaque(false);
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
      
    }

   
    
    private class TabButton extends JButton implements  ActionListener{
        public TabButton() {
          
            setIcon(createIcont(TAB_CLOSE_ICONS[0]));
            setPreferredSize(new Dimension(getIcon().getIconWidth()+5, getIcon().getIconHeight()+5));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
           setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            setBorderPainted(true);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
           // setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
            
          
            
            
        }

        public void actionPerformed(ActionEvent e) {
            int i = mainWindow.tabPane.indexOfTabComponent(TabController.this);
       
           
             String title =  "";
             String remove =  null;
             String key  =  null;
             
          if(mainWindow.saveBtn.isEnabled()){
           tabManager.confirmBeforeRemovingTab(remove, key, i);
         }else{ 
              tabManager.removeTab(remove, key, i);
          }
           

        }//end method 
        
  
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

        }
    }

    private final  MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
               button.setIcon(createIcont(TAB_CLOSE_ICONS[1]));
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                 button.setIcon(createIcont(TAB_CLOSE_ICONS[0]));
                button.setBorderPainted(false);
            }
        }
    };
    }
