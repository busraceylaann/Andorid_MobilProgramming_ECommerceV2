package com.example.sananelazimv2.Model;

public class ResimDuzenleSliderModel{
	private String image;
	private String imageId;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setImageId(String imageId){
		this.imageId = imageId;
	}

	public String getImageId(){
		return imageId;
	}

	@Override
 	public String toString(){
		return 
			"ResimDuzenleSliderModel{" + 
			"image = '" + image + '\'' + 
			",imageId = '" + imageId + '\'' + 
			"}";
		}
}
