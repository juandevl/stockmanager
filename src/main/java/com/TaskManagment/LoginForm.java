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
  private JPanel loginPanel;
  private JLabel lblUsername;
  private JTextField txtUsername;
  private JLabel lblPassword;
  private JPasswordField txtPassword;
  private JButton btnLogin;
  private JButton btnCancel;
  private JLabel lblTitle;
  private JButton btnRegister;
  
  public LoginForm () {
  // Accion de click en login
    btnLogin.addActionListener (new ActionListener () {
    @Override
    public void actionPerformed (ActionEvent e)
    {
      System.out.println ("user: " + txtUsername.getText ());
      System.out.println ("pass: " + new String (txtPassword.getPassword ()));
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
    btnRegister.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e)
      {
//        User user = new User(txtUsername.getText(), new String(txtPassword.getPassword()), "admin");
//        signInUser(user);
      
      }
    });
  }
  
  public static void main (String[] args)
  {
    JFrame frame = new JFrame ("LoginForm");
    frame.setContentPane (new LoginForm ().loginPanel);
    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    frame.pack ();
    frame.setLocationRelativeTo (null);
    frame.setVisible (true);
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
  
}
