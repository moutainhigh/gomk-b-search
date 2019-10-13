package io.gomk.service;

import io.gomk.model.entity.DZbPkg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 招投标标段维度表 服务类
 * </p>
 *
 * @author guanhua
 * @since 2019-10-13
 */
public interface DZbPkgService extends IService<DZbPkg> {

    List<Map<String,Object>> queryData(String metaName);

    Map<String,Object> chartData(String metaname);
}
