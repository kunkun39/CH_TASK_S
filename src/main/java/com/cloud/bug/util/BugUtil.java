package com.cloud.bug.util;

import com.cloud.platform.BugConstants;

public class BugUtil {

	/**
	 * get status name
	 * 
	 * @param status
	 * @return
	 */
	public static String getStatusName(int status) {
		
		String name = "";
		
		switch(status) {
		case BugConstants.BUG_STATUS_INIT:
			name = "初始化";
			break;
		case BugConstants.BUG_STATUS_AUDIT:
			name = "待审核";
			break;
		case BugConstants.BUG_STATUS_SOLVE:
			name = "修改中";
			break;
		case BugConstants.BUG_STATUS_TEST:
			name = "回归测试中";
			break;
		case BugConstants.BUG_STATUS_HANGUP:
			name = "挂起";
			break;
		case BugConstants.BUG_STATUS_CLOSE:
			name = "关闭";
			break;
		}
		
		return name;
	}
}
