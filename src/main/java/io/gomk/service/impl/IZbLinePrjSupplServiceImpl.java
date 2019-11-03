package io.gomk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.IZbLinePrjSuppl;
import io.gomk.mapper.IZbLinePrjSupplMapper;
import io.gomk.service.IZbLinePrjSupplService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 行项目_供应商报价数据 服务实现类
 * </p>
 *
 * @author guanhua
 * @since 2019-11-02
 */
@Service
public class IZbLinePrjSupplServiceImpl extends ServiceImpl<IZbLinePrjSupplMapper, IZbLinePrjSuppl> implements IZbLinePrjSupplService {

    @Autowired
    private IZbLinePrjSupplMapper supplMapper;
    @Override
    public IPage<Map<String, String>> queryQuote(Page<Map<String, String>> param, String mateName) {
        return supplMapper.queryQuote(param,mateName);
    }
}
