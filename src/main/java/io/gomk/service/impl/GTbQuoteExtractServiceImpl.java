package io.gomk.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.ParseInfo;

import io.gomk.framework.utils.parsefile.ParseFile;
import io.gomk.mapper.GTbQuoteExtractMapper;
import io.gomk.model.GTbQuoteExtract;
import io.gomk.service.IGTbQuoteExtractService;

/**
 * <p>
 * 身份证信息抽取（投标文件） 服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-11-10
 */
@Service
@DS("oneself")
public class GTbQuoteExtractServiceImpl extends ServiceImpl<GTbQuoteExtractMapper, GTbQuoteExtract> implements IGTbQuoteExtractService {

	@Autowired
	ParseFile parseFile;
	
	@Override
	@Transactional
	public void insertPrice(ByteArrayInputStream byteArrayInputStream, GTbQuoteExtract qe) throws IOException {
		List<LinkedHashMap<Integer, String>> list = parseFile.parsePdfTable(byteArrayInputStream);
		for (LinkedHashMap<Integer, String> map : list) {
			GTbQuoteExtract tbqe =  new GTbQuoteExtract();
			BeanUtils.copyProperties(qe, tbqe);
			tbqe.setMaterialsName(map.get(1));
			tbqe.setSpecifications(map.get(2));
			tbqe.setVendor(map.get(3));
			tbqe.setAmount(map.get(4));
			tbqe.setUnitPrice(map.get(5));
			tbqe.setTotalPrice(map.get(6));
			baseMapper.insert(tbqe);
		}
	}

}
