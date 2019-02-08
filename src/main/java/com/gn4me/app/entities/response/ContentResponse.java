package com.gn4me.app.entities.response;

import com.gn4me.app.entities.Content;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ContentResponse extends GeneralResponse {
	
	private Content content;
	
}
