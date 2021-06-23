package com.authentication.payloads.request;

import java.util.Set;

import javax.validation.constraints.*;
 
public class SignUpRequest {
    @NotBlank
    private String username;
 
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    private String phoneNumber;
    
    
  
    public SignUpRequest(@NotBlank String username, @NotBlank @Email String email, @NotBlank String password,
			String phoneNumber) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
  
}