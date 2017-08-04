/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Menu {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;
   private JLabel msglabel;

   public Menu(){
      prepareGUI();
   }
   public static void main(String[] args){
      Menu  swingContainerDemo = new Menu();  
      swingContainerDemo.launchMenu();
   }
   private void prepareGUI(){
      mainFrame = new JFrame("Git Da Bys To Gander");
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(3, 1));
      
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    
      statusLabel.setSize(350,100);
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }
   private void launchMenu(){
      headerLabel.setText("Git Da Bys to Gander");      
      JPanel panel = new JPanel();
      panel.setBackground(Color.magenta);
      panel.setLayout(new FlowLayout());        
      controlPanel.add(panel);        
      mainFrame.setVisible(true);      
   }   
}
