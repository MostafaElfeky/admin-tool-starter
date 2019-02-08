package com.gn4me.app.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gn4me.app.core.dao.AuthDao;
import com.gn4me.app.entities.Privilege;
import com.gn4me.app.entities.Role;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.entities.enums.ResponseCode;
import com.gn4me.app.entities.enums.SystemStatusEnum;
import com.gn4me.app.entities.response.ResponseStatus;
import com.gn4me.app.util.AppException;
import com.gn4me.app.util.SystemLoader;

@Repository
public class SystemAuthDao implements AuthDao {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	
	@Override
	public boolean save(User user, Transition transition) throws AppException {
		
		
		String query = "";
		boolean inserted = false;
		
		try {
			query = "INSERT INTO admin ( admin.EMAIL, admin.FIRST_NAME, admin.LAST_NAME, admin.IMAGE, admin.USER_PASSWORD, admin.STATUS_ID, admin.AUTO_CREATED, admin.Token, admin.TOKEN_EXPIRY_DATE ) "
				  + "VALUES( :email, :firstName, :lastName, :image, :password, :statusId, :autoCreated, :token, :tokenExpiryDate )";
			
			KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
			
			int insertedRows = jdbcTemplate.update(query, namedParameters,generatedKeyHolder);
			
			if (insertedRows > 0) {
				user.setId(generatedKeyHolder.getKey().intValue());
				inserted = true;
			}
		} catch(DuplicateKeyException exp) {
			throw new AppException(new ResponseStatus(ResponseCode.ALREADY_EXIST), exp, transition);
		} catch (Exception exp) {
			throw new AppException("save admin", exp, transition);
		}

		return inserted;
	}


	@Override
	public User findUserByEmail(String email, Transition transition) throws AppException {
		
		String query = "";
		Map<String, Object> mapParameters = null;
		User user =  null;
		
		try {
			
			query = " SELECT admin.* FROM admin WHERE admin.EMAIL= :email and admin.deleted=0";
			
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("email", email);

			user =  (User) jdbcTemplate.queryForObject(query, mapParameters, new UserMapper());
			
			if(user != null) {
				user.setRoles(getUserRoles(user.getId(), transition));
				user.setStatus(SystemLoader.statusPerId.get(user.getStatusId()));
			}
			
		} catch (Exception exp) {
			throw new AppException("find admin By Email", exp, transition);
		}
		return user;
	}
	
	@Override
	public User findUserById(Transition transition) throws AppException {
		
		String query = "";
		Map<String, Object> mapParameters = null;
		User user =  null;
		
		try {
			
			query = " SELECT admin.* FROM admin WHERE admin.ID= :userId and admin.deleted=0";
			
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("userId", transition.getUserId());

			user =  (User) jdbcTemplate.queryForObject(query, mapParameters, new UserMapper());
			
			if(user != null) {
				user.setRoles(getUserRoles(user.getId(), transition));
				user.setStatus(SystemLoader.statusPerId.get(user.getStatusId()));
			}
			
		} catch (Exception exp) {
			throw new AppException("find admin By ID", exp, transition);
		}
		return user;
	}
	
	public List<Role> getUserRoles(int userId, Transition transition) throws AppException {
		
		String query = "";
		Map<String, Object> mapParameters = null;
		List<Role> roles =  null;
		
		try {
			
			query = " SELECT role.*, privilege.* FROM system_role role" + 
					" JOIN admin_role on role.ID = admin_role.ROLE_ID and admin_role.admin_id = :userId and admin_role.deleted=0" + 
					" LEFT JOIN system_role_privilege role_privilege ON (role.ID = role_privilege.role_id) and role_privilege.deleted =0" + 
					" LEFT JOIN system_privilege privilege on(role_privilege.PRIVILEGE_ID = privilege.ID) and privilege.deleted=0" + 
					" WHERE role.deleted = 0 ";
			
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("userId", userId);
						
			roles =  jdbcTemplate.query(query, mapParameters, new RoleMapper());
			
		} catch (Exception exp) {
			
			throw new AppException("get admin Roles", exp, transition);
		}
		return roles;
	}
	
	@Override
	public boolean updatePassword(String newPassword, Transition transition) throws AppException {
		
		String query = "";
		Map<String, Object> mapParameters = null;
		boolean updated = false;
		
		try {
			
			query = " UPDATE admin SET admin_PASSWORD= :password WHERE admin.ID= :userId AND DELETED=0";
			
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("password", newPassword);
			mapParameters.put("userId", transition.getUserId());
						
			int updatedRows =  jdbcTemplate.update(query, mapParameters);
			
			if(updatedRows > 0) {
				updated = true;
			}
		} catch (Exception exp) {
			throw new AppException("update admin Password", exp, transition);
		}
		return updated;
	}
	

	@Override
	public boolean updateValidationToken(User user, Transition transition) throws AppException {
		
		String query = "";
		Map<String, Object> mapParameters = null;
		boolean updated = false;
		
		try {
			
			query = " UPDATE admin SET admin.Token= :token, admin.TOKEN_EXPIRY_DATE= :expiryDate WHERE admin.EMAIL= :email AND DELETED=0";
			System.out.println("UUID.randomUUID(): " + UUID.randomUUID());
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("email", user.getEmail());
			mapParameters.put("token", user.getToken());
			mapParameters.put("expiryDate", user.getTokenExpiryDate());
						
			int updatedRows =  jdbcTemplate.update(query, mapParameters);
			
			if(updatedRows > 0) {
				updated = true;
			}
		} catch (Exception exp) {
			throw new AppException("update validation Token", exp, transition);
		}
		return updated;
	}
	
	@Override
	public boolean restPassword(String token, String password, Transition transition) throws AppException {
		
		String query = "";
		Map<String, Object> mapParameters = null;
		boolean updated = false;
		
		try {
			
			query = " UPDATE admin SET admin_PASSWORD= :password, admin.TOKEN= :tokenVal WHERE admin.Token= :token AND admin.TOKEN_EXPIRY_DATE > CURRENT_TIMESTAMP AND admin.DELETED=0";
			
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("password", password);
			mapParameters.put("tokenVal", null);
			mapParameters.put("expiryDate", null);
			mapParameters.put("token", token);
						
			int updatedRows =  jdbcTemplate.update(query, mapParameters);
			
			if(updatedRows > 0) {
				updated = true;
			}
		} catch (Exception exp) {
			throw new AppException("Reset admin Password", exp, transition);
		}
		return updated;
	}
	
	@Override
	public boolean activiteAccount(String token, Transition transition) throws AppException {
		String query = "";
		Map<String, Object> mapParameters = null;
		boolean updated = false;
		
		int activeStatus = SystemLoader.statusPerCode.get(SystemStatusEnum.ACTIVE.name()).getId();
		
		try {
			
			query = " UPDATE admin SET STATUS_ID= :activeStatus, admin.TOKEN= :tokenVal WHERE admin.Token= :token AND admin.TOKEN_EXPIRY_DATE > CURRENT_TIMESTAMP AND admin.DELETED=0";
			
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("activeStatus", activeStatus);
			mapParameters.put("tokenVal", null);
			mapParameters.put("expiryDate", null);
			mapParameters.put("token", token);
						
			int updatedRows =  jdbcTemplate.update(query, mapParameters);
			
			if(updatedRows > 0) {
				updated = true;
			}
		} catch (Exception exp) {
			throw new AppException("Activite admin Account", exp, transition);
		}
		return updated;
	}
	
	
	//Get user details
	class UserMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int arg1) throws SQLException {
			User user = new User();
			
			user.setId(rs.getInt("admin.ID"));
			user.setFirstName(rs.getString("admin.FIRST_NAME"));
			user.setLastName(rs.getString("admin.LAST_NAME"));
			user.setEmail(rs.getString("admin.EMAIL"));
			user.setPassword(rs.getString("admin.admin_PASSWORD"));
			user.setStatusId(rs.getInt("admin.STATUS_ID"));
			user.setTrusted(rs.getBoolean("admin.TRUSTED"));
			user.setImage(rs.getString("admin.image"));
			user.setTrusted(rs.getInt("admin.TRUSTED")  == 1 ? true : false );
			user.setTokenExpiryDate(rs.getTimestamp("admin.TOKEN_EXPIRY_DATE"));
			user.setToken(rs.getString("admin.TOKEN"));
			user.setInsertDate(rs.getTimestamp("admin.INSERT_DATE"));
			
			return user;
		}
	}

	//List specific user roles
	class RoleMapper implements ResultSetExtractor<List<Role>> {
		@Override
		public List<Role> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, Role> map = new HashMap<Integer, Role>();
			Role role = null;
			Privilege privilege = null;
			while (rs.next()) {
				
				int roleId = rs.getInt("role.ID");
				int privilegeId = rs.getInt("privilege.ID");
				
				if(map.get(roleId) == null) {
					role = new Role(roleId);
					role.setRoleAr(rs.getString("role.ROLE_AR"));
					role.setRoleEn(rs.getString("role.ROLE_EN"));
					role.setCode(rs.getString("role.ROLE_CODE"));
					role.setInsertDate(rs.getTimestamp("role.INSERT_DATE"));
					role.setPrivileges(new ArrayList<>());
					map.put(roleId, role);
				}
				
				if(privilegeId > 0) {
					privilege = new Privilege();
					privilege.setId(rs.getInt("privilege.ID"));
					privilege.setPrivilegeAr(rs.getString("privilege.PRIVILEGE_AR"));
					privilege.setPrivilegeEn(rs.getString("privilege.PRIVILEGE_EN"));
					privilege.setCode(rs.getString("privilege.PRIV_CODE"));
					privilege.setInsertDate(rs.getTimestamp("privilege.INSERT_DATE"));
					role.getPrivileges().add(privilege);
				}
			}
			return new ArrayList<Role>(map.values());
		}
	}

	


	
}
