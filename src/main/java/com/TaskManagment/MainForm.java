package com.TaskManagment;

import javax.swing.*;

public class MainForm
{
  private JFrame frame;
  private JPanel mainPanel;
  private JScrollPane scrollPanel;
  private JTable stockTable;
  private JButton button1;
  
  public MainForm()
  {
    frame = new JFrame("MainForm");
    frame.setContentPane(mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo (null);
    frame.setVisible(true);
  }
  
  public static void main(String[] args)
  {
  }
  
  
}
