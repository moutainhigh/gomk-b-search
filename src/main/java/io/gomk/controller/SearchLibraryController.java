package io.gomk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.common.rs.response.ResponseData;
import io.gomk.model.entity.DCust;
import io.gomk.model.entity.DZbExpert;
import io.gomk.service.DCustService;
import io.gomk.service.DZbExpertService;
import io.gomk.service.DZbPkgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author guanhua
 * @date 2019/10/30 00:05
 */
@RestController
@RequestMapping("/library")
@Api(description = "专家、造价项目、招标项目、招标人库")
public class SearchLibraryController {
    @Autowired
    private DZbPkgService dZbPkgService;

    @Autowired
    private DZbExpertService dZbExpertService;

    @Autowired
    private DCustService dCustService;

    @GetMapping("/bindProject")
    public ResponseData<IPage<Map<String,String>>> biddingProject(int page, int pageSize,String keyWord) throws Exception {
        // 当前页码，每页条数
        Page<Map<String,String>> pageParam = new Page<>(page, pageSize);

        IPage<Map<String,String>> pageResult = dZbPkgService.biddingProject(pageParam,keyWord);
        return  ResponseData.success(pageResult);
    }

    @GetMapping("/costProject")
    public ResponseData<IPage<Map<String,String>>> costProject(int page, int pageSize,String keyWord) throws Exception {
        // 当前页码，每页条数
        Page<Map<String,String>> pageParam = new Page<>(page, pageSize);

        IPage<Map<String,String>> pageResult = dZbPkgService.costProject(pageParam,keyWord);
        return  ResponseData.success(pageResult);
    }

    @GetMapping("/bindPerson")
    public ResponseData<IPage<Map<String,String>>> bindPerson(int page, int pageSize,String keyWord) throws Exception {
        // 当前页码，每页条数
        Page<Map<String,String>> pageParam = new Page<>(page, pageSize);

        IPage<Map<String,String>> pageResult = dZbPkgService.biddingPerson(pageParam,keyWord);
        return  ResponseData.success(pageResult);
    }

    @ApiOperation(value = "专家列表分页(d_zb_expert)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page", value="当前页", required=true, paramType="query", dataType="int", defaultValue="1"),
            @ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10")
    })
    @GetMapping(value = "/expert")
    public ResponseData<IPage<DZbExpert>> sbequipmentf(int page, int pageSize,String keyWord){

        if (StringUtils.isBlank(keyWord)) {
            return ResponseData.success();
        }
        QueryWrapper<DZbExpert> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(DZbExpert::getExpertName,keyWord);

        IPage<DZbExpert> pages = dZbExpertService.page(new Page<>(page, pageSize),queryWrapper);
        return ResponseData.success(pages);
    }

    @GetMapping(value = "/custDetails")
    public ResponseData<DCust> custDetails(String id){


        QueryWrapper<DCust> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(DCust::getId,id);

        DCust dcust = dCustService.getOne(queryWrapper);
        return ResponseData.success(dcust);
    }

    @GetMapping(value = "/expertDetails")
    public ResponseData<DZbExpert> expertDetails(String expertCode){

        QueryWrapper<DZbExpert> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(DZbExpert::getExpertCode,expertCode);

        DZbExpert expert = dZbExpertService.getOne(queryWrapper);
        return ResponseData.success(expert);
    }
}
