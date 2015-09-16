package com.cloud.bug.service;

import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.bug.util.BugUtil;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;
import com.cloud.platform.IDao;
import com.cloud.platform.StringUtil;

@Service
public class BugStatService {

	@Autowired
	private IDao dao;
	
	/**
	 * statistic projects' bug info
	 * 
	 * @param managerId
	 * @return
	 * @throws Exception
	 */
	public JSONArray statBugProject(String ownerId) throws Exception {
		
		JSONArray stat = new JSONArray();
		
		if(StringUtil.isNullOrEmpty(ownerId)) {
			return stat;
		}
		
		// search status and count info
		String hql = "select projectId,count(*) from Bug where ownerId = ?"
				+ " and projectId is not null and projectId != '' group by projectId";
		
		List<Object[]> info = dao.getAllByHql(hql, ownerId);
		
		// get stat total number
		long count = 0;
		
		for(Object[] o : info) {
			count += (Long) o[1];
		}
		
		// format list data
		JSONArray arr = null;
		float percent = 0;
		
		for(Object[] o : info) {
			percent = (Float.parseFloat(o[1].toString())) / count;
			
			arr = new JSONArray();
			arr.add(BugConstants.getProjectNameById((String) o[0]));
			arr.add(percent * 100);
			
			stat.add(arr);
		}
		
		return stat;
	}
	
	/**
	 * statistic person solve bug info
	 * 
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public JSONArray statPersonSolve(String projectId) throws Exception {
		
		JSONArray stat = new JSONArray();
		
		if(StringUtil.isNullOrEmpty(projectId)) {
			return stat;
		}
		
		// search status and count info
		String hql = "select modifierId,count(*) from Bug where projectId = ?"
				+ " and modifierId is not null and modifierId != '' group by modifierId";
		
		List<Object[]> info = dao.getAllByHql(hql, projectId);
		
		// get stat total number
		long count = 0;
		
		for(Object[] o : info) {
			count += (Long) o[1];
		}
		
		// format list data
		JSONArray arr = null;
		float percent = 0;
		
		for(Object[] o : info) {
			percent = (Float.parseFloat(o[1].toString())) / count;
			
			arr = new JSONArray();
			arr.add(Constants.getUsernameById((String) o[0]));
			arr.add(percent * 100);
			
			stat.add(arr);
		}
		
		return stat;
	}
	
	/**
	 * statistic person recive bug info
	 * 
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public JSONArray statPersonRecive(String projectId) throws Exception {
		
		JSONArray stat = new JSONArray();
		
		if(StringUtil.isNullOrEmpty(projectId)) {
			return stat;
		}
		
		// search status and count info
		String hql = "select ownerId,count(*) from Bug where projectId = ?"
				+ " and ownerId is not null and ownerId != '' group by ownerId";
		
		List<Object[]> info = dao.getAllByHql(hql, projectId);
		
		// get stat total number
		long count = 0;
		
		for(Object[] o : info) {
			count += (Long) o[1];
		}
		
		// format list data
		JSONArray arr = null;
		float percent = 0;
		
		for(Object[] o : info) {
			percent = (Float.parseFloat(o[1].toString())) / count;
			
			arr = new JSONArray();
			arr.add(Constants.getUsernameById((String) o[0]));
			arr.add(percent * 100);
			
			stat.add(arr);
		}
		
		return stat;
	}
	
	/**
	 * statistic bug status info
	 * 
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public JSONArray statBugStatus(String objectId, boolean isUser) throws Exception {
		
		JSONArray stat = new JSONArray();
		
		if(StringUtil.isNullOrEmpty(objectId)) {
			return stat;
		}
		
		// search status and count info
		String hql = null;
		
		if(isUser) {
			hql = "select status,count(*) from Bug where ownerId = ? group by status order by status";
		} else {
			hql = "select status,count(*) from Bug where projectId = ? group by status order by status";
		}
		
		List<Object[]> info = dao.getAllByHql(hql, objectId);
		
		// get stat total number
		long count = 0;
		
		for(Object[] o : info) {
			count += (Long) o[1];
		}
		
		// format list data
		JSONArray arr = null;
		float percent = 0;
		
		for(Object[] o : info) {
			percent = (Float.parseFloat(o[1].toString())) / count;
			
			arr = new JSONArray();
			arr.add(BugUtil.getStatusName((Integer) o[0]));
			arr.add(percent * 100);
			
			stat.add(arr);
		}
		
		return stat;
	}
}
