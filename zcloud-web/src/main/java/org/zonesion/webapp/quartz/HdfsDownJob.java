package org.zonesion.webapp.quartz;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.xml.sax.SAXException;
import org.zonesion.hadoop.base.util.PropertiesUtil;
import org.zonesion.hadoop.hdfs.util.RestHDFS;

public class HdfsDownJob implements Job {
	private RestHDFS restHDFS;
	private Logger logger;
	private Properties properties;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		logger =  Logger.getLogger(HdfsDownJob.class);
		properties = PropertiesUtil.loadFromInputStream(this.getClass().getResourceAsStream("/config.properties"));
		logger.info("==========================开始执行zcloud-hdfs定时任务==========================");
		// 定时上传HDFS
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				logger.info("执行了thread方法");
				String hostname = properties.getProperty("fs.default.name.hostname");
				String hostport = properties.getProperty("fs.default.name.port");
				logger.info("hostname:"+hostname);
				logger.info("hostport:"+hostport);
				String url = String.format("hdfs://%s:%s", hostname, hostport);
				logger.info("tark-fs.default.name:" + url);
				try {
					restHDFS = new RestHDFS(url);
					restHDFS.executeJob(this.getClass().getResource("/sensors.xml").getPath());// 类路径下加载sensors.xml
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					logger.info(e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.info(e.getMessage());
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.info(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
}
