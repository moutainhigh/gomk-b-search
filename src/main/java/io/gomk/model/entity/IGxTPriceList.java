package io.gomk.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
 * @author nick
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="IGxTPriceList对象", description="")
public class IGxTPriceList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("DATA_GUID")
    private String dataGuid;

    @TableField("DATA_SOURCE")
    private String dataSource;

    @TableField("HDIBATCHNO")
    private String hdibatchno;

    @TableField("ETL_TIME")
    private LocalDate etlTime;

    @TableField("ETL_TIME1")
    private LocalDate etlTime1;

    @TableField("BEGIN_DATE")
    private String beginDate;

    @TableField("END_DATE")
    private String endDate;

    @TableField("VAILD_FLAG")
    private String vaildFlag;

    @TableField("PROJECTBH")
    private String projectbh;

    @TableField("BIAODUANBH")
    private String biaoduanbh;

    @TableField("KAIBIAODATE")
    private LocalDateTime kaibiaodate;

    @TableField("ORDERNUM")
    private BigDecimal ordernum;

    @TableField("VENDORBH")
    private String vendorbh;

    @TableField("VENDORNAME")
    private String vendorname;

    @TableField("ITEM_GWC")
    private String itemGwc;

    @TableField("ITEM_SXH")
    private String itemSxh;

    @TableField("ITEM_WLBH")
    private String itemWlbh;

    @TableField("ITEM_WLMC")
    private String itemWlmc;

    @TableField("ITEM_LBBH")
    private String itemLbbh;

    @TableField("ITEM_LBMC")
    private String itemLbmc;

    @TableField("ITEM_DW")
    private String itemDw;

    @TableField("ITEM_YSDJ")
    private BigDecimal itemYsdj;

    @TableField("ITEM_YSZE")
    private BigDecimal itemYsze;

    @TableField("ITEM_BZ")
    private String itemBz;

    @TableField("ITEM_CGCL")
    private String itemCgcl;

    @TableField("ITEM_NBZS")
    private String itemNbzs;

    @TableField("ITEM_XQDW")
    private String itemXqdw;

    @TableField("ITEM_TBDW")
    private String itemTbdw;

    @TableField("ITEM_GCJK")
    private String itemGcjk;

    @TableField("TOUBIAOBAOJIA")
    private BigDecimal toubiaobaojia;

    @TableField("TIJIAOSHULIANG")
    private BigDecimal tijiaoshuliang;

    @TableField("ITEM_JHQXY")
    private String itemJhqxy;

    @TableField("ITEM_HTQDHTS")
    private String itemHtqdhts;

    @TableField("ITEM_DJ1")
    private BigDecimal itemDj1;

    @TableField("ITEM_BZ1")
    private String itemBz1;

    @TableField("ITEM_HL1")
    private BigDecimal itemHl1;

    @TableField("ITEM_JGTJ1")
    private String itemJgtj1;

    @TableField("ITEM_DJ2")
    private BigDecimal itemDj2;

    @TableField("ITEM_BZ2")
    private String itemBz2;

    @TableField("ITEM_HL2")
    private BigDecimal itemHl2;

    @TableField("ITEM_JGTJ2")
    private String itemJgtj2;

    @TableField("ITEM_DJ3")
    private BigDecimal itemDj3;

    @TableField("ITEM_BZ3")
    private String itemBz3;

    @TableField("ITEM_HL3")
    private BigDecimal itemHl3;

    @TableField("ITEM_JGTJ3")
    private String itemJgtj3;

    @TableField("JIAOHUOADDRESSXY")
    private String jiaohuoaddressxy;

    @TableField("SFHANSHUI")
    private String sfhanshui;

    @TableField("SHUILV")
    private String shuilv;

    @TableField("DATAFROM")
    private BigDecimal datafrom;

    @TableField("ID")
    private String id;

    @TableField("UPDATETIME")
    private LocalDate updatetime;

    @TableField("SRM_PR")
    private String srmPr;

    @TableField("SRM_PR_ITEM")
    private String srmPrItem;


}
