package io.gomk.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.gomk.common.rs.response.ResponseData;
import io.gomk.model.entity.DZbPkg;
import io.gomk.service.DZbPkgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 招投标标段维度表 前端控制器
 * </p>
 *
 * @author guanhua
 * @since 2019-10-13
 */
@RestController
@RequestMapping("/knowledge")
@Api(description = "图谱关系")
public class DZbPkgController {

    @Autowired
    private DZbPkgService dZbPkgService;

    @ApiOperation("根据项目编号查询图谱")
    @RequestMapping(value = "/{metaName}", method = RequestMethod.GET)
    public ResponseData<Map<String,Object>> dictItems(@PathVariable String metaName){


        return ResponseData.success(dZbPkgService.chartData(metaName));
    }
}

