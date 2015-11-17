package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.FSConstants.DatanodeReportType;
import org.json.JSONObject;
import org.zonesion.webapp.bean.DataNodeInfo;

public class NameNodeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4670311237960053132L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		InetSocketAddress nameNodeAddr = new InetSocketAddress("192.168.100.141", 9000);
		ClientProtocol clientProtocl;
		List<DataNodeInfo> datanodes = new ArrayList<DataNodeInfo>();
		try {
			clientProtocl = DFSClient.createNamenode(nameNodeAddr, conf);
			DatanodeInfo[] datanodeInfoList = clientProtocl.getDatanodeReport(DatanodeReportType.ALL);
			float capacity=0,nonuserd=0,dfsused=0;
			for(DatanodeInfo info:datanodeInfoList){
				capacity = capacity+info.getCapacity();
				dfsused = dfsused+info.getDfsUsed();
				nonuserd = nonuserd+info.getNonDfsUsed();
				String adminState = (info.isDecommissioned()?"Decommissioned":(info.isDecommissionInProgress()?"Decommission In Progress":"In Service"));
				DataNodeInfo datenode = new DataNodeInfo(info.getCapacity(),info.getDfsUsed(),info.getNonDfsUsed(),info.getName(),adminState);
				datanodes.add(datenode);
			}
			response.setContentType("application/x-json");// 需要设置ContentType 为"application/x-json"
			JSONObject resultJSON = new JSONObject();// 构建一个JSONObject
			resultJSON.put("capacity", capacity);
			resultJSON.put("dfsused", dfsused);
			resultJSON.put("nondfsused", nonuserd);
			resultJSON.put("datanode", datanodes);
			PrintWriter out = response.getWriter();
			out.println(resultJSON.toString());// 向客户端输出JSONObject字符串
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
