package com.gn4me.app.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gn4me.app.config.WebConfig;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.mail.MailHandler;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
public class MailHandlerTest {
	
	@Autowired
	MailHandler mailHandler;
   
	//@Ignore
	@Test
    public void testAdd() throws Exception {

		User user = new User();
		user.setEmail("mostafa.ahmed.elfeky@gmail.com");
		mailHandler.sendResetPasswordMail(user, "/test/path", new Transition());
		assert(true);
    
	}
	
	
}