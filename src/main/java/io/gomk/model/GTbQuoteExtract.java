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
 * @since 2019-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_g_tb_quote_extract")
@ApiModel(value="GTbQuoteExtract对象", description="身份证信息抽取（投标文件）")
public class GTbQuoteExtract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String prjCode;

    private String prjName;

    @ApiModelProperty(value = "材料名称")
    private String materialsName;

    @ApiModelProperty(value = "规格型号")
    private String specifications;

    private String uuid;

    private String title;

    @ApiModelProperty(value = "生产厂商")
    private String vendor;

    @ApiModelProperty(value = "数量")
    private String amount;

    @ApiModelProperty(value = "单价")
    private String unitPrice;

    @ApiModelProperty(value = "总价")
    private String totalPrice;


}
