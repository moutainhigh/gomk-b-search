package io.gomk.controller.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagClassifyRequest {
	@ApiModelProperty(value = "名称")
	@NotNull
    private String name;
	
	@ApiModelProperty(value = "父ID，顶级为0")
	@NotNull
    private Integer parentId;
	
	@ApiModelProperty(value = "描述（只在建标签时需要传值）")
    private String desc;
}
