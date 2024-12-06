package com.TaskManagment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainForm
{
  private JFrame frame;
  private JPanel mainPanel;
  private JScrollPane scrollPanel;
  private JTable stockTable;
  private JButton button1;
  private static List<Product> productsDB = new ArrayList<>();
  private static final String pathProducts = "src/products/products.json";
  
  public MainForm()
  {
    setProducts();
    frame = new JFrame("MainForm");
    frame.setContentPane(mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo (null);
    frame.setVisible(true);
    
    productsDB.forEach( p -> {
      System.out.println(p.getId() + p.getModel());
    });
    
    stockTable.setModel(new ProductModel(productsDB));
    scrollPanel.add(stockTable);
    
  }
  
  private static void setProducts(){
    File fileProducts = new File(pathProducts);
    if(fileProducts.exists() && fileProducts.isFile()){
      if(fileProducts.length() != 0){
        try{
          FileReader reader = new FileReader(fileProducts);
          Gson gson = new Gson();
//          Type productType = new TypeToken<List<Product>>(){}.getType();
//          response = gson.fromJson(reader, productType);
          Product[] aux = gson.fromJson(reader, Product[].class);
          productsDB = Arrays.asList(aux);
        }
        catch ( Exception e ){
          throw new RuntimeException(e);
        }
      }
    }
  }
  
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(() -> new MainForm());
  }
  
  
}
