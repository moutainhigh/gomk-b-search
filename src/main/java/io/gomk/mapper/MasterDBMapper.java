package io.gomk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

	
	@Select({
		"<script>",
				"SELECT ",
				"	a.uuid, a.syscode as fileType,a.wjtm," ,
				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName," , 
				"	c.PRJ_NAME,c.PRJ_CODE,c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," ,
			    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," ,
				"	d.INDUSTRY_NAME as prjIndustry,d.PRJ_TYPE,d.PRJ_NATURE,d.CUST_NAME as prjCust" ,
				" FROM BIZ_Z_DFILE a " ,
				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid" ,
				"	left join D_ZB_PKG c on a.xmbh=c.pkg_code" ,
				"	left join D_ZB_PRJ d on d.prj_code=c.prj_code" ,
				" where a.uuid not in ",
				"<foreach item='item' index='index' collection='ids'",
                "open='(' separator=',' close=')'>",
                "#{item}",
                "</foreach> limit 10",
		"</script>"
	})
	List<DBInfoBean> getDBInfo(@Param("ids") List<String> ids);


	
	@Select({
		"<script>",
				"SELECT ",
				"	a.uuid, a.syscode as fileType,a.wjtm," ,
				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName" , 
				" FROM BIZ_Z_DFILE a " ,
				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid" ,
				" where a.uuid not in ",
				"<foreach item='item' index='index' collection='ids'",
                "open='(' separator=',' close=')'>",
                "#{item}",
                "</foreach> limit 10",
		"</script>"
	})
	List<DBInfoBean> getTestDBInfo(@Param("ids") List<String> ids);
	
}
