package com.ait.app.requestBody;

import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;

@Data
public class UserDto {

	private String name;
	private String email;
	private String mobno;
	private String roles;
	private List<AddressDto> addresses;
	@CreationTimestamp
	private String createdDt;

	public UserDto() {
		super();

	}

	public UserDto(String name, String email, String roles, String mobno, List<AddressDto> addresses,
			String createdDt) {
		super();

		this.name = name;
		this.email = email;
		this.roles = roles;
		this.mobno = mobno;
		this.addresses = addresses;
		this.createdDt = createdDt;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobno() {
		return mobno;
	}

	public void setMobno(String mobno) {
		this.mobno = mobno;
	}

	public List<AddressDto> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDto> addresses) {
		this.addresses = addresses;
	}

	public String getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	

}
