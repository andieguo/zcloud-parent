package org.zonesion.webapp.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.zonesion.hadoop.hbase.bean.QueryResult;
import org.zonesion.hadoop.hbase.service.HConnectionService;
import org.zonesion.hadoop.hbase.service.QueryResultService;
import org.zonesion.hadoop.hbase.service.QueryResultServiceImpl;

/**
 * 每次GET请求访问该路径,QueryResultResource就会被执行一次，故使用单例模式获取HConnectionService，而HConnectionService的connect()方法放在启动web
容器时执行，disconnect()方法在关闭web容器时执行。
**/
@Path("/result")
public class QueryResultResource {

	private HConnectionService hConnectionService = HConnectionService.getInstance("zcloud");//单例模式
	private QueryResultService service = new QueryResultServiceImpl(hConnectionService);
	
	//默认返回200条记录
	@GET
	@Path("/userid/{userid}/")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<QueryResult> getQueryResult(@PathParam("userid") String userid){
		return service.findByUserid(userid);
	}
	
	//指定返回记录数
	@GET
	@Path("/userid/{userid}/limit/{limit}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<QueryResult> getQueryResult(@PathParam("userid") String userid,@PathParam("limit")  long limit){
		return service.findByUserid(userid,limit);
	}
	
	//默认返回200条记录
	@GET
	@Path("/userid/{userid}/channal/{channal}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<QueryResult> getQueryResult(@PathParam("userid") String userid,@PathParam("channal") String channal){
		return service.findByChannal(userid, channal);
	}
	
	//指定返回记录数
	@GET
	@Path("/userid/{userid}/channal/{channal}/limit/{limit}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<QueryResult> getQueryResult(@PathParam("userid") String userid,@PathParam("channal") String channal,@PathParam("limit") long limit){
		return service.findByChannal(userid, channal,limit);
	}
	
	//默认返回200条记录
	@GET
	@Path("/userid/{userid}/channal/{channal}/type/{type}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<QueryResult> getQueryResult(@PathParam("userid") String userid,@PathParam("channal") String channal,@PathParam("type") String type){
		return service.findByType(userid, channal, type);
	}
	
	//指定返回的记录数
	@GET
	@Path("/userid/{userid}/channal/{channal}/type/{type}/limit/{limit}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<QueryResult> getQueryResult(@PathParam("userid") String userid,@PathParam("channal") String channal,@PathParam("type") String type,@PathParam("limit") long limit){
		return service.findByType(userid, channal, type,limit);
	}
	
	//指定starttime和endtime，默认返回200
	@GET
	@Path("/userid/{userid}/channal/{channal}/type/{type}/starttime/{starttime}/endtime/{endtime}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<QueryResult> getQueryResult(@PathParam("userid") String userid,@PathParam("channal") String channal,@PathParam("type") String type,@PathParam("starttime") String starttime,@PathParam("endtime") String endtime){
		return service.findByType(userid, channal, type,starttime,endtime);
	}
	
	//指定starttime和endtime，指定返回记录条数
	@GET
	@Path("/userid/{userid}/channal/{channal}/type/{type}/starttime/{starttime}/endtime/{endtime}/limit/{limit}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<QueryResult> getQueryResult(@PathParam("userid") String userid,@PathParam("channal") String channal,@PathParam("type") String type,@PathParam("starttime") String starttime,@PathParam("endtime") String endtime,@PathParam("limit") long limit){
		return service.findByType(userid, channal, type,starttime,endtime,limit);
	}
	
}
