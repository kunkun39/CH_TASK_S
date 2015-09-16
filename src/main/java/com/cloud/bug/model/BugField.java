package com.cloud.bug.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_bug_field")
public class BugField {

	private String id;
	private int type;
	private String name;
	private String label;
	private String htmlType;
	
	private String typeName;
	private String htmlTypeName;
	
	@Id
	@Column(unique = true, nullable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(length = 1)
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	@Column(length = 255)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(length = 255)
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Column(length = 20)
	public String getHtmlType() {
		return htmlType;
	}
	
	public void setHtmlType(String htmlType) {
		this.htmlType = htmlType;
	}
	
	@Transient
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Transient
	public String getHtmlTypeName() {
		return htmlTypeName;
	}

	public void setHtmlTypeName(String htmlTypeName) {
		this.htmlTypeName = htmlTypeName;
	}
}
