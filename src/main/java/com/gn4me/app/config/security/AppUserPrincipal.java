package com.gn4me.app.config.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gn4me.app.entities.Privilege;
import com.gn4me.app.entities.Role;
import com.gn4me.app.entities.User;

public class AppUserPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private User user;
 
    public AppUserPrincipal(User user) {
        this.user = user;
    }
    
    public User getUser() {
    	return this.user;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		ArrayList<GrantedAuthority> auth = new ArrayList<>();
		
		if(user != null && user.getRoles() != null) {
			for(Role role : user.getRoles()) {
				if(role.getPrivileges() != null) {
					for(Privilege privilege : role.getPrivileges()) {
						
						auth.add(
							new GrantedAuthority() {
	
								private static final long serialVersionUID = 1L;
	
								@Override
								public String getAuthority() {
									return privilege.getCode();
								}
							}
						);
						
					}
				}
			}
		}
		
		return auth;
	}

	@Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return user.getEmail();
    }
 
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
 
    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}