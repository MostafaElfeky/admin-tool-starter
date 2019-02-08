package com.gn4me.app.file;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gn4me.app.config.props.FileProps;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.ResponseCode;
import com.gn4me.app.file.data.FileDao;
import com.gn4me.app.file.data.FileLoader;
import com.gn4me.app.file.entities.AppFile;
import com.gn4me.app.file.entities.FileInfo;
import com.gn4me.app.file.entities.FileModule;
import com.gn4me.app.file.entities.FileResponse;
import com.gn4me.app.file.entities.ImageSize;
import com.gn4me.app.file.enums.ContentType;
import com.gn4me.app.file.enums.ImageSizeEnum;
import com.gn4me.app.log.Logging;


@Component
public class FileUtil {

	
	private Logger logger = Logger.getLogger("AppDebugLogger");
	
	@Autowired
	FileProps fileProps;
	
	@Autowired
	FileDao fileDao;
	/**
	 * Checks if the type of file if image or not based on supported images from config file.
	 *
	 * @param fileName the file name
	 * @param transaction Trace code
	 * @return true, if is image
	 */
	public boolean isImage(String extension, Transition transition) {
		
		boolean image = false;
		
		logger.debug(Logging.format(">>>>> Check if file Type Image Or Not for File Name: " + extension, transition));
		
		if(extension != null ) {
			
			logger.debug(Logging.format(">>>>> File Extention: " + extension, transition));
			if(extension != null 
					&& fileProps.getImageSupportedType() != null 
					&&  fileProps.getImageSupportedType().contains("["+extension.trim().toLowerCase()+"]")) {
				image = true;
			} 
			
		} 
		
		logger.debug(Logging.format("Check if file Type Image Or Not Status: " + image, transition));
		
		return image;
	}
	
	public boolean isSupportedType(String fileName, Transition transition) {
		
		boolean image = false;
		
		logger.debug(Logging.format(">>>>> Check if file Type Suuported Or Not for File Name: " + fileName, transition));
		
		if(fileName != null ) {
			
			String fileExt = FilenameUtils.getExtension(fileName);
			logger.debug(Logging.format(">>>>> File Extention: " + fileExt, transition));
			if(fileExt!=null 
					&& fileProps.getImageSupportedType() != null 
					&&  fileProps.getImageSupportedType().contains("["+fileExt.trim().toLowerCase()+"]")) {
				image = true;
			} 
			
		} 
		
		logger.debug(Logging.format("Check if file Type Supported Or Not Status: " + image, transition));
		
		return image;
	}

	public FileResponse validateFile(MultipartFile file, String category,
									boolean asFile, Transition transition) {
		
		FileResponse response = new FileResponse();
		
		FileInfo fileInfo = null;
		String fileName = file.getOriginalFilename();

		long fileSize = file.getSize();
		
		boolean isSupported = isSupportedType(fileName, transition);
		
		if(isSupported) {
			logger.debug(Logging.format("File is Supporeted Type", transition));
			
			boolean isImage = isImage(fileName, transition);
			
			if(isImage && !asFile) {
				
				if(true) {
					logger.debug(Logging.format("Exceed Image Max size, fileSize: " + fileSize, transition));
					response.setResponseStatus(ResponseCode.EXCEED_MAX_SIZE);
					return response;
				}
				
				//fileInfo = new ImageInfo();
//				fileInfo.setImage(true);
				fileInfo.setName(FilenameUtils.getBaseName(fileName));
//				fileInfo.setExtension(FilenameUtils.getExtension(fileName));
				
			} else {
				
			}
		} else {
			response.setResponseStatus(ResponseCode.UNSUPPORTED_FILE_TYPE);
		}
		return response;
	}
	
	public ImageSizeEnum[] getFileSizes(String stockSizes, Transition transition) throws Exception {

		ImageSizeEnum[] sizeEnums = null;
		int count = 0;
		
		try {
			if(stockSizes != null) {
				
				stockSizes = stockSizes.replaceAll(" ", "");
				String[] sizes = stockSizes.split(",");
				
				count = sizes.length;
				sizeEnums = new ImageSizeEnum[count]; 
				
				for (int i=0; i< count; i++) {
				    if(sizes[i] != null && sizes[i] != "") {
				    	sizeEnums[i] = ImageSizeEnum.valueOf(sizes[i]);
			    	}
				}
				
			}
		} catch (Exception exp) {
			logger.debug(Logging.format("Faild To Parse file sizes. should be in the format [XL, S]", exp, transition));
		}
		
		return sizeEnums;
	}
	
	public ImageSize[] getCustomFileSizes(String stockSizes, Transition transition) throws Exception {
		
		ImageSize[] imageSizes = null;
		int count = 0;
		try {
			if(stockSizes != null) {
				
				stockSizes = stockSizes.replaceAll(" ", "");
				String[] sizes = stockSizes.split(",");
				
				count = sizes.length;
				imageSizes = new ImageSize[count]; 
				
				for (int i=0; i< count; i++) {
				    if(sizes[i] != null && sizes[i] != "") {
				    	String[] sizeDetails = sizes[i].split("_");

				    	imageSizes[i] = new ImageSize(Integer.parseInt(sizeDetails[0]), Integer.parseInt(sizeDetails[1]));
			    	}
				}
				
			}
		} catch (Exception exp) {
			logger.debug(Logging.format("Faild To Parse Custom file sizes. should be in the format [width_height, width_height] ", exp, transition));
		}

		return imageSizes;
	}
	
	public ImageSizeEnum getDefaultFileSize(String size, Transition transition) throws Exception {
		ImageSizeEnum imageSizeEnum = null;
		try {
			imageSizeEnum = ImageSizeEnum.valueOf(size);
		} catch (Exception exp) {
			logger.debug(Logging.format("Faild To Parse Default file sizes. Should be Single specific type Not Custom", exp, transition));
		}
		return imageSizeEnum;
	}


	public FileInfo getUploadFileInfo(MultipartFile file, String module, String tags, Transition transition) throws Exception {
		
		String filename =  FilenameUtils.getBaseName(file.getOriginalFilename());
		StringBuilder relativePath = new StringBuilder();
		
		FileModule fileModule = FileLoader.modulePerCode.get(module);
		
		if(fileModule == null) {
			throw new IllegalArgumentException("File Module Not Found.");
		}
		
		FileInfo info = new FileInfo();
		
		info.setName(filename);
		info.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		info.setFileModuleId(fileModule.getId());
		info.setFileModule(fileModule);
		info.setGeneratedCode(UUID.randomUUID().toString().replaceAll("-", "") + "." + info.getExtension());
		info.setTags(tags);
		info.setType(isImage(info.getExtension(), transition) ? ContentType.IMAGE : ContentType.FILE);
		
		relativePath.append("/")
			   .append( fileModule.getModule().toString().toLowerCase())
			   .append("/")
			   .append(info.getType().toString().toLowerCase());
		
		info.setRelativePath(relativePath);
		
		return info;
	}
	
	public FileInfo getDownloadFileInfo(AppFile file, Transition transition) {
		
		FileInfo info = new FileInfo(file);
		Map<String, String> files = new HashMap<String, String>();
		final String defaultFile = "DEFAULT";
		
		
		StringBuilder builder = new StringBuilder(fileProps.getDownloadRootPath());
		
		FileModule fileModule = FileLoader.modulePerId.get(file.getFileModuleId());
		logger.debug(Logging.format("Getting file module as, " + fileModule, transition));
		
		if(fileModule == null) {
			logger.debug(Logging.format("Failed to get File Module", transition));
			throw new IllegalArgumentException("File Module Not Found.");
		} else {
			info.setFileModule(fileModule);
		}
		
		info.setExtension(FilenameUtils.getExtension(file.getGeneratedCode()));

		info.setType(isImage(info.getExtension(), transition) ? ContentType.IMAGE : ContentType.FILE);
		
		builder.append("/")
			   .append( fileModule.getModule().toString().toLowerCase())
			   .append("/")
			   .append(info.getType().toString().toLowerCase())
			   .append("/");
		
		info.setRelativePath(builder);
		
		logger.debug(Logging.format("File Root path, " + info.getRelativePath(), transition));
		
		
		if(info.getType().equals(ContentType.IMAGE)) {
			
			
			logger.debug(Logging.format("File type is image and Going to Set image application specific sizes ", transition));
			
			for(ImageSizeEnum size : info.getFileModule().getSizes()) {
				
				info.setFilePath(info.getRelativePath() + size.toString().toLowerCase());
				
				if(size.equals(info.getFileModule().getDefaultSize())) {
					files.put(defaultFile, info.getFullPath());
				} else {
					files.put(size.toString(), info.getFullPath());
				}
				
			}
			
			logger.debug(Logging.format("Set images with Custom sizes, " + info.getFileModule().getCustomSizes(), transition));
			
			for(ImageSize size : info.getFileModule().getCustomSizes()) {
				info.setFilePath(info.getRelativePath() + size.getCode());
				files.put(size.getCode().toLowerCase(), info.getFullPath());
			}
			
			logger.debug(Logging.format("Set thumbnail image", transition));
			info.setFilePath(info.getRelativePath() + ImageSizeEnum.THUMBNAIL.toString().toLowerCase());
			info.setThumbnail(info.getFullPath());
			
		} else {
			logger.debug(Logging.format("File type is File and Going to Set as default path", transition));
			info.setFilePath(info.getRelativePath().toString());
			files.put(defaultFile, info.getFullPath());
		}
		
		if(files.get(defaultFile) == null) {
			
			logger.debug(Logging.format("As there is No default file yet we will set the first One as default.", transition));
			
			String firstKey = files.keySet().stream().findFirst().get();
			String fiestPath = files.remove(firstKey);
			files.put(defaultFile.toLowerCase(), fiestPath);
		}
		
		info.setFiles(files);
		
		return info;
	}
	
	public byte[] resizeImage(byte[] image, ImageSizeEnum size, Transition transition) throws Exception {
		return image;
	}
	
	public byte[] resizeImage(byte[] image, ImageSize size, Transition transition) throws Exception {
		return image;
	}
	
	public boolean updateFilesOwner(int ownerId, List<AppFile> files,
			Transition transition) throws Exception {
		
		String filesId = "";
		int fileModuleId = 0;
				
		if(files != null && files.size() > 0) {
			
			for(AppFile file : files) {
				filesId = filesId + file.getId() + ",";
				fileModuleId = file.getFileModuleId();
			}
			filesId = filesId.substring(0, filesId.length() - 1);
			
		} else {
			throw new IllegalArgumentException("Invalid Input:  ownerId: " + ownerId + " or files: " + files );
		}
		
		logger.debug(Logging.format("update files Owner with ownerId: " + ownerId
					+ " or filesId: "+filesId + " or fileModuleId: " + fileModuleId, transition));
		
		if(ownerId > 0 && fileModuleId > 0 && filesId != null) {
			return fileDao.updateFilesOwner(ownerId, filesId, fileModuleId, transition);
		} else {
			throw new IllegalArgumentException("Invalid Input:  ownerId: " + ownerId
					+ " or filesId: "+filesId + " or fileModuleId: " + fileModuleId);
		}
		
	}
	
	
	
}
