package com.example.sananelazimv2.Model;

public class CallPhoneModel{
	private String phone;

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	@Override
 	public String toString(){
		return 
			"CallPhoneModel{" + 
			"phone = '" + phone + '\'' +
			"}";
		}
}
