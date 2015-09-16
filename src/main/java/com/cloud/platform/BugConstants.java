package com.cloud.platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloud.bug.util.BugPageUtil;
import com.cloud.project.model.Project;
import com.cloud.system.model.SystemConfig;

public class BugConstants {

	public static String ROOTPATH = System.getProperty("ppmbug");
	public static SystemConfig systemConfig;
	
	public static final String EMPTY = "EMPTY";
	
	/*
	 * ================== bug status ==================
	 */
	public static final int BUG_STATUS_START = 0;
	public static final int BUG_STATUS_INIT = 1;
	public static final int BUG_STATUS_AUDIT = 2;
	public static final int BUG_STATUS_SOLVE = 3;
	public static final int BUG_STATUS_TEST = 4;
	public static final int BUG_STATUS_HANGUP = 5;
	public static final int BUG_STATUS_CLOSE = 6;
	
	/*
	 * ================== name id map ==================
	 */
	public static Map<String, String> projectNameIdMap;
	public static Map<String, String> projectIdNameMap;
	
	/**
	 * get system config
	 * 
	 * @return
	 */
	public static SystemConfig getSystemConfig() {
		
		if(systemConfig == null) {
			IDao dao = (IDao) SpringUtil.getBean("dao");
			List<SystemConfig> list = dao.getAllByHql("from SystemConfig");
			
			systemConfig = list.get(0);
		}
		
		return systemConfig;
	}
	
	/**
	 * get operate name by operate string
	 * 
	 * @param op
	 * @return
	 */
	public static String getOperateName(String op) {
		String opName = "";
		
		if(BugPageUtil.OP_CREATE.equals(op)) {
			opName = "创建";
		}
		else if(BugPageUtil.OP_EDIT.equals(op)) {
			opName = "编辑";
		}
		else if(BugPageUtil.OP_COMMIT.equals(op)) {
			opName = "提交审核";
		}
		else if(BugPageUtil.OP_ASSIGN.equals(op)) {
			opName = "指定修改";
		}
		else if(BugPageUtil.OP_UNPASS.equals(op)) {
			opName = "审核不通过";
		}
		else if(BugPageUtil.OP_HANGUP.equals(op)) {
			opName = "挂起";
		}
		else if(BugPageUtil.OP_CLOSE.equals(op)) {
			opName = "直接关闭";
		}
		else if(BugPageUtil.OP_GOTEST.equals(op)) {
			opName = "提交测试";
		}
		else if(BugPageUtil.OP_SUCCESS.equals(op)) {
			opName = "测试通过";
		}
		else if(BugPageUtil.OP_FAILURE.equals(op)) {
			opName = "测试不通过";
		}
		else if(BugPageUtil.OP_CHGTESTOR.equals(op)) {
			opName = "更换责任人";
		}
		else if(BugPageUtil.OP_REOPEN.equals(op)) {
			opName = "重新开启";
		}
		
		return opName;
	}
	
	/**
	 * ========================= project operate =========================
	 */
	public static String getProjectNameById(String projectId) {
		
		if(projectNameIdMap == null) {
			projectNameIdMap = new HashMap();
			
			IDao dao = (IDao) SpringUtil.getBean("dao");
			List<Project> projects = dao.getAllByHql("from Project");
			
			for(Project p : projects) {
				projectNameIdMap.put(p.getId(), p.getName());
			}
		}
		
		if(!projectNameIdMap.containsKey(projectId)) {
			return "";
		}
		
		return projectNameIdMap.get(projectId);
	}
	
	public static String getProjectIdByName(String projectName) {
		
		if(projectIdNameMap == null) {
			projectIdNameMap = new HashMap();
			
			IDao dao = (IDao) SpringUtil.getBean("dao");
			List<Project> projects = dao.getAllByHql("from Project");
			
			for(Project p : projects) {
				projectIdNameMap.put(p.getName(), p.getId());
			}
		}
		
		if(!projectIdNameMap.containsKey(projectName)) {
			return "";
		}
		
		return projectIdNameMap.get(projectName);
	}
} 
