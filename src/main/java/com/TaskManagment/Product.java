package com.TaskManagment;

public class Product
{
  private String id;
  private String model;
  private String brand;
  private int stock;
  
  public Product(String id, String model, String brand, int stock)
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
