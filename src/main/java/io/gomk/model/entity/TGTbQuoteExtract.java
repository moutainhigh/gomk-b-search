package io.gomk.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分项报价抽取（投标文件）
 * </p>
 *
 * @author nick
 * @since 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TGTbQuoteExtract对象", description="分项报价抽取（投标文件）")
public class TGTbQuoteExtract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("prj_code")
    private String prjCode;

    @TableField("prj_name")
    private String prjName;

    @ApiModelProperty(value = "材料名称")
    @TableField("materials_name")
    private String materialsName;

    @ApiModelProperty(value = "规格型号")
    @TableField("specifications")
    private String specifications;

    @TableField("uuid")
    private String uuid;

    @TableField("title")
    private String title;

    @ApiModelProperty(value = "生产厂商")
    @TableField("vendor")
    private String vendor;

    @ApiModelProperty(value = "数量")
    @TableField("amount")
    private String amount;

    @ApiModelProperty(value = "单价")
    @TableField("unit_price")
    private String unitPrice;

    @ApiModelProperty(value = "总价")
    @TableField("total_price")
    private String totalPrice;


}
