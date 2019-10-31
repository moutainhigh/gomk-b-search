package io.gomk.service.impl;

import io.gomk.model.GTagFormula;
import io.gomk.mapper.GTagFormulaMapper;
import io.gomk.service.IGTagFormulaService;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-20
 */
@Service
@DS("oneself")
public class GTagFormulaServiceImpl extends ServiceImpl<GTagFormulaMapper, GTagFormula> implements IGTagFormulaService {

}
