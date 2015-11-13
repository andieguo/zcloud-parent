package org.zonesion.hbase.service;


import java.util.List;

import org.zonesion.hadoop.hbase.bean.QueryResult;
import org.zonesion.hadoop.hbase.service.HConnectionService;
import org.zonesion.hadoop.hbase.service.QueryResultServiceImpl;

import junit.framework.TestCase;

public class QueryResultServiceImplTest extends TestCase
{

	private QueryResultServiceImpl service ;
	private HConnectionService hConnectionService;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		hConnectionService = HConnectionService.getInstance("zcloud");//单例模式
		hConnectionService.connect();
		service = new QueryResultServiceImpl(hConnectionService);
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		hConnectionService.disconnect();
	}
	
	public void testFindByUserid(){
		for(QueryResult queryResult:service.findByUserid("1155223953")){
			System.out.println(queryResult);
		}
	}
	
	public void testFindByChannal(){
		for(QueryResult queryResult:service.findByChannal("1155223953","00_12_4B_00_02_CB_A8_52_A0")){
			System.out.println(queryResult);
		}
	}
	
	public void testFindByType(){
		for(QueryResult queryResult:service.findByType("23710173","00_12_4B_00_01_49_2C_42_A0","month",200)){
			System.out.println(queryResult);
		}
	}
	
	public void testFindByType1(){
		List<QueryResult> results = service.findByType("23710173", "00_12_4B_00_01_49_2C_42_A0", "year","2014-05-21T11:00:00Z","2015-05-21T11:00:00Z");
		for(QueryResult result :results){
			System.out.println(result);
		}
	}
	
	public void testFindByType2(){
		List<QueryResult> results = service.findByType("23710173", "00_12_4B_00_01_49_2C_42_A0", "month","2014-05-21T11:00:00Z","2015-05-21T11:00:00Z");
		for(QueryResult result :results){
			System.out.println(result);
		}
	}
	
	public void testFindByType3(){
		List<QueryResult> results = service.findByType("23710173", "00_12_4B_00_01_49_2C_42_A0", "day","2014-05-21T11:00:00Z","2015-05-21T11:00:00Z");
		for(QueryResult result :results){
			System.out.println(result);
		}
	}
	
	public void testFindByType4(){
		List<QueryResult> results = service.findByType("23710173", "00_12_4B_00_01_49_2C_42_A0", "hour","2014-05-21T11:00:00Z","2015-05-21T11:00:00Z");
		for(QueryResult result :results){
			System.out.println(result);
		}
	}
    
}

