package com.gn4me.app.entities.response;

import java.util.List;

import com.gn4me.app.entities.SystemStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ListSystemStatusResponse extends GeneralResponse {

	private List<SystemStatus> statusList;
	
}
