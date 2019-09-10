package io.gomk.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmployeeSimpleVO {
	
	@ApiModelProperty(value = "用户ID")
    private Integer uid;
	
	@ApiModelProperty(value = "用户名")
	private String userName;
	
	@ApiModelProperty(value = "员工ID")
    private Integer employeeId;

    @ApiModelProperty(value = "姓名")
    private String trueName;
}
