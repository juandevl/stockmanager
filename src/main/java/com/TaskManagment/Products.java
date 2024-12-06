package com.TaskManagment;

public class Products
{
  private String id;
  private String model;
  private String brand;
  private int stock;
  
  public Products(String id, String model, String brand, int stock)
  {
    this.id = id;
    this.model = model;
    this.brand = brand;
    this.stock = stock;
  }
  
  public String getId(){ return id; }
  
  public String getModel(){ return model;  }
  
  public String getBrand(){ return brand;  }
  
  public int getStock(){ return stock; }
  
  
}
