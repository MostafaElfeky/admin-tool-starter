package com.gn4me.app.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gn4me.app.core.dao.ICategoryDao;
import com.gn4me.app.entities.Category;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.ResponseCode;
import com.gn4me.app.entities.filters.ListCategoriesFilter;
import com.gn4me.app.entities.response.GeneralResponse;
import com.gn4me.app.entities.response.ListCategoriesResponse;
import com.gn4me.app.log.Loggable;
import com.gn4me.app.log.Type;

@Service
@Loggable(Type = Type.SERVICCE)
public class CategoryService {
	
	@Autowired
	private ICategoryDao categoryDao;
	
	public GeneralResponse handleListCategories(ListCategoriesFilter listCategoriesFilter, Transition transition) throws Exception
	{
		ListCategoriesResponse response=new ListCategoriesResponse();		
		
		List<Category> categoriesList=categoryDao.listCategories(listCategoriesFilter, transition);
	
		if(categoriesList!=null && categoriesList.size()>0)
		{
			response.setResponseStatus(ResponseCode.SUCCESS);
			response.setCategoriesList(categoriesList);
		}else{
			response.setResponseStatus(ResponseCode.EMPTY_LIST);
		}	
		return response;
	}
}
