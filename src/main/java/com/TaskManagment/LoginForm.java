package com.TaskManagment;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LoginForm
{
  private JFrame frame;
  private JPanel loginPanel;
  private JLabel lblUsername;
  private JTextField txtUsername;
  private JLabel lblPassword;
  private JPasswordField txtPassword;
  private JButton btnLogin;
  private JButton btnCancel;
  private JLabel lblTitle;
  private static List<User> usersDB = new ArrayList<>();
  private static final String usersFile = "src/users/users.json";
  
  public LoginForm () {
    initialize(); //Inicializo los datos del LoginForm
    
    /*#### Listener ####*/
  // Accion de click en login
    btnLogin.addActionListener (new ActionListener () {
      @Override
      public void actionPerformed (ActionEvent e)
      {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if ( logInUser(username,password) ){
          System.out.println("Ingreso de usuario " + username);
          frame.dispose();
          JOptionPane.showMessageDialog(frame, "Bienvenido " + username, "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
/*          Inicializo formulario principal         */
          MainForm.openMainForm();
          
        }
        else {
          JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrecta. Intente nuevamente.", "Error de Login", JOptionPane.ERROR_MESSAGE);
        }
        
      }
    });
    // Accion de click en cancel
    btnCancel.addActionListener (new ActionListener () {
      @Override
      public void actionPerformed (ActionEvent e)
      {
        JFrame fr = (JFrame ) SwingUtilities.getWindowAncestor (btnCancel);
        //Si el frame existe, lo cierro.
        if(fr != null)
          fr.dispose ();
      }
    });
    
    txtUsername.addKeyListener(new KeyAdapter(){
      @Override
      public void keyPressed(KeyEvent e)
      {
        if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
          txtPassword.requestFocus();
        }
      }
    });
    
    txtPassword.addKeyListener(new KeyAdapter(){
      @Override
      public void keyPressed(KeyEvent e)
      {
        if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
          btnLogin.doClick();
        }
      }
    });
    
  }
  
  public void initialize(){
    frame = new JFrame ("Log in");
    frame.setContentPane (loginPanel);
    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    frame.pack ();
    frame.setLocationRelativeTo (null);
    frame.setResizable(false);
    frame.setVisible (true);
    
    // Inicializo lista de usuarios registrados, obtenida de archivo users.json
    Thread t = new Thread( LoginForm::setUsersDB );
    t.start();
    try{
      t.join();
    }
    catch ( Exception e ){
      e.printStackTrace();
    }
    
  }
  
  public boolean logInUser(String username, String password){
    for( User u : usersDB){
      if ( u.getUsername().equals(username) && u.getPassword().equals(password) ){
        return true;
      }
    }
    return false;
  }
  
  private static void setUsersDB()
  {
    File userfile = new File(usersFile);
    Gson gson     = new Gson(); //Objeto serializador de json
    
    if ( userfile.exists() && userfile.isFile() ){
      //    Si el archivo existe y contiene informacion
      if ( userfile.length() != 0 ){
        //      Leo el archivo users.json y creo una lista de usuarios
        try ( FileReader reader = new FileReader(userfile) ){
          // Leer el archivo y convertirlo en un List<User>
          User[] aux = gson.fromJson(reader, User[].class);
          usersDB = Arrays.asList(aux);
          //          System.out.println("Se cargo lista de usuarios registrados");
        }
        catch ( Exception e ){
          e.printStackTrace();
        }
      }
    }
  }
  
}
