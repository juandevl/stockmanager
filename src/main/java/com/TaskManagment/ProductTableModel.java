package com.TaskManagment;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {
  private List<Product> products;
  private String [] columnNames;
  
  public ProductTableModel(List<Product> products, String []columnNames) {
    this.products = products;
    this.columnNames = columnNames;
  }
  
  public void setProducts(List<Product> products)
  {
    this.products = products;
  }
  public void removeProduct(String id){
    int index = -1;
    for ( Product product : products ){
      if ( product.getId().equals(id) ){
        index = products.indexOf(product);
        break;
      }
    }
    if ( index != -1 ){
      products.remove(index);
      System.out.println("Se elimino producto de la tabla");
    }else{
      JOptionPane.showMessageDialog(null, "No se encontro item en despacho", "Error. Item no encontrado",
              JOptionPane.ERROR_MESSAGE);
    }
  }
  
  @Override
  public int getRowCount() {
    return products.size();
  }
  
  @Override
  public int getColumnCount() {
    return columnNames.length;
  }
  
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Product product = products.get(rowIndex);
    if ( product != null ){
      return switch ( columnIndex ){
                case 0 -> product.getId();
                case 1 -> product.getModel();
                case 2 -> product.getBrand();
                case 3 -> product.getStock();
                default -> null;
              };
    }
    return null;
  }
  
  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }
  
  
}