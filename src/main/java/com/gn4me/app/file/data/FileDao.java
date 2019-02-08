package com.gn4me.app.file.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gn4me.app.entities.Transition;
import com.gn4me.app.file.FileUtil;
import com.gn4me.app.file.entities.AppFile;
import com.gn4me.app.file.entities.FileModule;
import com.gn4me.app.log.Logging;
import com.gn4me.app.util.AppException;
import com.gn4me.app.util.SystemLoader;

@Repository
public class FileDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private Logger logger = Logger.getLogger("AppDebugLogger");
	
	@Autowired
	private FileUtil fileUtil;

	
	public List<FileModule> listFileModules(Transition transition) throws Exception {

		String query = "";
		List<FileModule> modules = null;

		try {

			query = "SELECT FILE_MODULE.* FROM FILE_MODULE WHERE DELETED=0;";

			modules = jdbcTemplate.query(query, new RowMapper<FileModule>() {

				@Override
				public FileModule mapRow(ResultSet rs, int arg1) throws SQLException {
					FileModule fileModule = new FileModule();
					fileModule.setId(rs.getInt("ID"));
					fileModule.setModule(rs.getString("MODULE_CODE"));
					try {
						fileModule.setSizes(fileUtil.getFileSizes(rs.getString("AVAILABLE_SIZES"), transition));
						fileModule.setCustomSizes(fileUtil.getCustomFileSizes(rs.getString("CUSTOM_SIZE"), transition));
						fileModule.setDefaultSize(fileUtil.getDefaultFileSize(rs.getString("DEFAULT_SIZE"), transition));
					} catch (Exception exp) {
						logger.debug(Logging.format("Format Image sizes", exp, transition));
					}
					
					fileModule.setCompressLevel(rs.getInt("COMPRESSION_LEVEL"));
					fileModule.setMaxSize(rs.getInt("MAX_SIZE"));
					return fileModule;
				}

			});

		} catch (Exception exp) {
			logger.debug(Logging.format("list File Modules", exp, transition));
		}

		return modules;
	}
	
	
	public AppFile save(AppFile file, Transition transition) throws AppException {
		
		StringBuilder query = new StringBuilder();

		String tableName = "file_" + file.getFileModule().getModule().toLowerCase(); 
		try {
			query.append("INSERT INTO ")
				 .append(tableName)
				 .append("( FILE_MODULE_ID, GENERATED_CODE, NAME, TAGS, TYPE ) ")
				 .append("VALUES( :fileModuleId, :generatedCode, :name, :tags, :typeStr ) ");
			
			KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(file);

			int insertedRows = jdbcTemplate.update(query.toString(), namedParameters, generatedKeyHolder);
			
			if (insertedRows > 0) {
				file.setId(generatedKeyHolder.getKey().intValue());
			}
		} catch (Exception exp) {
			logger.debug(Logging.format("Save File, " + file, exp, transition));
		}

		return file;
	}
	
	
	public boolean updateFilesOwner(int ownerId, String filesId, int fileModuleId, Transition transition) throws Exception {
		
		boolean updated = false;
		StringBuilder query = new StringBuilder();
		Map<String, Object> mapParameters = null;
		FileModule module = FileLoader.modulePerId.get(fileModuleId);

		String tableName = "file_" + module.getModule().toLowerCase(); 
		
		try {
			query.append("UPDATE ")
				 .append(tableName)
				 .append(" SET OWNER_ID = :ownerId WHERE DELETED=0 AND ID IN( :filesId )");
			
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("ownerId", ownerId);
			mapParameters.put("filesId", filesId);

			int insertedRows = jdbcTemplate.update(query.toString(), mapParameters);
			
			if (insertedRows > 0) {
				updated = true;
			}
		} catch (Exception exp) {
			logger.debug(Logging.format("Update Files Owner with ID: " + ownerId + ", filesId: " + filesId, exp, transition));
		}

		return updated;
	}

}
