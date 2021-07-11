package com.example.sananelazimv2.Model;

public class IlanDetayModel{
	private Object price;
	private Object description;
	private Object state;
	private Object title;
	private Object category;
	private Object memberId;

	public void setPrice(Object price){
		this.price = price;
	}

	public Object getPrice(){
		return price;
	}

	public void setDescription(Object description){
		this.description = description;
	}

	public Object getDescription(){
		return description;
	}

	public void setState(Object state){
		this.state = state;
	}

	public Object getState(){
		return state;
	}

	public void setTitle(Object title){
		this.title = title;
	}

	public Object getTitle(){
		return title;
	}

	public void setCategory(Object category){
		this.category = category;
	}

	public Object getCategory(){
		return category;
	}

	public void setMemberId(Object memberId){
		this.memberId = memberId;
	}

	public Object getMemberId(){
		return memberId;
	}

	@Override
 	public String toString(){
		return 
			"IlanDetayModel{" + 
			"price = '" + price + '\'' + 
			",description = '" + description + '\'' + 
			",state = '" + state + '\'' + 
			",title = '" + title + '\'' + 
			",category = '" + category + '\'' + 
			",memberId = '" + memberId + '\'' + 
			"}";
		}
}
