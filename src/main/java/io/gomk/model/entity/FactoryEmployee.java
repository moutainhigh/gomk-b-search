package io.gomk.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 电厂-员工表
 * </p>
 *
 * @author Robinxiao
 * @since 2019-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_factory_employee")
@ApiModel(value="FactoryEmployee对象", description="电厂-员工表")
public class FactoryEmployee implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "真实姓名")
    private String trueName;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "办公地点")
    private String workAddress;

    @ApiModelProperty(value = "入职时间")
    private LocalDateTime joinTime;

    @ApiModelProperty(value = "生日")
    private LocalDateTime birthday;

    @ApiModelProperty(value = "头像")
    private String profilePicture;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "手机")
    private String tel;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "部门编码")
    private String orgCode;

    @ApiModelProperty(value = "班组编码")
    private String teamCode;

    @ApiModelProperty(value = "职位")
    private Integer positionId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty(value = "工厂ID")
    private Long factoryId;

    @ApiModelProperty(value = "状态0正常1禁用")
    private StatusEnum status;


}
