package com.wifin.kachingme.pojo;

public class StoreDto {

	String product;
	String url;
	Integer image;
	String deel;

	public String getDeel() {
		return deel;
	}

	public void setDeel(String deel) {
		this.deel = deel;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getImage() {
		return image;
	}

	public void setImage(Integer image) {
		this.image = image;
	}

}
