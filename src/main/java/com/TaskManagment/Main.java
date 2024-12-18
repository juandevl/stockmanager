package com.TaskManagment;

import javax.swing.*;

public class Main
{
  public static void main (String[] args){

    //Inicializo la ejecucion, invocando el formulario de login
    SwingUtilities.invokeLater(() -> new LoginForm());
  }
  
}
