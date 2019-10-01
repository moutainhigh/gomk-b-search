package io.gomk.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_g_tag")
@ApiModel(value="GTag对象", description="")
public class GTag implements Serializable {

private static final long serialVersionUID=1L;

    private Integer id;

    private Integer classifyId;

    private String tagName;


}
