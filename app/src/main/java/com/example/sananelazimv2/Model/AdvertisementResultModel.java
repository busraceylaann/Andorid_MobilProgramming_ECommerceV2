package com.example.sananelazimv2.Model;

public class AdvertisementResultModel{
	private String advertisementId;
	private boolean tf;
	private String memberId;

	public void setAdvertisementId(String advertisementId){
		this.advertisementId = advertisementId;
	}

	public String getAdvertisementId(){
		return advertisementId;
	}

	public void setTf(boolean tf){
		this.tf = tf;
	}

	public boolean isTf(){
		return tf;
	}

	public void setMemberId(String memberId){
		this.memberId = memberId;
	}

	public String getMemberId(){
		return memberId;
	}

	@Override
 	public String toString(){
		return 
			"AdvertisementResultModel{" + 
			"advertisementId = '" + advertisementId + '\'' + 
			",tf = '" + tf + '\'' + 
			",memberId = '" + memberId + '\'' + 
			"}";
		}
}
