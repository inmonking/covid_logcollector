package com.codivplus.model;

import lombok.Data;

@Data
public class ReqParameterVO {
	private String ServiceKey;
	private String pageNo;
	private String numOfRows;
	private String startCreateDt;
	private String endCreateDt;
}
