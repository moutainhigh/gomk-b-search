package io.gomk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.IZbLinePrjSuppl;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 行项目_供应商报价数据 服务类
 * </p>
 *
 * @author guanhua
 * @since 2019-11-02
 */
public interface IZbLinePrjSupplService extends IService<IZbLinePrjSuppl> {

    IPage<Map<String,String>> queryQuote(Page<Map<String,String>> param, String mateName);
}
