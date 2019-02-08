package com.gn4me.app.core.service;


import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gn4me.app.core.dao.AuthDao;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.entities.enums.ResponseCode;
import com.gn4me.app.entities.enums.SystemStatusEnum;
import com.gn4me.app.entities.response.GeneralResponse;
import com.gn4me.app.entities.response.UserResponse;
import com.gn4me.app.log.Loggable;
import com.gn4me.app.log.Logging;
import com.gn4me.app.log.Type;
import com.gn4me.app.mail.MailHandler;
import com.gn4me.app.util.AppException;
import com.gn4me.app.util.SystemLoader;
import com.gn4me.app.util.UtilHandler;


@Service
@Loggable(Type = Type.SERVICCE)
public class UserService {

	@Autowired
	private AuthDao authDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@Value("${security.secret-key}")
	private String secretKey;
	
	@Value("${security.max-refresh-rate}")
	private int maxRefreshRate;
	
	@Value("${security.jwt.token.expire-length}")
	private long validityInMilliseconds;
	
	@Value("${security.relogin.state}")
	private int reloginState;
	
	@Value("${user.confirm.validityTime}")
	int validityTime;
	
	@Autowired
	private MailHandler mailHandler;
	
	@Autowired
	private UtilHandler utilHandler;
	
	private Logger logger = Logger.getLogger("AppDebugLogger");
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public UserResponse signin(String email, String password, Transition transition) throws AppException {
		
		UserResponse response  = new UserResponse();
		
		User user = authDao.findUserByEmail(email, transition);
		
		logger.debug(Logging.format("Getting User by email and going to validate User Password user, " + user, transition));

		if(user != null 
				&& user.getStatus().getCode().equals(SystemStatusEnum.ACTIVE.name())
				&& passwordEncoder.matches(password, user.getPassword())) {
			
			logger.debug(Logging.format("User Password Matched and going to create user Token", transition));
			 
			 //String token = jwtTokenProvider.createToken(user, 1);
			 //user.setSecToken(token);
			
			 response.setUser(user);
			 response.setResponseStatus(ResponseCode.SUCCESS);
			 
		} else {
			logger.debug(Logging.format("Invalid user password OR user status not Active", transition));
			response.setResponseStatus(ResponseCode.INVALID_AUTH, transition);
		}
		
		return response;
	}

		
	public GeneralResponse signup(User user, Transition transition) throws Exception {
		
		GeneralResponse response = new GeneralResponse();
		
		user.setStatusId(SystemLoader.statusPerCode.get(SystemStatusEnum.PENDING.name()).getId());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
		user.setTokenExpiryDate(utilHandler.getDateWith(new Date(), validityTime));
		
		boolean inserted = authDao.save(user, transition);
		
		String activiteAccountUrl = utilHandler.getAppInfo().getUserActiviteAccountUrl() + "/" + user.getToken();
		
		if(inserted) {
			//send Email To User to Confirm
			logger.debug(Logging.format("User Registered Successfully Going to send activition Mail", transition));
			mailHandler.sendActiviteAccountMail(user, activiteAccountUrl, transition);
			response.setResponseStatus(ResponseCode.SUCCESS);
		} else {
			response.setResponseStatus(ResponseCode.NO_DATA_SAVED, transition);
		}
		
		return response;
	}
	
	public GeneralResponse updatePassword(String oldPassword, String newPassword, Transition transition) throws AppException {
		
		GeneralResponse response = new GeneralResponse();

		if(newPassword != null 
				&& oldPassword != null 
				&& transition != null 
				&& transition.getUserId() > 0) {
			
			User user = authDao.findUserById(transition);
			
			logger.debug(Logging.format("Get Logged in User, Going to validate Password, user, " + user, transition));
			
			if(passwordEncoder.matches(oldPassword, user.getPassword())) {
				
				logger.debug(Logging.format("User Has a valid password So will update It", transition));
				
				boolean inserted = authDao.updatePassword(passwordEncoder.encode(newPassword), transition);
				
				if(inserted) {
					response.setResponseStatus(ResponseCode.SUCCESS);
				} else {
					response.setResponseStatus(ResponseCode.NOT_EXIST, transition);
				}
			} else {
				response.setResponseStatus(ResponseCode.FORBIDDEN, transition);
			}
			
		} else {
			response.setResponseStatus(ResponseCode.BAD_REQUEST, transition);
		}
		
		return response;
	}

	public GeneralResponse forgetPassword(String email, Transition transition) throws AppException, IOException {
		
		GeneralResponse response = new GeneralResponse();
		
		if(email != null && email != "") {
			
			User user = authDao.findUserByEmail(email, transition);
			
			if(user!= null && user.getId() > 0) {
				
				user.setEmail(email);
				user.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
				user.setTokenExpiryDate(utilHandler.getDateWith(new Date(), validityTime));
				
				logger.debug(Logging.format("Update User with validation Token and Date. user, " + user, transition));
				
				boolean updated = authDao.updateValidationToken(user, transition);
				String restPassUrl = utilHandler.getAppInfo().getUserResetPasswordUrl() + "/" + user.getToken();
				
				if(updated) {
					
					logger.debug(Logging.format("Token Updated successfully Going to send mail with path, " + restPassUrl , transition));
					
					//send Email To User to Confirm
					mailHandler.sendResetPasswordMail(user, restPassUrl, transition);
					response.setResponseStatus(ResponseCode.SUCCESS);
				} else {
					response.setResponseStatus(ResponseCode.GENERAL_FAILURE, transition);
				}
				
			} else {
				response.setResponseStatus(ResponseCode.NOT_EXIST, transition);
			}
			
		} else {
			response.setResponseStatus(ResponseCode.BAD_REQUEST, transition);
		}
		return response;
	}

	public GeneralResponse resetPassword(String token, String password, Transition transition) throws AppException {
		
		GeneralResponse response = new GeneralResponse();
		
		if(token != null && token != "") {
			
			boolean reset = authDao.restPassword(token, passwordEncoder.encode(password), transition);
			
			if(reset) {
				response.setResponseStatus(ResponseCode.SUCCESS);
			} else {
				response.setResponseStatus(ResponseCode.INVALID_TOKEN, transition);
			}
			
		} else {
			response.setResponseStatus(ResponseCode.BAD_REQUEST, transition);
		}
		return response;
	}
	
	public GeneralResponse activiteAccount(String token, Transition transition) throws AppException {
		
		GeneralResponse response = new GeneralResponse();
		
		if(token != null && token != "") {
			
			boolean activited = authDao.activiteAccount(token, transition);
			
			if(activited) {
				response.setResponseStatus(ResponseCode.SUCCESS);
			} else {
				response.setResponseStatus(ResponseCode.INVALID_TOKEN, transition);
			}
			
		} else {
			response.setResponseStatus(ResponseCode.BAD_REQUEST, transition);
		}
		return response;
	}
	

}
