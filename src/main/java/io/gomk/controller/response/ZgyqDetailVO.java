package io.gomk.controller.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ZgyqDetailVO {
	@ApiModelProperty(value = "招标范围")
	private String zbfw;
	@ApiModelProperty(value = "资格要求")
	private List<String> zgyqs;
}
