package com.example.sananelazimv2.Model;

public class IlanBilgiUpdate{
	private boolean tf;

	public void setTf(boolean tf){
		this.tf = tf;
	}

	public boolean isTf(){
		return tf;
	}

	@Override
 	public String toString(){
		return 
			"IlanBilgiUpdate{" + 
			"tf = '" + tf + '\'' + 
			"}";
		}
}
