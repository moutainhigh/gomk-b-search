package io.gomk.controller.response;

import java.util.List;

import io.gomk.model.GTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagListResponse {
	
	@ApiModelProperty(value = "TAG列表")
	private List<GTag> tags;
	
	@ApiModelProperty(value = "是否编辑")
	private boolean edit;
}
