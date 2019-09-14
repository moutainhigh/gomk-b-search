package io.gomk.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchResultVO {
	
	@ApiModelProperty(value = "标题")
    private String title;
	
	@ApiModelProperty(value = "正文")
	private String content;
}
