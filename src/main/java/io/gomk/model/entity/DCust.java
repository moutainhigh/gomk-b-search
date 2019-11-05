package io.gomk.model.entity;

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
 * 业主维度表
 * </p>
 *
 * @author guanhua
 * @since 2019-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("D_CUST")
@ApiModel(value="DCust对象", description="业主维度表")
public class DCust implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日期")
    @TableField("DATE_TIME")
    private String dateTime;

    @ApiModelProperty(value = "id")
    @TableField("ID")
    private String id;

    @ApiModelProperty(value = "统一社会信用代码/机构代码")
    @TableField("CUST_DOCUMENT_CODE")
    private String custDocumentCode;

    @ApiModelProperty(value = "业主证件类型")
    @TableField("CUST_DOCUMENT_TYPE")
    private String custDocumentType;

    @ApiModelProperty(value = "业主名称")
    @TableField("CUST_NAME")
    private String custName;

    @ApiModelProperty(value = "单位名称简称")
    @TableField("CUST_SHORT_NAME")
    private String custShortName;

    @ApiModelProperty(value = "所属板块")
    @TableField("CUST_BLK")
    private String custBlk;

    @ApiModelProperty(value = "参照国家标准")
    @TableField("UNIT_NATURE")
    private String unitNature;

    @ApiModelProperty(value = "集团内单位,1是0否")
    @TableField("GROUP_UNIT")
    private String groupUnit;

    @ApiModelProperty(value = "集团内单位代码,1是0否")
    @TableField("GROUP_UNIT_CODE")
    private String groupUnitCode;

    @ApiModelProperty(value = "大陆外单位1是0否")
    @TableField("EXT_UNIT")
    private String extUnit;

    @ApiModelProperty(value = "大陆外单位1是0否")
    @TableField("EXT_UNIT_CODE")
    private String extUnitCode;

    @ApiModelProperty(value = "集团板块")
    @TableField("INDUSTRY_GROUP")
    private String industryGroup;

    @ApiModelProperty(value = "集团板块代码")
    @TableField("INDUSTRY_GROUP_CODE")
    private String industryGroupCode;

    @ApiModelProperty(value = "省")
    @TableField("PROVINCES")
    private String provinces;

    @ApiModelProperty(value = "省代码")
    @TableField("PROVINCES_CODE")
    private String provincesCode;

    @ApiModelProperty(value = "市")
    @TableField("CITY")
    private String city;

    @ApiModelProperty(value = "所在城市代码")
    @TableField("CITY_CODE")
    private String cityCode;

    @ApiModelProperty(value = "县/区")
    @TableField("COUNTY")
    private String county;

    @ApiModelProperty(value = "所在区/县代码")
    @TableField("COUNTY_CODE")
    private String countyCode;

    @ApiModelProperty(value = "上级id")
    @TableField("PID")
    private String pid;

    @ApiModelProperty(value = "上级单位名称")
    @TableField("P_CUST_NAME")
    private String pCustName;

    @ApiModelProperty(value = "发票类型/zp增值税专用发票，pp增值税普通发票，dp电子发票")
    @TableField("INVOICE_TYPE")
    private String invoiceType;

    @ApiModelProperty(value = "纳税人识别号")
    @TableField("TAXPAYER_NUM")
    private String taxpayerNum;

    @ApiModelProperty(value = "地址、电话")
    @TableField("ADDR_AND_TEL")
    private String addrAndTel;

    @ApiModelProperty(value = "开户行及账号")
    @TableField("BANK_AND_ACCOUNT")
    private String bankAndAccount;

    @ApiModelProperty(value = "财务客户号（mdmcode）")
    @TableField("MDMCODE")
    private String mdmcode;

    @ApiModelProperty(value = "srm码")
    @TableField("SRMCODE")
    private String srmcode;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDate createTime;

    @ApiModelProperty(value = "最后修改时间")
    @TableField("UPDATE_TIME")
    private LocalDate updateTime;

    @ApiModelProperty(value = "停用/启用")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "标志")
    @TableField("FLAG")
    private String flag;

    @ApiModelProperty(value = "客户层级路径")
    @TableField("LEVELPATH")
    private String levelpath;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;


}
