package io.gomk.task;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ESInfoBean {
	//业务表ID
	private String uuid;
	//标题
	private String title;
	//内容
	private String content;
	//招标范围
	private String zbfw;
	//标段编号
	private String pkgCode;
	//标段名称
	private String pkgName;
	//项目编号 
	private String prjCode;
	//项目名称
	private String prjName;
	//项目类型
	private String prjType;
	//项目性质
	private String prjNature;
	//所属行业
	private String prjIndustry;
	//业主名称
	private String prjCust;
	//中标金额
	private BigDecimal winAmount;
	//委托金额
	private BigDecimal entrustAmt;
	//公告（发标）时间 
	private Date noticeDate;
	//招标方式
	private String zbType;
	//造价文件目录树
	private String directoryTree;
	//造价文件-当前文件路径
	private String currentPath;
	//添加日期
	private Date addDate;
	
	
	
}
