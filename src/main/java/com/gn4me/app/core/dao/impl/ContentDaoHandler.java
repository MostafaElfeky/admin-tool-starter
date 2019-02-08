package com.gn4me.app.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gn4me.app.core.dao.IContentDao;
import com.gn4me.app.entities.Content;
import com.gn4me.app.entities.ContentStatistic;
import com.gn4me.app.entities.ContentVideo;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.entities.UserSavedContent;
import com.gn4me.app.entities.enums.SystemStatusEnum;
import com.gn4me.app.entities.filters.ContentFilter;
import com.gn4me.app.entities.filters.StatusEditFilter;
import com.gn4me.app.file.FileUtil;
import com.gn4me.app.file.entities.AppFile;
import com.gn4me.app.util.AppException;
import com.gn4me.app.util.SystemLoader;

@Repository
public class ContentDaoHandler implements IContentDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	FileUtil fileUtil;
	

	@Override
	public List<Content> listContent(ContentFilter filter, Transition transition) throws Exception {

		List<Content> contentList = null;
		SqlParameterSource namedParameters = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		try {
			
			queryBuilder.append(" SELECT * FROM CONTENT c ")
						.append(" LEFT JOIN USER user ON(user.ID = c.USER_ID AND user.DELETED = 0) ")
						.append(" LEFT JOIN ADMIN admin ON(admin.ID = c.ADMIN_ID AND admin.DELETED = 0) ")
						.append(" LEFT JOIN CONTENT_VIDEO video ON (video.CONTENT_ID = c.ID AND video.DELETED = 0) ")
						.append(" LEFT JOIN FILE_CONTENT file ON (file.OWNER_ID = c.ID AND file.DELETED = 0) ")
						.append(" WHERE c.DELETED = 0 ");

			if(filter.getKey() != null) {
                queryBuilder.append(" AND c.TITLE LIKE :key ");
            }
            
            if(filter.getStartDate() != null) {
                queryBuilder.append(" AND date(c.INSERT_DATE) >= :startDate ");
            }
            
            if(filter.getEndDate() != null) {
                queryBuilder.append(" AND  date(c.INSERT_DATE) <= :endDate ");
            }
       
            if(filter.getStatusId() > 0 ) {
                queryBuilder.append(" AND c.STATUS_ID = :statusId ");
            }
            
            if(filter.getCategoryId() > 0) {
            	queryBuilder.append(" AND c.CATEGORY_ID = :categoryId ");
            }
            
            if(filter.getContentTypeId() > 0) {
            	queryBuilder.append(" AND c.CONTENT_TYPE_ID = :contentTypeId ");
            }
            
            if(filter.isFeatured()) {
            	queryBuilder.append(" AND c.FEATURED = :featured ");
            }
            
            queryBuilder.append(" ORDER BY c.ID DESC");
            
            if(filter.getCount() > 0 ) {
                queryBuilder.append(" Limit :start , :count ");
            }
			
            namedParameters = new BeanPropertySqlParameterSource(filter);

			contentList = jdbcTemplate.query(queryBuilder.toString(), namedParameters, new ContentMapper(filter, transition)); 
			
		} catch (Exception exp) {
			throw new AppException("list Content", exp, transition);
		}
		
		return contentList;
	}
	
	@Override
	public int countContent(ContentFilter filter, Transition transition) throws Exception {

		int totalCount = 0;
		SqlParameterSource namedParameters = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		try {
			
			queryBuilder.append(" SELECT count(*) AS TOTAL_COUNT FROM CONTENT c JOIN USER user ON(user.ID = c.USER_ID OR user.ID = c.ADMIN_ID) ")
						.append(" LEFT JOIN CONTENT_VIDEO video ON (video.CONTENT_ID = c.ID AND video.DELETED = 0)")
						.append(" LEFT JOIN FILE_CONTENT file ON (file.OWNER_ID = c.ID AND file.DELETED = 0)")
						.append(" WHERE c.DELETED = 0 AND user.DELETED = 0");
			
			if(filter.getKey() != null) {
                queryBuilder.append(" AND c.TITLE LIKE :key ");
            }
            
            if(filter.getStartDate() != null) {
                queryBuilder.append(" AND date(c.INSERT_DATE) >= :startDate ");
            }
            
            if(filter.getEndDate() != null) {
                queryBuilder.append(" AND date(c.INSERT_DATE) <= :endDate ");
            }
       
            if(filter.getStatusId() > 0 ) {
                queryBuilder.append(" AND c.STATUS_ID = :statusId ");
            }
            
            if(filter.getCategoryId() > 0) {
            	queryBuilder.append(" AND c.CATEGORY_ID = :categoryId ");
            }
            
            if(filter.getContentTypeId() > 0) {
            	queryBuilder.append(" AND c.CONTENT_TYPE_ID = :contentTypeId ");
            }
            
            if(filter.isFeatured()) {
            	queryBuilder.append(" AND c.FEATURED = :featured ");
            }
            
			
            namedParameters = new BeanPropertySqlParameterSource(filter);

			totalCount = jdbcTemplate.query(queryBuilder.toString(), namedParameters, new ResultSetExtractor<Integer>() {
                @Override
                public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Integer count = 0;
                    if(rs.next()) {
                        count = rs.getInt("TOTAL_COUNT");
                    }
                    return count;
                }
            });
			
		} catch (Exception exp) {
			throw new AppException("list Content", exp, transition);
		}
		
		return totalCount;
	}
	
	private Content getContentActions(Content content, ContentFilter filter, Transition transition) {
		
		String queryAction = "select  action.ACTION_CODE FROM system_action action, content_action "
				+ " where action.DELETED=0 and content_action.DELETED=0 "
				+ " and content_action.ACTION_ID=action.ID and content_action.USER_ID=:userId and content_action.CONTENT_ID=:contentId";

		Map<String, Object> actionMapParameters = new HashMap<String, Object>();
		actionMapParameters.put("userId", filter.getUserId());
		actionMapParameters.put("contentId", content.getId());

		String userAction = jdbcTemplate.queryForObject(queryAction, actionMapParameters, String.class);
		content.setUserActionCode(userAction);
		
		// Going to check if user save this content or not
		queryAction = "select * from user_saved_content "
				+ " where USER_ID=:userId and CONTENT_ID=:contentId and DELETED=0";
		UserSavedContent savedContent = jdbcTemplate.queryForObject(queryAction, actionMapParameters,
				UserSavedContent.class);
		if (savedContent != null) {
			content.setSavedStatus(true);
		}
		
		return content;
	}
	
	private Content getContentStatistics(Content content, Transition transition) {
		
		List<ContentStatistic> contentStatisticsesList = null;
		Map<String, Object> mapParameters = new HashMap<String, Object>();
		HashMap<String, Integer> contentStatisticsesMap = new HashMap<String, Integer>();
		
		String queryStat = "select * from content_statistics where CONTENT_ID=:contentId and DELETED=0 order by INSERT_DATE DESC";
		
		mapParameters.put("contentId", content.getId());

		contentStatisticsesList = jdbcTemplate.query(queryStat, mapParameters, new RowMapper<ContentStatistic>() {
											
										@Override
										public ContentStatistic mapRow(ResultSet rs, int arg1) throws SQLException {
											// TODO Auto-generated method stub
											ContentStatistic statistic = new ContentStatistic();
											statistic.setStatName(rs.getString("STAT_NAME"));
											statistic.setCount(rs.getInt("COUNT"));
											return statistic;
										}
								  });
		
		for (ContentStatistic contentStat : contentStatisticsesList) {
			contentStatisticsesMap.put(contentStat.getStatName(), contentStat.getCount());
		}
		
		content.setContentStatistics(contentStatisticsesMap);
		
		return content;
	}
	
	@Override
	public Content save(Content content, Transition transition) throws Exception {
		
		String query = "";
		
		try {
			query = "INSERT INTO content ( CONTENT_TYPE_ID, CATEGORY_ID, TEXT, TITLE, STATUS_ID, FEATURED, PUBLISH_DATE, ADMIN_ID ) "
				  + "VALUES ( :contentTypeId, :categoryId, :text, :title, :statusId, :featured, :publishDate, :adminId )";
			
			KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(content);
			
			int insertedRows = jdbcTemplate.update(query, namedParameters,generatedKeyHolder);
			
			if (insertedRows > 0) {
				content.setId(generatedKeyHolder.getKey().intValue());
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new AppException("save content ", exp, transition);
		}

		return content;
	}
	
	@Override
	public ContentVideo saveVedio(ContentVideo vedio, Transition transition) throws Exception {
		
		String query = "";
		
		try {
			query = "INSERT INTO content_video (VIDEO_ID, TITLE, DESCRIPTION, THUMBNAIL, NO_OF_VIEWS, PUBLISH_DATE, CONTENT_ID)" 
				  +	"VALUES (:videoId, :title, :description, :thumbnail, :noOfViews, :publishDate, :contentId)";
			
			KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(vedio);
			
			int insertedRows = jdbcTemplate.update(query, namedParameters,generatedKeyHolder);
			
			if (insertedRows > 0) {
				vedio.setId(generatedKeyHolder.getKey().intValue());
			}
		} catch (Exception exp) {
			throw new AppException("save content ", exp, transition);
		}

		return vedio;
	}
	
	
	@Override
	public boolean editContentFeatured(int contentId, boolean featured, Transition transition) throws Exception {
		
		String query = "";
		boolean updated = false;
		Map<String, Object> mapParameters = new HashMap<>();
		
		try {
			query = "UPDATE CONTENT SET FEATURED = :featured WHERE ID= :contentId AND DELETED=0";
			
			mapParameters.put("contentId", contentId);
			mapParameters.put("featured", featured);
			
			int updatedRows = jdbcTemplate.update(query, mapParameters);
			
			if (updatedRows > 0) {
				updated = true;
			}
		} catch (Exception exp) {
			throw new AppException("update content featured ", exp, transition);
		}

		return updated;
	}
	
	@Override
	public boolean deleteContent(int contentId, Transition transition) throws Exception {
		
		String query = "";
		boolean updated = false;
		Map<String, Object> mapParameters = new HashMap<>();
		
		try {
			query = "UPDATE CONTENT SET DELETED = 1 WHERE ID= :contentId ";
			
			mapParameters.put("contentId", contentId);
			
			int updatedRows = jdbcTemplate.update(query, mapParameters);
			
			if (updatedRows > 0) {
				updated = true;
			}
		} catch (Exception exp) {
			throw new AppException("update content featured ", exp, transition);
		}

		return updated;
	}
	
	@Override
	public boolean editStatus(Content content, StatusEditFilter filter, Transition transition) throws Exception {
		
		boolean updated = false;
		StringBuilder query = new StringBuilder();
		Map<String, Object> mapParameters = new HashMap<>();
		
		try {
			query.append("UPDATE CONTENT SET STATUS_ID = :statusId ");
			
			if(filter.isWithPublishDate()) {
				query.append(" ,PUBLISH_DATE= :publishDate ");
				mapParameters.put("publishDate", content.getPublishDate());
			}
			
			if(filter.isWithReason()) {
				query.append(" ,REASON_ID= :reasonId ");
				mapParameters.put("reasonId", content.getReasonId());
			}
				 
			query.append(" WHERE ID= :contentId AND DELETED=0");
			
			mapParameters.put("contentId", content.getId());
			mapParameters.put("statusId", content.getStatusId());
			
			int updatedRows = jdbcTemplate.update(query.toString(), mapParameters);
			
			if (updatedRows > 0) {
				updated = true;
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new AppException("update content featured ", exp, transition);
		}

		return updated;
	}
	
	// Mappers Start here...
	
	class ContentMapper implements RowMapper<Content> {

		ContentFilter filter; 
		Transition transition;
		
		ContentMapper(ContentFilter filter, Transition transition) {
			this.filter = filter;
			this.transition = transition;
		}
		
		@Override
		public Content mapRow(ResultSet rs, int arg1) throws SQLException {
			Content content = new Content();
			content.setId(rs.getInt("c.ID"));
			content.setText(rs.getString("c.TEXT"));
			content.setTitle(rs.getString("c.TITLE"));
			content.setPublishDate(rs.getTimestamp("c.PUBLISH_DATE"));
			content.setInsertDate(rs.getTimestamp("c.INSERT_DATE"));
			content.setAdminId(rs.getInt("c.ADMIN_ID"));
			content.setUserId(rs.getInt("c.USER_ID"));
			content.setContentTypeId(rs.getInt("c.CONTENT_TYPE_ID"));
			content.setCategoryId(rs.getInt("c.CATEGORY_ID"));
			content.setFeatured(rs.getBoolean("c.FEATURED"));
			content.setStatusId(rs.getInt("c.STATUS_ID"));
			content.setReasonId(rs.getInt("c.REASON_ID"));
			
			//SET file content (video or Image)
			int imageId = rs.getInt("file.ID");
			int videoId = rs.getInt("video.ID");

			if (videoId > 0) {
				ContentVideo video = new ContentVideo();
				video.setId(videoId);
				video.setVideoId(rs.getString("video.VIDEO_ID"));
				video.setDescription(rs.getString("video.DESCRIPTION"));
				video.setThumbnail(rs.getString("video.THUMBNAIL"));
				video.setNoOfViews(rs.getInt("video.NO_OF_VIEWS"));
				
				content.setVideo(video);
			} else if(imageId > 0) {
				
				AppFile file=new AppFile();
				file.setId(imageId);
				file.setFileModuleId(rs.getInt("file.FILE_MODULE_ID"));
				file.setGeneratedCode(rs.getString("file.GENERATED_CODE"));
				file.setTags(rs.getString("file.TAGS"));
				file.setName(rs.getString("file.NAME"));
				
				ArrayList<AppFile> images = new ArrayList<>();
				images.add(fileUtil.getDownloadFileInfo(file, transition));
				content.setImages(images);
			}
			
			String userType = "user";
			if(content.getAdminId() > 0) {
				content.setAdminUser(true);
				userType = "admin";
			}

			User user = new User();
			user.setId(rs.getInt(userType + ".ID"));
			user.setFirstName(rs.getString(userType + ".FIRST_NAME"));
			user.setLastName(rs.getString(userType + ".LAST_NAME"));
			user.setEmail(rs.getString(userType + ".EMAIL"));
			user.setStatusId(rs.getInt(userType + ".STATUS_ID"));
			user.setTrusted(rs.getBoolean(userType + ".TRUSTED"));
			user.setImage(rs.getString(userType + ".image"));
			user.setInsertDate(rs.getTimestamp(userType + ".INSERT_DATE"));
			
			if (content.isAdminUser()) { content.setAdmin(user); } else { content.setUser(user); }
			
			content.setContentType(SystemLoader.contentTypePerId.get(content.getContentTypeId()));
			content.setCategory(SystemLoader.categoryPerId.get(content.getCategoryId()));
			content.setStatus(SystemLoader.statusPerId.get(content.getStatusId()));
			content.setReason(SystemLoader.reasonsPerId.get(content.getReasonId()));
			
			if(content.getStatus().getCode().equals(SystemStatusEnum.ACTIVE.name())) {
				content.setPublished(true);
			}

			return content;
		}
	}

	
	
}
