package org.zonesion.hadoop.hbase.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;
import org.zonesion.hadoop.hbase.bean.QueryResult;

public class QueryResultServiceImpl implements QueryResultService{
	
	private HConnectionService hConnectionService;
	
	public QueryResultServiceImpl(HConnectionService hConnectionService) {
		super();
		this.hConnectionService = hConnectionService;
	}
	
	public List<QueryResult> findByUserid(String userid) {
		return findByUserid(userid,200);
	}

	public List<QueryResult> findByUserid(String userid,long limit) {
		// TODO Auto-generated method stub
		MessageDigest md;
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] useridhash  = md.digest(Bytes.toBytes(userid));
			byte[] endkey =  md.digest(Bytes.toBytes(userid));
			endkey[useridhash.length-1]++;
			queryResults = hConnectionService.scan(useridhash, endkey,limit);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queryResults;
	}
	
	public List<QueryResult> findByChannal(String userid, String channal) {
		return findByChannal(userid, channal, 200);
	}

	public List<QueryResult> findByChannal(String userid, String channal,long limit) {
		// TODO Auto-generated method stub
		MessageDigest md;
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] useridhash  = md.digest(Bytes.toBytes(userid));
			byte[] channalBytes = Bytes.toBytes(channal);
			byte[] startkey = new byte[useridhash.length+channalBytes.length];
			byte[] endkey = new byte[useridhash.length+channalBytes.length];
			int offset = 0;
			offset = Bytes.putBytes(startkey, offset, useridhash, 0, useridhash.length);
			Bytes.putBytes(startkey, offset, channalBytes, 0, channalBytes.length);
			System.arraycopy(startkey, 0,endkey, 0, endkey.length);
			endkey[endkey.length-1]++;
			queryResults = hConnectionService.scan(startkey, endkey,limit);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return queryResults;
	}
	
	public List<QueryResult> findByType(String userid, String channal,String type,String starttime,String endtime){
		return findByType(userid, channal, type,starttime,endtime,200);
	}
	
	public List<QueryResult> findByType(String userid, String channal,String type,String starttime,String endtime,long limit){
		//2014-05-21T11:00:00Z - 2015-05-21T11:00:00Z
		MessageDigest md;
		List<QueryResult> queryResults = new ArrayList<QueryResult>();	
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date startDate = format.parse(starttime);
			Date endDate = format.parse(endtime);
			SimpleDateFormat fomateDate = null;
			if(type.equals("year")){
				fomateDate  = new SimpleDateFormat("yyyy");
			}else if(type.equals("month")){
				fomateDate  = new SimpleDateFormat("yyyy-MM");
			}else if(type.equals("day")){
				fomateDate = new SimpleDateFormat("yyyy-MM-dd");
			}else if(type.equals("hour")){
				fomateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
			}
			String startstr =  fomateDate.format(startDate);
			String endstr = fomateDate.format(endDate);
			//构造startbytes,endbytes
			md = MessageDigest.getInstance("MD5");
			byte[] useridhash  = md.digest(Bytes.toBytes(userid));
			byte[] channalBytes = Bytes.toBytes(channal);
			byte[] typeBytes = Bytes.toBytes(":"+type);
			byte[] dateBytes = Bytes.toBytes(":"+startstr);
			int len = useridhash.length+channalBytes.length+typeBytes.length+dateBytes.length;
			byte[] startkey = new byte[len];
			buildBytes(startkey,useridhash,channalBytes,typeBytes,dateBytes);
			byte[] endkey = new byte[len];
			dateBytes = Bytes.toBytes(":"+endstr);
			System.out.println(new String(startkey));
			buildBytes(endkey,useridhash,channalBytes,typeBytes,dateBytes);
			System.out.println(new String(endkey));
			queryResults = hConnectionService.scan(startkey, endkey,limit);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return queryResults;
	}
	
	public byte[] buildBytes(byte[] tgtBytes ,byte[]... srcByteses){
		int offset = 0;
		for(byte[] srcBytes:srcByteses){
			offset = 	Bytes.putBytes(tgtBytes, offset, srcBytes, 0, srcBytes.length);
		}
		return tgtBytes;
	}

	public List<QueryResult> findByType(String userid, String channal,String type,long limit) {
		MessageDigest md;
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] useridhash  = md.digest(Bytes.toBytes(userid));
			byte[] channalBytes = Bytes.toBytes(channal);
			byte[] typeBytes = Bytes.toBytes(":"+type);
			byte[] startkey = new byte[useridhash.length+channalBytes.length+typeBytes.length];
			byte[] endkey = new byte[useridhash.length+channalBytes.length+typeBytes.length];
			buildBytes(startkey,useridhash,channalBytes,typeBytes);
			System.arraycopy(startkey, 0,endkey, 0, endkey.length);
			endkey[endkey.length-1]++;
			queryResults = hConnectionService.scan(startkey, endkey,limit);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return queryResults;
	}

	public List<QueryResult> findByType(String userid, String channal,String type) {
		// TODO Auto-generated method stub
		return  findByType( userid,  channal, type, 200) ;
	}
	
}
