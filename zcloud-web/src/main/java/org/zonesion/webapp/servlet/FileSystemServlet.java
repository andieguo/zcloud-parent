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

public class FileSystemServlet extends HttpServlet {

	private static final long serialVersionUID = 5009026145243904234L;
	private Logger logger =  Logger.getLogger(FileSystemServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Properties properties = PropertiesUtil.loadFromInputStream(this.getClass().getResourceAsStream("/config.properties"));
		String hostname = properties.getProperty("fs.default.name.hostname");//192.168.100.141
		String port = properties.getProperty("hadoop.namenode.port");//50070
		String command = request.getParameter("command");//LISTSTATUS
		String parentDir = request.getParameter("parentDir");//LISTSTATUS
		String url = null;
		logger.info("hostname:"+hostname);
		logger.info("port:"+port);
		logger.info("command:"+command);
		logger.info("parentDir:"+parentDir);
		try {
			if(command.equals("LISTSTATUS")){
				url = MessageFormat.format("http://{0}:{1}/webhdfs/v1{2}?op={3}", hostname,port,parentDir,command);
				logger.info("url:"+url);
			}else if(command.equals("OPEN")){
				url = MessageFormat.format("http://{0}:{1}/webhdfs/v1{2}?op={3}", hostname,port,parentDir,command);
				logger.info("url:"+url);
			}
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
		String url2 = "http://192.168.100.141:50070/webhdfs/v1/user/hadoop/zcloud/1155223953/00_12_4B_00_02_63_3E_B5_A0?op=LISTSTATUS";
		try {
			String resultJSON = Rest.doRest("GET",url2,null);
			System.out.println(resultJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
