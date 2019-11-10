package io.gomk.service;

import io.gomk.model.GTbQuoteExtract;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 身份证信息抽取（投标文件） 服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-11-10
 */
public interface IGTbQuoteExtractService extends IService<GTbQuoteExtract> {

	void insertPrice(ByteArrayInputStream byteArrayInputStream, GTbQuoteExtract qe) throws IOException;


}
