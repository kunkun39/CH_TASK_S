package com.cloud.work.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cloud.bug.model.BugRecord;
import com.cloud.bug.service.BugService;
import com.cloud.bug.service.BugStatService;
import com.cloud.bug.vo.BugSearchVo;
import com.cloud.platform.Constants;
import com.cloud.platform.StringUtil;
import com.cloud.project.model.Project;
import com.cloud.project.service.ProjectService;
import com.cloud.system.model.UserStatus;
import com.cloud.system.service.UserStatusService;

@Controller
@RequestMapping("work")
public class WorkBean {

	private static Logger logger = Logger.getLogger(WorkBean.class);
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BugService bugService;
	
	@Autowired
	private BugStatService bugStatService;
	
	@Autowired
	private UserStatusService statusService;
	
	public final int RECORD_SIZE = 20;
	
	/**
	 * show bugs by view when user select view
	 * 
	 * @param viewId
	 * @return
	 */
	@RequestMapping("/selectView.do")
	public ModelAndView selectView(HttpServletRequest request,
			@RequestParam("viewId") String viewId,
			@RequestParam(value = "col", required = false) String col) {
		
		bugService.resetDefaultView(viewId);
		
		return openWork(request, col, null);
	}
	
	/**
	 * show more system record
	 * 
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/moreRecord.do")
	public String showMoreRecord(@RequestParam("page") int page) {
		
		// get operate activities
		BugSearchVo searchVo = new BugSearchVo();
		searchVo.setPage(page);
		searchVo.setPageSize(RECORD_SIZE);
		
		List<BugRecord> records = bugService.searchBugOperateRecords(null, searchVo);
		
		// get has more info
		String hasMore = searchVo.getPage() < searchVo.getPageNum() ? Constants.VALID_YES
				: Constants.VALID_NO;
		
		// combine result
		JSONObject result = new JSONObject();
		result.put("hasMore", hasMore);
		result.put("records", records);
		
		return result.toString();
	}
	
	/**
	 * open user work panel
	 * 
	 * @return
	 */
	@RequestMapping("/openWork.do")
	public ModelAndView openWork(HttpServletRequest request,
			@RequestParam(value = "col", required = false) String col,
			@RequestParam(value = "readStatus", required = false) String readStatus) {
		
		/**
		 * init return page by work col set
		 */
		String returnPage = "";
		
		if(Constants.VALID_YES.equals(readStatus)) {
			col = statusService.getUserStatus().getWorkCol();
		}
		
		if("1".equals(col)) {
			returnPage = "-one";
		} else if("2".equals(col)) {
			returnPage = "-two";
		}
		
		ModelAndView mv = new ModelAndView("work/work" + returnPage);
		
		// save workpanel col status
		if(!Constants.VALID_YES.equals(readStatus)) {
			UserStatus status = statusService.getUserStatus();
			
			if(StringUtil.isNullOrEmpty(status.getId())) {
				status.setId(Constants.getID());
				status.setUserId(Constants.getLoginUserId());
			}
			
			status.setWorkCol(col);
			statusService.saveUserStatus(status);
		}
		
		try {
			String loginUser = Constants.getLoginUserId();
			
			BugSearchVo searchVo = new BugSearchVo();
			searchVo.setOwnerIds(loginUser);
			
			// get my projects
			List<Project> projects = projectService.searchProjects(searchVo, false);
			JSONArray pjtsArr = JSONArray.fromObject(projects);
			
			// get bug status statistic info
			JSONArray statusStat = bugStatService.statBugStatus(loginUser, true);
			
			// get bug belong project statistic info
			JSONArray bugPjtStat = bugStatService.statBugProject(loginUser);
			
			// get operate activities
			searchVo = new BugSearchVo();
			searchVo.setPage(1);
			searchVo.setPageSize(RECORD_SIZE);
			
			List<BugRecord> records = bugService.searchBugOperateRecords(null, searchVo);
			JSONArray recordsArr = JSONArray.fromObject(records);
			
			String hasMore = searchVo.getPage() < searchVo.getPageNum() ? Constants.VALID_YES
					: Constants.VALID_NO;
			
			// add objects
			mv.addObject("projects", pjtsArr);
			mv.addObject("statusStat", statusStat);
			mv.addObject("bugPjtStat", bugPjtStat);
			mv.addObject("records", recordsArr);
			mv.addObject("hasMore", hasMore);
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：openWork", e);
		}
		
		return mv;
	}
}
