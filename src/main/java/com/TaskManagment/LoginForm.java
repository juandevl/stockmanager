package com.TaskManagment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Type;
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
  private JButton btnRegister;
  private static List<User> usersDB = new ArrayList<>();
  private static final String usersFile = "src/users/users.json";
  
  public LoginForm () {
    
    frame = new JFrame ("LoginForm");
    frame.setContentPane (loginPanel);
    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    frame.pack ();
    frame.setLocationRelativeTo (null);
    frame.setResizable(false);
    frame.setVisible (true);
    
    setUsersDB(); //Seteo la lista de usuarios registrados
    
  // Accion de click en login
    btnLogin.addActionListener (new ActionListener () {
      @Override
      public void actionPerformed (ActionEvent e)
      {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        /*System.out.println ("user: " + username);
        System.out.println ("pass: " + password);*/
        if ( logInUser(username,password) ){
          System.out.println("Ingreso de usuario " + username);
          frame.dispose();
          Main.openMainForm(); //Apertura de formulario principal
//          JOptionPane.showMessageDialog(frame, "Bienvenido " + username, "Login Exitoso",
//                  JOptionPane.INFORMATION_MESSAGE);
        }
        else {
//          JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrecta. Intente nuevamente.",
//                  "Error de Login", JOptionPane.ERROR_MESSAGE);
          System.out.println("Datos invalidos");
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
//    btnRegister.addActionListener(new ActionListener(){
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
////        User user = new User(txtUsername.getText(), new String(txtPassword.getPassword()), "admin");
////        signInUser(user);
//
//      }
//    });
    
//  Oculto boton de registro
    btnRegister.setVisible(false);
    main(null);
  }
  
  public static void main (String[] args)
  {
  }
  
//  Metodo para registar usuario en archivo users.json
  public void signInUser(User user){
    String path = "src/users/";
    String filename = "users.json";
    String filefinal = path+filename;
    List<User> users = new ArrayList<>();
//    Si no existe el directorio users, lo creo
    File dir = new File(path);
    if (!dir.exists()){
      dir.mkdir();
    }
    
//  Serializador para convertir el objeto User en texto JSON
    Gson gson = new Gson();
    
    
//  Una vez creado, creo el archivo donde almaceno los registros de usuarios
    File file = new File(filefinal);
    if(!file.exists()){
      try{
        file.createNewFile();
        System.out.println("Se creo el archivo " + filefinal);
      }
      catch ( IOException e ){
        throw new RuntimeException(e);
      }
    }
    
    if(file.exists() && !file.isDirectory()){
//    Si el archivo existe y contiene informacion
      if(file.length() != 0){
//      Leo el archivo users.json y creo una lista de usuarios
        try ( FileReader reader = new FileReader(file) ){
          // Leer el archivo y convertirlo en un List<User>
          Type userListType = new TypeToken<List<User>>(){ }.getType();
          users = gson.fromJson(reader, userListType);
          reader.close();
        }catch ( FileNotFoundException e ){
          e.printStackTrace();
        }catch ( IOException e ){
          throw new RuntimeException(e);
        }
      }
      
//    Agrego el nuevo usuario a la lista
      users.add(user);
      
      try( FileWriter fw = new FileWriter(file) ){
        gson.toJson(users, fw);
      }catch ( IOException e ){
        e.printStackTrace();
      }
        
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
  
  private static void setUsersDB(){
    File userfile = new File(usersFile);
    Gson gson = new Gson(); //Objeto serializador de json
    
    if(userfile.exists() && userfile.isFile()){
      //    Si el archivo existe y contiene informacion
      if(userfile.length() != 0){
        //      Leo el archivo users.json y creo una lista de usuarios
        try ( FileReader reader = new FileReader(userfile) ){
          // Leer el archivo y convertirlo en un List<User>
          Type usersType = new TypeToken<List<User>>(){ }.getType();
          usersDB = gson.fromJson(reader, usersType);
          System.out.println("Se cargo lista de usuarios registrados");
        }catch ( FileNotFoundException e ){
          e.printStackTrace();
        }catch ( IOException e ){
          throw new RuntimeException(e);
        }
      }
      return;
    }
    
    
  }
  
  
}
