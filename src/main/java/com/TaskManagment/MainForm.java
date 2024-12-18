package com.TaskManagment;

import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import java.awt.event.*;
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
  private JPanel filterPanel;
  private JLabel lblFilter;
  private JTextField txtFilter;
  private JButton btnAdd;
  private JScrollPane dispatchScrollPane;
  private JTable dispatchTable;
  private JPanel btnPanel;
  private JButton btnDelete;
  private JSeparator horizLine;
  private JButton btnFinish;
  private static ProductTableModel productTableModel;
  private static ProductTableModel dispatchTableModel;
  private static List<Product> productsDB = new ArrayList<>();
  private static List<Product> productsFilter = new ArrayList<>();
  private static List<Product> productDispatch = new ArrayList<>();
  private static final String pathProducts = "src/products/products.json";
  
  public MainForm()
  {
    initialize();
    
    /* #### Listeners #### */
    txtFilter.getDocument().addDocumentListener(new DocumentListener(){
      @Override
      public void insertUpdate(DocumentEvent e)
      {
        productFilter();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e){
        productFilter();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e){}
    });
    
    btnAdd.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e)
      {
        addToDispatch();
      }
    });
    
    btnDelete.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e)
      {
        // Valido si hay filas en la tabla
        if ( dispatchTable.getRowCount() != 0 ){
          int res = JOptionPane.showConfirmDialog(frame, "¿Desea eliminar el item?", "Confirmar",
                JOptionPane.YES_OPTION);
          /*Si res == 0, se confirma la eliminacion, 1 si se cancela.*/
          if ( res == 0 ){
            removeFromDispatch();
          }
        }else{
          JOptionPane.showMessageDialog(frame, "La tabla se encuentra vacía", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
      }
    });
    btnFinish.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e)
      {
        try{
          dispatchTable.print();
        }
        catch ( PrinterException ex ){
          JOptionPane.showMessageDialog(frame, "Error al imprimir", "Error con impresora", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      }
    });
  }
  
  /*Metodo para setear los productos*/
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
  
//   Metodo para ajustar el ancho de las columnas al máximo del contenido de la celda
  private static void adjustColumnWidths(JTable table) {
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
  
  public static void openMainForm(){
    // Inicializo lista de productos, obtenida de archivo products.json
    Thread t = new Thread( MainForm::setProducts );
    t.start();
    try{
      t.join();
    }
    catch ( Exception e ){
      e.printStackTrace();
    }
    
    SwingUtilities.invokeLater(() -> new MainForm());
  }
  /*Metodo para filtrar los productos de la tabla*/
  private void productFilter(){
    String str = txtFilter.getText().trim().toLowerCase();
    if ( str.length() <= 1 ){
      productTableModel.setProducts(productsDB);
      productTableModel.fireTableDataChanged();
      return;
    }
    //Utilizo stream para filtrar los elementos, ya que crea un duplicado de la lista original
    productsFilter = productsDB
                             .stream()
                             .filter(p ->
                              p.getBrand().toLowerCase().contains(str) ||
                              p.getModel().toLowerCase().contains(str) ||
                              p.getId().toLowerCase().contains(str))
                             .toList();
    
    if ( !productsFilter.isEmpty() ){
      productTableModel.setProducts(productsFilter);
      productTableModel.fireTableDataChanged();
    }else {
      JOptionPane.showMessageDialog(frame, "No se encuentró ningún producto.");
    }
    
  }
  
  /*Metodo para inicializar los datos del form*/
  public void initialize(){
    productTableModel = new ProductTableModel(productsDB, new String[]{"ID", "Modelo", "Marca", "Stock disponible"});
    dispatchTableModel = new ProductTableModel(productDispatch, new String[]{"ID", "Modelo", "Marca", "Stock"});
    frame = new JFrame("Stock Manager");
    stockTable.setAutoCreateColumnsFromModel(true);
    stockTable.setModel(productTableModel);
    dispatchTable.setAutoCreateColumnsFromModel(true);
    dispatchTable.setModel(dispatchTableModel);
    adjustColumnWidths(stockTable);
    adjustColumnWidths(dispatchTable);
    
    scrollPanel.setViewportView(stockTable);
    dispatchScrollPane.setViewportView(dispatchTable);
    
    frame.setContentPane(mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo (null);
    frame.setResizable(false);
    frame.setVisible(true);
    
  }
  
  /*Metodo para agregar producto a dispatchTable y actualizar los valores de la tabla stockTable*/
  public void addToDispatch(){
    int index = stockTable.getSelectedRow();
    if ( index != -1 ){
      String input = JOptionPane.showInputDialog(frame, "Ingrese la cantidad:", "Cantidad:",
              JOptionPane.PLAIN_MESSAGE);
      // Valido que los datos ingresados sean validos
      if ( input != null  && !input.trim().isEmpty()){
        try {
          int quantity = Integer.parseInt(input); //convierto el valor ingresado a entero
          
          String id = (String) stockTable.getValueAt(index,0);
          String model = (String) stockTable.getValueAt(index,1);
          String brand = (String) stockTable.getValueAt(index,2);
          int stock = (int) stockTable.getValueAt(index,3);
          
          //Valido si la cantidad esta en el rango permitido
          if ( quantity > 0 && quantity <= stock){
            /*Verifico si el item ya se encuentra en el dispatch*/
            boolean exist = false;
            for ( Product product : productDispatch ){
              if ( product.getId().equals(id) ) {
                exist = true;
                product.setStock(product.getStock()+quantity); // Actualizo el stock
                dispatchTableModel.fireTableDataChanged(); // Informo que el model del dispatch se actualizo
                break;
              }
            }
            //Cargo nuevo producto a la tabla dispatch
            if ( !exist ){
              Product p = new Product(id, model, brand, quantity);
              productDispatch.add(p);
              dispatchTableModel.setProducts(productDispatch);
              dispatchTableModel.fireTableDataChanged(); //Informo al model que la tabla se actualizo
            }
            //Actualizo la tabla de productos original
            for ( Product product : productsDB ){
              if ( product.getId().equals(id) ){
                product.setStock(stock - quantity);
                productTableModel.fireTableDataChanged(); //Informo al model que la tabla se actualizo
                break;
              }
            }
            
            JOptionPane.showMessageDialog(null, "Producto agregado correctamente");
          }else{
            //Si no se ingreso una cantidad >0 && <= stock, muestro mensaje de error
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida", "Invalid input", JOptionPane.ERROR_MESSAGE);
          }
        } catch ( NumberFormatException ex ){
//          ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ingrese un dato válido", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
      }
    } else {
      //Si no se selecciono producto de la tabla, genero pop up de error
      JOptionPane.showMessageDialog(null, "Seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  /* Metodo para remover elemento del dispatch*/
  private void removeFromDispatch(){
    int index = dispatchTable.getSelectedRow();
    if ( index != -1 ){
      String id = (String) dispatchTable.getValueAt(index,0);
      String model = (String) dispatchTable.getValueAt(index,1);
      String brand = (String) dispatchTable.getValueAt(index,2);
      int stock = (int) dispatchTable.getValueAt(index,3);
      
      dispatchTableModel.removeProduct(id);
      dispatchTableModel.fireTableDataChanged();
      
      //Actualizo la tabla de productos original
      for ( Product product : productsDB ){
        if ( product.getId().equals(id) ){
          product.setStock(product.getStock() + stock);
          productTableModel.fireTableDataChanged(); //Informo al model que la tabla se actualizo
          break;
        }
      }
      JOptionPane.showMessageDialog(null, "Se elimino producto");
    }else{
      JOptionPane.showMessageDialog(frame, "No se selecciono ningun item", "ERROR!", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  
}
