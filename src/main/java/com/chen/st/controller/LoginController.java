package com.chen.st.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/manage")
public class LoginController {
	
	@RequestMapping("/login")
	@ResponseBody
	public boolean login() {
		String error = null;
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("Chen", "121");
		try {
			subject.login(token);
			subject.getSession().setAttribute("uid", "21jj32io2n3n");
		} catch (UnknownAccountException e) {  
            error = "用户名/密码错误";  
        } catch (IncorrectCredentialsException e) {  
            error = "用户名/密码错误";  
        }
		System.out.println(error);
		return subject.isAuthenticated();
	}
	
	@RequestMapping("/failed")
	@ResponseBody
	public String failed() {
		return "login failed";
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "logout";
	}

}
