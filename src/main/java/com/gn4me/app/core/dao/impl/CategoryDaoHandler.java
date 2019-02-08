package com.gn4me.app.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gn4me.app.core.dao.ICategoryDao;
import com.gn4me.app.entities.Category;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.SystemConfigurations;
import com.gn4me.app.entities.filters.ListCategoriesFilter;
import com.gn4me.app.util.AppException;
import com.gn4me.app.util.SystemLoader;

@Repository
public class CategoryDaoHandler implements ICategoryDao{

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Override
	public List<Category> listCategories(ListCategoriesFilter listCategoriesFilter, Transition transition) throws Exception{
		// TODO Auto-generated method stub
		String query="";
		List<Category> categoriesList=null;
		Map<String, Object> mapParameters = null;
		try{
			
			query="select * from category where DELETED=0 and HIDDEN=0 and SHOW_IN_MENU =:inMenu order by category.ORDER desc,INSERT_DATE desc";
			
			mapParameters = new HashMap<String, Object>();
			mapParameters.put("inMenu",listCategoriesFilter.isShowInMenu());
			categoriesList=jdbcTemplate.query(query, mapParameters,new RowMapper<Category>(){

				@Override
				public Category mapRow(ResultSet rs, int arg1) throws SQLException {
					Category category= new Category();
					category.setId(rs.getInt("ID"));
					category.setName(rs.getString("NAME"));
					if(rs.getString("ICON")!=null && !rs.getString("ICON").isEmpty())
					{
						category.setIcon(rs.getString("ICON"));
					}else{
						category.setIcon(SystemLoader.systemConfigurations.get(SystemConfigurations.CATEGORY_ICON_DEFAULT.getValue()));
					}
					category.setAdminId(rs.getInt("ADMIN_ID"));
					category.setOrder(rs.getInt("ORDER"));
					category.setShowInMenu(rs.getBoolean("SHOW_IN_MENU"));
					return category;
				}			
			});
		} catch (Exception exp) {
			throw new AppException("list Categories", exp, transition);
		}
		return categoriesList;
	}

}
