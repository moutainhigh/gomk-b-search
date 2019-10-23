package io.gomk.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_g_tag_classify_scope")
@ApiModel(value="GTagClassifyScope对象", description="")
public class GTagClassifyScope implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer classifyId;

    private Integer scopes;


}
