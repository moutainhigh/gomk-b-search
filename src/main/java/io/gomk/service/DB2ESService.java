package io.gomk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import io.gomk.task.DBInfoBean;

@Service
public class DB2ESService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public List<DBInfoBean> getDBInfo(List<String> ids) {
		String sql = "SELECT " + 
				"	a.uuid, a.syscode as fileType," + 
				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName," + 
				"	c.PRJ_NAME,c.PRJ_CODE,c.PKG_WIN_BID_AMT as winAmount,c.PKG_NAME,c.PKG_CODE,c.BID_TYPE as zbType," + 
			    "   c.ANNOUNC_DATE as noticeDate,c.entrust_amt," + 
				"	d.INDUSTRY_NAME as prjIndustry,d.PRJ_TYPE,d.PRJ_NATURE,d.CUST_NAME as prjCust" + 
				" FROM biz_z_dfile a " + 
				"	inner join biz_z_efile_completed b on a.uuid=b.puuid" + 
				"	inner join d_zb_pkg c on a.xmbh=c.pkg_code" + 
				"	inner join d_zb_prj d on d.prj_code=c.prj_code" + 
				" where a.uuid in (:param)";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("param", ids);
		NamedParameterJdbcTemplate givenParamJdbcTemp = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<DBInfoBean> list = givenParamJdbcTemp.queryForList(sql, paramMap, DBInfoBean.class);
		return list;
	}
}
