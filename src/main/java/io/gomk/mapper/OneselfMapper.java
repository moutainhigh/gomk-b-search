package io.gomk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.dynamic.datasource.annotation.DS;

import io.gomk.task.DBInfoBean;

@DS("oneself")
public interface OneselfMapper {
	@Insert("insert into biz_z_dfile_sign values(#{uuid})")
	public void insertFileSign(String uuid);
	
	@Select("select uuid from biz_z_dfile_sign limit 1")
	String selectStoredDateTImeSTOREDATETIME();

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
	
	@Select({
		"<script>",
				"SELECT ",
				"	a.uuid, a.syscode as fileType,a.wjtm," ,
				"	b.title,b.ext,b.store as storeType,b.storepath as storeUrl,b.pathname as pathName" , 
				" FROM BIZ_Z_DFILE a " ,
				"	inner join BIZ_Z_EFILE_COMPLETED b on a.uuid=b.puuid " ,
                "  limit 10",
		"</script>"
	})
	List<DBInfoBean> getTestALLDBInfo(@Param("ids") List<String> ids);

	@Update("update biz_z_dfile_sign set uuid = #{storedatetime} where uuid = #{timeSign}")
	public void updateTimeSign(@Param("timeSign") String timeSign, @Param("storedatetime") String storedatetime);

	
}
