package io.gomk.model.entity;

import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 专家维度表
 * </p>
 *
 * @author guanhua
 * @since 2019-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DZbExpert对象", description="专家维度表")
public class DZbExpert implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日期")
    @TableField("DATE_TIME")
    private String dateTime;

    @ApiModelProperty(value = "专家编号")
    @TableField("EXPERT_CODE")
    private String expertCode;

    @ApiModelProperty(value = "专家名称")
    @TableField("EXPERT_NAME")
    private String expertName;

    @ApiModelProperty(value = "专家性别")
    @TableField("SEX")
    private String sex;

    @ApiModelProperty(value = "专家通讯地址")
    @TableField("COMM_ADDRESS")
    private String commAddress;

    @ApiModelProperty(value = "专家行业")
    @TableField("EXPERT_INDUST")
    private String expertIndust;

    @ApiModelProperty(value = "是否冻结")
    @TableField("IF_FROZEN")
    private String ifFrozen;

    @ApiModelProperty(value = "出生日期")
    @TableField("BIRTH_DATE")
    private LocalDate birthDate;

    @ApiModelProperty(value = "专家入库时间")
    @TableField("STORAGE_IN_TIME")
    private String storageInTime;

    @ApiModelProperty(value = "专家出库时间")
    @TableField("STORAGE_OUT_TIME")
    private LocalDate storageOutTime;

    @ApiModelProperty(value = "专家冻结时间")
    @TableField("FROZEN_TIME")
    private LocalDate frozenTime;

    @ApiModelProperty(value = "专家当前状态")
    @TableField("CUR_STATE")
    private String curState;

    @ApiModelProperty(value = "专家单位证件类型")
    @TableField("SUPPL_DOCUMENT_TYPE")
    private String supplDocumentType;

    @ApiModelProperty(value = "专家单位证件类型")
    @TableField("SUPPL_DOCUMENT_CODE")
    private String supplDocumentCode;

    @ApiModelProperty(value = "是否集团内标识")
    @TableField("IF_GROUP_IN")
    private String ifGroupIn;

    @ApiModelProperty(value = "地区编号")
    @TableField("DISTRICT")
    private String district;

    @ApiModelProperty(value = "是否退休")
    @TableField("IF_RETIRE")
    private String ifRetire;

    @ApiModelProperty(value = "健康状况")
    @TableField("HEALTH")
    private String health;

    @ApiModelProperty(value = "技术职称")
    @TableField("TECH_TTL")
    private String techTtl;

    @ApiModelProperty(value = "职称获取时间")
    @TableField("TECH_TTL_DATE")
    private String techTtlDate;

    @ApiModelProperty(value = "职务")
    @TableField("POST")
    private String post;

    @ApiModelProperty(value = "联系电话")
    @TableField("CONTACT_NUM")
    private String contactNum;

    @ApiModelProperty(value = "办公电话")
    @TableField("OFFICE_NUM")
    private String officeNum;

    @ApiModelProperty(value = "传真")
    @TableField("FAX")
    private String fax;

    @ApiModelProperty(value = "电子邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty(value = "通讯地址")
    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty(value = "工作起始时间")
    @TableField("WORK_DATE")
    private String workDate;

    @ApiModelProperty(value = "推荐人")
    @TableField("REFEREE")
    private String referee;

    @ApiModelProperty(value = "专家来源方式")
    @TableField("SOURCE_TYPE")
    private String sourceType;

    @ApiModelProperty(value = "所在单位名称")
    @TableField("COMPANY")
    private String company;

    @ApiModelProperty(value = "现工作地点")
    @TableField("WORK_PLACE")
    private String workPlace;

    @ApiModelProperty(value = "开户银行")
    @TableField("BANK")
    private String bank;

    @ApiModelProperty(value = "银行账号")
    @TableField("BANK_NUM")
    private String bankNum;

    @ApiModelProperty(value = "工作简历")
    @TableField("RESUME")
    private String resume;

    @ApiModelProperty(value = "职业资格证书名称")
    @TableField("PROF_QULIF_CERT")
    private String profQulifCert;

    @ApiModelProperty(value = "职业资格获取时间")
    @TableField("PROF_QULIF_CERT_DATE")
    private String profQulifCertDate;

    @ApiModelProperty(value = "邮政编码")
    @TableField("POSTAL_CODE")
    private String postalCode;

    @ApiModelProperty(value = "民族")
    @TableField("NATION")
    private String nation;

    @ApiModelProperty(value = "专家等级")
    @TableField("EXPECT_LVL")
    private String expectLvl;

    @ApiModelProperty(value = "最高学历")
    @TableField("HIGH_EDU")
    private String highEdu;

    @ApiModelProperty(value = "政治面貌")
    @TableField("POLITC")
    private String politc;

    @ApiModelProperty(value = "所在地区")
    @TableField("AREA")
    private String area;

    @ApiModelProperty(value = "专家编号")
    @TableField("EXPERT_NUM")
    private String expertNum;

    @ApiModelProperty(value = "毕业院校")
    @TableField("GRADU_UNIVST")
    private String graduUnivst;

    @ApiModelProperty(value = "所学专业")
    @TableField("MAJOR")
    private String major;

    @ApiModelProperty(value = "是否应急评委")
    @TableField("IF_EMEGY")
    private String ifEmegy;

    @ApiModelProperty(value = "离岗类型")
    @TableField("LEAVE_TYPE")
    private String leaveType;

    @ApiModelProperty(value = "是否长期冻结")
    @TableField("IF_LONG_FROZEN")
    private String ifLongFrozen;

    @ApiModelProperty(value = "离职时间开始")
    @TableField("LEAVE_BEGIN_TIME")
    private LocalDate leaveBeginTime;

    @ApiModelProperty(value = "离职截至时间")
    @TableField("LEAVE_END_TIME")
    private LocalDate leaveEndTime;

    @ApiModelProperty(value = "是否解冻")
    @TableField("IS_ICE_OUT")
    private String isIceOut;


}
