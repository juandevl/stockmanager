package com.TaskManagment;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductModel extends AbstractTableModel {
  private List<Product> productList;
  private String[] columnNames;  // Nombres de las columnas (propiedades de Product)
  
  // Constructor que recibe la lista de productos
  public ProductModel(List<Product> productList) {
    this.productList = productList;
    
    // Suponiendo que Product tiene estos atributos como ejemplo
    columnNames = new String[] {"ID", "Model", "Brand", "Stock"};  // Los nombres de las columnas
  }
  
  @Override
  public int getRowCount() {
    return productList.size();  // El número de filas es igual al tamaño de la lista
  }
  
  @Override
  public int getColumnCount() {
    return columnNames.length;  // El número de columnas es el tamaño de columnNames
  }
  
  @Override
  public String getColumnName(int columnIndex) {
    return columnNames[columnIndex];  // Retorna el nombre de la columna
  }
  
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Product product = productList.get(rowIndex);
    
    switch (columnIndex) {
      case 0:
        return product.getId();
      case 1:
        return product.getModel();
      case 2:
        return product.getBrand();
      case 3:
        return product.getStock();
      default:
        return null;
    }
  }
}
