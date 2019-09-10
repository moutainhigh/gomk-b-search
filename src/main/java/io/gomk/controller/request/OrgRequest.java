package io.gomk.controller.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Robinxiao
 * @since 2019-06-08
 */
@Data
@ApiModel(value = "OrgRequest", description = "")
public class OrgRequest {

	@ApiModelProperty(value = "名称")
	@NotBlank
	private String orgName;

	@ApiModelProperty(value = "父级ID")
	@Min(1)
	private Integer parentId;

}
