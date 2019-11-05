package io.gomk.task;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class DBInfoBean {
	private String uuid;
	
	private String wjtm;
	
	private String title;
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
	//文件类型
	private String fileType;
	//文件存储地址 
	private String pathName;
	//文件存储地址 
	private String storeUrl;
	//存储类型
	private String storeType;
	//扩展名
	private String ext;
	//备注 取标识为成果文件 的造价文件
	private String bz;
		
}
