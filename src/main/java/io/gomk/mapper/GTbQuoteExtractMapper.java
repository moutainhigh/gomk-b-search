package io.gomk.mapper;

import io.gomk.model.GTbQuoteExtract;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 身份证信息抽取（投标文件） Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2019-11-10
 */
@DS("oneself")
public interface GTbQuoteExtractMapper extends BaseMapper<GTbQuoteExtract> {

}
