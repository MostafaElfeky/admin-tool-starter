package com.gn4me.app.core.dao;

import java.util.List;

import com.gn4me.app.entities.Category;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.filters.ListCategoriesFilter;

public interface ICategoryDao {
	
	public List<Category> listCategories(ListCategoriesFilter listCategoriesFilter, Transition transition) throws Exception;
}
