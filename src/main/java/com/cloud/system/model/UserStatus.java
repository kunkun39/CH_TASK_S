package com.cloud.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_status")
public class UserStatus {

	private String id;
	private String userId;
	private String workCol;
	
	@Id
	@Column(unique = true, nullable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(length = 36)
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(length = 1)
	public String getWorkCol() {
		return workCol;
	}
	
	public void setWorkCol(String workCol) {
		this.workCol = workCol;
	}
}
