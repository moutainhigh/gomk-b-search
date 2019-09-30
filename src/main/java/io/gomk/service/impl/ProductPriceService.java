package io.gomk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.gomk.mapper.ProductPriceMapper;
import io.gomk.service.IProductPriceService;

@Service
public class ProductPriceService implements IProductPriceService {
	@Autowired
	ProductPriceMapper mapper;
	@Override
	public String getPrice(String keyWord) {
		return mapper.getProductPrice(keyWord);
	}
}
