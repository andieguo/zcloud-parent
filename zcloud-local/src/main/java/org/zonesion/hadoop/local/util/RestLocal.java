package org.zonesion.hadoop.local.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.zonesion.hadoop.base.bean.Gate;
import org.zonesion.hadoop.base.bean.HistoryURL;
import org.zonesion.hadoop.base.bean.Sensor;
import org.zonesion.hadoop.base.util.LogWriter;
import org.zonesion.hadoop.base.util.PropertiesHelper;
import org.zonesion.hadoop.base.util.Rest;
import org.zonesion.hadoop.base.util.XmlService;
import org.zonesion.hadoop.local.view.DownloadView;

//数据存储与访问有2种方式：1）访问数据库；2）访问xml文件；
public class RestLocal 
{
	private HttpURLConnection connection;
	private PropertiesHelper propertiesHelper ;
	private LogWriter logger;
	private String dstPath;

	public RestLocal(String dstPath) {
		super();
		this.dstPath = dstPath;
		this.propertiesHelper = PropertiesHelper.getInstance("local_update.properties");//记录最新的更新时间
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			logger = LogWriter.getLogWriter(format.format(new java.util.Date())+".log");//日志文件存放在类路径log文件夹下
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//解析JSON格式字符串	
	//加上synchronized
	public synchronized  String  analysis(HistoryURL historyURL) throws Exception{
		String result = Rest.doRest(connection,historyURL);
		JSONObject resultObj = new JSONObject(result);
		JSONArray  datapoints = resultObj.getJSONArray("datapoints");
		int size = datapoints.length();
		if(size >= 1){
			String startAt = datapoints.getJSONObject(0).getString("at");//并读取第一个时间点
			String endAt = datapoints.getJSONObject(size-1).getString("at");//并读取最后一个时间点
			logger.log("startAt:"+startAt+";"+"endAt:"+endAt+";"+"size:"+size);
			//本地创建文件夹zcloud
			File mkdirZcloud = new File(dstPath+File.separator+"zcloud");
			if(!mkdirZcloud.exists()){
				mkdirZcloud.mkdir();
			}
			//本地创建文件夹zcloud/userid
			File mkdirUserID = new File(mkdirZcloud.getPath()+File.separator+historyURL.getId());
			if(!mkdirUserID.exists()){
				mkdirUserID.mkdir();
			}
			//本地创建文件夹zcloud/userid/channal
			File mkdirChannal = new File(mkdirUserID.getPath()+File.separator+historyURL.getChannal().replace(":", "_"));
			if(!mkdirChannal.exists()){
				mkdirChannal.mkdir();
			}
			//本地创建文件zcloud/userid/channal/file
			File file = new File(mkdirChannal.getPath()+File.separator+startAt.replace(":", "-"));//文件名以startAt命令
			if(file.exists() && size == 2000){//如果文件存在，且数据为2000个
				logger.log("完整文件已经保存");
			}else{
				OutputStream outputStream = new FileOutputStream(file);
				OutputStreamWriter writer = new OutputStreamWriter(outputStream,"GBk");
				logger.logTextArea("成功下载："+file);
				logger.log("成功下载："+file);
				writer.write(result);//保存读取到的数据到文件
				writer.close();
			}
			if(size == 2000){//判断其为完整的文件的条件
				return endAt;
			}else{//最后一片段文件
				//记录每一个分片的开始时间点,用于指定开始时间
				propertiesHelper.setProperty(historyURL.getId()+";"+historyURL.getChannal(), startAt);
				return "";
			}
		}
		return "";
	}

	//查询
	public  class QueryRunnable implements Runnable{
		
		HistoryURL historyURL;
		CountDownLatch downLatch;
		Exchanger<Integer> exchanger;   
		
		public QueryRunnable(CountDownLatch downLatch,Exchanger<Integer> exchanger,HistoryURL historyURL) {
			super();
			this.downLatch = downLatch;
			this.historyURL = historyURL;
			this.exchanger = exchanger;
		}

		public void run() {
			String endAt = "";
			try {
				endAt = analysis(historyURL);//第一次读取
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			 while(true){//什么时候退出
				 try {
						 if(!endAt.equals("")){
							 historyURL.setStarttime(endAt);
							 endAt = analysis(historyURL);//第归读取
						 }else{
							 break;
						 }
					} catch (Exception e) {
						e.printStackTrace();
						break;//当网络不可达时，报异常跳出循环
					}
			 }
			 logger.log(historyURL.getChannal()+"获取历史数据结束！");
			 logger.logTextArea(historyURL.getChannal()+"下载结束！");
			 try {
				exchanger.exchange(1);//与主线程交换信息
			} catch (InterruptedException e) {
				e.printStackTrace();
			}   
			 this.downLatch.countDown();//计数减少1
		}
		
	}
	
	public class FinnishJobRunable implements Runnable{
		CountDownLatch downLatch;
		XmlService xmlService;
		List<Gate> gateList;
		public  FinnishJobRunable(CountDownLatch downLatch,XmlService xmlService,List<Gate> gateList){
			this.downLatch = downLatch;
			this.xmlService = xmlService;
			this.gateList = gateList;
		}
		public void run() {
			logger.log("FinnishJobRunable在等待所有的读历史数据线程执行完毕！");
			logger.logTextArea("等待读取历史数据线程开始执行......");
			try {
				this.downLatch.await();
				logger.logTextArea("任务执行完毕！");
				logger.log("FinnishJobRunable释放连接资源！");
				//释放资源
				 //connection.disconnect();//释放连接
				 propertiesHelper.destroy();
				 logger.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 执行下载历史数据job
	 * @param filepath 配置文件（执行下载哪些传感器的历史数据）
	 */
	public void executeJob(String filepath){
		ExecutorService service = Executors.newCachedThreadPool(); //创建一个线程池
		Exchanger<Integer> exchanger = new Exchanger<Integer>();//子线程与主线程交换数据
		XmlService xmlService = XmlService.getInstance();
		InputStream inputStream;
		try {
			//读配置文件
			inputStream = new FileInputStream(new File(filepath));
			List<Gate> gateList = xmlService.readXML(inputStream);//读取完配置文件,自动关闭输入流
			int sum = 0;
			for(Gate gate:gateList){//获取任务个数
				sum+= gate.getSensors().size();
			}
			CountDownLatch downLatch = new CountDownLatch(sum);
			for(Gate gate:gateList){
				for(Sensor sensor:gate.getSensors()){
					//读取最新的更新记录
					String starttime = propertiesHelper.getProperty(gate.getUserid()+";"+sensor.getChannal());
					HistoryURL historyURL = new HistoryURL(gate.getServerAddr(), gate.getUserid(),gate.getUserkey(), sensor.getChannal(),starttime);
					QueryRunnable queryRunnable = new QueryRunnable(downLatch,exchanger, historyURL);//多线程执行--是否有公共资源
					 service.execute(queryRunnable);//为线程池添加任务
				}
			}
			 FinnishJobRunable finnishJobRunable = new FinnishJobRunable(downLatch,xmlService,gateList);
			 service.execute(finnishJobRunable);//为线程池添加任务
			Integer totalResult = Integer.valueOf(0);
			 for(int i=0;i<sum;i++){
				 //当主线程调用Exchange对象的exchange()方法后，他会陷入阻塞状态
				 //直到queryRunnable线程也调用了exchange()方法，然后以线程安全的方式交换数据，之后主线程继续运行
				 Integer partialResult = exchanger.exchange(Integer.valueOf(0));
				 if (partialResult != 0) {
						totalResult = totalResult + partialResult;
						System.out.println(String.format("Progress: %s/%s",totalResult, sum));
						//更新进度条
						int precent=(int)( ((float)totalResult/(float)sum)*100); 
						DownloadView.progressBar.setValue(precent);  
					}
			 }
			 service.shutdown();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		RestLocal rest = new RestLocal("/home/hadoop/workspace_maven/HistorySqoop");
		rest.executeJob("/home/hadoop/workspace_maven/HistorySqoop/sensors.xml");
	}	
}
