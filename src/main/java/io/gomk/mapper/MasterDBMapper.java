package io.gomk.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.gomk.model.entity.DZbPrj;
import io.gomk.task.DBInfoBean;

public interface MasterDBMapper {
//	SELECT 
//	a.syscode,
//	b.title,b.ext,b.store,b.storepath,b.pathname,
//	c.PRJ_NAME,c.PRJ_CODE,c.PKG_WIN_BID_AMT,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE,c.ANNOUNC_DATE,
//	d.INDUSTRY_NAME,d.PRJ_TYPE,d.PRJ_NATURE,d.CUST_NAME
//FROM biz_z_dfile a 
//	inner join biz_z_efile_completed b on a.uuid=b.puuid
//	inner join d_zb_pkg c on a.xmbh=c.pkg_code
//	inner join d_zb_prj d on d.prj_code=c.prj_code
//limit 0, 10

	@Select("select distinct substring_index(item_wlmc, '\\\\', 1) as ss from I_GX_T_PRICE_LIST ")
	public List<String> selectCompletion();
	
	@Select({
		"<script>",
		"SELECT ",
			"	b.uuid, a.bz,a.syscode as fileType,a.wjtm,a.xmbh as prjCode, a.xmmc as prjName, a.hyfl as prjIndustry," ,
			"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName,date_format(b.STOREDATETIME, '%Y-%m-%d %H:%i:%s') as STOREDATETIME ," , 
			"	c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
		    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
			"	d.PRJ_NATURE,d.CUST_NAME as prjCust,d.industry_name as industryName,d.prj_type as prjType,d.if_cent_purchas as ifCentPurchas, d.prj_nature,d.capital_source as capitalSource" ,
			" FROM BIZ_Z_DFILE a inner join " ,
				"	(select * from BIZ_Z_EFILE_COMPLETED where uuid in "
				+ " (select max(uuid) from BIZ_Z_EFILE_COMPLETED group by puuid,title,pathname))"
				+ " b on a.uuid=b.puuid" ,
				"	inner join D_ZB_PKG c on a.xmbh=c.prj_code" ,
				"	inner join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
				" where b.store != 'FAILED' #{sql}  ",
                " order by b.STOREDATETIME limit #{numberSign}, 10",
		"</script>"
	})
	public List<DBInfoBean> getDBInfoByNumber(@Param("numberSign") int numberSign, @Param("sql") String sql);
	
//	@Select({
//		"<script>",
//		"SELECT ",
//			"	b.uuid, a.bz,a.syscode as fileType,a.wjtm,a.xmbh as prjCode, a.xmmc as prjName, a.xmlx as prjType , a.hyfl as prjIndustry," ,
//			"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName,date_format(b.STOREDATETIME, '%Y-%m-%d %H:%i:%s') as STOREDATETIME ," , 
//			"	c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
//		    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
//			"	d.PRJ_NATURE,d.CUST_NAME as prjCust" ,
//			" FROM BIZ_Z_DFILE a inner join " ,
//			"	(select * from BIZ_Z_EFILE_COMPLETED where uuid in "
//					+ " (select max(uuid) from BIZ_Z_EFILE_COMPLETED group by puuid,title,pathname))"
//					+ " b on a.uuid=b.puuid" ,
//				"	left join D_ZB_PKG c on a.xmbh=c.prj_code" ,
//				"	left join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
//				" where b.store != 'FAILED' and a.syscode='ztb'  and a.wjtm ='投标文件' ",
//                " order by b.STOREDATETIME limit #{numberSign}, 10",
//		"</script>"
//	})
//	public List<DBInfoBean> getDBInfoByNumber2(@Param("numberSign") int numberSign);
//	
//	@Select({
//		"<script>",
//		"SELECT ",
//			"	b.uuid, a.bz,a.syscode as fileType,a.wjtm,a.xmbh as prjCode, a.xmmc as prjName, a.xmlx as prjType , a.hyfl as prjIndustry," ,
//			"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName,date_format(b.STOREDATETIME, '%Y-%m-%d %H:%i:%s') as STOREDATETIME ," , 
//			"	c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
//		    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
//			"	d.PRJ_NATURE,d.CUST_NAME as prjCust" ,
//			" FROM BIZ_Z_DFILE a inner join " ,
//			"	(select * from BIZ_Z_EFILE_COMPLETED where uuid in "
//					+ " (select max(uuid) from BIZ_Z_EFILE_COMPLETED group by puuid,title,pathname))"
//					+ " b on a.uuid=b.puuid" ,
//				"	left join D_ZB_PKG c on a.xmbh=c.prj_code" ,
//				"	left join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
//				" where b.store != 'FAILED' and a.syscode='gczj'  and a.bz ='成果文件' ",
//                " order by b.STOREDATETIME limit #{numberSign}, 10",
//		"</script>"
//	})
//	public List<DBInfoBean> getDBInfoByNumber3(@Param("numberSign") int numberSign);
//	

	@Select({
		"<script>",
		"SELECT ",
			"	b.uuid, a.bz,a.syscode as fileType,a.wjtm,a.xmbh as pkjCode, a.xmmc as pkjName, a.xmlx as prjType , a.hyfl as prjIndustry," ,
			"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName,date_format(b.STOREDATETIME, '%Y-%m-%d %H:%i:%s') as STOREDATETIME ," , 
			"	c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
		    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
			"	d.PRJ_NATURE,d.CUST_NAME as prjCust" ,
			" FROM BIZ_Z_DFILE a " ,
				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid" ,
				"	left join D_ZB_PKG c on a.xmbh=c.pkg_code" ,
				"	left join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
				" where b.store != 'FAILED' and  b.STOREDATETIME>#{timeSigns} ",
                " order by b.STOREDATETIME limit 10",
		"</script>"
	})
	List<DBInfoBean> getDBInfo(@Param("timeSigns") String timeSigns);

	@Select({
		"<script>",
		"SELECT ",
			"	b.uuid, a.bz,a.syscode as fileType,a.wjtm,a.xmbh as pkjCode, a.xmmc as pkjName, a.xmlx as prjType , a.hyfl as prjIndustry," ,
			"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName,date_format(b.STOREDATETIME, '%Y-%m-%d %H:%i:%s') as STOREDATETIME ," , 
			"	c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
		    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
			"	d.PRJ_NATURE,d.CUST_NAME as prjCust" ,
			" FROM BIZ_Z_DFILE a " ,
				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid" ,
				"	left join D_ZB_PKG c on a.xmbh=c.pkg_code" ,
				"	left join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
				" where b.store != 'FAILED' ",
                " order by b.STOREDATETIME limit #{numberSign}, 10",
		"</script>"
	})
	public List<DBInfoBean> getDBInfoByNumber(@Param("numberSign") int numberSign);
	
	@Select({
		"<script>",
				"SELECT ",
				"	b.uuid, a.bz, a.syscode as fileType,a.wjtm,a.xmbh as pkjCode, a.xmmc as pkjName, a.xmlx as prjType , a.hyfl as prjIndustry," ,
				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName,date_format(b.STOREDATETIME, '%Y-%m-%d %H:%i:%s') as STOREDATETIME ," , 
				"	c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
			    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
				"	d.PRJ_NATURE,d.CUST_NAME as prjCust" ,
				"  FROM BIZ_Z_DFILE a " ,
					"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid" ,
					"	left join D_ZB_PKG c on a.xmbh=c.pkg_code" ,
					"	left join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
				" where b.store != 'FAILED' and  a.uuid not in ",
				" <foreach item='item' index='index' collection='ids'",
                " 	open='(' separator=',' close=')'>",
                " 	#{item}",
                " </foreach> limit 10",
		"</script>"
	})
	List<DBInfoBean> getTestALLDBInfo(@Param("ids") List<String> ids);

	@Select({
		"<script>",
				"SELECT ",
				"	b.uuid, a.bz, a.syscode as fileType,a.wjtm,a.xmbh as pkjCode, a.xmmc as pkjName, a.xmlx as prjType , a.hyfl as prjIndustry," ,
				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName," , 
				"	c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
			    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
				"	d.PRJ_NATURE,d.CUST_NAME as prjCust" ,
				" FROM BIZ_Z_DFILE a " ,
				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid" ,
				"	left join D_ZB_PKG c on a.xmbh=c.pkg_code" ,
				"	left join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
				" 	where b.store != 'FAILED' and a.bz='成果文件' and a.syscode='gczj'",
		"</script>"
	})
	List<DBInfoBean> getRarAndZipDBInfo();


	@Select("select d.prj_code, d.INDUSTRY_NAME,d.PRJ_TYPE,d.PRJ_NATURE, d.IF_CENT_PURCHAS, d.CAPITAL_SOURCE"
			+ " from BIZ_Z_DFILE a " + 
			" inner join D_ZB_PRJ d on d.prj_code=a.xmbh"
			+ "limit #{i}, #{j}")
	List<DZbPrj> getPageInfo(@Param("i")int i, @Param("j")int j);


	@Select("select d.prj_code, d.INDUSTRY_NAME,d.PRJ_TYPE,d.PRJ_NATURE, d.IF_CENT_PURCHAS, d.CAPITAL_SOURCE"
			+ " from BIZ_Z_DFILE a " + 
			" inner join D_ZB_PRJ d on d.prj_code=a.xmbh"
			+ "limit 0,1")
	DZbPrj getTagInfo(String prjCode);


//	@Select("select from D_ZB_PRJ group by ")
//	Set<String> getTagByClassify11();

	@Select("select industry_name from D_ZB_PRJ group by industry_name")
	Set<String> getTagByClassify12();
	@Select("select prj_type from D_ZB_PRJ group by prj_type")
	Set<String> getTagByClassify13();
	@Select("select case if_cent_purchas when '0' then '否' else '是' end from D_ZB_PRJ group by if_cent_purchas")
	Set<String> getTagByClassify14();
	@Select("select prj_nature from D_ZB_PRJ group by prj_nature")
	Set<String> getTagByClassify15();
	@Select("select capital_source from D_ZB_PRJ group by capital_source")
	Set<String> getTagByClassify16();

	@Select({
		"<script>",
				"SELECT ",
				"	b.uuid, a.bz, a.syscode as fileType,a.wjtm,a.xmbh as pkjCode, a.xmmc as pkjName, a.xmlx as prjType , a.hyfl as prjIndustry," ,
				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName," , 
				"	c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
			    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
				"	d.PRJ_NATURE,d.CUST_NAME as prjCust" ,
				" FROM BIZ_Z_DFILE a " ,
				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid" ,
				"	left join D_ZB_PKG c on a.xmbh=c.pkg_code" ,
				"	left join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
				" 	where b.store != 'FAILED' and a.wjtm='投标文件' and a.syscode='ztb' and ext='pdf'",
		"</script>"
	})
	public List<DBInfoBean> getToubiaoDBInfo();


	
	
	
}
