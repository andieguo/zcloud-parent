package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.mapred.JobClient;
import org.zonesion.hadoop.hbase.service.HConnectionService;
/**
 * 定时任务监听器，当web容器启动时执行定时任务
 * @author hadoop
 *
 */
public class HistoryRestTaskListener implements ServletContextListener {
	
	private HConnectionService hConnectionService;

	/**
	 * 当Servlet 容器终止Web 应用时调用该方法。
	 */  
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("==========================关闭容器=========================");
		hConnectionService.disconnect();
	}

	/**
	 * 当Servlet 容器启动Web 应用时调用该方法。
	 */  
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		System.out.println("==========================启动容器=========================");
		hConnectionService = HConnectionService.getInstance("zcloud");//单例模式
		hConnectionService.connect();//执行HBase连接，为HBase REST服务提供访问HBase连接。
		ServletContext servletContext = servletContextEvent.getServletContext();
		Configuration conf = new Configuration();
		JobClient jobClient;
		ClientProtocol clientProtocol;
		try {
			//获取jobClient
			jobClient = new JobClient(new InetSocketAddress("192.168.100.141",9001), conf);
			servletContext.setAttribute("jobClient", jobClient);
			//获取namenode
			clientProtocol = DFSClient.createNamenode(new InetSocketAddress("192.168.100.141", 9000), conf);
			servletContext.setAttribute("clientProtocol", clientProtocol);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new TimerManager();//启动定时任务(访问智云历史数据，并上传到HDFS)
	}

}
