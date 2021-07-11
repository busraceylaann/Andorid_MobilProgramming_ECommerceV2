package com.example.sananelazimv2.Model;

public class AccountModel{
	private Object password;
	private Object phone;
	private Object email;
	private Object username;

	public void setPassword(Object password){
		this.password = password;
	}

	public Object getPassword(){
		return password;
	}

	public void setPhone(Object phone){
		this.phone = phone;
	}

	public Object getPhone(){
		return phone;
	}

	public void setEmail(Object email){
		this.email = email;
	}

	public Object getEmail(){
		return email;
	}

	public void setUsername(Object username){
		this.username = username;
	}

	public Object getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"AccountModel{" + 
			"password = '" + password + '\'' + 
			",phone = '" + phone + '\'' + 
			",email = '" + email + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}
