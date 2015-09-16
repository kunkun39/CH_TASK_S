package com.cloud.project.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.bug.vo.BugSearchVo;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;
import com.cloud.platform.HqlUtil;
import com.cloud.platform.IDao;
import com.cloud.platform.StringUtil;
import com.cloud.project.model.Project;

@Service
public class ProjectService {

	@Autowired
	private IDao dao;
	
	/**
	 * get closed projects
	 * 
	 * @return
	 */
	public List<Project> searchCloseProjects() {
		
		return dao.getAllByHql("from Project where isClose = ? order by name", Constants.VALID_YES);
	}
	
	/**
	 * close project's bugs
	 * 
	 * @param projectId
	 */
	public void closeProject(String projectId) {
		
		// update project close statuss
		Project project = getProject(projectId);
		project.setIsClose(Constants.VALID_YES);
		dao.saveObject(project);
		
		// close project's bugs
		String hql = "update Bug set status = ? where projectId = ?";
		dao.updateByHql(hql, new Object[] {BugConstants.BUG_STATUS_CLOSE, projectId});
	}
	
	/**
	 * get bug by id
	 * 
	 * @param bugId
	 * @return
	 */
	public Project getProject(String projectId) {
		
		if(StringUtil.isNullOrEmpty(projectId)) {
			return new Project();
		}
		
		Project project = (Project) dao.getObject(Project.class, projectId);
		project.setManager(Constants.getUsernameById(project.getManagerId()));
		
		return project;
	}
	
	/**
	 * search type users
	 * 
	 * @param type
	 * @return
	 */
	public List<Project> searchProjects(BugSearchVo searchVo, boolean showClose) {
		
		// search projects
		StringBuffer hql = new StringBuffer();
		hql.append("select p,u.username from Project p,User u where p.managerId = u.id");
		
		if(!showClose) {
			hql.append(" and (isClose is null or isClose = 'N')");
		}
		
		if(searchVo != null) {
			// combine name filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getName())) {
				hql.append(" and p.name like '%" + searchVo.getName() + "%'");
			}
			
			// combine owner filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getOwnerIds())) {
				hql.append(" and " + HqlUtil.combineInHql("p.managerId", searchVo.getOwnerIds(), true, true));
			}
		}
		
		hql.append(" order by p.createTime desc");
		
		List<Object[]> list = dao.getPageByHql(hql.toString(), searchVo);
		
		// combine manager name
		Project p = null;
		List<Project> projects = new ArrayList();
		
		for(Object[] o : list) {
			p = (Project) o[0];
			p.setManager((String) o[1]);
			
			projects.add(p);
		}
		
		return projects;
	}
	
	/**
	 * save project
	 * 
	 * @param project
	 */
	public void saveProject(Project project) {
		
		if(StringUtil.isNullOrEmpty(project.getId())) {
			project.setId(Constants.getID());
			project.setCreateTime(new Date());
			
		} else {
			Project p = getProject(project.getId());
			project.setCreateTime(p.getCreateTime());
		}
		
		project.setIntro(project.getIntro().replaceAll("\r\n", "<br>"));
		
		dao.saveObject(project);
		
		// reset project map
		BugConstants.projectNameIdMap = null;
	}
}
