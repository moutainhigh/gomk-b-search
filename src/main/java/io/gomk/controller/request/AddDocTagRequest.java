package io.gomk.controller.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddDocTagRequest {
	
	@ApiModelProperty(value = "库范围1(招标文件库)2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)6(政策法规库)7(招标范本库)8(投标文件库)")
	@NotNull
    private int scope;
	
	@ApiModelProperty(value = "tag名称")
	@NotNull
    private String tag;
	
	@ApiModelProperty(value = "文档ID集合")
	@NotNull
    private List<String> ids;
	
}
