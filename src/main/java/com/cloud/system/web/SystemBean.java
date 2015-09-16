package com.cloud.system.web;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cloud.bug.model.BugField;
import com.cloud.platform.BugConstants;
import com.cloud.platform.SpringUtil;
import com.cloud.project.model.Project;
import com.cloud.project.service.ProjectService;
import com.cloud.system.model.SystemConfig;
import com.cloud.system.service.SystemService;

@Controller
@RequestMapping("system")
public class SystemBean {

	@Autowired
	private SystemService systemService;
	
	/**
	 * init mail config before page
	 * 
	 * @return
	 */
	@RequestMapping("/openMailConfig.do")
	public ModelAndView openMailConfig() {
		
		ModelAndView mv = new ModelAndView("system/mail");
		
		SystemConfig systemConfig = BugConstants.getSystemConfig();
		mv.addObject("config", systemConfig.getMailConfig());
		
		return mv;
	}
	
	/**
	 * save mail config
	 * 
	 * @param mailConfig
	 */
	@ResponseBody
	@RequestMapping("/saveMailConfig.do")
	public void saveMailConfig(@RequestParam("mailConfig") String mailConfig) {
		
		SystemConfig systemConfig = BugConstants.getSystemConfig();
		
		systemConfig.setMailConfig(mailConfig);
		
		systemService.resetSystemConfig(systemConfig);
	}
	
	/**
	 * clean system data
	 * 
	 * @param projectIds
	 */
	@ResponseBody
	@RequestMapping("/cleanData.do")
	public void cleanData(@RequestParam("projectIds") String projectIds) {
		
		systemService.cleanData(projectIds);
	}
	
	/**
	 * get closed project for open clean page
	 * 
	 * @return
	 */
	@RequestMapping("/openClean.do")
	public ModelAndView openClean() {
		
		ProjectService projectService = (ProjectService) SpringUtil.getBean("projectService");
		List<Project> projects = projectService.searchCloseProjects();
		
		ModelAndView mv = new ModelAndView("system/clean");
		mv.addObject("projects", projects);
		
		return mv;
	}
	
	/**
	 * remove page field
	 * 
	 * @param pageFieldId
	 */
	@ResponseBody
	@RequestMapping("/removePageField.do")
	public void removePageField(@RequestParam("pageFieldId") String pageFieldId) {
		
		systemService.removePageField(pageFieldId);
	}
	
	/**
	 * get specified page's fields
	 * 
	 * @param pageFlag
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPageFields.do")
	public String getPageFields(@RequestParam("pageFlag") int pageFlag) {
		
		List<Object[]> pageFields = systemService.getPageFieldInfos(pageFlag);
		
		JSONArray result = JSONArray.fromObject(pageFields);
		return result.toString();
	}
	
	/**
	 * save specified page field
	 * 
	 * @param feildId
	 * @param sortSn
	 * @param isRequire
	 */
	@ResponseBody
	@RequestMapping("/savePageField.do")
	public void savePageField(@RequestParam("pageFlag") int pageFlag,
			@RequestParam("fieldId") String fieldId,
			@RequestParam("sortSn") int sortSn,
			@RequestParam("isRequire") String isRequire) {
		
		systemService.savePageField(pageFlag, fieldId, sortSn, isRequire);
	}
	
	/**
	 * get fields
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSelectFields.do")
	public String getSelectFields() {
		
		List<BugField> fields = systemService.getFields();
		JSONArray selectFields = new JSONArray();
		
		for(BugField field : fields) {
			JSONObject selectField = new JSONObject();
			selectField.put("i", field.getId());
			selectField.put("v", field.getLabel());
			
			selectFields.add(selectField);
		}
		
		return selectFields.toString();
	}
	
	/**
	 * open form page
	 * 
	 * @return
	 */
	@RequestMapping("/openPage.do")
	public ModelAndView openPage(@RequestParam("pageFlag") int pageFlag) {
		
		ModelAndView mv = new ModelAndView("system/formPage");
		
		return mv;
	}
	
	/**
	 * open field page
	 * 
	 * @return
	 */
	@RequestMapping("/openField.do")
	public ModelAndView openField() {
		
		ModelAndView mv = new ModelAndView("system/field");
		
		List<BugField> fields = systemService.getFields();
		mv.addObject("fields", fields);
		
		return mv;
	}
	
	/**
	 * get system name
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSystemName.do")
	public String getSystemName() {
		
		return BugConstants.getSystemConfig().getSystemName();
	}
	
	/**
	 * modify system name
	 * 
	 * @param systemName
	 */
	@ResponseBody
	@RequestMapping("/modifySystemName.do")
	public void modifySystemName(@RequestParam("systemName") String systemName) {
		
		SystemConfig systemConfig = BugConstants.getSystemConfig();
		
		systemConfig.setSystemName(systemName);
		
		systemService.resetSystemConfig(systemConfig);
	}
}
