package io.gomk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.IGxTPriceList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nick
 * @since 2019-11-13
 */
public interface IGxTPriceListService extends IService<IGxTPriceList> {

    IPage<Map<String,String>> queryQuote(Page<Map<String,String>> param, String mateName);

    List<Map<String,Object>> selectPriceCharts(String mateName);
}
