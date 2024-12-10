package com.TaskManagment;

import javax.swing.*;

public class Main
{
  public static void main (String[] args){
//    System.out.println("static Main::main");
    SwingUtilities.invokeLater(() -> new LoginForm());
  }
  
}
