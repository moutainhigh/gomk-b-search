package io.gomk.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormulaVO {
	@ApiModelProperty(value = "字段")
	private String field;
	@ApiModelProperty(value = "运算符")
	private String mark;
	@ApiModelProperty(value = "值")
	private String value;
	
}
