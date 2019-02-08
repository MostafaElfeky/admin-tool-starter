package com.gn4me.app.test;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gn4me.app.config.WebConfig;
import com.gn4me.app.core.dao.impl.ContentDaoHandler;
import com.gn4me.app.core.dao.impl.SystemAuthDao;
import com.gn4me.app.entities.Content;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.entities.enums.Language;
import com.gn4me.app.entities.filters.ContentFilter;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
public class ContentDaoTest {
	
	@Autowired
	ContentDaoHandler daoHandler;
	
	
   
	//@Ignore
	@Test
    public void testAdd() throws Exception {

		ContentFilter filter = new ContentFilter();
		List<Content> list = daoHandler.listContent(filter, new Transition());
		System.out.println("content list: " + list);
		assert(list != null);
    
	}
	
	
	
}