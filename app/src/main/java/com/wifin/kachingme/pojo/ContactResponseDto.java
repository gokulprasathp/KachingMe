package com.wifin.kachingme.pojo;

import java.util.List;

/**
 * @author sivanesan
 */
public class ContactResponseDto {

	private List<Integer> uIdList;

	private List<ContactSyncDto> contactListDtos;

	public List<Integer> getuIdList() {
		return uIdList;
	}

	public void setuIdList(List<Integer> uIdList) {
		this.uIdList = uIdList;
	}

	public List<ContactSyncDto> getContactListDtos() {
		return contactListDtos;
	}

	public void setContactListDtos(List<ContactSyncDto> contactListDtos) {
		this.contactListDtos = contactListDtos;
	}

}
