package com.cloud.bug.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cloud.platform.Constants;

@Entity
@Table(name = "t_bug_record")
public class BugRecord {

	private String id;
	private String bugId;
	private String operate;
	private String note;
	private int fromStatus;
	private int toStatus;
	private String creatorId;
	private Date createTime;
	
	// Transient
	private String creator;
	private String bugName;
	private String opName;
	
	public BugRecord() {
		this.id = Constants.getID();
	}
	
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
	
	@Column(length = 36)
	public String getOperate() {
		return operate;
	}
	
	public void setOperate(String operate) {
		this.operate = operate;
	}
	
	@Column(length = 2048)
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(length = 1)
	public int getFromStatus() {
		return fromStatus;
	}

	public void setFromStatus(int fromStatus) {
		this.fromStatus = fromStatus;
	}
	
	@Column(length = 1)
	public int getToStatus() {
		return toStatus;
	}

	public void setToStatus(int toStatus) {
		this.toStatus = toStatus;
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
	
	@Transient
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	@Transient
	public String getBugName() {
		return bugName;
	}

	public void setBugName(String bugName) {
		this.bugName = bugName;
	}
	
	@Transient
	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}
}
