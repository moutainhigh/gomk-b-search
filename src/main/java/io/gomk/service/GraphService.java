package io.gomk.service;

import io.gomk.common.constants.GraphConstant;
import io.gomk.model.TargetMapDTO;
import io.gomk.projection.*;
import io.gomk.repository.GraphRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.ImmutablePath;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.janusgraph.graphdb.relations.CacheEdge;
import org.janusgraph.graphdb.relations.RelationIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;

@Service
@Slf4j
public class GraphService implements GraphConstant {

    @Autowired
    JanusGraph graph;

    @Autowired
    GraphRepository repository;

    private static final int pageSize = 10;

    public TargetMapDTO queryTargetMap(String targetId) {
        Map<Object, Map<String, Object>> edgeMap = new HashMap<>();
        Map<Object, Map<String, Object>> vertexMap = new HashMap<>();

        Set<Path> pathSet =
                graph.traversal().V().has(V_NODE_ID, targetId).bothE().otherV().simplePath()
                        .path().by(valueMap(true)).by(valueMap().union(path())).toSet();
        for (Path path : pathSet) {
            for (int i = 0; i < path.size(); i++) {
                //处理边
                if ((i + 1) % 2 == 0) {
                    ImmutablePath immutablePath = path.get(i);
                    if (immutablePath != null && immutablePath.size() > 0) {
                        CacheEdge edge = (CacheEdge) immutablePath.get(0);
                        RelationIdentifier relationIdentifier = edge.id();
                        String label = edge.getType().name();
                        Map<String, Object> tempEdgeMap = new HashMap<>();
                        tempEdgeMap.put(DATA_ID, relationIdentifier.getRelationId());
                        tempEdgeMap.put(DATA_START_NODE, relationIdentifier.getOutVertexId());
                        tempEdgeMap.put(DATA_END_NODE, relationIdentifier.getInVertexId());
                        tempEdgeMap.put(LABEL, label);

                        edgeMap.put(tempEdgeMap.get(DATA_ID), tempEdgeMap);
                    }
                } else {
                    //处理点
                    HashMap<Object, Object> originalMap = path.get(i);
                    HashMap<String, Object> tempVertexMap = new HashMap<>();
                    //将所有类型的key转为字符串key
                    for (Object key : originalMap.keySet()) {
                        tempVertexMap.put(key.toString(), originalMap.get(key));
                    }
                    if (tempVertexMap.containsKey(LABEL)) {
                        Object nodeId = objectArrayToObject(tempVertexMap.getOrDefault(V_NODE_ID, ""));
                        tempVertexMap.put(DATA_ID, tempVertexMap.getOrDefault(DATA_ID, ""));

                        tempVertexMap.put(LABEL, tempVertexMap.getOrDefault(LABEL, ""));
                        tempVertexMap.put(V_NODE_ID, nodeId);
                        if (V_LABEL_TARGET.equals(tempVertexMap.getOrDefault(LABEL, ""))) {
                            tempVertexMap.put(V_PRJ_CODE, objectArrayToObject(tempVertexMap.getOrDefault(V_PRJ_CODE, "")));
                        }
                        if (V_LABEL_PRICE.equals(tempVertexMap.getOrDefault(LABEL, ""))) {
                            tempVertexMap.put(V_PRICE, objectArrayToObject(tempVertexMap.getOrDefault(V_PRICE, "")));
                        } else {
                            tempVertexMap.put(V_NAME, objectArrayToObject(tempVertexMap.getOrDefault(V_NAME, "")));
                        }
                        vertexMap.put(tempVertexMap.get(DATA_ID), tempVertexMap);
                    }
                }
            }

        }
        TargetMapDTO targetMapDTO = new TargetMapDTO();
        targetMapDTO.setEdges(new ArrayList<>(edgeMap.values()));
        targetMapDTO.setNodes(new ArrayList<>(vertexMap.values()));
        return targetMapDTO;
    }

    public void insertData() {
        Integer pageNum = 0;
        Page<TargetProjection> targetProjectionPage = repository.queryAllTarget(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "guid")));
        while (targetProjectionPage.getNumberOfElements() > 0) {
            dealTarget(targetProjectionPage);
            pageNum++;
            log.info("处理第{}页数据", pageNum);
            targetProjectionPage = repository.queryAllTarget(PageRequest.of(pageNum, pageSize));
        }
        log.info("数据处理完毕");
    }

    public void insertData(LocalDate localDate) {
        Integer pageNum = 0;
        Page<TargetProjection> targetProjectionPage = repository.queryTargetByDate(localDate.toString(), PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.DESC, "guid")));
        while (targetProjectionPage.getNumberOfElements() > 0) {
            dealTarget(targetProjectionPage);
            pageNum++;
            log.info("处理第{}页数据", pageNum);
            targetProjectionPage = repository.queryAllTarget(PageRequest.of(pageNum, pageSize));
        }
        log.info("数据处理完毕");
    }


    private void dealTarget(Page<TargetProjection> targetProjectionPage) {
        if (!targetProjectionPage.hasContent()) {
            for (TargetProjection targetProjection : targetProjectionPage.getContent()) {
                String targetCode = targetProjection.getMetaCode();
//                System.out.println("************" + targetProjection.getMetaName() + "************");
                Vertex targetVertex = createTargetVertex(targetProjection);
                List<PackageProjection> packageProjectionList = repository.queryPackageByMetaName(targetProjection.getMetaName());
                packageProjectionList.stream().forEach(packageProjection -> {
                    Vertex vertex = createPackageVertex(packageProjection);
                    if (!hasEdge(targetCode, EDGE_LABEL_PACKAGE, packageProjection.getPackageCode())) {
                        Edge edge = targetVertex.addEdge(EDGE_LABEL_PACKAGE, vertex);
                    }
                });
//                System.out.println("标段包：" + packageProjectionList.size());
                List<ProjectProjection> projectProjectionList = repository.queryProjectByMetaName(targetProjection.getMetaName());
                projectProjectionList.stream().forEach(projectProjection -> {
                    Vertex vertex = createProjectVertex(projectProjection);
                    if (!hasEdge(targetCode, EDGE_LABEL_PROJECT, projectProjection.getPrjCode())) {
                        Edge edge = targetVertex.addEdge(EDGE_LABEL_PROJECT, vertex);
                    }
                });
//                System.out.println("项目：" + projectProjectionList.size());
                List<BidSubjectProjection> toubiaos = repository.queryToubiaoByMetaName(targetProjection.getMetaName());
                toubiaos.stream().forEach(toubiao -> {
                    Vertex vertex = createBidSubjectVertex(toubiao);
                    if (!hasEdge(targetCode, EDGE_LABEL_BID_IN, toubiao.getSubjectCode())) {
                        Edge edge = targetVertex.addEdge(EDGE_LABEL_BID_IN, vertex);
                    }
                });
//                System.out.println("投标：" + toubiaos.size());
                List<BidSubjectProjection> zhaobiaos = repository.queryZhaobiaoByMetaName(targetProjection.getMetaName());
                zhaobiaos.stream().forEach(zhaobiao -> {
                    Vertex vertex = createBidSubjectVertex(zhaobiao);
                    if (!hasEdge(zhaobiao.getSubjectCode(), EDGE_LABEL_BID_OUT, targetCode)) {
                        Edge edge = vertex.addEdge(EDGE_LABEL_BID_OUT, targetVertex);
                    }
                });
//                System.out.println("招标：" + zhaobiaos.size());
                List<PriceProjection> priceProjectionList = repository.queryPriceByMetaName(targetProjection.getMetaName());
                priceProjectionList.stream().forEach(priceProjection -> {
                    Vertex vertex = createPriceVertex(priceProjection, targetProjection.getMetaCode());
                    if (!hasEdge(targetCode, EDGE_LABEL_PRICE, vertex.value(V_NODE_ID).toString())) {
                        Edge edge = targetVertex.addEdge(EDGE_LABEL_PRICE, vertex);
                    }
                });
//                System.out.println("价格：" + priceProjectionList.size());
                graph.tx().commit();
            }
        }

    }

    public Boolean hasEdge(String startId, String label, String endId) {
        try {
            Vertex endVertex = graph.traversal().V().has(V_NODE_ID, endId).next();
            return graph.traversal().V().has(V_NODE_ID, startId).out(label).hasId(endVertex.id()).hasNext();
        } catch (Exception e) {
            return false;
        }
    }

    public Vertex createTargetVertex(TargetProjection object) {
        String id = object.getMetaCode();
        if (graph.traversal().V().has(V_NODE_ID, id).hasNext()) {
            return graph.traversal().V().has(V_NODE_ID, id).next();
        }
        String name = object.getMetaName();
        Vertex vertex = graph.addVertex(V_LABEL_TARGET);
        if (id != null) {
            vertex.property(V_NODE_ID, id);
        }
        if (name != null) {
            vertex.property(V_NAME, name);
        }
        return vertex;
    }

    public Vertex createProjectVertex(ProjectProjection object) {
        String id = object.getPrjCode();
        if (graph.traversal().V().has(V_NODE_ID, id).hasNext()) {
            return graph.traversal().V().has(V_NODE_ID, id).next();
        }
        String name = object.getPrjName();
        Vertex vertex = graph.addVertex(V_LABEL_PROJECT);
        if (id != null) {
            vertex.property(V_NODE_ID, id);
        }
        if (name != null) {
            vertex.property(V_NAME, name);
        }
        return vertex;
    }

    public Vertex createBidSubjectVertex(BidSubjectProjection object) {
        String id = object.getSubjectCode();
        if (graph.traversal().V().has(V_NODE_ID, id).hasNext()) {
            return graph.traversal().V().has(V_NODE_ID, id).next();
        }
        String name = object.getSubjectName();
        Vertex vertex = graph.addVertex(V_LABEL_BID_SUBJECT);
        if (id != null) {
            vertex.property(V_NODE_ID, id);
        }
        if (name != null) {
            vertex.property(V_NAME, name);
        }
        return vertex;
    }

    public Vertex createPriceVertex(PriceProjection object, String targetId) {

        String price = object.getPrice();
        if (graph.traversal().V().has(V_NODE_ID, targetId).out(EDGE_LABEL_PRICE).has(V_PRICE, price).hasNext()) {
            return graph.traversal().V().has(V_NODE_ID, targetId).out(EDGE_LABEL_PRICE).has(V_PRICE, price).next();
        }
        String id = UUID.randomUUID().toString();
        Vertex vertex = graph.addVertex(V_LABEL_PRICE);
        if (id != null) {
            vertex.property(V_NODE_ID, id);
        }
        if (price != null) {
            vertex.property(V_PRICE, price);
        }
        return vertex;
    }

    public Vertex createPackageVertex(PackageProjection object) {
        String id = object.getPackageCode();
        if (graph.traversal().V().has(V_NODE_ID, id).hasNext()) {
            return graph.traversal().V().has(V_NODE_ID, id).next();
        }
        String name = object.getPackageName();
        String prjCode = object.getPrjCode();
        Vertex vertex = graph.addVertex(V_LABEL_PACKAGE);
        if (id != null) {
            vertex.property(V_NODE_ID, id);
        }
        if (name != null) {
            vertex.property(V_NAME, name);
        }
        if (prjCode != null) {
            vertex.property(V_PRJ_CODE, prjCode);
        }
        return vertex;
    }


    public void buildGraph() {
        JanusGraphManagement mgmt = graph.openManagement();

        //创建顶点Label
        VertexLabel labelBidSubject = mgmt.makeVertexLabel(V_LABEL_BID_SUBJECT).make();
        VertexLabel labelPackage = mgmt.makeVertexLabel(V_LABEL_PACKAGE).make();
        VertexLabel labelPrice = mgmt.makeVertexLabel(V_LABEL_PRICE).make();
        VertexLabel labelProject = mgmt.makeVertexLabel(V_LABEL_PROJECT).make();
        VertexLabel labelTarget = mgmt.makeVertexLabel(V_LABEL_TARGET).make();
        //创建边Label
        mgmt.makeEdgeLabel(EDGE_LABEL_BID_IN).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(EDGE_LABEL_BID_OUT).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(EDGE_LABEL_PRICE).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(EDGE_LABEL_PROJECT).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(EDGE_LABEL_PACKAGE).multiplicity(Multiplicity.MULTI).make();

        //创建属性
        //顶点共有属性
        PropertyKey nodeId = mgmt.makePropertyKey(V_NODE_ID).dataType(String.class).make();
        PropertyKey name = mgmt.makePropertyKey(V_NAME).dataType(String.class).make();

        //价格属性
        PropertyKey price = mgmt.makePropertyKey(V_PRICE).dataType(String.class).make();

        //标段包的项目属性
        PropertyKey prjCode = mgmt.makePropertyKey(V_PRJ_CODE).dataType(String.class).make();

        //关系共有属性


        //创建索引
        String nodeIdIndex = "nodeIdIndex";
        mgmt.buildIndex(nodeIdIndex, Vertex.class)
                .addKey(nodeId)
                .unique()
                .buildCompositeIndex();

        mgmt.commit();

        try {
            //注册索引
            ManagementSystem
                    .awaitGraphIndexStatus(graph, nodeIdIndex)
                    .status(SchemaStatus.REGISTERED)
                    .call();
            ManagementSystem.awaitGraphIndexStatus(graph, nodeIdIndex).status(SchemaStatus.ENABLED).call();
        } catch (Exception e) {
            log.error("注册索引失败", e);
        }
        //等待索引ok
        log.info("创建索引成功");
    }

    private Object objectArrayToObject(Object obejct) {
        try {
            return ((List) obejct).get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
