package com.gn4me.app.core.dao;

import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.util.AppException;
 
public interface AuthDao {

	public boolean save(User user, Transition transition) throws AppException;
	
	public User findUserByEmail(String email, Transition transition) throws AppException;
	
	public User findUserById(Transition transition) throws AppException;
	
	public boolean updatePassword(String newPassword, Transition transition) throws AppException;
	
	public boolean updateValidationToken(User user, Transition transition) throws AppException;

	public boolean restPassword(String token, String password, Transition transition) throws AppException;
	
	public boolean activiteAccount(String token, Transition transition) throws AppException;
	
}
