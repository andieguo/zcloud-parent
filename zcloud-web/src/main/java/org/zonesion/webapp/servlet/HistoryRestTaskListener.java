package org.zonesion.webapp.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.zonesion.hadoop.hbase.service.HConnectionService;
/**
 * 定时任务监听器，当web容器启动时执行定时任务
 * @author hadoop
 *
 */
public class HistoryRestTaskListener implements ServletContextListener {
	
	private HConnectionService hConnectionService = HConnectionService.getInstance("zcloud");//单例模式

	/**
	 * 当Servlet 容器终止Web 应用时调用该方法。
	 */  
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		hConnectionService.disconnect();
	}

	/**
	 * 当Servlet 容器启动Web 应用时调用该方法。
	 */  
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		hConnectionService.connect();//执行HBase连接，为HBase REST服务提供访问HBase连接。
		new TimerManager();//启动定时任务(访问智云历史数据，并上传到HDFS)
	}

}
