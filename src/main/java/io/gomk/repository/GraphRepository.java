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


    @Query(value = "SELECT s.mate_name AS metaName, s.mate_code AS metaCode FROM i_zb_line_prj_suppl s WHERE s.mate_name = :name",
            nativeQuery = true)
    Page<TargetProjection> queryByPersonNameIn(String name, Pageable pageable);

    @Query(value = "SELECT distinct mate_name AS metaName, mate_code AS metaCode FROM i_zb_line_prj_suppl",
            nativeQuery = true)
    Page<TargetProjection> queryAllTarget(Pageable pageable);

    @Query(value = "SELECT  mate_name AS metaName, mate_code AS metaCode FROM i_zb_line_prj_suppl WHERE etl_time = :localDate ",
            countQuery = "select count(*) from i_zb_line_prj_suppl where etl_time = :localDate",
            nativeQuery = true)
    Page<TargetProjection> queryTargetByDate(String localDate, Pageable pageable);

    @Query(value = " select distinct\n" +
            "    t.pkg_code as packageCode,t.pkg_name as packageName,t.prj_code as prjCode\n" +
            "    from d_zb_pkg t\n" +
            "    inner join i_zb_line_prj_suppl t1 on t1.pkg_code=t.pkg_code\n" +
            "    where  t1.mate_name = :metaName",
            nativeQuery = true)
    List<PackageProjection> queryPackageByMetaName(String metaName);

    @Query(value = "select distinct t.prj_code as prjCode,t.prj_name as prjName from d_zb_prj t\n" +
            "    inner join d_zb_pkg t1 on t1.prj_code=t.prj_code\n" +
            "    inner join i_zb_line_prj_suppl t2 on t2.pkg_code=t1.pkg_code\n" +
            "    where t2.mate_name = :metaName",
            nativeQuery = true)
    List<ProjectProjection> queryProjectByMetaName(String metaName);

    @Query(value = "select distinct document_name as subjectName, document_code as subjectCode from i_zb_line_prj_suppl where mate_name= " +
            ":metaName",
            nativeQuery = true)
    List<BidSubjectProjection> queryToubiaoByMetaName(String metaName);

    @Query(value = "select distinct t.cust_name as subjectName, t.cust_document_code as subjectCode\n" +
            "    from d_zb_prj t\n" +
            "    inner join d_zb_pkg t1 on t1.prj_code=t.prj_code\n" +
            "    inner join i_zb_line_prj_suppl t2 on t2.pkg_code=t1.pkg_code\n" +
            "    where t2.mate_name= :metaName",
            nativeQuery = true)
    List<BidSubjectProjection> queryZhaobiaoByMetaName(String metaName);

    @Query(value = " select distinct price as price from i_zb_line_prj_suppl where  if_win_bid='1' and mate_name= :metaName and price is" +
            " " +
            " not " +
            " null",
            nativeQuery = true)
    List<PriceProjection> queryPriceByMetaName(String metaName);


}
