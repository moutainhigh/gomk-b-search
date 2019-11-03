package io.gomk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.DZbPkg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 招投标标段维度表 Mapper 接口
 * </p>
 *
 * @author guanhua
 * @since 2019-10-13
 */
@Repository
public interface DZbPkgMapper extends BaseMapper<DZbPkg> {

    List<DZbPkg> queryGroup(String pkgCode);

    /**
     * 标的物查询
     * @param metaName
     * @return
     */
    String matename(String metaName);

    /**
     * 这个标的物属于那个标段
     * @param metaName
     * @return
     */
    List<Map<String,String>> pkgname(String metaName);

    /**
     * 这个标的物属于那个项目
     * @param metaName
     * @return
     */
    List<Map<String,String>> prjname(String metaName);

    List<Map<String,String>> documentname(String metaName);

    List<Map<String,String>> price(String metaName);

    List<Map<String,String>> custname(String metaName);

    IPage<Map<String,String>> biddingProject(Page<Map<String,String>> param,@Param("prjName") String prjName);

    IPage<Map<String,String>> costProject(Page<Map<String,String>> param,@Param("prjName")String prjName);

    IPage<Map<String,String>> biddingPerson(Page<Map<String,String>> param,@Param("custName")String custName);

    Map<String,Object> biddingDetails(@Param("prjCode")String prjCode);

    Map<String,Object> costDetails(@Param("prjCode")String prjCode);
}
