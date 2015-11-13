package org.zonesion.hadoop.hbase.service;

import java.util.*;

import org.zonesion.hadoop.hbase.bean.QueryResult;

public interface QueryResultService {

	public List<QueryResult> findByUserid(String userid);
	
	public List<QueryResult> findByUserid(String userid,long limit);

	public List<QueryResult> findByChannal(String userid,String channal);
	
	public List<QueryResult> findByChannal(String userid,String channal,long limit);
	
	public List<QueryResult> findByType(String userid,String channal,String type);
	
	public List<QueryResult> findByType(String userid,String channal,String type,long limit);
	
	public List<QueryResult> findByType(String userid, String channal,String type,String starttime,String endtime);
	
	public List<QueryResult> findByType(String userid, String channal,String type,String starttime,String endtime,long limit);
	
}
