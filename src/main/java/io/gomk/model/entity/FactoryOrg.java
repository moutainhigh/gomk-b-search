package io.gomk.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.gomk.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机构部门表
 * </p>
 *
 * @author Robinxiao
 * @since 2019-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_factory_org")
@ApiModel(value="FactoryOrg对象", description="机构部门表")
public class FactoryOrg implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "编码")
    private String orgCode;

    @ApiModelProperty(value = "名称")
    private String orgName;

    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty(value = "子集数量")
    private Integer childAmount;

    @ApiModelProperty(value = "父级")
    private Integer parentId;

    @ApiModelProperty(value = "电厂ID")
    private Long factoryId;
    
    @ApiModelProperty(value = "状态 0正常1禁用2删除")
    private StatusEnum status;


}
