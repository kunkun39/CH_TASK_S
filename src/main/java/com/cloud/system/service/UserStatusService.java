package com.cloud.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.platform.Constants;
import com.cloud.platform.IDao;
import com.cloud.system.model.UserStatus;

@Service
public class UserStatusService {

	@Autowired
	private IDao dao;
	
	/**
	 * get user status
	 * 
	 * @return
	 */
	public UserStatus getUserStatus() {
		
		UserStatus userStatus = (UserStatus) dao.getObject(UserStatus.class,
				"from UserStatus where userId = ?", Constants.getLoginUserId());
		
		return userStatus;
	}
	
	/**
	 * save user status
	 * 
	 * @param status
	 */
	public void saveUserStatus(UserStatus status) {
		dao.saveObject(status);
	}
}
