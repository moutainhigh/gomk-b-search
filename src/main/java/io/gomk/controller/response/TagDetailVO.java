package io.gomk.controller.response;

import java.util.List;

import io.gomk.model.GTagFormula;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagDetailVO {
	
	@ApiModelProperty(value = "ID")
	private Integer id;

	@ApiModelProperty(value = "名称")
    private String tagName;
	
	@ApiModelProperty(value = "父ID")
	private Integer classifyId;
	
	@ApiModelProperty(value = "规则")
    private Integer rule;
	
	@ApiModelProperty(value = "公式集合")
    private List<GTagFormula> formulas;
	
	@ApiModelProperty(value = "关键词集合")
    private List<String> keywords;
	
	@ApiModelProperty(value = "描述")
    private String tagDesc;
}
