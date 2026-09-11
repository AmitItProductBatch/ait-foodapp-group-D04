package com.ait.app.requestBody;

import lombok.Data;

@Data
public class UserDto {

	
	private String name;
	private String email;
	private String role;
	private String mobno;
	private String createdDt;

	public UserDto() {
		super();

	}

	public UserDto( String name, String email, String role, String mobno, String createdDt) {
		super();
		
		this.name = name;
		this.email = email;
		this.role = role;
		this.mobno = mobno;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMobno() {
		return mobno;
	}

	public void setMobno(String mobno) {
		this.mobno = mobno;
	}

	public String getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}

}
