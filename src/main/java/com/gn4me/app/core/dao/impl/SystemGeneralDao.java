package com.gn4me.app.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gn4me.app.core.dao.IGeneralDao;
import com.gn4me.app.entities.Category;
import com.gn4me.app.entities.ContentType;
import com.gn4me.app.entities.Gender;
import com.gn4me.app.entities.Section;
import com.gn4me.app.entities.SectionListingContentType;
import com.gn4me.app.entities.SectionPage;
import com.gn4me.app.entities.SystemConfiguration;
import com.gn4me.app.entities.SystemReason;
import com.gn4me.app.entities.SystemStatus;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.ReasonType;
import com.gn4me.app.entities.enums.SystemModuleEnum;
import com.gn4me.app.util.AppException;

@Repository
public class SystemGeneralDao implements IGeneralDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;


	@Override
	public List<SystemStatus> listSytemStatus(SystemModuleEnum module, Transition transition) throws Exception {

		String query = "";
		Map<String, Object> mapParameters = null;
		List<SystemStatus> status = null;

		try {
			
			query = "SELECT SYSTEM_STATUS.* FROM SYSTEM_STATUS WHERE DELETED=0 "+(module == null ? "" : "AND (MODULE = :module OR MODULE = :all) ");
			
			mapParameters = new HashMap<String, Object>();
			if(module != null) {
				mapParameters.put("module", module.name());
				mapParameters.put("all", SystemModuleEnum.ALL.name());
			}

			status = jdbcTemplate.query(query, mapParameters, new RowMapper<SystemStatus>() {

				@Override
				public SystemStatus mapRow(ResultSet rs, int arg1) throws SQLException {
					SystemStatus status = new SystemStatus();
					status.setId(rs.getInt("ID"));
					status.setCode(rs.getString("STATUS_CODE"));
					status.setStatusAr(rs.getString("STATUS_AR"));
					status.setStatusEn(rs.getString("STATUS_EN"));
					return status;
				}

			});

		} catch (Exception exp) {
			throw new AppException("list System status", exp, transition);
		}

		return status;
	}
	
	@Override
	public List<Gender> listGenders(Transition transition) throws Exception {

		String query = "";
		List<Gender> genders = null;

		try {

			query = "SELECT SYSTEM_STATUS.* FROM SYSTEM_STATUS WHERE DELETED=0 ";

			genders = jdbcTemplate.query(query, new RowMapper<Gender>() {

				@Override
				public Gender mapRow(ResultSet rs, int arg1) throws SQLException {
					Gender gender = new Gender();
					gender.setId(rs.getInt("ID"));
					gender.setGenderAr(rs.getString("STATUS_AR"));
					gender.setGenderEn(rs.getString("STATUS_EN"));
					return gender;
				}

			});

		} catch (Exception exp) {
			throw new AppException("list Genders", exp, transition);
		}

		return genders;
	}
	
	@Override
	public List<SystemConfiguration> listSytemConfigurations(Transition transition) throws Exception {

		String query = "";
		List<SystemConfiguration> configurationsList = null;

		try {		
			query = "SELECT * FROM system_configuration WHERE DELETED=0";
		
			configurationsList = jdbcTemplate.query(query, new RowMapper<SystemConfiguration>() {

				@Override
				public SystemConfiguration mapRow(ResultSet rs, int arg1) throws SQLException {
					SystemConfiguration configuration = new SystemConfiguration();
					configuration.setId(rs.getInt("ID"));
					configuration.setKey(rs.getString("KEY"));
					configuration.setValue(rs.getString("GENERAL_VALUE"));
					configuration.setFrontEndDisplay(rs.getBoolean("FRONTEND_DISPLAY"));
					return configuration;
				}
				
			});
		} catch (Exception exp) {
			throw new AppException("list System Configuration", exp, transition);
		}
		return configurationsList;
	}

	@Override
	public List<Section> listSections(Transition transition) throws Exception {
		String query = "";
		List<Section> sectionsList = null;
		
		try{
			query=" select section.ID sectionId,section.NAME sectionName,SECTION_CODE,CATEGORY_ID,CONTENT_TYPE_ID,"
				 +" section_listing_content_types.ID listingId,section_listing_content_types.METHOD_NAME,section_listing_content_types.VIEW_NAME,"
				 +" section_page.ID pageId,section_page.NAME pageName,section_page.PAGE_CODE "
				 +" from section , section_listing_content_types, section_page"
				 +" where section.DELETED=0 and section_listing_content_types.DELETED=0 and section_page.DELETED=0"
				 +" and section.SECTION_PAGE_ID=section_page.ID and section.LISTING_TYPE_ID=section_listing_content_types.ID";
		
				sectionsList=jdbcTemplate.query(query, new RowMapper<Section>() {
					@Override
					public Section mapRow(ResultSet rs, int arg1) throws SQLException {
						// TODO Auto-generated method stub
						Section section =new Section();
						section.setId(rs.getInt("sectionId"));
						section.setName(rs.getString("sectionName"));
						section.setSectionCode(rs.getString("SECTION_CODE"));
						section.setCategoryId(rs.getInt("CATEGORY_ID"));
						section.setContentTypeId(rs.getInt("CONTENT_TYPE_ID"));
						
						SectionListingContentType listingType=new SectionListingContentType();
						listingType.setId(rs.getInt("listingId"));
						listingType.setMethodName(rs.getString("METHOD_NAME"));
						listingType.setViewName(rs.getString("VIEW_NAME"));
						
						SectionPage page=new SectionPage();
						page.setId(rs.getInt("pageId"));
						page.setName(rs.getString("pageName"));
						page.setPageCode(rs.getString("PAGE_CODE"));
						
						section.setSectionListingContentType(listingType);
						section.setSectionPage(page);
						return section;
					}				
				});
		}catch (Exception exp) {
			throw new AppException("list Sections", exp, transition);
		}
		return sectionsList;
	}
	
	@Override
	public List<Category> listCategories(Transition transition) throws Exception {

		String query = "";
		Map<String, Object> mapParameters = null;
		List<Category> categories = null;

		try {
			
			query = "SELECT CATEGORY.* FROM category WHERE DELETED = 0";
			
			categories = jdbcTemplate.query(query, mapParameters, new RowMapper<Category>() {

				@Override
				public Category mapRow(ResultSet rs, int arg1) throws SQLException {
					Category category = new Category();
					category.setId(rs.getInt("ID"));
					category.setName(rs.getString("NAME"));
					category.setCode(rs.getString("CATEGORY_CODE"));
					category.setIcon(rs.getString("ICON"));
					category.setHidden(rs.getBoolean("HIDDEN"));
					category.setShowInMenu(rs.getBoolean("SHOW_IN_MENU"));
					category.setInsertDate(rs.getTimestamp("INSERT_DATE"));
					category.setOrder(rs.getInt("ORDER"));
					return category;
				}

			});

		} catch (Exception exp) {
			throw new AppException("list categories", exp, transition);
		}

		return categories;
	}
	
	@Override
	public List<ContentType> listContentTypes(Transition transition) throws Exception {

		String query = "";
		Map<String, Object> mapParameters = null;
		List<ContentType> types = null;

		try {
			
			query = "SELECT Content_Type.* FROM Content_Type WHERE DELETED = 0";
			
			types = jdbcTemplate.query(query, mapParameters, new RowMapper<ContentType>() {

				@Override
				public ContentType mapRow(ResultSet rs, int arg1) throws SQLException {
					ContentType type = new ContentType();
					type.setId(rs.getInt("ID"));
					type.setName(rs.getString("NAME"));
					type.setCode(rs.getString("TYPE_CODE"));
					type.setInsertDate(rs.getTimestamp("INSERT_DATE"));
					return type;
				}

			});

		} catch (Exception exp) {
			throw new AppException("list content Types", exp, transition);
		}

		return types;
	}

	@Override
	public List<SystemReason> listReasons(ReasonType type, Transition transition) throws Exception {
		
		StringBuilder query = new StringBuilder();
		Map<String, Object> mapParameters = new HashMap<String, Object>();
		List<SystemReason> reasons = null;

		try {
			
			query.append("SELECT SYSTEM_REASON.* FROM SYSTEM_REASON WHERE DELETED=0");
			
			if(type != null) {
				query.append(" AND TYPE= :type ");
				mapParameters.put("type", type.name());
			}
			
			reasons = jdbcTemplate.query(query.toString(), mapParameters, new RowMapper<SystemReason>() {

				@Override
				public SystemReason mapRow(ResultSet rs, int arg1) throws SQLException {
					SystemReason reason = new SystemReason();
					reason.setId(rs.getInt("ID"));
					reason.setType(rs.getString("TYPE"));
					reason.setReason(rs.getString("REASON"));
					return reason;
				}

			});

		} catch (Exception exp) {
			throw new AppException("list System reasons", exp, transition);
		}
		return reasons;
	}
	
	
}
