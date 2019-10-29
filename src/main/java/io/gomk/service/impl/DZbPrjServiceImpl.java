package io.gomk.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.gomk.model.entity.DZbPrj;
import io.gomk.mapper.DZbPrjMapper;
import io.gomk.service.IDZbPrjService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 招投标项目维度表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2019-10-28
 */
@Service
public class DZbPrjServiceImpl extends ServiceImpl<DZbPrjMapper, DZbPrj> implements IDZbPrjService {
    @Override
    public DZbPrj getPrjByPrjCode(String prjCode) {
        QueryWrapper<DZbPrj> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prj_code",prjCode);
        return this.getOne(queryWrapper);
    }
}
