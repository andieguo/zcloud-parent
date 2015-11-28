package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.util.Properties;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.zonesion.hadoop.base.util.PropertiesUtil;
import org.zonesion.hadoop.hdfs.util.RestHDFS;
import org.zonesion.hadoop.local.util.RestLocal;
/**
 * 定时任务：定时执行获取智云平台上的历史数据
 * @author hadoop
 *
 */
public class HistoryRestTimerTask  extends TimerTask{

	private RestLocal restLocal;
	private RestHDFS restHDFS;
	private Logger logger;
	private Properties propertis;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		logger =  Logger.getLogger(HistoryRestTimerTask.class);
		propertis = PropertiesUtil.loadFromInputStream(this.getClass().getResourceAsStream("/config.properties"));
		logger.info("==========================开始执行定时任务==========================");
		//hdfsTask();
		//localTask();
	}
	
	public void localTask(){
		//定时上传到本地
		restLocal =  new RestLocal(propertis.getProperty("zcloud.download.local.home"));//在构造方法中有资源的初始化
		logger.info("tark-zcloud.download.local.home:"+propertis.getProperty("zcloud.download.local.home"));
		restLocal.executeJob(this.getClass().getResource("/sensors.xml").getPath());//在执行完job后自动释放资源
	}
	
	public void hdfsTask(){
		//定时上传HDFS
		restHDFS = new RestHDFS(propertis.getProperty("fs.default.name"));
		logger.info("tark-fs.default.name:"+propertis.getProperty("fs.default.name"));
		try {
			restHDFS.executeJob(this.getClass().getResource("/sensors.xml").getPath());//类路径下加载sensors.xml
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
