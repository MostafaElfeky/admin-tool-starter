package com.gn4me.app.config.security;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.gn4me.app.core.dao.AuthDao;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.util.AppException;

@Service
public class AppDetailsService implements UserDetailsService, Serializable {
 
	private static final long serialVersionUID = 1L;
	
	@Autowired
    private AuthDao authDao;
 
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = null;
		try {
			user = authDao.findUserByEmail(email, new Transition());
		} catch (AppException e) {
			e.printStackTrace();
		}

		AppUserPrincipal userPrincipal = new AppUserPrincipal(user);

        return userPrincipal;
    }
    
}