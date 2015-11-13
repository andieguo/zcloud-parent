package org.zonesion.hadoop.hbase.api;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;

public class PutRow {
	/**
	 * 添加记录
	 */
	public static void main(String[] args) {
		put("tb_admin","andieguo","info", "birthday","1990-09-08");
		put("tb_admin","andieugo","info","age","25");
		put("tb_admin", "andieguo", "info", "company", "zonesion");
		put("tb_admin", "andieguo", "address", "country", "China");
		put("tb_admin", "andieguo", "address", "province", "hubei");
		put("tb_admin", "andieguo", "address", "city", "wuhan");
	}

	public static void put(String tabelName,String rowKey,String family,String qualifier,String value) {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum","slave0.zonesion,slave1.zonesion,slave2.zonesion,slave3.zonesion,slave4.zonesion");
		HTable table = null;
		try {
			table = new HTable(conf, tabelName);
			Put putRow1 = new Put(rowKey.getBytes());
			putRow1.add(family.getBytes(), qualifier.getBytes(),value.getBytes());
			table.put(putRow1);
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
