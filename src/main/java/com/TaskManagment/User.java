package com.TaskManagment;

public class User
{
  private int userId;
  private String username;
  private String password;
  private String role;
  
  public User (String username, String password, String role, int userId)
  {
    this.username = username;
    this.password = password;
    this.role = role;
    this.userId = userId;
  }
  
  public String getUsername()
  {
    return username;
  }
  
  public String getPassword()
  {
    return password;
  }
  
  public String getRole()
  {
    return role;
  }
  
  public int getUserId() { return userId; }
}
