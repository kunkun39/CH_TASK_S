package com.cloud.bug.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_bug_pagefield")
public class BugPageField {

	private String id;
	private int pageFlag;
	private String fieldId;
	private String isRequire;
	private int sortSn;
	
	// field properties
	private BugField field;
	
	@Id
	@Column(unique = true, nullable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(length = 5)
	public int getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(int pageFlag) {
		this.pageFlag = pageFlag;
	}
	
	@Column(length = 36)
	public String getFieldId() {
		return fieldId;
	}
	
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	
	@Column(length = 1)
	public String getIsRequire() {
		return isRequire;
	}
	
	public void setIsRequire(String isRequire) {
		this.isRequire = isRequire;
	}
	
	@Column(length = 5)
	public int getSortSn() {
		return sortSn;
	}

	public void setSortSn(int sortSn) {
		this.sortSn = sortSn;
	}
	
	@Transient
	public BugField getField() {
		return field;
	}

	public void setField(BugField field) {
		this.field = field;
	}
}
