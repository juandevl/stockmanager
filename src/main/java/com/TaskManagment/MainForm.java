package com.TaskManagment;

import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainForm
{
  private JFrame frame;
  private JPanel mainPanel;
  private JScrollPane scrollPanel;
  private JTable stockTable;
  private JSeparator horizLine;
  private JLabel lblTitleDispatch;
  private JPanel dispatchPanel;
  private JPanel filterPanel;
  private JLabel lblFilter;
  private JTextField txtFilter;
  private JButton btnAdd;
  private static List<Product> productsDB = new ArrayList<>();
  private static final String pathProducts = "src/products/products.json";
  private List<Product> productDispatch = new ArrayList<>();
  
  public MainForm()
  {
    Thread t = new Thread( () -> setProducts());
    t.start();
    try{
      t.join();
    }
    catch ( Exception e ){
      e.printStackTrace();
    }
    
    DefaultTableModel tmodel = initTableModel();
    
    frame = new JFrame("Stock Manager");
    stockTable.setAutoCreateColumnsFromModel(true);
    stockTable.setModel(tmodel);
    adjustColumnWidths(stockTable);

    scrollPanel.setViewportView(stockTable);

    frame.setContentPane(mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo (null);
    frame.setResizable(false);
    frame.setVisible(true);
    
    
    /* #### Listeners #### */
    txtFilter.addKeyListener(new KeyAdapter(){
      @Override
      public void keyTyped(KeyEvent e)
      {
        //        super.keyTyped(e);
        System.out.println(txtFilter.getText());
        
      }
    });
    
    
    btnAdd.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e)
      {
        int index = stockTable.getSelectedRow();
        String id = (String) stockTable.getValueAt(index,0);
        String model = (String) stockTable.getValueAt(index,1);
        String brand = (String) stockTable.getValueAt(index,2);
        int stock = (int) stockTable.getValueAt(index,3);
        Product p = new Product(id, model, brand, stock);
        productDispatch.add(p);
        
        System.out.println(p.toString());
        
        
      }
    });
  }
  
  
  private static void setProducts(){
    File fileProducts = new File(pathProducts);
    if(fileProducts.exists() && fileProducts.isFile()){
      if(fileProducts.length() != 0){
        try{
          FileReader reader = new FileReader(fileProducts);
          Gson gson = new Gson();
          Product[] aux = gson.fromJson(reader, Product[].class);
          productsDB = Arrays.asList(aux);
        }
        catch ( Exception e ){
          e.printStackTrace();
        }
      }
    }
  }
  
  
//   Método para ajustar el ancho de las columnas al máximo del contenido de la celda
  private void adjustColumnWidths(JTable table) {
    for (int col = 0; col < table.getColumnCount()-1; col++) {
      int maxWidth = 0;
      
      // Iterar por todas las filas para obtener el ancho maximo de la columna
      for (int row = 0; row < table.getRowCount(); row++) {
        var renderer = table.getCellRenderer(row, col);
        var comp = table.prepareRenderer(renderer, row, col);
        maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
      }
      
      TableColumn column = table.getColumnModel().getColumn(col);
      column.setPreferredWidth(maxWidth + 10);
    }
  }
  
  private DefaultTableModel initTableModel (){
    String [] columns = {"ID", "Modelo", "Marca", "Stock disponible"};
    DefaultTableModel model = new DefaultTableModel(columns, 0);
    // Convierto lista de productos a Object para introducirlos al modelo de tabla.
    for ( Product p : productsDB ){
      Object[] res = {p.getId(), p.getModel(), p.getBrand(), p.getStock()};
      model.addRow(res);
    }
    return model;
  }
  
  private Product getProductRow(){
    
    return null;
  }
  
  
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(() -> new MainForm());
  }
  
}
