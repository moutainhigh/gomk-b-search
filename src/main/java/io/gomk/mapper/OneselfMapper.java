package io.gomk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.dynamic.datasource.annotation.DS;

import io.gomk.task.DBInfoBean;
import io.gomk.task.ESInfoBean;

@DS("oneself")
public interface OneselfMapper {
	
	@Select("select uuid from biz_z_dfile_sign where sign_type = #{type} limit 1")
	String selectSign(int type);

	@Update("update biz_z_dfile_sign set uuid = #{sign} where sign_type = #{type}")
	public void updateSign(@Param("sign") String sign, @Param("type") int type);

	
	@Select("select uuid from biz_z_dfile_sign")
	List<String> selectIds();

//	@Select({
//		"<script>",
//				"SELECT ",
//				"	a.uuid, a.syscode as fileType,a.wjtm," ,
//				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName" , 
//				" FROM BIZ_Z_DFILE a " ,
//				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid" ,
//				" where a.uuid not in ",
//				"<foreach item='item' index='index' collection='ids'",
//                "open='(' separator=',' close=')'>",
//                "#{item}",
//                "</foreach> limit 10",
//		"</script>"
//	})
//	List<DBInfoBean> getTestDBInfo(@Param("ids") List<String> ids);
//	
//	@Select({
//		"<script>",
//				"SELECT ",
//				"	a.uuid, a.syscode as fileType,a.wjtm," ,
//				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName" , 
//				" FROM BIZ_Z_DFILE a " ,
//				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid " ,
//                "  limit 10",
//		"</script>"
//	})
//	List<DBInfoBean> getTestALLDBInfo(@Param("ids") List<String> ids);


	@Select("select product_name as title from t_g_product")
	public List<String> selectCompletion();


	
}
