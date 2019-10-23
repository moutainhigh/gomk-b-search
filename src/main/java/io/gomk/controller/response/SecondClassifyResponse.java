package io.gomk.controller.response;

import java.util.List;

import io.gomk.model.GTagClassify;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SecondClassifyResponse {
	
	@ApiModelProperty(value = "标签分类")
	private GTagClassify classify;
	
	@ApiModelProperty(value = "库范围")
	private List<Integer> scopes;
}
