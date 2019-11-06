package io.gomk.controller;

import io.gomk.model.entity.TargetMapDTO;
import io.gomk.service.GraphService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/graph")
@Slf4j
public class GraphController {

    @Autowired
    GraphService graphService;

    @GetMapping("/init")
    @ApiOperation(value = "初始化库",notes = "初始化库")
    public String initGraph() {
        Long start = System.currentTimeMillis();
        graphService.buildGraph();
        log.info("初始库花费时间：{}",System.currentTimeMillis() - start);
        return "success";
    }

    @GetMapping("/import-data")
    @ApiOperation(value = "导入数据",notes = "导入数据")
    public String importData() {
        Long start = System.currentTimeMillis();
        graphService.insertData();
        log.info("插入数据花费时间：{}",System.currentTimeMillis() - start);
        return "success";
    }

    @GetMapping("/query/map")
    @ApiOperation(value = "查询图谱",notes = "查询图谱")
    public TargetMapDTO query(@RequestParam(name = "targetId") String targetId) {
        return graphService.queryTargetMap(targetId);
    }


    @GetMapping("/import-data-increment")
    @ApiOperation(value = "增量导入数据",notes = "增量导入数据")
    public String importDataIncrement() {
        Long start = System.currentTimeMillis();
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusDays(1);
        graphService.insertData(localDate);
        log.info("增量插入数据花费时间：{}",System.currentTimeMillis() - start);
        return "success";
    }
}
