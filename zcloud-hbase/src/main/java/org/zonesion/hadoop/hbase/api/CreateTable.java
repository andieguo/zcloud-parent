package org.zonesion.hadoop.hbase.api;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
// 默认的构造方式会尝试从hbase-default.xml和hbase-site.xml中读取配置。如果classpath没有这两个文件，就需要你自己设置配置。
// 由于export HBASE_MANAGES_ZK=true
// //此配置信息，设置由hbase自己管理zookeeper；故设置conf.set("hbase.zookeeper.quorum",
// "Master");
public class CreateTable {
	/**
	 * 创建表
	 */
	public static void main(String[] args) {
		create("tb_admin","info","address");
	}

	public static void create(String tableName,String... families) {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum","slave2.zonesion,slave3.zonesion,slave4.zonesion");
		HBaseAdmin admin = null;
		try {
			admin = new HBaseAdmin(conf);
			HTableDescriptor tableDescriptor = new HTableDescriptor(
					tableName.getBytes());
			if (admin.tableExists(tableName)) {// 表存在
				System.out.println(tableName + "已经存在！");
			} else {
				for(String family :families){
					tableDescriptor.addFamily(new HColumnDescriptor(family));
				}
				admin.createTable(tableDescriptor);
			}
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (admin != null)
				try {
					System.out.println("关闭HBaseAdmin");
					admin.close();
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
