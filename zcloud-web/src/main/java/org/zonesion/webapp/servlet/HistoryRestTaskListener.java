package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.mapred.JobClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.zonesion.hadoop.base.util.PropertiesUtil;
import org.zonesion.hadoop.hbase.service.HConnectionService;
import org.zonesion.webapp.quartz.HdfsDownJob;
import org.zonesion.webapp.quartz.LocalDownJob;
import org.zonesion.webapp.quartz.QuartzManager;
import org.zonesion.webapp.timer.TimerManager;

/**
 * 定时任务监听器，当web容器启动时执行定时任务
 * @author hadoop
 *
 */
public class HistoryRestTaskListener implements ServletContextListener {
	
	private HConnectionService hConnectionService;
	private Configuration conf;
	private JobClient jobClient;
	private ClientProtocol clientProtocol;
	private Logger logger;
	private Properties properties;
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
		//从类路径下加载配置文件
		PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j/log4j.properties"));
		properties = PropertiesUtil.loadFromInputStream(this.getClass().getResourceAsStream("/config.properties"));
		logger =  Logger.getLogger(HistoryRestTaskListener.class);
		hConnectionService = HConnectionService.getInstance("zcloud");//单例模式,加载zcloud-hbase模块的配置文件
		hConnectionService.connect();//执行HBase连接，为HBase REST服务提供访问HBase连接。
		ServletContext servletContext = servletContextEvent.getServletContext();
		conf = new Configuration();
		String hostname = servletContext.getInitParameter("fs.default.name.hostname");//192.168.100.141
		String fsPort = servletContext.getInitParameter("fs.default.name.port");//9000
		String jobPort = servletContext.getInitParameter("mapred.job.tracker.port");//9001
		logger.info("fs.default.name.hostname:"+hostname);
		logger.info("fs.default.name.port:"+fsPort);
		logger.info("mapred.job.tracker.port:"+jobPort);
		try {
			//获取jobClient
			jobClient = new JobClient(new InetSocketAddress(hostname,Integer.valueOf(jobPort)), conf);
			servletContext.setAttribute("jobClient", jobClient);
			//获取namenode
			clientProtocol = DFSClient.createNamenode(new InetSocketAddress(hostname,Integer.valueOf(fsPort)), conf);
			servletContext.setAttribute("clientProtocol", clientProtocol);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**========================================定时任务执行=====================================================**/
		//启动定时任务(访问智云历史数据，并上传到HDFS)
		logger.info("hdfs_timer:"+properties.getProperty("zcloud.download.hdfs.timer"));
		QuartzManager.addJob("job_zcloud_hdfs", HdfsDownJob.class.getName(), properties.getProperty("zcloud.download.hdfs.timer"));
		//启动定时任务（访问智云历史数据，并下载到本地）
		logger.info("local_timer:"+properties.getProperty("zcloud.download.local.timer"));
		QuartzManager.addJob("job_zcloud_local", LocalDownJob.class.getName(), properties.getProperty("zcloud.download.hdfs.timer"));
	}
	
	public static void main(String[] args) {
		String fsDefaultName = "hdfs://192.168.100.141:9000";
		String fsPort = fsDefaultName.substring(fsDefaultName.lastIndexOf(":")+1, fsDefaultName.length());
		String fsHostName = fsDefaultName.substring(fsDefaultName.lastIndexOf("/")+1, fsDefaultName.lastIndexOf(":"));
		System.out.println(fsPort);
		System.out.println(fsHostName);
	}

}
