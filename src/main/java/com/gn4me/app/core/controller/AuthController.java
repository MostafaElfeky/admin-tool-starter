package com.gn4me.app.core.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gn4me.app.core.service.UserService;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.entities.response.GeneralResponse;
import com.gn4me.app.log.Loggable;
import com.gn4me.app.log.Type;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/auth")
@Loggable(Type = Type.CONTROLLER)
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public GeneralResponse signup(@RequestBody User user, 
			Transition transition) throws Exception {
		
		return  userService.signup(user, transition);
		
	}
	
	@ApiOperation(value = "Forget password send email to get an email with reset process")
	@PostMapping("/forget-password")
	public GeneralResponse forgetPassword(
			@RequestParam String email, Transition transition) throws Exception {
		
		return userService.forgetPassword(email, transition);
		
	}
	
	@ApiOperation(value = "Forget password send email to get an email with reset process")
	@PostMapping("/reset-password")
	public GeneralResponse resetPassword(
			@RequestParam String token,
			@RequestParam String password,
			Transition transition) throws Exception {
		
		return userService.resetPassword(token, password, transition);
		
	}
	
	@ApiOperation(value = "Activite Account based on registeration mail")
	@PostMapping("/activite-account")
	public GeneralResponse activiteAccount(
			@RequestParam String token,
			Transition transition) throws Exception {
		
		return userService.activiteAccount(token, transition);
		
	}

}
