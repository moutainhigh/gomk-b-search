package io.gomk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.baomidou.dynamic.datasource.annotation.DS;

@DS("oneself")
public interface DB2ESMapper {
	@Insert("insert into biz_z_dfile_sign values(#{uuid})")
	public void insertFileSign(String uuid);
	
	@Select("select a.uuid from biz_z_dfile a where a.uuid not in (select uuid from biz_z_dfile_sign) limit 0, #{size}")
	List<String> selectIDS(int size);
	
}
