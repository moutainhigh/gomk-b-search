package io.gomk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.gomk.model.entity.DPrjManufacturCost;
import io.gomk.mapper.DPrjManufacturCostMapper;
import io.gomk.model.entity.DZbPrj;
import io.gomk.service.IDPrjManufacturCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 招投标项目维度表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2019-11-02
 */
@Service
public class DPrjManufacturCostServiceImpl extends ServiceImpl<DPrjManufacturCostMapper, DPrjManufacturCost> implements IDPrjManufacturCostService {

    @Override
    public DPrjManufacturCost getPrjCostByPrjCode(String prjCode) {
        QueryWrapper<DPrjManufacturCost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prj_code",prjCode);
        return this.getOne(queryWrapper);
    }
}
