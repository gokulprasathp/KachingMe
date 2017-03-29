package com.wifin.kachingme.pojo;

public class RestClaimDto {

	private String phoneNumber;
	private Integer offerId;
	private Integer type;
	private Long bux;
	private String remarks;

	private Integer shopId;
	private String countryName;
	private String name;
	private String address;
	private String email;
	private Double lat;
	private Double lon;
	private String shopPhoneNumber;

	public String getShopPhoneNumber() {
		return shopPhoneNumber;
	}

	public void setShopPhoneNumber(String shopPhoneNumber) {
		this.shopPhoneNumber = shopPhoneNumber;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getOfferId() {
		return offerId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getBux() {
		return bux;
	}

	public void setBux(Long bux) {
		this.bux = bux;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

}
