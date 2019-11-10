package io.gomk.common.constants;

public interface GraphConstant {
    /**
     * 点的label
     */
    /**
     * 标的物
     */
    String V_LABEL_TARGET = "TARGET";
    /**
     * 项目
     */
    String V_LABEL_PROJECT = "PROJECT";
    /**
     * 招标投标主体
     */
    String V_LABEL_BID_SUBJECT = "BID_SUBJECT";
    /**
     * 标段包
     */
    String V_LABEL_PACKAGE = "PACKAGE";
    /**
     * 价格
     */
    String V_LABEL_PRICE = "PRICE";

    /**
     * 点的属性
     */
    String V_NODE_ID = "nodeId";
    String V_NAME = "name";

    String V_PRICE = "price";
    String V_PRJ_CODE = "prjCode";

    /**
     * 线的label
     */
    String EDGE_LABEL_PROJECT = "PROJECT";
    /**
     * 招标
     */
    String EDGE_LABEL_BID_OUT = "BID_OUT";

    /**
     * 投标
     */
    String EDGE_LABEL_BID_IN = "BID_IN";

    /**
     * 标段包
     */
    String EDGE_LABEL_PACKAGE = "PACKAGE";

    /**
     * 价格
     */
    String EDGE_LABEL_PRICE = "PRICE";

    String DATA_ID = "id";
    String DATA_START_NODE = "startNode";
    String DATA_END_NODE = "endNode";
    String LABEL = "label";


}
