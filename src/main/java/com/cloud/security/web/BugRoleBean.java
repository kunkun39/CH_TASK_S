package com.cloud.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.bug.model.Bug;
import com.cloud.bug.service.BugService;
import com.cloud.platform.Constants;
import com.cloud.platform.SpringUtil;
import com.cloud.security.util.BugRoleResUtil;

@Controller
@RequestMapping("role")
public class BugRoleBean extends RoleBean {

	/**
	 * has bug all operate authority
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/hasBugAllOpAuth.do")
	public String hasBugAllOpAuth(@RequestParam("bugId") String bugId) {
		
		// check if is owner
		boolean isOwner = false;
		
		BugService bugService = (BugService) SpringUtil.getBean("bugService");
		Bug bug = bugService.getBug(bugId);
		
		if(Constants.getLoginUserId().equals(bug.getOwnerId())) {
			isOwner = true;
		}
		
		// check role authority
		boolean roleAuth = roleService.hasOperateAuth(BugRoleResUtil.RES_OP_BUG_ALL_OPERATE);
		
		return (roleAuth || isOwner) ? Constants.VALID_YES : Constants.VALID_NO;
	}
	
	/**
	 * has bug remove authority
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/hasBugRemoveAuth.do")
	public String hasBugRemoveAuth() {
		
		return roleService.hasOperateAuth(BugRoleResUtil.RES_OP_BUG_REMOVE) ? Constants.VALID_YES
				: Constants.VALID_NO;
	}
	
	/**
	 * has project manage authority
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/hasPjtManageAuth.do")
	public String hasPjtManageAuth() {
		
		return roleService.hasOperateAuth(BugRoleResUtil.RES_OP_PROJECT_MANAGE) ? Constants.VALID_YES
				: Constants.VALID_NO;
	}
}
