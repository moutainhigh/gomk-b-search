package io.gomk.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 电厂-职位
 * </p>
 *
 * @author Robinxiao
 * @since 2019-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_factory_position")
@ApiModel(value="FactoryPosition对象", description="电厂-职位")
public class FactoryPosition implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "code")
    private String code;
    
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "工厂ID")
    private Long factoryId;


}
