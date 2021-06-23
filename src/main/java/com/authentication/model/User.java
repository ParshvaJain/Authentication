package com.authentication.model;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {
  @Id
  private String id;

  
  @Size(max = 20)
  private String username;

  
  @Size(max = 50)
  @Email
  private String email;

  
  @Size(max = 120)
  private String password;

 
  private String phoneNumber;

  
  private String userType = "user";
  private boolean confirmed = false;
  
  public User() {
	  super();
  }
  
  
  public User(String id, String username, String email, String password, String phoneNumber) {
	super();
	this.id = id;
	this.username = username;
	this.email = email;
	this.password = password;
	this.phoneNumber = phoneNumber;
}



public User(String id, String username, String email, String password, String phoneNumber, String userType,
		boolean confirmed) {
	super();
	this.id = id;
	this.username = username;
	this.email = email;
	this.password = password;
	this.phoneNumber = phoneNumber;
	this.userType = userType;
	this.confirmed = confirmed;
  }

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getUserType() {
		return userType;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}
	
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	  

}