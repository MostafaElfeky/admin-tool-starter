package com.gn4me.app.util;



import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.gn4me.app.config.security.AppUserPrincipal;
import com.gn4me.app.entities.User;


@Component
public class LoggedInUser {

	
	
	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	public static String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
	
	public static User getLoggedInUser() {
		
		Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null && auth.getPrincipal() instanceof AppUserPrincipal) {
			
			AppUserPrincipal userDetails =
					 (AppUserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return userDetails.getUser();
		} else {
			return null;
		}
		
	}
	
	
	
	public static boolean isCurrentAuthenticationAnonymous() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			return authentication.getPrincipal().equals("anonymousUser");
		} else {
			return false;
		}
	}

	
}
