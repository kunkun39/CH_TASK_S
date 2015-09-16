package com.cloud.bug.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_bug_view")
public class BugView {

	private String id;
	
	private String name;
	private String isDefault;
	
	private int pageSize = 15;
	
	private String sortField;
	private String sortTo;
	
	private String bugName;
	private String projectIds;
	private String status;
	private String ownerIds;
	private String levels;
	private String priorities;
	
	private String creatorId;
	private Date createTime;
	
	@Id
	@Column(unique = true, nullable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(length = 255)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(length = 1)
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	@Column(length = 5)
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	@Column(length = 36)
	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	@Column(length = 36)
	public String getSortTo() {
		return sortTo;
	}

	public void setSortTo(String sortTo) {
		this.sortTo = sortTo;
	}
	
	@Column(length = 255)
	public String getBugName() {
		return bugName;
	}

	public void setBugName(String bugName) {
		this.bugName = bugName;
	}
	
	@Column(length = 2048)
	public String getProjectIds() {
		return projectIds;
	}
	
	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds;
	}
	
	@Column(length = 2048)
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(length = 2048)
	public String getOwnerIds() {
		return ownerIds;
	}
	
	public void setOwnerIds(String ownerIds) {
		this.ownerIds = ownerIds;
	}
	
	@Column(length = 36)
	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	@Column(length = 36)
	public String getPriorities() {
		return priorities;
	}

	public void setPriorities(String priorities) {
		this.priorities = priorities;
	}
	
	@Column(length = 36)
	public String getCreatorId() {
		return creatorId;
	}
	
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	@Column
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
