package org.zonesion.hadoop.hbase.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.zonesion.hadoop.base.util.PropertiesUtil;
import org.zonesion.hadoop.hbase.bean.QueryResult;

public class HConnectionService {
	
	private HConnection connection;
	private HTableInterface htable;
	private static HConnectionService hConnectionService;
	private String tablename;
	
	private HConnectionService(String tablename){
		this.tablename = tablename;
	}
	
	public static HConnectionService getInstance(String tablename){//单例模式
		if(hConnectionService ==null){
			hConnectionService = new HConnectionService(tablename);
		}
		return hConnectionService;
	}
	
	public void connect(){
		Configuration conf = HBaseConfiguration.create();
		Properties properties = PropertiesUtil.loadFromInputStream(this.getClass().getResourceAsStream("/hbase-config.properties"));
		conf.set("hbase.zookeeper.quorum",properties.getProperty("hbase.zookeeper.quorum"));
		try {
			 connection = HConnectionManager.createConnection(conf);
			 htable = connection.getTable(tablename);
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public List<QueryResult> findByUserid(String userid) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] useridhash  = md.digest(Bytes.toBytes(userid));
		byte[] endkey =  md.digest(Bytes.toBytes(userid));
		endkey[useridhash.length-1]++;
		List<QueryResult> result = scan(useridhash, endkey,200);
		return result;
	}
	
	public List<QueryResult> scan(byte[] startkey,byte[] endkey,long number){
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		ResultScanner scanner = null;
		try {
			Scan scan = new Scan(startkey, endkey); 
			Filter filter = new PageFilter(number);
			scan.setFilter(filter);	
			ResultScanner rs = htable.getScanner(scan); 
			for(Result row : rs){
				QueryResult queryResult = new QueryResult();
				for(Map.Entry<byte[], byte[]> entry : row.getFamilyMap("content".getBytes()).entrySet()){
					String column = new String(entry.getKey());
					String value = new String(entry.getValue());
					if(column.equals("at")) queryResult.setAt(value);
					if(column.equals("channal")) queryResult.setChannal(value);
					if(column.equals("userid")) queryResult.setUserid(value);
					if(column.equals("type")) queryResult.setType(value);
					if(column.equals("min")) queryResult.setMin(Float.valueOf(value));
					if(column.equals("max")) queryResult.setMax(Float.valueOf(value));
					if(column.equals("avg")) queryResult.setAvg(Float.valueOf(value));
					if(column.equals("classify")) queryResult.setClassify(value);
				}
				queryResults.add(queryResult);
			}
			return queryResults;
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(scanner!=null) scanner.close();
		}
		return queryResults;
	}
	
	public void disconnect(){
			try {
				if(htable != null) htable.close();
				if(connection != null) connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		HConnectionService hConnectionService = new HConnectionService("zcloud");
		hConnectionService.connect();
		List<QueryResult> queryResult = hConnectionService.findByUserid("1155223953");
		System.out.println(queryResult.size());
		hConnectionService.disconnect();
	}

}
