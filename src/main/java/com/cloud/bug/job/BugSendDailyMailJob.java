package com.cloud.bug.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cloud.bug.model.Bug;
import com.cloud.bug.util.BaseInfoUtil;
import com.cloud.bug.util.BugUtil;
import com.cloud.mail.MailSender;
import com.cloud.platform.BugConstants;
import com.cloud.platform.DateUtil;
import com.cloud.platform.IDao;
import com.cloud.platform.RegexUtil;
import com.cloud.platform.SpringUtil;
import com.cloud.platform.StringUtil;
import com.cloud.security.model.User;
import com.cloud.security.service.UserService;
import com.cloud.system.model.SystemConfig;

public class BugSendDailyMailJob implements Job {
	
	private static Logger logger = Logger.getLogger(BugSendDailyMailJob.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		// check if mail config has daily config
		SystemConfig systemConfig = BugConstants.getSystemConfig();
		String mailConfig = systemConfig.getMailConfig();
		
		if(StringUtil.isNullOrEmpty(mailConfig) || mailConfig.indexOf("d1") == -1) {
			return;
		}
		
		// get all un-close bugs
		IDao dao = SpringUtil.getDao();
		List<Bug> bugs = dao.getAllByHql("from Bug where status != " + BugConstants.BUG_STATUS_CLOSE);
		
		// group by user
		Map<String, List<Bug>> userBugs = new HashMap();
		
		for(Bug bug : bugs) {
			if(StringUtil.isNullOrEmpty(bug.getOwnerId())) {
				continue;
			}
			
			if(!userBugs.containsKey(bug.getOwnerId())) {
				userBugs.put(bug.getOwnerId(), new ArrayList());
			}
			
			userBugs.get(bug.getOwnerId()).add(bug);
		}
			
		// iterate users
		List<Bug> uBugs = null;
		Map<Integer, List<Bug>> statusBugs = new HashMap();
		Map<String, List<Bug>> pjtBugs = new HashMap();
		Map<String, List<Bug>> proirityBugs = new HashMap();
		Map<String, List<Bug>> levelBugs = new HashMap();
		
		for(String user : userBugs.keySet()) {
			uBugs = userBugs.get(user);

			for(Bug bug : uBugs) {
				// group by status
				if(!statusBugs.containsKey(bug.getStatus())) {
					statusBugs.put(bug.getStatus(), new ArrayList());
				}
				statusBugs.get(bug.getStatus()).add(bug);
				
				// group by project
				if(!pjtBugs.containsKey(bug.getProjectId())) {
					pjtBugs.put(bug.getProjectId(), new ArrayList());
				}
				pjtBugs.get(bug.getProjectId()).add(bug);
				
				// group by priority
				if(StringUtil.isNullOrEmpty(bug.getPriority())) {
					bug.setPriority(BugConstants.EMPTY);
				}
				if(!proirityBugs.containsKey(bug.getPriority())) {
					proirityBugs.put(bug.getPriority(), new ArrayList());
				}
				proirityBugs.get(bug.getPriority()).add(bug);
				
				// group by level
				if(StringUtil.isNullOrEmpty(bug.getLevel())) {
					bug.setLevel(BugConstants.EMPTY);
				}
				if(!levelBugs.containsKey(bug.getLevel())) {
					levelBugs.put(bug.getLevel(), new ArrayList());
				}
				levelBugs.get(bug.getLevel()).add(bug);
			}
			
			// send daily mail
			sendDailyMail(user, uBugs.size(), statusBugs, pjtBugs, proirityBugs, levelBugs);
		}
	}
	
	/**
	 * send daily mail
	 * 
	 * @param userId
	 */
	private void sendDailyMail(String userId, int totalCount,
			Map<Integer, List<Bug>> statusBugs, Map<String, List<Bug>> pjtBugs,
			Map<String, List<Bug>> proirityBugs,
			Map<String, List<Bug>> levelBugs) {
		try {
			// check user email
			UserService userService = (UserService) SpringUtil.getBean("userService");
			User user = userService.getUserById(userId);
			
			if(StringUtil.isNullOrEmpty(user.getEmail()) || !RegexUtil.emailFormat(user.getEmail())) {
				return;
			}
			
			// init mail info
			String mailTo = user.getEmail();
			String title = "PPM系统每日缺陷统计";
			StringBuilder content = new StringBuilder();
			
			content.append("PPM系统于 " + DateUtil.getDateStr(new Date()));
			content.append(" 统计你在系统中的缺陷情况如下：<br><br>");
			
			content.append("缺陷总数：" + outline(totalCount) + " 个<br>");
			
			content.append("缺陷状态统计：");
			for(Integer status : statusBugs.keySet()) {
				content.append(BugUtil.getStatusName(status) + " " + statusBugs.get(status).size() + " 个, ");
			}
			content.deleteCharAt(content.length() - 1);
			content.append("<br>");
			
			content.append("缺陷项目统计：");
			for(String projectId : pjtBugs.keySet()) {
				content.append(BugConstants.getProjectNameById(projectId) + " " + pjtBugs.get(projectId).size() + " 个, ");
			}
			content.deleteCharAt(content.length() - 1);
			content.append("<br>");
			
			content.append("缺陷优先级统计：");
			String priorityName = "";
			for(String priority : proirityBugs.keySet()) {
				if(BugConstants.EMPTY.equals(priority)) {
					priorityName = "无";
				} else {
					priorityName = BaseInfoUtil.getItemName("priority", priority);
				}
				content.append(priorityName + " " + proirityBugs.get(priority).size() + " 个, ");
			}
			content.deleteCharAt(content.length() - 1);
			content.append("<br>");
			
			content.append("缺陷严重性统计：");
			String levelName = "";
			for(String level : levelBugs.keySet()) {
				if(BugConstants.EMPTY.equals(level)) {
					levelName = "无";
				} else {
					levelName = BaseInfoUtil.getItemName("level", level);
				}
				content.append(levelName + " " + levelBugs.get(level).size() + " 个, ");
			}
			content.deleteCharAt(content.length() - 1);
			content.append("<br>");
			
			content.append("<br><br>");
			
			// send mail
			MailSender sender = (MailSender) SpringUtil.getBean("mailSender");
			sender.sendHtmlMail(mailTo, title, content.toString());
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：BugSendDailyMailJob execute", e);
		}
	}
	
	/**
	 * combine outline html
	 * 
	 * @param text
	 * @return
	 */
	private String outline(Object text) {
		return "<span style='color: red'>" + text.toString() + "</span>";
	}
}
