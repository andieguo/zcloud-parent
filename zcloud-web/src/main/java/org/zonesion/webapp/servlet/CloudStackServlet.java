package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.zonesion.hadoop.base.util.PropertiesUtil;
import org.zonesion.hadoop.base.util.Rest;

public class CloudStackServlet extends HttpServlet {

	private static final long serialVersionUID = -7751517020504894974L;
	private Logger logger =  Logger.getLogger(CloudStackServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Properties properties = PropertiesUtil.loadFromInputStream(this.getClass().getResourceAsStream("/config.properties"));
		String command = request.getParameter("command");
		String hostname = properties.getProperty("cloudstack.api.hostname");
		String port = properties.getProperty("cloudstack.api.port");
		logger.info("command:"+command);
		logger.info("hostname:"+hostname);
		logger.info("port:"+port);
		String url = null;
		try {
			if(command.equals("listVirtualMachines")){
				url = MessageFormat.format("http://{0}:{1}/client/api?command={2}&details=all&account=admin&response=json", hostname,port,command);
			}else if(command.equals("listZones")){
				url = MessageFormat.format("http://{0}:{1}/client/api?command={2}&showcapacities=true&response=json", hostname,port,command);
			}else if(command.equals("listClusters")){
				url = MessageFormat.format("http://{0}:{1}/client/api?command={2}&showcapacities=true&response=json", hostname,port,command);
			}else if(command.equals("listHosts")){
				url = MessageFormat.format("http://{0}:{1}/client/api?command={2}&details=capacity&response=json", hostname,port,command);
			}else if(command.equals("listVolumes")){
				url = MessageFormat.format("http://{0}:{1}/client/api?command={2}&account=admin&response=json", hostname,port,command);
			}
			logger.info("url:"+url);
			String resultJSON = Rest.doRest("GET",url,null);
			PrintWriter out = response.getWriter();
			out.println(resultJSON);// 向客户端输出JSONObject字符串
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String url = "http://192.168.100.10:8096/client/api?command=listZones&showcapacities=true&response=json";
		try {
			String resultJSON = Rest.doRest("GET",url,null);
			System.out.println(resultJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
