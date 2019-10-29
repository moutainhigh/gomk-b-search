package io.gomk.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 招投标项目维度表
 * </p>
 *
 * @author chen
 * @since 2019-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_zb_prj")
@ApiModel(value="项目", description="")
public class DZbPrj implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @TableField("DATE_TIME")
    private String dateTime;

    /**
     * 项目编号
     */
    @TableField("PRJ_CODE")
    private String prjCode;

    /**
     * 项目名称
     */
    @TableField("PRJ_NAME")
    private String prjName;

    /**
     * 中心编号
     */
    @TableField("DEPT2_CODE")
    private String dept2Code;

    /**
     * 中心名称
     */
    @TableField("DEPT2_NAME")
    private String dept2Name;

    /**
     * 部门编号
     */
    @TableField("DEPT3_CODE")
    private String dept3Code;

    /**
     * 部门名称
     */
    @TableField("DEPT3_NAME")
    private String dept3Name;

    /**
     * 行业名称
     */
    @TableField("INDUSTRY_NAME")
    private String industryName;

    /**
     * 项目所属板块
     */
    @TableField("PRJ_PLATE")
    private String prjPlate;

    /**
     * 项目类型
     */
    @TableField("PRJ_TYPE")
    private String prjType;

    /**
     * 项目性质
     */
    @TableField("PRJ_NATURE")
    private String prjNature;

    /**
     * 业主证件代码
     */
    @TableField("CUST_DOCUMENT_CODE")
    private String custDocumentCode;

    /**
     * 业主证件类型
     */
    @TableField("CUST_DOCUMENT_TYPE")
    private String custDocumentType;

    /**
     * 业主名称
     */
    @TableField("CUST_NAME")
    private String custName;

    /**
     * 项目经理编号
     */
    @TableField("PM_CODE")
    private String pmCode;

    /**
     * 项目经理名称
     */
    @TableField("PM_NAME")
    private String pmName;

    /**
     * 委托函编码
     */
    @TableField("ENTRST_BOOK_CODE")
    private String entrstBookCode;

    /**
     * 委托函名称
     */
    @TableField("ENTRST_BOOK_NAME")
    private String entrstBookName;

    /**
     * 委托金额(元)
     */
    @TableField("ENTRUST_AMT")
    private BigDecimal entrustAmt;

    /**
     * 采购方式
     */
    @TableField("PROCUREMENT_METHOD")
    private String procurementMethod;

    /**
     * 采购策略
     */
    @TableField("PURCHASING_STRATEGY")
    private String purchasingStrategy;

    /**
     * 项目所在省
     */
    @TableField("PRJ_PROVINCE")
    private String prjProvince;

    /**
     * 项目所在市
     */
    @TableField("PRJ_CITY")
    private String prjCity;

    /**
     * 项目所在县
     */
    @TableField("PRJ_COUNTY")
    private String prjCounty;

    /**
     * 项目地址
     */
    @TableField("PRJ_ADDRESS")
    private String prjAddress;

    /**
     * 依法必招
     */
    @TableField("ACCORDANCE_LAW")
    private String accordanceLaw;

    /**
     * 招标组织形式
     */
    @TableField("TENDERING_FORM")
    private String tenderingForm;

    /**
     * 评标办法
     */
    @TableField("EVALUATION_METHOD")
    private String evaluationMethod;

    /**
     * 资金来源
     */
    @TableField("CAPITAL_SOURCE")
    private String capitalSource;

    /**
     * 招标内容
     */
    @TableField("TENDERING_CONTENT")
    private String tenderingContent;

    /**
     * 招标通知书id
     */
    @TableField("TENDERING_NOTICE")
    private String tenderingNotice;

    /**
     * 立项时间
     */
    @TableField("SET_TIME")
    private LocalDate setTime;

    /**
     * 是否集采标识
     */
    @TableField("IF_CENT_PURCHAS")
    private String ifCentPurchas;

    /**
     * 是否电子标标识
     */
    @TableField("IF_ELECTRONIC")
    private String ifElectronic;

    /**
     * 是否线上标标识
     */
    @TableField("IF_ONLINE")
    private String ifOnline;

    /**
     * 是否寄售
     */
    @TableField("IF_CONSIGNMENT")
    private String ifConsignment;

    /**
     * 是否集团管控项目
     */
    @TableField("IF_GROUP_CONTROL")
    private String ifGroupControl;

    /**
     * 是否发出中标通知书标识
     */
    @TableField("IF_SENT_WIN_BID_NOTICE")
    private String ifSentWinBidNotice;

    /**
     * 是否结束标识
     */
    @TableField("IF_END")
    private String ifEnd;

    /**
     * 是否终止标识
     */
    @TableField("IF_STOP")
    private String ifStop;

    /**
     * 是否重大项目
     */
    @TableField("IF_BIG")
    private String ifBig;

    /**
     * 结束类型
     */
    @TableField("END_TYPE")
    private String endType;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    private LocalDate endDate;

    /**
     * 备注
     */
    @TableField("NOTE")
    private String note;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 采购类型
     */
    @TableField("PROCUREMENT_TYPE")
    private String procurementType;


}
