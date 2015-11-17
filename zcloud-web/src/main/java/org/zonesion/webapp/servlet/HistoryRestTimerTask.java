package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.util.Properties;
import java.util.TimerTask;

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
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("==========================开始执行定时任务==========================");
		//localTask();
	}
	
	public void localTask(){
		//定时上传到本地
		restLocal =  new RestLocal("/home/hadoop/Download/");//在构造方法中有资源的初始化
		restLocal.executeJob(this.getClass().getResource("/sensors.xml").getPath());//在执行完job后自动释放资源
	}
	
	public void hdfsTask(){
		//定时上传HDFS
		Properties propertis = PropertiesUtil.loadFromInputStream(this.getClass().getResourceAsStream("/config.properties"));
		restHDFS = new RestHDFS(propertis.getProperty("fs.default.name"));
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
