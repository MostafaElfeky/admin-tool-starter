package com.gn4me.app.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.gn4me.app.config.props.FileProps;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.ResponseCode;
import com.gn4me.app.entities.response.ResponseStatus;
import com.gn4me.app.file.FileUploader;
import com.gn4me.app.file.entities.FileInfo;
import com.gn4me.app.log.Logging;


public class SystemFileUploader implements FileUploader {

	private Logger logger = Logger.getLogger("AppDebugLogger");
	
	@Override
	public boolean upload(FileInfo info, byte[] bytes, Transition transition) throws Exception {

		BufferedOutputStream stream = null;

		boolean uploaded;

		try {

			
			// Creating the directory to store file
			File dir = new File(info.getFilePath());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			File serverFile = new File(info.getFullPath());

			stream = new BufferedOutputStream(new FileOutputStream(serverFile));

			stream.write(bytes);
			stream.flush();

			logger.debug(Logging.format("Server File Location= " + serverFile.getAbsolutePath() 
					      + ", file type uploaded [" + info.getId() , transition));
			
			uploaded = true;
			
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new AppException(new ResponseStatus(ResponseCode.GENERAL_FAILURE), transition);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					throw new AppException(new ResponseStatus(ResponseCode.GENERAL_FAILURE), transition);
				}
			}
		}

		return uploaded;
	}

	@Override
	public boolean delete(FileInfo info, Transition transition) throws Exception {

		boolean deleted = false;

		try {
			File file = null;//new File(getPath(info));

			if (file.exists()) {
				deleted = file.delete();
			}
			if (deleted) {
				logger.debug(Logging.format(file.getName() + " is deleted!", transition));
			} else {
				logger.debug(Logging.format("Delete operation" + " is failed for file with Info: " + info , transition));
			}

		} catch (Exception exp) {
			
			throw new AppException(new ResponseStatus(ResponseCode.GENERAL_FAILURE), transition);
			
		}
		return deleted;
	}

}
