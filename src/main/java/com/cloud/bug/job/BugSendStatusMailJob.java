package com.cloud.bug.job;

import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cloud.bug.model.Bug;
import com.cloud.bug.util.BugUtil;
import com.cloud.mail.MailSender;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;
import com.cloud.platform.SpringUtil;

public class BugSendStatusMailJob implements Job {

	private static Logger logger = Logger.getLogger(BugSendStatusMailJob.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// get datas
			Map data = context.getJobDetail().getJobDataMap();
			
			String operatorId = (String) data.get("operatorId");
			String operate = (String) data.get("operate");
			Bug bug = (Bug) data.get("bug");
			String mailTo = (String) data.get("mailTo");
			
			// init mail title
			String title = "PPM系统缺陷提醒";
			
			// init mail content
			StringBuilder content = new StringBuilder();
			content.append(outline(Constants.getUsernameById(operatorId)));
			content.append(" 通过 " + outline(BugConstants.getOperateName(operate)) + " 操作");
			content.append("向你提交了一个缺陷。<br><br>");
			
			content.append("缺陷名称：" + outline(bug.getName()) + "<br>");
			content.append("缺陷状态：" + BugUtil.getStatusName(bug.getStatus()) + "<br>");
			content.append("所属项目:" + BugConstants.getProjectNameById(bug.getProjectId()) + "<br>");
			content.append("缺陷描述：" + (bug.getIntro() == null ? "" : bug.getIntro()) + "<br>");
			content.append("操作备注：" + (bug.getNote() == null ? "" : bug.getNote()) + "<br>");
			
			content.append("<br><br>");
			
			// send mail
			MailSender sender = (MailSender) SpringUtil.getBean("mailSender");
			sender.sendHtmlMail(mailTo, title, content.toString());
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：BugSendStatusMailJob execute", e);
		}
	}
	
	/**
	 * combine outline html
	 * 
	 * @param text
	 * @return
	 */
	private String outline(String text) {
		return "<span style='color: red'>" + text + "</span>";
	}
}
