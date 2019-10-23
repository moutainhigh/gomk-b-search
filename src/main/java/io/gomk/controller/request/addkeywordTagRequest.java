package io.gomk.controller.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class addkeywordTagRequest {
	@ApiModelProperty(value = "父ID")
	@NotNull
	private Integer classifyId;
	
	@ApiModelProperty(value = "名称")
	@NotNull
    private String name;
	
	@ApiModelProperty(value = "描述")
    private String desc;
	
	@ApiModelProperty(value = "全部关键词")
    private String mustAll;
	
	@ApiModelProperty(value = "完整关键词")
    private String mustFull;
	
	@ApiModelProperty(value = "任意一个关键词")
    private String anyone;
	
	@ApiModelProperty(value = "不包含")
    private String mustNo;
	
	@ApiModelProperty(value = "时间范围")
    private Integer dateRange;
	
	
}
