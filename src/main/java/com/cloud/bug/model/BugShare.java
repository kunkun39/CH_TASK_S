package com.cloud.bug.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_bug_share")
public class BugShare {

	private String id;
	private String bugId;
	private Date createTime;
	
	@Id
	@Column(unique = true, nullable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(length = 36)
	public String getBugId() {
		return bugId;
	}
	
	public void setBugId(String bugId) {
		this.bugId = bugId;
	}
	
	@Column
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
