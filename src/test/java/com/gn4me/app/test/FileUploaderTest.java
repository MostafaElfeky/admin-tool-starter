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
import com.gn4me.app.file.FileUtil;
import com.gn4me.app.file.entities.AppFile;
import com.gn4me.app.file.entities.FileInfo;
import com.gn4me.app.file.enums.ContentType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
public class FileUploaderTest {
	
	@Autowired
	FileUtil util;
   
	//@Ignore
	@Test
    public void testAdd() throws Exception {

		FileInfo info = util.getDownloadFileInfo(getDummyFile(), new Transition());
        System.out.println(" File info, " + info);
		assert(true);
    
	}
	
	
	
	
	public AppFile getDummyFile() {
		AppFile file = new AppFile();
		file.setId(50);
		file.setFileModuleId(1);
		file.setName("test file");
		file.setGeneratedCode("734dd7dcfbb14bc68f8798fffe19292c.jpg");
		file.setTags("tags tags");
		file.setType(ContentType.IMAGE);
		return file;
	}
}