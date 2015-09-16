package com.cloud.bug.web;

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

import com.cloud.attach.Attach;
import com.cloud.attach.AttachService;
import com.cloud.bug.model.Bug;
import com.cloud.bug.model.BugRecord;
import com.cloud.bug.model.BugView;
import com.cloud.bug.service.BugService;
import com.cloud.bug.util.BugPageUtil;
import com.cloud.bug.vo.BugSearchVo;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;

@Controller
@RequestMapping("bug")
public class BugBean {
	
	private static Logger logger = Logger.getLogger(BugBean.class);

	@Autowired
	private BugService bugService;
	
	@Autowired
	private AttachService attachService;
	
	/**
	 * remove bug view
	 * 
	 * @param viewId
	 * @return
	 */
	@RequestMapping("/removeView.do")
	public String removeView(@RequestParam("viewId") String viewId) {
		
		bugService.removeView(viewId);
		
		return "bug/bug";
	}
	
	/**
	 * add bug view info before create or edit bug view
	 * 
	 * @param viewId
	 * @return
	 */
	@RequestMapping("/createOrEditView.do")
	public ModelAndView createOrEditView(
			@RequestParam(value = "viewId", required = false) String viewId) {
		
		ModelAndView mv = new ModelAndView("bug/bugViewAdd");
		
		BugView view = bugService.getBugViewById(viewId);
		
		// set default page size
		mv.addObject("view", view);
		
		return mv;
	}
	
	/**
	 * show bugs by view when user select view
	 * 
	 * @param viewId
	 * @return
	 */
	@RequestMapping("/selectView.do")
	public String selectView(@RequestParam("viewId") String viewId) {
		
		bugService.resetDefaultView(viewId);
		
		return "bug/bug";
	}
	
	/**
	 * get bug views
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getViews.do")
	public String getViews() {
		
		List<BugView> views = bugService.searchViews();
		
		JSONArray viewsArr = JSONArray.fromObject(views);
		
		return viewsArr.toString();
	}
	
	/**
	 * save bug view
	 * 
	 * @param view
	 * @return
	 */
	@RequestMapping("/saveView.do")
	public String saveView(BugView view) {
		
		bugService.saveView(view);
		
		return "bug/bug";
	}
	
	/**
	 * open operate page
	 * 
	 * @param operate
	 * @return
	 */
	@RequestMapping("/openOperate.do")
	public ModelAndView openOperate(@RequestParam("op") String operate,
			@RequestParam(value = "bugId", required = false) String bugId,
			@RequestParam(value = "workspace", required = false) String workspace) {
		
		ModelAndView mv = new ModelAndView("bug/bugOperate");
		
		try {
			// get bug info
			Bug b = bugService.getBug(bugId);
			
			// get operate page button html
			String btnHtml = BugPageUtil.combineBtnHtml(operate, b, workspace);
			
			// get operate page field html
			String fieldHtml = BugPageUtil.combineFieldHtml(operate, b);
			
			// get bug attachments
			List<Attach> attachs = attachService.searchEntityAttachs(b.getId());
			
			mv.addObject("btnHtml", btnHtml);
			mv.addObject("fieldHtml", fieldHtml);
			mv.addObject("attachs", attachs);
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：openOperate", e);
		}
		
		return mv;
	}
	
	/**
	 * open bug
	 * 
	 * @param bugId
	 * @return
	 */
	@RequestMapping("/openBug.do")
	public ModelAndView openBug(@RequestParam("bugId") String bugId) {
		
		ModelAndView mv = null;
		
		try {
			// get bug info
			Bug bug = bugService.getBug(bugId);
			Object[] pageInfo = getReturnPage(bug.getStatus());
			
			// get view page field html
			String fieldHtml = BugPageUtil.combineViewFieldHtml((Integer) pageInfo[1], bug);
			
			// get bug attachments
			List<Attach> attachs = attachService.searchEntityAttachs(bug.getId());
			
			// get bug operate records
			List<BugRecord> records = bugService.searchBugOperateRecords(bug.getId(), null);
			
			// get bug share status
			boolean shareStatus = bugService.getBugShareStatus(bugId);
			
			// init model and view
			mv = new ModelAndView((String) pageInfo[0]);  
			mv.addObject("bug", bug);
			mv.addObject("fieldHtml", fieldHtml);
			mv.addObject("attachs", attachs);
			mv.addObject("records", records);
			mv.addObject("shareStatus", shareStatus ? Constants.VALID_YES : Constants.VALID_NO);
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：openBug", e);
		}
		
		return mv;
	}
	
	/**
	 * get return page by bug status
	 * 
	 * @param status
	 * @param isEdit
	 * @return
	 */
	private Object[] getReturnPage(int status) {
		
		String view = null;
		int pageFlag = BugPageUtil.PAGE_VIEW_INIT;
		
		switch(status) {
		case BugConstants.BUG_STATUS_INIT:
			view = "viewInit";
			pageFlag = BugPageUtil.PAGE_VIEW_INIT;
			break;
		case BugConstants.BUG_STATUS_AUDIT:
			view = "viewAudit";
			pageFlag = BugPageUtil.PAGE_VIEW_AUDIT;
			break;
		case BugConstants.BUG_STATUS_SOLVE:
			view = "viewSolve";
			pageFlag = BugPageUtil.PAGE_VIEW_SOLVE;
			break;
		case BugConstants.BUG_STATUS_HANGUP:
			view = "viewHangup";
			pageFlag = BugPageUtil.PAGE_VIEW_HANGUP;
			break;
		case BugConstants.BUG_STATUS_CLOSE:
			view = "viewClose";
			pageFlag = BugPageUtil.PAGE_VIEW_CLOSE;
			break;
		case BugConstants.BUG_STATUS_TEST:
			view = "viewTest";
			pageFlag = BugPageUtil.PAGE_VIEW_TEST;
			break;
		}
		
		return new Object[] {"bug/" + view, pageFlag};
	}
	
	/**
	 * get bugs
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getBugs.do")
	public String getBugs(BugSearchVo searchVo) {
		
		// get bugs
		List<Bug> bugs = bugService.searchBugs(searchVo);
		
		// convert to json format
		JSONObject info = new JSONObject();
		info.put("page", searchVo);
		info.put("bugs", bugs);
		
		return info.toString();
	}
	
	/**
	 * create bug
	 * 
	 * @param bug
	 * @return
	 */
	@RequestMapping("/saveBug.do")
	public String saveBug(Bug bug, @RequestParam("fromStatus") int fromStatus,
			@RequestParam("status") int toStatus,
			@RequestParam("operate") String operate,
			@RequestParam("attachIds") String attachIds,
			@RequestParam("workspace") String workspace) {
		
		try {
			// save bug info
			String bugId = bugService.saveBug(bug, fromStatus, toStatus, operate);
			
			// save bug attach
			attachService.updateEntityAttach(bugId, attachIds);
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：saveBug", e);
		}
		
		return Constants.VALID_YES.equals(workspace) ? "forward:/work/openWork.do?readStatus=Y" : "bug/bug";
	}
	
	/**
	 * remove bug
	 * 
	 * @param bugId
	 * @return
	 */
	@RequestMapping("/removeBug.do")
	public String removeBug(@RequestParam("bugId") String bugId) {
		
		bugService.removeBug(bugId);
		
		return "bug/bug";
	}
}
