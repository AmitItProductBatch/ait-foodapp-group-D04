package com.ait.app.response;

public class UserProfileResponse {
	private int id;
//	@CreationTimestamp
//	private LocalDateTime createdDt;
	private String email;
	private String mobno;
	private String name;
	private String role;

	public UserProfileResponse() {
	}

	public UserProfileResponse(int id, String email, String mobno, String name, String role) {
		super();
		this.id = id;
		this.email = email;
		this.mobno = mobno;
		this.name = name;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
