package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.TimerTask;

import org.xml.sax.SAXException;
import org.zonesion.hadoop.hdfs.util.RestHDFS;
import org.zonesion.hadoop.local.util.RestLocal;
/**
 * 定时任务：定时执行获取智云平台上的历史数据
 * @author hadoop
 *
 */
public class HistoryRestTimerTask  extends TimerTask{

	@SuppressWarnings("unused")
	private RestLocal restLocal;
	private RestHDFS restHDFS;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("==========================开始执行定时任务==========================");
		//定时上传到本地
		//restLocal =  new RestLocal("/home/hadoop/");//在构造方法中有资源的初始化
		//restLocal.executeJob("home/hadoop/workspace/zcloud-parent/sensors.xml");//在执行完job后自动释放资源
		//定时上传HDFS
		Properties propertis = new Properties();
		InputStream input = this.getClass().getResourceAsStream("/config.properties");
		try {
			propertis.load(input);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		restHDFS = new RestHDFS(propertis.getProperty("fs.default.name"));
		try {
			System.out.println("path:"+this.getClass().getResourceAsStream("/sensors.xml").toString());
			restHDFS.executeJob(this.getClass().getResourceAsStream("/sensors.xml").toString());//类路径下加载sensors.xml
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
