package io.gomk.service;

import io.gomk.model.entity.DPrjManufacturCost;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gomk.model.entity.DZbPrj;

/**
 * <p>
 * 招投标项目维度表 服务类
 * </p>
 *
 * @author chen
 * @since 2019-11-02
 */
public interface IDPrjManufacturCostService extends IService<DPrjManufacturCost> {
    DPrjManufacturCost getPrjCostByPrjCode(String prjCode);
}
