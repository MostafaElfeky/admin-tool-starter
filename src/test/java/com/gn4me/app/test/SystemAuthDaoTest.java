package com.gn4me.app.test;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gn4me.app.config.WebConfig;
import com.gn4me.app.core.dao.impl.SystemAuthDao;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.entities.enums.Language;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
public class SystemAuthDaoTest {
	
	@Autowired
	SystemAuthDao authDao;
	
	@Autowired
	PasswordEncoder encoder;
   
	@Ignore
	@Test
    public void testAdd() throws Exception {

		boolean inserted = authDao.save(getDummyUser(), new Transition());
        System.out.println(" User Insert status: " + inserted);
		assert(inserted);
    
	}
	
	
	@Test
	@Ignore
    public void testSignin() throws Exception {

		User user = authDao.findUserByEmail("mostafa.ahmed.elfeky@gmail.com", new Transition());
        System.out.println(" User SignIn status: " + user);
		assert(user != null);
    
	}
	
	@Test
	@Ignore
    public void testGetUserRoles() throws Exception {

        System.out.println(" User Roles: " + authDao.getUserRoles(5, new Transition()));
		assert(true);
    
	}
	
	//@Ignore
	@Test
    public void testUpdatePassword() throws Exception {

		Transition transition = new Transition(Language.EN);
		boolean updated = authDao.updatePassword(encoder.encode("mostafa"), transition);
        System.out.println("Update User Password status: " + updated);
		assert(updated);
    
	}
	
	public User getDummyUser() {
		User user = new User();
		user.setEmail("mostafa.ahmed.elfeky@gmail.com");
		user.setFirstName("mostafa");
		user.setLastName("ahmed");
		user.setImage("image");
		user.setPassword("password");
		user.setStatusId(1);
		return user;
	}
}