package io.gomk.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.common.rs.response.ResponseData;
import io.gomk.service.DZbPkgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 招投标标段维度表 前端控制器
 * </p>
 *
 * @author nick
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

