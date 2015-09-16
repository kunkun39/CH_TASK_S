package com.cloud.system.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloud.system.service.UserStatusService;

@Controller
@RequestMapping("status")
public class UserStatusBean {

	@Autowired
	private UserStatusService statusService;
}
