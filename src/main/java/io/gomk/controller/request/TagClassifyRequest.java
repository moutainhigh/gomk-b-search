package io.gomk.controller.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagClassifyRequest {
	@ApiModelProperty(value = "名称")
	@NotNull
    private String name;
	
	@ApiModelProperty(value = "父ID")
	@NotNull
    private Integer parentId;
	
	@ApiModelProperty(value = "描述")
    private String desc;
	
	@ApiModelProperty(value = "库范围集合(建一级时不传)")
    private Set<Integer> scopes;
}
