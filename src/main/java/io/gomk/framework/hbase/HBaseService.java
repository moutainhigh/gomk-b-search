package io.gomk.framework.hbase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;

/**
 * @Auther: ZHG
 * @Date: 2019/5/23 0023 10:04
 * @Description:
 */
public class HBaseService {

	private Logger log = LoggerFactory.getLogger(HBaseService.class);
	private Admin admin = null;
	private Connection connection = null;

	public HBaseService(Configuration conf) {
		try {
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
		} catch (IOException e) {
			log.error("获取HBase连接失败");
		}
	}

	/**
	 * 创建表 create
	 * <table>
	 * , {NAME => <column family>, VERSIONS => <VERSIONS>} shell command: create
	 * ‘user’, ‘cf1’
	 */
	public boolean creatTable(String tableName, List<String> columnFamily) {
		try {
			// 列族 column family
			List<ColumnFamilyDescriptor> cfDesc = new ArrayList<ColumnFamilyDescriptor>(columnFamily.size());
			columnFamily.forEach(cf -> {
				cfDesc.add(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build());
			});
			// 表 table
			TableDescriptor tableDesc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
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
	 * 查询库中所有表的表名 shell command: list
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
	 * 遍历查询指定表中的所有数据 shell command: scan 'user'
	 */
	public Map<String, Map<String, String>> getResultScanner(String tableName) {
		Scan scan = new Scan();
		return this.queryData(tableName, scan);
	}

	/**
	 * 通过表名以及过滤条件查询数据
	 */
	private Map<String, Map<String, String>> queryData(String tableName, Scan scan) {
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
						rowKey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
					}
					columnMap.put(
							// 列限定符
							Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
									cell.getQualifierLength()),
							// 列族
							Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
				}

				if (rowKey != null) {
					result.put(rowKey, columnMap);
				}
			}
		} catch (IOException e) {
			log.error(MessageFormat.format("遍历查询指定表中的所有数据失败,tableName:{0}", tableName), e);
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
							Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
									cell.getQualifierLength()),
							Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
				}
			}
		} catch (IOException e) {
			log.error(MessageFormat.format("查询一行的数据失败,tableName:{0},rowKey:{1}", tableName, rowKey), e);
		} finally {
			close(null, null, table);
		}

		return result;
	}

	/**
	 * 为表添加 or 更新数据
	 */
	// 放置多个key-value方法
	public void putData(String tableName, String rowKey, String familyName, String[] columns, String[] values) {
		// 获取表
		Table table = null;
		try {
			table = getTable(tableName);

			putData(table, rowKey, tableName, familyName, columns, values);
		} catch (Exception e) {
			log.error(MessageFormat.format("为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}", tableName, rowKey,
					familyName), e);
		} finally {
			close(null, null, table);
		}
	}

	// 放置单个key-value方法
	public void putData(String tableName, String rowKey, String familyName, String column, byte[] value) {
//        System.out.println("PUT value To Hbase..............................................");
		log.info("PUT value To Hbase Start............................................");
		// 获取表
		Table table = null;
		try {
			table = getTable(tableName);
			putData(table, rowKey, tableName, familyName, column, value);
		} catch (Exception e) {
			log.error(MessageFormat.format("为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}", tableName, rowKey,
					familyName), e);
		} finally {
			close(null, null, table);
		}
	}

	// 放置多个key-value方法
	private void putData(Table table, String rowKey, String tableName, String familyName, String[] columns,
			String[] values) {
		try {
			// 设置rowkey
			Put put = new Put(Bytes.toBytes(rowKey));

			if (columns != null && values != null && columns.length == values.length) {
				for (int i = 0; i < columns.length; i++) {
					if (columns[i] != null && values[i] != null) {
						put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columns[i]), Bytes.toBytes(values[i]));
					} else {
						throw new NullPointerException(
								MessageFormat.format("列名和列数据都不能为空,column:{0},value:{1}", columns[i], values[i]));
					}
				}
			}

			table.put(put);
			log.debug("putData add or update data Success,rowKey:" + rowKey);
			table.close();
		} catch (Exception e) {
			log.error(MessageFormat.format("为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}", tableName, rowKey,
					familyName), e);
		}
	}

//    放置单个key-value方法
	private void putData(Table table, String rowKey, String tableName, String familyName, String columns,
			byte[] values) {
		try {
			// 设置rowkey
			Put put = new Put(Bytes.toBytes(rowKey));

			if (columns != null && values != null) {
				put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columns), values);
				log.info("PUT value To Hbase Success............................................");
			} else {
				throw new NullPointerException(
						MessageFormat.format("列名和列数据都不能为空,column:{0},value:{1}", columns, new String(values)));
			}

			table.put(put);
			log.debug("putData add or update data Success,rowKey:" + rowKey);
			table.close();
		} catch (Exception e) {
			log.error(MessageFormat.format("为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}", tableName, rowKey,
					familyName), e);
		}
	}

	/**
	 * 根据表名 获取table Used to communicate with a single HBase table. Table can be used
	 * to get, put, delete or scan data from a table.
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

	public InputStream down(String text) throws IOException {
		BASE64Decoder be = new BASE64Decoder();
		byte[] c = be.decodeBuffer(text);
		InputStream in = new ByteArrayInputStream(c);
		return in;
	}

	public void GetData(String tableName, String rowKey) {
		System.out.println("GET value..............................................");
		Map<String, String> result = new HashMap<>();

		Get get = new Get(Bytes.toBytes(rowKey));
		System.out.println(get);
		// 获取表
		Table table = null;
		BASE64Decoder bd = new BASE64Decoder();
		try {
			table = getTable(tableName);
			System.out.println(table);
			Result hTableResult = table.get(get);
			System.out.println(hTableResult);
			System.out.println("表获取成功" + table);

//            Get get = new Get(rowKey.getBytes());
			// get.addFamily(COLUMN_FAMILY_NAME.getBytes());
			// get.addColumn(COLUMN_FAMILY_NAME.getBytes(),COLUMNS[0].getBytes());
//            Result result = table.get(get);
			// get column family
//            result.getFamilyMap(COLUMN_FAMILY[0].getBytes()).forEach((k,v) ->
//                    System.out.println(new String(k) + ":" + new String(v)));
			NavigableMap<byte[], byte[]> familyMap = hTableResult.getFamilyMap("a".getBytes());
			System.out.println("文件获取完成： " + familyMap.keySet().size());
			File file = null;
			FileOutputStream fos = null;
			for (byte[] bytes : familyMap.keySet()) {
//                String nameUrl = new String(bytes, "UTF-8");
				System.out.println(rowKey);
				file = new File("/temp/" + rowKey + ".pdf");
				fos = new FileOutputStream(file);
				byte[] bytes1 = bd.decodeBuffer(new String(familyMap.get(bytes)));
				fos.write(bytes1);
			}
			fos.close();
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InputStream getInputStream(String tableName, String rowKey) {

		Get get = new Get(Bytes.toBytes(rowKey));
		// 获取表
		Table table = null;
		BASE64Decoder bd = new BASE64Decoder();
		try {
			table = getTable(tableName);
			Result hTableResult = table.get(get);
		//	System.out.println("表获取成功" + hTableResult.toString());

			NavigableMap<byte[], byte[]> familyMap = hTableResult.getFamilyMap("a".getBytes());
			System.out.println("文件获取完成： " + familyMap.keySet().size());
			for (byte[] bytes : familyMap.keySet()) {
				byte[] bytes1 = bd.decodeBuffer(new String(familyMap.get(bytes)));
				InputStream input = new ByteArrayInputStream(bytes1);
				return input;
			}
			table.close();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
			//e.printStackTrace();
		}

		return null;
	}

}
