package com.cloud.bug.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_bug_bug")
public class Bug {

	private String id;
	
	private String code;
	private String ownerId;
	private int status;
	
	public String name;
	public String projectId;
	public String auditorId;
	public String intro;
	public String reappear;
	public String modifierId;
	public String testorId;
	public String solveInfo;
	public String relateTest;
	
	public String level;
	public String priority;
	
	public String creatorId;
	public Date createTime;
	public Date modifyTime;
	
	// Transient
	private String owner;
	private String creator;
	private String statusName;
	private String projectName;
	private String levelName;
	private String priorityName;

	// note for operate record
	public String note;

    public int requireDays;

	@Id
	@Column(unique = true, nullable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(length = 255)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(length = 36)
	public String getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	@Column(length = 5)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(length = 36)
	public String getProjectId() {
		return projectId;
	}
	
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	@Column(length = 36)
	public String getAuditorId() {
		return auditorId;
	}
	
	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}
	
	@Column(length = 2048)
	public String getIntro() {
		return intro;
	}
	
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	@Column(length = 2048)
	public String getReappear() {
		return reappear;
	}
	
	public void setReappear(String reappear) {
		this.reappear = reappear;
	}
	
	@Column(length = 36)
	public String getModifierId() {
		return modifierId;
	}

	public void setModifierId(String modifierId) {
		this.modifierId = modifierId;
	}
	
	@Column(length = 36)
	public String getTestorId() {
		return testorId;
	}

	public void setTestorId(String testorId) {
		this.testorId = testorId;
	}

	@Column(length = 2048)
	public String getSolveInfo() {
		return solveInfo;
	}

	public void setSolveInfo(String solveInfo) {
		this.solveInfo = solveInfo;
	}

	@Column(length = 2048)
	public String getRelateTest() {
		return relateTest;
	}

	public void setRelateTest(String relateTest) {
		this.relateTest = relateTest;
	}
	
	@Column(length = 1)
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(length = 1)
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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

	@Column
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@Transient
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

    @Transient
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Transient
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	@Transient
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Transient
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	@Transient
	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	
	@Transient
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

    @Column
    public int getRequireDays() {
        return requireDays;
    }

    public void setRequireDays(int requireDays) {
        this.requireDays = requireDays;
    }
}
