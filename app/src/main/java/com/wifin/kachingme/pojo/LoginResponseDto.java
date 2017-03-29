package com.wifin.kachingme.pojo;

import java.util.ArrayList;
import java.util.List;

public class LoginResponseDto {

	private List<FreebieMainDto> stzzleCampgnDetailsRestDtos = new ArrayList<FreebieMainDto>();
	private RestUserDetailsDto restUserDetailsDto;

	public List<FreebieMainDto> getStzzleCampgnDetailsRestDtos() {
		return stzzleCampgnDetailsRestDtos;
	}

	public void setStzzleCampgnDetailsRestDtos(
			List<FreebieMainDto> stzzleCampgnDetailsRestDtos) {
		this.stzzleCampgnDetailsRestDtos = stzzleCampgnDetailsRestDtos;
	}

	public RestUserDetailsDto getRestUserDetailsDto() {
		return restUserDetailsDto;
	}

	public void setRestUserDetailsDto(RestUserDetailsDto restUserDetailsDto) {
		this.restUserDetailsDto = restUserDetailsDto;
	}

}
