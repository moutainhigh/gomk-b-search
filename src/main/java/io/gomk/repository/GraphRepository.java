package io.gomk.repository;

import io.gomk.model.entity.SupplyDO;
import io.gomk.projection.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GraphRepository extends JpaRepository<SupplyDO, String>, JpaSpecificationExecutor {

    /**
     * 查询标的物
     * @param name
     * @param pageable
     * @return
     */
    @Query(value = "select distinct substring_index(item_wlmc , '\\\\', 1) as metaName  \n" +
            "       from I_GX_T_PRICE_LIST \n" +
            "       where substring_index(item_wlmc , '\\\\', 1)  =  :name",
            countQuery = "select count(*) from I_GX_T_PRICE_LIST s WHERE substring_index(item_wlmc , '\\\\', 1)  = :name",
            nativeQuery = true)
    Page<TargetProjection> queryByTargetName(String name, Pageable pageable);

    /**
     * 查询全部标的物
     * @param pageable
     * @return
     */
    @Query(value = "SELECT distinct substring_index(item_wlmc , '\\\\', 1) AS metaName FROM I_GX_T_PRICE_LIST  ",
            nativeQuery = true)
    Page<TargetProjection> queryAllTarget(Pageable pageable);

    /**
     * 根据日期查询标的物
     * @param localDate
     * @param pageable
     * @return
     */
    @Query(value = "SELECT  substring_index(item_wlmc , '\\\\', 1) as metaName FROM I_GX_T_PRICE_LIST WHERE etl_time = :localDate ",
            countQuery = "select count(*) from I_GX_T_PRICE_LIST where etl_time = :localDate",
            nativeQuery = true)
    Page<TargetProjection> queryTargetByDate(String localDate, Pageable pageable);

    /**
     * 查询标段
     * @param metaName
     * @return
     */
    @Query(value = "select distinct \n" +
            "         t.pkg_code as packageCode,t.pkg_name as packageName,t.prj_code as prjCode \n" +
            "         from D_ZB_PKG t \n" +
            "        inner join I_GX_T_PRICE_LIST t1 on t1.biaoduanbh=t.pkg_code \n" +
            "        where  substring_index(t1.item_wlmc , '\\\\', 1)  = :metaName",
            nativeQuery = true)
    List<PackageProjection> queryPackageByMetaName(String metaName);

    /**
     * 查询项目
     * @param metaName
     * @return
     */
    @Query(value="select distinct t.prj_code as prjCode,t.prj_name as prjName \n" +
            "        from D_ZB_PRJ t\n" +
            "        inner join D_ZB_PKG t1 on t1.prj_code=t.prj_code\n" +
            "        inner join I_GX_T_PRICE_LIST t2 on t2.biaoduanbh=t1.pkg_code\n" +
            "        where substring_index(t2.item_wlmc , '\\\\', 1)  = :metaName ",  nativeQuery = true)
    List<ProjectProjection> queryProjectByMetaName(String metaName);

    /**
     * 查询投标
     * @param metaName
     * @return
     */
    @Query(value = "select distinct \n" +
            "        t2.DANWEINAME  as subjectName,t2.SUPPL_DOCUMENT_CODE as subjectCode\n" +
            "        from  F_ZB_WIN_BID_DETAIL t\n" +
            "        inner join D_ZB_PKG t1 on t1.pkg_code=t.pkg_code\n" +
            "        inner join D_ZB_SUPPL t2 on t2.SUPPL_DOCUMENT_CODE  = t.document_code\n" +
            "         inner join I_GX_T_PRICE_LIST t3 on t3.biaoduanbh=t1.pkg_code\n" +
            "     where substring_index(t3.item_wlmc , '\\\\', 1)  = :metaName ", nativeQuery = true)
    List<BidSubjectProjection> queryToubiaoByMetaName(String metaName);

    /**
     * 招标
     * @param metaName
     * @return
     */
    @Query(value = "select distinct t.cust_name as subjectName,t.CUST_DOCUMENT_CODE as subjectCode \n" +
            "        from D_ZB_PRJ t\n" +
            "        inner join D_ZB_PKG t1 on t1.prj_code=t.prj_code\n" +
            "        inner join I_GX_T_PRICE_LIST t2 on t2.biaoduanbh=t1.pkg_code\n" +
            "        where substring_index(t2.item_wlmc , '\\\\', 1)  = :metaName", nativeQuery = true)
    List<BidSubjectProjection> queryZhaobiaoByMetaName(String metaName);

    /**
     * 价格
     * @param metaName
     * @return
     */
    @Query(value = " select distinct t1.win_price as price \n" +
            " \tfrom I_GX_T_PRICE_LIST t\n" +
            " \tinner join F_ZB_WIN_BID_DETAIL t1 on t1.pkg_code = t.biaoduanbh\n" +
            " \twhere  \n" +
            "               substring_index(t.item_wlmc , '\\\\', 1)  = :metaName\n" +
            "                and t1.win_price is not null", nativeQuery = true)
    List<PriceProjection> queryPriceByMetaName(String metaName);


}
