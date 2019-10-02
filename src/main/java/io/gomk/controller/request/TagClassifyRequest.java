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
	
	@ApiModelProperty(value = "范围1(招标文件库)2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)，哪几个库？")
    private List<Integer> scopes;
	
	@ApiModelProperty(value = "描述")
    private String desc;
}
