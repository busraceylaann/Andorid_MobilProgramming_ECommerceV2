package com.example.sananelazimv2.Model;

public class IlanlarimModel{
	private String result;
	private Object advertisementId;
	private Object image;
	private boolean tf;
	private Object price;
	private Object description;
	private Object sayi;
	private Object title;
	private Object memberId;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setAdvertisementId(Object advertisementId){
		this.advertisementId = advertisementId;
	}

	public Object getAdvertisementId(){
		return advertisementId;
	}

	public void setImage(Object image){
		this.image = image;
	}

	public Object getImage(){
		return image;
	}

	public void setTf(boolean tf){
		this.tf = tf;
	}

	public boolean isTf(){
		return tf;
	}

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

	public void setSayi(Object sayi){
		this.sayi = sayi;
	}

	public Object getSayi(){
		return sayi;
	}

	public void setTitle(Object title){
		this.title = title;
	}

	public Object getTitle(){
		return title;
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
			"IlanlarimModel{" + 
			"result = '" + result + '\'' + 
			",advertisementId = '" + advertisementId + '\'' + 
			",image = '" + image + '\'' + 
			",tf = '" + tf + '\'' + 
			",price = '" + price + '\'' + 
			",description = '" + description + '\'' + 
			",sayi = '" + sayi + '\'' + 
			",title = '" + title + '\'' + 
			",memberId = '" + memberId + '\'' + 
			"}";
		}
}
