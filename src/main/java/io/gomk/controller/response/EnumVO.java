package io.gomk.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EnumVO {
	@ApiModelProperty(value = "id")
    private Integer id;
	
	@ApiModelProperty(value = "desc")
    private String desc;
}
