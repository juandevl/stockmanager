package com.TaskManagment;

import javax.swing.*;

public class Main
{
  public static void main (String[] args){
    System.out.println("static main");
    SwingUtilities.invokeLater(() -> new LoginForm());
  }
  
  public static void openMainForm(){
    System.out.println("static openMainForm");
    SwingUtilities.invokeLater(() -> new MainForm());
  }
  
}
