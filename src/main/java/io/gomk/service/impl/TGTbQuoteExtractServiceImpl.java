package io.gomk.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import io.gomk.model.entity.TGTbQuoteExtract;
import io.gomk.mapper.TGTbQuoteExtractMapper;
import io.gomk.service.TGTbQuoteExtractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分项报价抽取（投标文件） 服务实现类
 * </p>
 *
 * @author nick
 * @since 2019-11-11
 */
@Service
@DS("oneself")
public class TGTbQuoteExtractServiceImpl extends ServiceImpl<TGTbQuoteExtractMapper, TGTbQuoteExtract> implements TGTbQuoteExtractService {

}
