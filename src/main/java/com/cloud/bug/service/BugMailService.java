package com.cloud.bug.service;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.stereotype.Service;

import com.cloud.bug.job.BugSendStatusMailJob;
import com.cloud.bug.model.Bug;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;
import com.cloud.platform.RegexUtil;
import com.cloud.platform.SpringUtil;
import com.cloud.platform.StringUtil;
import com.cloud.security.model.User;
import com.cloud.security.service.UserService;
import com.cloud.system.model.SystemConfig;

@Service
public class BugMailService {

	/**
	 * send bug status mail
	 * 
	 * @param status
	 * @param ownerId
	 * @throws SchedulerException 
	 */
	public void sendStatusMail(int status, String ownerId, String operatorId, String operate,
			Bug bug) throws SchedulerException {
		
		// check owner email
		UserService userService = (UserService) SpringUtil.getBean("userService");
		User owner = userService.getUserById(ownerId);
		
		if(StringUtil.isNullOrEmpty(owner.getEmail()) || !RegexUtil.emailFormat(owner.getEmail())) {
			return;
		}
		
		// check if in mail config
		SystemConfig systemConfig = BugConstants.getSystemConfig();
		String mailConfig = systemConfig.getMailConfig();
		
		if(StringUtil.isNullOrEmpty(mailConfig) || mailConfig.indexOf("s" + status) == -1) {
			return;
		}
		
		// init mail send job
		JobDetail jobDetail = new JobDetail(Constants.getID(), BugSendStatusMailJob.class);
		
		jobDetail.getJobDataMap().put("operatorId", operatorId);
		jobDetail.getJobDataMap().put("operate", operate);
		jobDetail.getJobDataMap().put("bug", bug);
		jobDetail.getJobDataMap().put("mailTo", owner.getEmail());
		
		// trigger once and now
		SimpleTrigger trigger = new SimpleTrigger(Constants.getID());
		trigger.setStartTime(new Date());
		trigger.setRepeatCount(0);
		trigger.setRepeatInterval(1000);  // no sense, for not throw exception
		
		// get scheduler
		Scheduler scheduler = (Scheduler) SpringUtil.getBean("scheduler");
		
		// schedule job
		scheduler.scheduleJob(jobDetail, trigger);
	}
}
