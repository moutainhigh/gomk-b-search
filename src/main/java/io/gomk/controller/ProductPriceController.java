package io.gomk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.framework.controller.SuperController;
import io.gomk.service.IProductPriceService;
import io.swagger.annotations.Api;

/**
 * @author Robinxiao
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/price")
@Api(description = "产品价格服务")
public class ProductPriceController extends SuperController {

	@Autowired
	IProductPriceService service;
	@PostMapping("/product")
	public ResponseData<?> getPrice(String keyWord) throws Exception {
		service.getPrice(keyWord);
		return ResponseData.success();
	}
	
}
