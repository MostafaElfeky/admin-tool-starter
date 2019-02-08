package com.gn4me.app.entities.response;

import java.util.List;

import com.gn4me.app.entities.SystemReason;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ListSystemReasonResponse extends GeneralResponse {

	private List<SystemReason> reasons;
	
}
