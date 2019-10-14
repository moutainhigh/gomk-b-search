package io.gomk.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ContrastVO {
	@ApiModelProperty(value = "相似度")
	private String similarity;
	@ApiModelProperty(value = "比对内容")
	private String content;
}
