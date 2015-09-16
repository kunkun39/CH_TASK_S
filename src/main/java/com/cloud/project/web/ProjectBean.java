package com.cloud.project.web;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cloud.bug.service.BugStatService;
import com.cloud.bug.vo.BugSearchVo;
import com.cloud.platform.Constants;
import com.cloud.project.model.Project;
import com.cloud.project.service.ProjectService;

@Controller
@RequestMapping("project")
public class ProjectBean {
	
	private static Logger logger = Logger.getLogger(ProjectBean.class);

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BugStatService bugStatService;
	
	/**
	 * open project
	 * 
	 * @param projectId
	 */
	@ResponseBody
	@RequestMapping("/reopenProject.do")
	public void reopenProject(@RequestParam("projectId") String projectId) {
		
		Project project = projectService.getProject(projectId);
		project.setIsClose(Constants.VALID_NO);
		projectService.saveProject(project);
	}
	
	/**
	 * close project
	 * 
	 * @param projectId
	 */
	@ResponseBody
	@RequestMapping("/closeProject.do")
	public void closeProject(@RequestParam("projectId") String projectId) {
		
		projectService.closeProject(projectId);
	}
	
	/**
	 * add project info before create or edit project
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/createOrEdit.do")
	public ModelAndView createOrEdit(
			@RequestParam(value = "projectId", required = false) String projectId) {
		
		ModelAndView mv = new ModelAndView("project/projectAdd");
		
		Project project = projectService.getProject(projectId);
		mv.addObject("project", project);
		
		return mv;
	}
	
	/**
	 * open project
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/openProject.do")
	public ModelAndView openProject(@RequestParam("projectId") String projectId) {
		
		ModelAndView mv = new ModelAndView("project/projectLook");
		
		try {
			// get bug info
			Project project = projectService.getProject(projectId);
			
			// get bug status statistic info
			JSONArray statusStat = bugStatService.statBugStatus(projectId, false);
			
			// get person recive bug statistic info
			JSONArray personReciveStat = bugStatService.statPersonRecive(projectId);
			
			// get person solve bug statistic info
			JSONArray personSolveStat = bugStatService.statPersonSolve(projectId);
			
			// if login user is project manager
			String canEdit = Constants.getLoginUserId().equals(
					project.getManagerId()) ? Constants.VALID_YES : Constants.VALID_NO;
			
			// init model and view
			mv.addObject("project", project);
			mv.addObject("statusStat", statusStat);
			mv.addObject("personReciveStat", personReciveStat);
			mv.addObject("personSolveStat", personSolveStat);
			mv.addObject("canEdit", canEdit);
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：openProject", e);
		}
		
		return mv;
	}
	
	/**
	 * get projects
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getProjects.do")
	public String getProjects(BugSearchVo searchVo,
			@RequestParam(value = "showClose", required = false) String showClose) {
		
		// get projects
		List<Project> projects = projectService.searchProjects(searchVo, Constants.VALID_YES.equals(showClose));
		
		// convert to json format
		JSONObject info = new JSONObject();
		info.put("page", searchVo);
		info.put("projects", projects);
		
		return info.toString();
	}
	
	/**
	 * save project 
	 * 
	 * @param project
	 * @return
	 */
	@RequestMapping("/saveProject.do")
	public String saveProject(Project project) {
		
		try {
			projectService.saveProject(project);
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：saveProject", e);
		}
		
		return "project/project";
	}
}
