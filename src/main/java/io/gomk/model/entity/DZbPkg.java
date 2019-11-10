package io.gomk.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 招投标标段维度表
 * </p>
 *
 * @author nick
 * @since 2019-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("D_ZB_PKG")
@ApiModel(value="DZbPkg对象", description="招投标标段维度表")
public class DZbPkg implements Serializable {

private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "日期")
    @TableField("DATE_TIME")
    private String dateTime;

    @ApiModelProperty(value = "标段编号")
    @TableField("PKG_CODE")
    private String pkgCode;

    @ApiModelProperty(value = "标段名称")
    @TableField("PKG_NAME")
    private String pkgName;

    @ApiModelProperty(value = "标段描述")
    @TableField("PKG_DESCRIBE")
    private String pkgDescribe;

    @ApiModelProperty(value = "项目编号")
    @TableField("PRJ_CODE")
    private String prjCode;

    @ApiModelProperty(value = "项目名称")
    @TableField("PRJ_NAME")
    private String prjName;

    @ApiModelProperty(value = "状态")
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value = "中标类型")
    @TableField("WIN_BID_TYPE")
    private String winBidType;

    @ApiModelProperty(value = "总工期")
    @TableField("ALL_TIME")
    private String allTime;

    @ApiModelProperty(value = "委托金额")
    @TableField("ENTRUST_AMT")
    private BigDecimal entrustAmt;

    @ApiModelProperty(value = "招标次数")
    @TableField("TENDERING_CNT")
    private BigDecimal tenderingCnt;

    @ApiModelProperty(value = "公告时间")
    @TableField("ANNOUNC_DATE")
    private LocalDateTime announcDate;

    @ApiModelProperty(value = "重新招标公告日期")
    @TableField("RE_BID_ANNOUNC_DATE")
    private LocalDateTime reBidAnnouncDate;

    @ApiModelProperty(value = "重新招标原因")
    @TableField("RE_BID_CAUSE")
    private String reBidCause;

    @ApiModelProperty(value = "资审公告日期")
    @TableField("QULIF_EXAM_ANNOUNC_DATE")
    private LocalDateTime qulifExamAnnouncDate;

    @ApiModelProperty(value = "标段包创建时间")
    @TableField("SET_TIME")
    private LocalDateTime setTime;

    @ApiModelProperty(value = "预计开标时间（第一次）")
    @TableField("EXPECT_OPEN_BID_DATE")
    private LocalDateTime expectOpenBidDate;

    @ApiModelProperty(value = "开标时间（最后一次）")
    @TableField("REAL_OPEN_BID_DATE")
    private LocalDateTime realOpenBidDate;

    @ApiModelProperty(value = "评标结果发送招标人时间")
    @TableField("EVAL_BID_RLT_SEND_TIME")
    private LocalDateTime evalBidRltSendTime;

    @ApiModelProperty(value = "招标人返回中标确认函时间")
    @TableField("EVAL_BID_RLT_CALLBACK_TIME")
    private LocalDateTime evalBidRltCallbackTime;

    @ApiModelProperty(value = "评标时间")
    @TableField("EVAL_BID_DATE")
    private LocalDateTime evalBidDate;

    @ApiModelProperty(value = "评标天数")
    @TableField("EVAL_BID_DAYS")
    private BigDecimal evalBidDays;

    @ApiModelProperty(value = "中标候选人公示日期")
    @TableField("WIN_BID_CADI_PUBL_DATE")
    private LocalDateTime winBidCadiPublDate;

    @ApiModelProperty(value = "中标通知书发出日期")
    @TableField("WIN_BID_NOTICE_SEND_DATE")
    private LocalDateTime winBidNoticeSendDate;

    @ApiModelProperty(value = "开评标地点")
    @TableField("OPEN_BID_PLACE")
    private String openBidPlace;

    @ApiModelProperty(value = "谈判方式")
    @TableField("NEGO_TYPE")
    private String negoType;

    @ApiModelProperty(value = "谈判日期")
    @TableField("NEGO_DATE")
    private LocalDateTime negoDate;

    @ApiModelProperty(value = "谈判地点")
    @TableField("NEGO_PLACE")
    private String negoPlace;

    @ApiModelProperty(value = "结果通知书发出日期")
    @TableField("RESULT_NOTICE_SEND_DATE")
    private LocalDateTime resultNoticeSendDate;

    @ApiModelProperty(value = "招标终止节点")
    @TableField("STOP_STEP")
    private String stopStep;

    @ApiModelProperty(value = "招标流程结束节点")
    @TableField("END_STEP")
    private String endStep;

    @ApiModelProperty(value = "招标流程结束原因")
    @TableField("END_REASON")
    private String endReason;

    @ApiModelProperty(value = "标段包总价中标金额")
    @TableField("PKG_WIN_BID_AMT")
    private BigDecimal pkgWinBidAmt;

    @ApiModelProperty(value = "发出中标通知书数量")
    @TableField("WIN_BID_NOTICE_QTY")
    private BigDecimal winBidNoticeQty;

    @ApiModelProperty(value = "当前状态时间")
    @TableField("CUR_STATE_TIME")
    private LocalDateTime curStateTime;

    @ApiModelProperty(value = "是否发出中标通知书标识")
    @TableField("IF_SENT_WIN_BID_NOTICE")
    private String ifSentWinBidNotice;

    @ApiModelProperty(value = "是否终止标识")
    @TableField("IF_STOP")
    private String ifStop;

    @ApiModelProperty(value = "是否正常完成标识")
    @TableField("IF_NORMAL_END")
    private String ifNormalEnd;

    @ApiModelProperty(value = "是否结束标识")
    @TableField("IF_END")
    private String ifEnd;

    @ApiModelProperty(value = "是否变更开标")
    @TableField("IF_CHANGE_OPEN_BID")
    private String ifChangeOpenBid;

    @ApiModelProperty(value = "是否变更中标")
    @TableField("IF_CHANGE_WIN_BID")
    private String ifChangeWinBid;

    @ApiModelProperty(value = "是否重新招标标识")
    @TableField("IF_RE_BID")
    private String ifReBid;

    @ApiModelProperty(value = "是否无中标人")
    @TableField("IF_NO_BIDDER")
    private String ifNoBidder;

    @ApiModelProperty(value = "是否变更采购方式")
    @TableField("IF_CHANGE_PUR_TYPE")
    private String ifChangePurType;

    @ApiModelProperty(value = "是否无中标候选人")
    @TableField("IF_NO_BIDDER_CADI")
    private String ifNoBidderCadi;

    @ApiModelProperty(value = "结束类型")
    @TableField("END_TYPE")
    private String endType;

    @ApiModelProperty(value = "结束日期")
    @TableField("END_DATE")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "终止原因")
    @TableField("STOP_REASON")
    private String stopReason;

    @ApiModelProperty(value = "终止日期")
    @TableField("STOP_DATE")
    private LocalDateTime stopDate;

    @ApiModelProperty(value = "数据来源")
    @TableField("DATA_SOURCE")
    private String dataSource;

    @ApiModelProperty(value = "招标方式_G-公开招标-Q-邀请招标-D-单一来源-J-竞争性谈判-GJ-公开竞价-QJ-邀请竞价-99-非招标")
    @TableField("BID_TYPE")
    private String bidType;

    @ApiModelProperty(value = "招标方式_G-公开招标-Q-邀请招标-D-单一来源-J-竞争性谈判-GJ-公开竞价-QJ-邀请竞价-99-非招标")
    @TableField("BID_TYPE_CODE")
    private String bidTypeCode;


}
