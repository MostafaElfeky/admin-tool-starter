package com.gn4me.app.test;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gn4me.app.config.WebConfig;
import com.gn4me.app.core.dao.IGeneralDao;
import com.gn4me.app.entities.Gender;
import com.gn4me.app.entities.SystemStatus;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.SystemModuleEnum;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
public class SystemGeneralDaoTest {
	
	@Autowired
	IGeneralDao generalDao;

   
	//@Ignore
	@Test
    public void testListSystemStatus() throws Exception {

		List<SystemStatus> status = generalDao.listSytemStatus(SystemModuleEnum.USER, new Transition());
        System.out.println(" System status: " + status);
		assert(status != null);
    
	}
	
	@Ignore
	@Test
    public void testListGenders() throws Exception {

		List<Gender> genders = generalDao.listGenders(new Transition());
        System.out.println(" Genders: " + genders);
		assert(genders != null);
    
	}
	
}