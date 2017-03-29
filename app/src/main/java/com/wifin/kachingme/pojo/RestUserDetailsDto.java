package com.wifin.kachingme.pojo;

import java.util.ArrayList;
import java.util.List;

public class RestUserDetailsDto {

	private Long userId;
	private Long bux;
	List<CartDetailsDto> cartDetailsDtos = new ArrayList<CartDetailsDto>();
	Boolean freeBeeFlag;

	public Boolean getFreeBeeFlag() {
		return freeBeeFlag;
	}

	public void setFreeBeeFlag(Boolean freeBeeFlag) {
		this.freeBeeFlag = freeBeeFlag;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBux() {
		return bux;
	}

	public void setBux(Long bux) {
		this.bux = bux;
	}

	public List<CartDetailsDto> getCartDetailsDtos() {
		return cartDetailsDtos;
	}

	public void setCartDetailsDtos(List<CartDetailsDto> cartDetailsDtos) {
		this.cartDetailsDtos = cartDetailsDtos;
	}

}
