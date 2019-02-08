package com.gn4me.app.core.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.util.LoggedInUser;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class Home {


	@GetMapping("/")
	public ModelAndView welecom(HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		ModelAndView mav = null;
		
		if(LoggedInUser.isCurrentAuthenticationAnonymous()) {
			mav = new ModelAndView("login");
		} else {
			mav = new ModelAndView("home");
			User user = LoggedInUser.getLoggedInUser();
			mav.addObject("user", user);
		}	
		
		return mav;
	}
	
	@GetMapping("/login")
	public ModelAndView signin(Transition transition) throws Exception {

		ModelAndView mav = null;
		
//		if(LoggedInUser.isCurrentAuthenticationAnonymous()) {
//			mav = new ModelAndView("login");
//		} else {
//			mav = new ModelAndView("home");
//			User user = LoggedInUser.getLoggedInUser();
//			mav.addObject("user", user);
//		}	
		
		mav = new ModelAndView("login");
		
		return mav;
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/test")
	public ModelAndView test() {
		return new ModelAndView("test");
	}
	

}
