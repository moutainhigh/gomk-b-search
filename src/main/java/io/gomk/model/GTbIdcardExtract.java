package io.gomk.model;

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
 * 身份证信息抽取（投标文件）
 * </p>
 *
 * @author Robinxiao
 * @since 2019-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_g_tb_idcard_extract")
@ApiModel(value="GTbIdcardExtract对象", description="身份证信息抽取（投标文件）")
public class GTbIdcardExtract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String prjCode;

    private String prjName;

    private String idcardName;

    private String idcardNumber;

    private String uuid;

    private String title;


}
