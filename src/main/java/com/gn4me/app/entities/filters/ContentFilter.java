package com.gn4me.app.entities.filters;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper=true)
public class ContentFilter extends SearchFilter {
	
	private int contentTypeId;
	private int categoryId;
	private boolean featured;

}
