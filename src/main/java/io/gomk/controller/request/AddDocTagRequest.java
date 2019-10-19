package io.gomk.controller.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddDocTagRequest {
	
	@ApiModelProperty(value = "库范围")
	@NotNull
    private int scope;
	
	@ApiModelProperty(value = "tag名称")
	@NotNull
    private String tag;
	
	@ApiModelProperty(value = "文档ID集合")
	@NotNull
    private List<String> ids;
	
}
