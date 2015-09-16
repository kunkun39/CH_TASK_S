package com.cloud.bug.vo;

import com.cloud.bug.util.DateUtils;
import com.cloud.platform.DateUtil;
import com.cloud.platform.SearchVo;
import org.springframework.util.StringUtils;


public class BugSearchVo extends SearchVo {

	/**
	 * sort condition
	 */
	private String sort;
	
	/**
	 * filter condition
	 */
	private String code;
	private String name;
	private String projectIds;
	private String status;
	private String ownerIds;
	private String levels;
	private String priorities;
	
	/**
	 * if from workspace
	 */
	private String workspace;

    /**
     * time limitation
     */
    private int year = -1;
    private int month = -1;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getProjectIds() {
		return projectIds;
	}
	
	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getOwnerIds() {
		return ownerIds;
	}
	
	public void setOwnerIds(String ownerIds) {
		this.ownerIds = ownerIds;
	}
	
	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	public String getPriorities() {
		return priorities;
	}

	public void setPriorities(String priorities) {
		this.priorities = priorities;
	}
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean hasTimeLimit() {
        if (year > 0 && month > 0) {
            return true;
        }
        return false;
    }

    public String getFromTime() {
        return DateUtils.getMonthStartTime(year, month);
    }

    public String getEndTime() {
        return DateUtils.getMonthEndTime(year, month);
    }
}
