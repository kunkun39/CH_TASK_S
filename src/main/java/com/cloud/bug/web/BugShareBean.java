package com.cloud.bug.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cloud.bug.service.BugShareService;
import com.cloud.platform.Constants;

@Controller
@RequestMapping("share")
public class BugShareBean {

	@Autowired
	private BugShareService shareService;
	
	/**
	 * open share
	 * 
	 * @return
	 */
	@RequestMapping("/openShare.do")
	public ModelAndView openShare() {
		
		ModelAndView mv = new ModelAndView("bug/share");
		
		// search bug shares
		List<Object[]> shareInfo = shareService.searchBugShares();
		mv.addObject("shares", shareInfo);
		
		return mv;
	}
	
	/**
	 * share bug
	 * 
	 * @param bugId
	 */
	@ResponseBody
	@RequestMapping("/shareBug.do")
	public void shareBug(@RequestParam("bugId") String bugId, @RequestParam("flag") String flag) {

		// share bug
		if(Constants.VALID_YES.equals(flag)) {
			shareService.shareBug(bugId);
		}
		// un-share bug
		else {
			shareService.removeShare(bugId);
		}
	}
}
