package org.zonesion.hadoop.hbase.api;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class GetScanner {

	public static void main(String[] args) throws NoSuchAlgorithmException {
//		list("zcloud","1155223953","1155223954");
//		list("zcloud","1155223953:00_12_4B_00_02_CB_A8_52_A0","1155223953:00_12_4B_00_02_CB_A8_52_A1");
//		list("zcloud","1155223953:00_12_4B_00_02_CB_A8_52_A1","1155223953:00_12_4B_00_02_CB_A8_52_A2");
//		list("zcloud","1155223953:00_12_4B_00_02_CB_A8_52_A1:hour","1155223953:00_12_4B_00_02_CB_A8_52_A1:houz");
//		list("zcloud","1155223953:00_12_4B_00_02_CB_A8_52_A1:day","1155223953:00_12_4B_00_02_CB_A8_52_A1:daz");
//		list("zcloud","367953136:00_12_4B_00_05_3C_E2_7B_A1:month","367953136:00_12_4B_00_05_3C_E2_7B_A1:montz");
//		list("zcloud","367953136","367953137");
		findByUserid("1155223953");
		findByChannal("1155223953", "00_12_4B_00_02_CB_A8_52_A0");
	}
	
	public static void findByChannal(String userid,String channal)  throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] useridhash  = md.digest(Bytes.toBytes(userid));
		byte[] channalBytes = Bytes.toBytes(channal);
		byte[] startkey = new byte[useridhash.length+channalBytes.length];
		byte[] endkey = new byte[useridhash.length+channalBytes.length];
		int offset = 0;
		offset = Bytes.putBytes(startkey, offset, useridhash, 0, useridhash.length);
		Bytes.putBytes(startkey, offset, channalBytes, 0, channalBytes.length);
		System.arraycopy(startkey, 0,endkey, 0, endkey.length);
		endkey[endkey.length-1]++;
		list("zcloud", startkey, endkey);
	}
	
	public static void findByUserid(String userid) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] useridhash  = md.digest(Bytes.toBytes(userid));
		byte[] endkey =  md.digest(Bytes.toBytes(userid));
		endkey[useridhash.length-1]++;
		list("zcloud", useridhash, endkey);
	}
	
	public static void list(String tableName,byte[] startkey,byte[] endkey){
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum","slave0.zonesion,slave1.zonesion,slave2.zonesion,slave3.zonesion,slave4.zonesion");
		HTable table = null;
		ResultScanner rs = null;
		try {
			Scan scan = new Scan(startkey,endkey); 
			table = new HTable(conf, tableName);
			rs = table.getScanner(scan);
			for(Result row : rs){
				System.out.format("ROW\t%s\n",new String(row.getRow()));
				for(Map.Entry<byte[], byte[]> entry : row.getFamilyMap("content".getBytes()).entrySet()){
					String column = new String(entry.getKey());
					String value = new String(entry.getValue());
					System.out.format("COLUMN\t content:%s\t%s\n",column,value);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null)rs.close();
			if(table!=null)
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static void list(String tableName,String startkey,String endkey) {
		list(tableName, Bytes.toBytes(startkey), Bytes.toBytes(endkey));
	}
}
