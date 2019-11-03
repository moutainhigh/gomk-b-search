package io.gomk.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 招投标项目维度表
 * </p>
 *
 * @author chen
 * @since 2019-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)

public class DPrjManufacturCost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @TableField("DATE_TIME")
    private String dateTime;

    /**
     * 项目编码
     */
    @TableField("PRJ_CODE")
    private String prjCode;

    /**
     * 项目名称
     */
    @TableField("PRJ_NAME")
    private String prjName;

    /**
     * 中心ID
     */
    @TableField("DEPT2_CODE")
    private String dept2Code;

    /**
     * 中心名称
     */
    @TableField("DEPT2_NAME")
    private String dept2Name;

    /**
     * 部门ID
     */
    @TableField("DEPT3_CODE")
    private String dept3Code;

    /**
     * 部门名称
     */
    @TableField("DEPT3_NAME")
    private String dept3Name;

    /**
     * 行业ID
     */
    @TableField("INDUSTRY_ID")
    private String industryId;

    /**
     * 行业名称
     */
    @TableField("INDUSTRY_NAME")
    private String industryName;

    /**
     * 归属建设项目编码
     */
    @TableField("BUILD_PRJ_ID")
    private String buildPrjId;

    /**
     * 委托日期
     */
    @TableField("ENTRUST_TIME")
    private LocalDate entrustTime;

    /**
     * 要求交付日期
     */
    @TableField("DELIVER_TIME")
    private LocalDate deliverTime;

    /**
     * 合同编码
     */
    @TableField("CONTRACT_ID")
    private String contractId;

    /**
     * 业主证件代码
     */
    @TableField("DOCUMENT_CODE")
    private String documentCode;

    /**
     * 业主证件类型
     */
    @TableField("DOCUMENT_TYPE")
    private String documentType;

    /**
     * 项目负责人
     */
    @TableField("PRJ_LEADER")
    private String prjLeader;

    /**
     * 项目起始日期
     */
    @TableField("PRJ_BEGIN_TIME")
    private LocalDate prjBeginTime;

    /**
     * 项目预计终止日期
     */
    @TableField("PRJ_END_TIME")
    private LocalDate prjEndTime;

    /**
     * 项目交付日期
     */
    @TableField("PRJ_DELIVER_TIME")
    private LocalDate prjDeliverTime;

    /**
     * 项目归档日期
     */
    @TableField("PRJ_FILE_TIME")
    private LocalDate prjFileTime;

    /**
     * 项目类型
     */
    @TableField("PRJ_TYPE")
    private String prjType;

    /**
     * 项目状态
     */
    @TableField("PRJ_STATE")
    private String prjState;

    /**
     * 咨询方式
     */
    @TableField("CONSULTATION_METHOD")
    private String consultationMethod;

    /**
     * 咨询类别
     */
    @TableField("CONSULTATION_TYPE")
    private String consultationType;

    /**
     * 编制金额
     */
    @TableField("ORGANIZATION_AMT")
    private BigDecimal organizationAmt;

    /**
     * 报审金额
     */
    @TableField("REPORT_AMT")
    private BigDecimal reportAmt;

    /**
     * 审定金额
     */
    @TableField("APPROVE_AMT")
    private BigDecimal approveAmt;

    /**
     * 预计收入
     */
    @TableField("ESTIMATE_INCOME")
    private BigDecimal estimateIncome;

    /**
     * 预计成本
     */
    @TableField("EXPECTED_COST")
    private BigDecimal expectedCost;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 项目经理员工号
     */
    @TableField("PM_CODE")
    private String pmCode;

    /**
     * 录入时间即项目立项时间
     */
    @TableField("PRJ_SET_TIME")
    private LocalDate prjSetTime;

    /**
     * 是否暂停
     */
    @TableField("IF_SUSPEND")
    private String ifSuspend;

    /**
     * 合同编码
     */
    @TableField("CONTRACT_CODE")
    private String contractCode;

    /**
     * 是否外委
     */
    @TableField("IF_OUTSOURCED")
    private String ifOutsourced;

    /**
     * 业主名称
     */
    @TableField("DOCUMENT_NAME")
    private String documentName;


}
