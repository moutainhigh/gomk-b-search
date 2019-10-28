package io.gomk.framework.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ZHG
 * @Date: 2019/5/23 0023 10:04
 * @Description:
 */
//@Service
public class HBaseService {

    private Logger log = LoggerFactory.getLogger(HBaseService.class);
    private Admin admin = null;
    private Connection connection = null;
    
    static Configuration conf=null;
    static {
        conf= HBaseConfiguration.create();
        //conf.set("hbase.zookeeper.quorum","58.119.224.26");
        conf.set("hbase.zookeeper.quorum","10.212.169.157");
        conf.set("hbase.zookeeper.property.clientPort","2181");
        conf.set("log4j.logger.org.apache.hadoop.hbase","WARN");
    }

    public HBaseService() {
        try {
            connection = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
        } catch (IOException e) {
            log.error("获取HBase连接失败");
        }
    }

    /**
     * 创建表
     * create <table>, {NAME => <column family>, VERSIONS => <VERSIONS>}
     * shell command: create ‘user’, ‘cf1’
     */
    public boolean creatTable(String tableName, List<String> columnFamily) {
        try {
            //列族 column family
            List<ColumnFamilyDescriptor> cfDesc = new ArrayList<ColumnFamilyDescriptor>(columnFamily.size());
            columnFamily.forEach(cf -> {
                cfDesc.add(ColumnFamilyDescriptorBuilder.newBuilder(
                        Bytes.toBytes(cf)).build());
            });
            //表 table
            TableDescriptor tableDesc = TableDescriptorBuilder
                    .newBuilder(TableName.valueOf(tableName))
                    .setColumnFamilies(cfDesc).build();

//            if (admin.tableExists(TableName.valueOf(tableName))) {
//                log.debug("table Exists!");
//            } else {
                admin.createTable(tableDesc);
                log.debug("create table Success!");
           // }
        } catch (IOException e) {
            log.error(MessageFormat.format("创建表{0}失败", tableName), e);
            return false;
        } finally {
            close(admin, null, null);
        }
        return true;
    }

    /**
     * 查询库中所有表的表名
     * shell command: list
     */
    public List<String> getAllTableNames() {
        List<String> result = new ArrayList<>();
        try {
            TableName[] tableNames = admin.listTableNames();
            for (TableName tableName : tableNames) {
                result.add(tableName.getNameAsString());
            }
        } catch (IOException e) {
            log.error("获取所有表的表名失败", e);
        } finally {
            close(admin, null, null);
        }
        return result;
    }

    /**
     * 遍历查询指定表中的所有数据
     * shell command: scan 'user'
     */
    public Map<String, Map<String, String>> getResultScanner(String tableName) {
        Scan scan = new Scan();
        return this.queryData(tableName, scan);
    }

    /**
     * 通过表名以及过滤条件查询数据
     */
    private Map<String, Map<String, String>> queryData(String tableName,
                                                       Scan scan) {
        // <rowKey,对应的行数据>
        Map<String, Map<String, String>> result = new HashMap<>();

        ResultScanner rs = null;
        // 获取表
        Table table = null;
        try {
            table = getTable(tableName);
            rs = table.getScanner(scan);
            for (Result r : rs) {
                // 每一行数据
                Map<String, String> columnMap = new HashMap<>();
                String rowKey = null;
                // 行键，列族和列限定符一起确定一个单元（Cell）
                for (Cell cell : r.listCells()) {
                    if (rowKey == null) {
                        rowKey = Bytes.toString(cell.getRowArray(),
                                cell.getRowOffset(), cell.getRowLength());
                    }
                    columnMap.put(
                            // 列限定符
                            Bytes.toString(cell.getQualifierArray(),
                                    cell.getQualifierOffset(),
                                    cell.getQualifierLength()),
                            // 列族
                            Bytes.toString(cell.getValueArray(),
                                    cell.getValueOffset(),
                                    cell.getValueLength()));
                }

                if (rowKey != null) {
                    result.put(rowKey, columnMap);
                }
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("遍历查询指定表中的所有数据失败,tableName:{0}",
                    tableName), e);
        } finally {
            close(null, rs, table);
        }

        return result;
    }

    /**
     * 根据tableName和rowKey精确查询行数据
     */
    public Map<String, String> getRowData(String tableName, String rowKey) {
        // 返回的键值对
        Map<String, String> result = new HashMap<>();

        Get get = new Get(Bytes.toBytes(rowKey));
        // 获取表
        Table table = null;
        try {
            table = getTable(tableName);
            Result hTableResult = table.get(get);
            if (hTableResult != null && !hTableResult.isEmpty()) {
                for (Cell cell : hTableResult.listCells()) {
                    result.put(
                            Bytes.toString(cell.getQualifierArray(),
                                    cell.getQualifierOffset(),
                                    cell.getQualifierLength()),
                            Bytes.toString(cell.getValueArray(),
                                    cell.getValueOffset(),
                                    cell.getValueLength()));
                }
            }
        } catch (IOException e) {
            log.error(MessageFormat.format(
                    "查询一行的数据失败,tableName:{0},rowKey:{1}", tableName, rowKey), e);
        } finally {
            close(null, null, table);
        }

        return result;
    }

    /**
     * 为表添加 or 更新数据
     */
    //    放置多个key-value方法
    public void putData(String tableName, String rowKey, String familyName,
                        String[] columns, String[] values) {
        // 获取表
        Table table = null;
        try {
            table = getTable(tableName);

            putData(table, rowKey, tableName, familyName, columns, values);
        } catch (Exception e) {
            log.error(MessageFormat.format(
                    "为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}",
                    tableName, rowKey, familyName), e);
        } finally {
            close(null, null, table);
        }
    }
    //    放置单个key-value方法
    public void putData(String tableName, String rowKey, String familyName,
                        String column, byte[] value) {
//        System.out.println("PUT value To Hbase..............................................");
        log.info("PUT value To Hbase Start............................................");
        // 获取表
        Table table = null;
        try {
            table = getTable(tableName);
            putData(table, rowKey, tableName, familyName, column, value);
        } catch (Exception e) {
            log.error(MessageFormat.format(
                    "为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}",
                    tableName, rowKey, familyName), e);
        } finally {
            close(null, null, table);
        }
    }
    //    放置多个key-value方法
    private void putData(Table table, String rowKey, String tableName,
                         String familyName, String[] columns, String[] values) {
        try {
            // 设置rowkey
            Put put = new Put(Bytes.toBytes(rowKey));

            if (columns != null && values != null
                    && columns.length == values.length) {
                for (int i = 0; i < columns.length; i++) {
                    if (columns[i] != null && values[i] != null) {
                        put.addColumn(Bytes.toBytes(familyName),
                                Bytes.toBytes(columns[i]),
                                Bytes.toBytes(values[i]));
                    } else {
                        throw new NullPointerException(MessageFormat.format(
                                "列名和列数据都不能为空,column:{0},value:{1}", columns[i],
                                values[i]));
                    }
                }
            }

            table.put(put);
            log.debug("putData add or update data Success,rowKey:" + rowKey);
            table.close();
        } catch (Exception e) {
            log.error(MessageFormat.format(
                    "为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}",
                    tableName, rowKey, familyName), e);
        }
    }
//    放置单个key-value方法
    private void putData(Table table, String rowKey, String tableName,
                         String familyName, String columns, byte[] values) {
        try {
            // 设置rowkey
            Put put = new Put(Bytes.toBytes(rowKey));

            if (columns != null && values != null) {
                        put.addColumn(Bytes.toBytes(familyName),
                                Bytes.toBytes(columns),
                               values);
             log.info("PUT value To Hbase Success............................................");
            }else {
                throw new NullPointerException(MessageFormat.format(
                        "列名和列数据都不能为空,column:{0},value:{1}", columns,
                        new String(values)));
            }

            table.put(put);
            log.debug("putData add or update data Success,rowKey:" + rowKey);
            table.close();
        } catch (Exception e) {
            log.error(MessageFormat.format(
                    "为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}",
                    tableName, rowKey, familyName), e);
        }
    }
    /**
     * 根据表名 获取table
     * Used to communicate with a single HBase table.
     * Table can be used to get, put, delete or scan data from a table.
     */
    private Table getTable(String tableName) throws IOException {
        return connection.getTable(TableName.valueOf(tableName));
    }

    /**
     * 关闭流
     */
    private void close(Admin admin, ResultScanner rs, Table table) {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                log.error("关闭Admin失败", e);
            }
        }
        if (rs != null) {
            rs.close();
        }
        if (table != null) {
            try {
                table.close();
            } catch (IOException e) {
                log.error("关闭Table失败", e);
            }
        }
    }

}
