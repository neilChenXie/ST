package com.chen.st.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/task")
public class TaskController {
	private final Logger log = LoggerFactory.getLogger(TaskController.class);
	
	@RequestMapping("/addNew")
	@ResponseBody
	public String addNewTask() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		System.out.println(session.getId());
		return "hello world";
	}
}
