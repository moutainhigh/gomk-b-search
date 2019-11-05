package io.gomk.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行项目_供应商报价数据
 * </p>
 *
 * @author guanhua
 * @since 2019-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("I_ZB_LINE_PRJ_SUPPL")
@ApiModel(value="IZbLinePrjSuppl对象", description="行项目_供应商报价数据")
public class IZbLinePrjSuppl implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日期")
    @TableField("DATE_TIME")
    private String dateTime;

    @ApiModelProperty(value = "唯一标识")
    @TableField("GUID")
    private String guid;

    @ApiModelProperty(value = "物料编码")
    @TableField("MATE_CODE")
    private String mateCode;

    @ApiModelProperty(value = "物料名称")
    @TableField("MATE_NAME")
    private String mateName;

    @ApiModelProperty(value = "标段包编号")
    @TableField("PKG_CODE")
    private String pkgCode;

    @ApiModelProperty(value = "招标人证件代码")
    @TableField("DOCUMENT_CODE")
    private String documentCode;

    @ApiModelProperty(value = "招标人证件类型")
    @TableField("DOCUMENT_TYPE")
    private String documentType;

    @ApiModelProperty(value = "供应商证件代码")
    @TableField("SUPPL_DOCUMENT_CODE")
    private String supplDocumentCode;

    @ApiModelProperty(value = "供应商证件类型")
    @TableField("SUPPL_DOCUMENT_TYPE")
    private String supplDocumentType;

    @ApiModelProperty(value = "数量")
    @TableField("QTY")
    private BigDecimal qty;

    @ApiModelProperty(value = "历史价")
    @TableField("PRICE_HISTORY")
    private BigDecimal priceHistory;

    @ApiModelProperty(value = "单价(元)")
    @TableField("PRICE")
    private BigDecimal price;

    @ApiModelProperty(value = "不含税单价(元)")
    @TableField("PRICE_NO_TAX")
    private BigDecimal priceNoTax;

    @ApiModelProperty(value = "报价金额(元)")
    @TableField("OFFER_AMT")
    private BigDecimal offerAmt;

    @ApiModelProperty(value = "供应商是否中标")
    @TableField("IF_WIN_BID")
    private String ifWinBid;

    @ApiModelProperty(value = "供应商报价时间")
    @TableField("OFFER_TIME")
    private LocalDate offerTime;

    @ApiModelProperty(value = "数据来源")
    @TableField("DATA_SOURCE")
    private String dataSource;

    @ApiModelProperty(value = "抽取时间")
    @TableField("ETL_TIME")
    private LocalDate etlTime;

    @ApiModelProperty(value = "购物车编号")
    @TableField("BUY_CAR_ID")
    private String buyCarId;

    @ApiModelProperty(value = "标段包编号")
    @TableField("PKG_NAME")
    private String pkgName;

    @ApiModelProperty(value = "产品分类编号")
    @TableField("PKG_PD_ID")
    private String pkgPdId;

    @ApiModelProperty(value = "产品分类名称")
    @TableField("PKG_PD_NAME")
    private String pkgPdName;

    @ApiModelProperty(value = "项目经理编号")
    @TableField("PM_CODE")
    private String pmCode;

    @ApiModelProperty(value = "项目经理名称")
    @TableField("PM_NAME")
    private String pmName;

    @ApiModelProperty(value = "供应商物资中标金额")
    @TableField("WIN_AMT")
    private BigDecimal winAmt;

    @ApiModelProperty(value = "供应商中标时间")
    @TableField("WIN_DATE")
    private LocalDate winDate;

    @ApiModelProperty(value = "招标人名称")
    @TableField("DOCUMENT_NAME")
    private String documentName;

    @TableField("DATA_GUID")
    private String dataGuid;

    @TableField("HDIBATCHNO")
    private String hdibatchno;

    @TableField("ETL_TIME1")
    private LocalDate etlTime1;

    @TableField("BEGIN_DATE")
    private String beginDate;

    @TableField("END_DATE")
    private String endDate;

    @TableField("VAILD_FLAG")
    private String vaildFlag;


}
