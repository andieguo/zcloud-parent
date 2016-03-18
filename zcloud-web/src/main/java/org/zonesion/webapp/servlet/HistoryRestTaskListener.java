package org.zonesion.webapp.servlet;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.mapred.JobClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.zonesion.hadoop.base.util.PropertiesUtil;
import org.zonesion.hadoop.hbase.service.HConnectionService;
import org.zonesion.hadoop.hdfs.util.HadoopUtil;
import org.zonesion.webapp.quartz.HdfsDownJob;
import org.zonesion.webapp.quartz.LocalDownJob;
import org.zonesion.webapp.quartz.QuartzManager;


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
		hConnectionService.connect(properties.getProperty("hbase.zookeeper.quorum"));//执行HBase连接，为HBase REST服务提供访问HBase连接。
		ServletContext servletContext = servletContextEvent.getServletContext();
		String hostname = properties.getProperty("fs.default.name.hostname");//192.168.100.141
		String fsPort = properties.getProperty("fs.default.name.port");//9000
		String jobPort = properties.getProperty("mapred.job.tracker.port");//9001
		logger.info("fs.default.name.hostname:"+hostname);
		logger.info("fs.default.name.port:"+fsPort);
		logger.info("mapred.job.tracker.port:"+jobPort);
		conf = new Configuration();
		conf.set("fs.default.name", "hdfs://"+hostname+":"+fsPort);
		conf.set("mapred.job.tracker", "http://"+hostname+":"+jobPort);
		servletContext.setAttribute("fs.default.name", "hdfs://"+hostname+":"+fsPort);
		servletContext.setAttribute("mapred.job.tracker", "http://"+hostname+":"+jobPort);
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
		//配置HBaseJob所有的jar
		String path = HistoryRestTaskListener.class.getClassLoader().getResource("").toString().replaceAll("%20", " ");
		
		System.setProperty("path.separator", ":");
		List<String> jarPathList = new ArrayList<String>();
		jarPathList.add(path.substring(0, path.indexOf("classes")) + "lib/json-20140107.jar");
		jarPathList.add(path.substring(0, path.indexOf("classes")) + "lib/guava-11.0.2.jar");
		jarPathList.add(path.substring(0, path.indexOf("classes")) + "lib/hbase-0.94.20.jar");
		jarPathList.add(path.substring(0, path.indexOf("classes")) + "lib/protobuf-java-2.4.0a.jar");
		jarPathList.add(path.substring(0, path.indexOf("classes")) + "lib/zookeeper-3.4.5.jar");
		for(String key : jarPathList){
			try {
				System.out.println("xxx key "+key);
				addJarToDistributedCache(key, conf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**========================================定时任务执行=====================================================**/
		//启动定时任务(访问智云历史数据，并上传到HDFS)
		//logger.info("hdfs_timer:"+properties.getProperty("zcloud.download.hdfs.timer"));
		//String t = properties.getProperty("zcloud.download.hdfs.timer");
		//if (t!=null && t.length()>0) {
		//	QuartzManager.addJob("job_zcloud_hdfs", HdfsDownJob.class.getName(), t);
		//}
		//启动定时任务（访问智云历史数据，并下载到本地）
		//logger.info("local_timer:"+properties.getProperty("zcloud.download.local.timer"));
		//QuartzManager.addJob("job_zcloud_local", LocalDownJob.class.getName(), properties.getProperty("zcloud.download.local.timer"));
	}
	
	public static void addJarToDistributedCache(String path, Configuration conf) throws IOException {

		File jarFile = new File(path);

		// Mount HDFS
		FileSystem fs = FileSystem.get(conf);

		// Copy (override) jar file to HDFS
		Path srcPath = new Path(path); // 原路径
		Path mkdir = new Path("/user/hadoop/lib/");
		if(!fs.exists(mkdir)){
			fs.mkdirs(mkdir);
		}
		Path dstPath = new Path("/user/hadoop/lib/" + jarFile.getName());// 目标路径
		// 调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
		if(!fs.exists(dstPath)){
			fs.copyFromLocalFile(false, srcPath, dstPath);
		}

		// Add jar to distributed classPath
		DistributedCache.addArchiveToClassPath(dstPath, conf);
	}
	
	public static void main(String[] args) {
		String fsDefaultName = "hdfs://192.168.1.124:9000";
		Configuration conf = new Configuration();
		conf.set("fs.default.name", fsDefaultName);
		
		//配置HBaseJob所有的jar
		String path = "J:/zonesion02/40.hadoop/zcloud-parent/zcloud-web/target/zcloud-web/WEB-INF/";
		System.setProperty("path.separator", ":");
		List<String> jarPathList = new ArrayList<String>();
		jarPathList.add(path + "lib/json-20140107.jar");
		jarPathList.add(path + "lib/guava-11.0.2.jar");
		jarPathList.add(path + "lib/hbase-0.94.20.jar");
		jarPathList.add(path + "lib/protobuf-java-2.4.0a.jar");
		jarPathList.add(path + "lib/zookeeper-3.4.5.jar");
		for(String key : jarPathList){
			try {
				addJarToDistributedCache(key, conf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
