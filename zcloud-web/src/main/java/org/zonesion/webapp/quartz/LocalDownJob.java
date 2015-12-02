package org.zonesion.webapp.quartz;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.zonesion.hadoop.base.util.PropertiesUtil;
import org.zonesion.hadoop.local.util.RestLocal;

public class LocalDownJob implements Job{
	private RestLocal restLocal;
	private Logger logger;
	private Properties properties;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		logger =  Logger.getLogger(LocalDownJob.class);
		properties = PropertiesUtil.loadFromInputStream(this.getClass().getResourceAsStream("/config.properties"));
		logger.info("==========================开始执行zcloud-local定时任务==========================");
		//定时上传到本地
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				restLocal =  new RestLocal(properties.getProperty("zcloud.download.local.home"));//在构造方法中有资源的初始化
				logger.info("tark-zcloud.download.local.home:"+properties.getProperty("zcloud.download.local.home"));
				restLocal.executeJob(this.getClass().getResource("/sensors.xml").getPath());//在执行完job后自动释放资源
			}
		});
		thread.start();
	}

}
