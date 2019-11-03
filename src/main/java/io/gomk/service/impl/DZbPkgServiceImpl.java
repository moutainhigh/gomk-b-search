package io.gomk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.common.utils.PageResult;
import io.gomk.model.entity.DZbPkg;
import io.gomk.mapper.DZbPkgMapper;
import io.gomk.service.DZbPkgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 招投标标段维度表 服务实现类
 * </p>
 *
 * @author guanhua
 * @since 2019-10-13
 */
@Service
public class DZbPkgServiceImpl extends ServiceImpl<DZbPkgMapper, DZbPkg> implements DZbPkgService {

    @Autowired
    private DZbPkgMapper dZbPkgMapper;

    @Override
    public List<Map<String,Object>> queryData(String metaName) {

        List<Map<String,Object>> qd = new ArrayList<>();

        return qd;
    }

    @Override
    public Map<String, Object> chartData(String metaName) {
        Map<String,Object> chart = new HashMap<>();

        List<Map<String,Object>> qd = new ArrayList<>();

        List<Map<String,Object>> linkdata = new ArrayList<>();

        String matename = dZbPkgMapper.matename(metaName);

        Map<String,Object> metaMap = new HashMap<>();

        metaMap.put("name",matename);
        metaMap.put("symbolSize","50");
        metaMap.put("draggable","true");
        qd.add(metaMap);

        //  标段包
        List<Map<String,String>> bdblist = dZbPkgMapper.pkgname(metaName);

        Map<String,Object> bdb = new HashMap<>();
        Map<String,Object> metalink = new HashMap<>();

        bdb.put("name","标段包");
        bdb.put("symbolSize","9");
        bdb.put("category","标段包");
        bdb.put("draggable","true");
        bdb.put("value",bdblist.size());
        qd.add(bdb);

        metalink.put("source",matename);
        metalink.put("target","标段包");
        linkdata.add(metalink);

        for(Map<String,String> bdbv : bdblist){
            Map<String,Object> bdbm = new HashMap<>();
            Map<String,Object> bdblink = new HashMap<>();

            bdbm.put("name",bdbv.get("pkg_name"));
            bdbm.put("symbolSize",3);
            bdbm.put("category","标段包");
            bdbm.put("draggable","true");
            qd.add(bdbm);

            bdblink.put("source","标段包");
            bdblink.put("target",bdbv.get("pkg_name"));
            linkdata.add(bdblink);
        }
        // 项目
        List<Map<String,String>> xmlist = dZbPkgMapper.prjname(metaName);
        Map<String,Object> xm = new HashMap<>();
        Map<String,Object> xmlink = new HashMap<>();

        xm.put("name","项目信息");
        xm.put("symbolSize","9");
        xm.put("category","项目信息");
        xm.put("draggable","true");
        xm.put("value",xmlist.size());
        qd.add(xm);

        xmlink.put("source",matename);
        xmlink.put("target","项目信息");
        linkdata.add(xmlink);

        for(Map<String,String> bdbv : xmlist){
            Map<String,Object> xmm = new HashMap<>();
            Map<String,Object> xmmlink = new HashMap<>();
            xmm.put("name",bdbv.get("prj_name"));
            xmm.put("symbolSize",3);
            xmm.put("category","项目信息");
            xmm.put("draggable","true");
            qd.add(xmm);

            xmmlink.put("source","项目信息");
            xmmlink.put("target",bdbv.get("prj_name"));
            linkdata.add(xmmlink);
        }
        // 投标人
        List<Map<String,String>> tbrlist = dZbPkgMapper.documentname(metaName);
        Map<String,Object> tbr = new HashMap<>();
        Map<String,Object> tbrlink = new HashMap<>();
        tbr.put("name","投标人");
        tbr.put("symbolSize","9");
        tbr.put("category","投标人");
        tbr.put("draggable","true");
        tbr.put("value",tbrlist.size());
        qd.add(tbr);

        tbrlink.put("source",matename);
        tbrlink.put("target","投标人");
        linkdata.add(tbrlink);
        for(Map<String,String> bdbv : tbrlist){
            Map<String,Object> tbrm = new HashMap<>();
            Map<String,Object> tbrmlink = new HashMap<>();
            tbrm.put("name",bdbv.get("document_name").concat("(投)"));
            tbrm.put("symbolSize",3);
            tbrm.put("category","投标人");
            tbrm.put("draggable","true");
            qd.add(tbrm);

            tbrmlink.put("source","投标人");
            tbrmlink.put("target",bdbv.get("document_name").concat("(投)"));
            linkdata.add(tbrmlink);
        }
        //价格
        List<Map<String,String>> jglist = dZbPkgMapper.price(metaName);
        Map<String,Object> jr = new HashMap<>();
        Map<String,Object> jrlink = new HashMap<>();
        jr.put("name","价格");
        jr.put("symbolSize","9");
        jr.put("category","价格");
        jr.put("draggable","true");
        jr.put("value",jglist.size());
        qd.add(jr);
        jrlink.put("source",matename);
        jrlink.put("target","价格");
        linkdata.add(jrlink);

        for(Map<String,String> bdbv : jglist){
            Map<String,Object> jrm = new HashMap<>();
            Map<String,Object> jrmlink = new HashMap<>();
            jrm.put("name",String.valueOf(bdbv.get("price")));
            jrm.put("symbolSize",3);
            jrm.put("category","价格");
            jrm.put("draggable","true");
            qd.add(jrm);
            jrmlink.put("source","价格");
            jrmlink.put("target",String.valueOf(bdbv.get("price")));
            linkdata.add(jrmlink);
        }
        //招标人
        List<Map<String,String>> zbrlist = dZbPkgMapper.custname(metaName);
        Map<String,Object> zbr = new HashMap<>();
        Map<String,Object> zbrlink = new HashMap<>();
        zbr.put("name","招标人");
        zbr.put("symbolSize","9");
        zbr.put("category","招标人");
        zbr.put("draggable","true");
        zbr.put("value",zbrlist.size());
        qd.add(zbr);
        zbrlink.put("source",matename);
        zbrlink.put("target","招标人");
        linkdata.add(zbrlink);
        for(Map<String,String> bdbv : zbrlist){
            Map<String,Object> zbrm = new HashMap<>();
            Map<String,Object> zbrmlink = new HashMap<>();
            zbrm.put("name",bdbv.get("cust_name").concat("(招)"));
            zbrm.put("symbolSize",3);
            zbrm.put("category","招标人");
            zbrm.put("draggable","true");
            qd.add(zbrm);
            zbrmlink.put("source","招标人");
            zbrmlink.put("target",bdbv.get("cust_name").concat("(招)"));
            linkdata.add(zbrmlink);
        }

        List<Map<String,String>> categories = new ArrayList<>();

        Map<String,String> zbc = new HashMap<>();
        zbc.put("name","招标人");
        Map<String,String> xmc = new HashMap<>();
        xmc.put("name","项目信息");
        Map<String,String> tbrc = new HashMap<>();
        tbrc.put("name","投标人");
        Map<String,String> jgc = new HashMap<>();
        jgc.put("name","价格");
        Map<String,String> bdbc = new HashMap<>();
        bdbc.put("name","标段包");
        categories.add(zbc);
        categories.add(xmc);
        categories.add(tbrc);
        categories.add(jgc);
        categories.add(bdbc);

        chart.put("data",qd);
        chart.put("links",linkdata);
        chart.put("categories",categories);
        return chart;
    }

    @Override
    public IPage<Map<String, String>> biddingProject(Page<Map<String, String>> param,String prjName) {
        return dZbPkgMapper.biddingProject(param,prjName);
    }

    @Override
    public IPage<Map<String, String>> costProject(Page<Map<String, String>> param,String prjName) {
        return dZbPkgMapper.costProject(param,prjName);
    }

    @Override
    public IPage<Map<String, String>> biddingPerson(Page<Map<String, String>> param,String custName) {
        return dZbPkgMapper.biddingPerson(param,custName);
    }

    @Override
    public List<DZbPkg> findPkg(String prjCode) {
        QueryWrapper<DZbPkg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prj_code",prjCode);
        return dZbPkgMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> biddingDetails(String prjCode) {
        Map<String, Object> details = new HashMap<>();
        Map<String,Object> bidding = dZbPkgMapper.biddingDetails(prjCode);
        details.put("bidding",bidding);

        QueryWrapper<DZbPkg> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(DZbPkg::getPrjCode,prjCode);

        List<DZbPkg> pkglist = dZbPkgMapper.selectList(queryWrapper);
        details.put("pkgs",pkglist);
        return details;
    }

    @Override
    public Map<String, Object> costDetails(String prjCode) {
        return dZbPkgMapper.costDetails(prjCode);
    }
}
