package io.gomk.controller.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class addFormulaTagRequest {
	@ApiModelProperty(value = "名称")
	@NotNull
    private String name;
	
	@ApiModelProperty(value = "父ID")
	@NotNull
	private Integer classifyId;
	
	@ApiModelProperty(value = "公式集合")
    private Set<FormulaVO> formulas;
	
	@ApiModelProperty(value = "描述")
    private String desc;
}
