package io.gomk.model.entity;

import java.time.LocalDate;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author guanhua
 * @since 2019-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ShzjProductpriceNew对象", description="")
public class ShzjProductpriceNew implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("belongxiaqucode")
    private String belongxiaqucode;

    @TableField("operateusername")
    private String operateusername;

    @TableField("operatedate")
    private LocalDate operatedate;

    @ApiModelProperty(value = "yearflag为2：广材网同步；yearflag为1：招投标同步；yearflag为空：手工录入")
    @TableField("yearflag")
    private String yearflag;

    @ApiModelProperty(value = "唯一标识")
    @TableField("rowguid")
    private String rowguid;

    @ApiModelProperty(value = "产品编码")
    @TableField("product_code")
    private String productCode;

    @ApiModelProperty(value = "产品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty(value = "厂商/供货商名称")
    @TableField("supplier_name")
    private String supplierName;

    @ApiModelProperty(value = "厂商/供货商所在地")
    @TableField("supplier_location")
    private String supplierLocation;

    @ApiModelProperty(value = "联系电话")
    @TableField("contact")
    private String contact;

    @ApiModelProperty(value = "计量单位")
    @TableField("measurementunit")
    private String measurementunit;

    @ApiModelProperty(value = "单价")
    @TableField("unitprice")
    private String unitprice;

    @ApiModelProperty(value = "价格提供人")
    @TableField("provider")
    private String provider;

    @ApiModelProperty(value = "采集时间")
    @TableField("collectdate")
    private LocalDate collectdate;

    @ApiModelProperty(value = "采集来源")
    @TableField("collectsource")
    private String collectsource;

    @ApiModelProperty(value = "更新人")
    @TableField("updateuser")
    private String updateuser;

    @ApiModelProperty(value = "更新时间")
    @TableField("updatedate")
    private LocalDate updatedate;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private Blob remark;

    @ApiModelProperty(value = "备用字段1")
    @TableField("attrib1")
    private String attrib1;

    @ApiModelProperty(value = "备用字段2")
    @TableField("attrib2")
    private String attrib2;

    @ApiModelProperty(value = "备用字段3")
    @TableField("attrib3")
    private String attrib3;

    @ApiModelProperty(value = "产品属性")
    @TableField("properties")
    private String properties;

    @ApiModelProperty(value = "除税单价")
    @TableField("unitnsprice")
    private String unitnsprice;

    @ApiModelProperty(value = "物料编码")
    @TableField("wl_no")
    private String wlNo;

    @TableField("clcateguid")
    private String clcateguid;

    @ApiModelProperty(value = "是否为模板")
    @TableField("ismuban")
    private String ismuban;


}
