package org.zonesion.hadoop.hdfs.util;

import java.io.File;
import java.io.FileInputStream;
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
import org.zonesion.hadoop.hdfs.util.HDFSUtil;
import org.zonesion.hadoop.hdfs.view.DownloadView;
import org.zonesion.hadoop.base.util.LogWriter;
import org.zonesion.hadoop.base.util.PropertiesHelper;
import org.zonesion.hadoop.base.util.Rest;
import org.zonesion.hadoop.base.util.XmlService;

//数据存储与访问有2种方式：1）访问数据库；2）访问xml文件；
public class RestHDFS 
{
	private HttpURLConnection connection = null;
	private PropertiesHelper propertiesHelper ;
	private LogWriter logger;
	private HDFSUtil hdfsUtil;

	public RestHDFS(String hdfsName) {
		super();
		this.propertiesHelper = PropertiesHelper.getInstance("hdfs_update.properties");//记录最新的更新时间,读取类路径下的配置文件
		hdfsUtil = new HDFSUtil(hdfsName);
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			logger = LogWriter.getLogWriter(File.separator+format.format(new java.util.Date())+".log");//保存到类路径下
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
			//创建文件夹
			if(!hdfsUtil.check("zcloud")){//HDFS创建zcloud
				hdfsUtil.mkdir("zcloud");//HDFS上的路径：/user/hadoop/zcloud/
			}
			String strUserID = "zcloud"+File.separator+historyURL.getId();
			if(!hdfsUtil.check(strUserID)){//HDFS创建
				hdfsUtil.mkdir(strUserID);//HDFS上的路径：/user/hadoop/zcloud/1155223593
			}
			String strChannal = strUserID+File.separator+historyURL.getChannal().replace(":", "_");//HDFS上的路径：/user/hadoop/zcloud/1155223593/00_11_22/
			if(!hdfsUtil.check(strChannal)){//HDFS创建
				 hdfsUtil.mkdir(strChannal);
			}
			//文件名以startAt命令
			File file = new File(strChannal+startAt.replace(":", "-"));///user/hadoop/zcloud/1155223593/channalid/startat.txt
			if(hdfsUtil.check(file.getPath()) && size == 2000){//如果文件在HDFS上存在，且数据为2000个
				logger.log("完整文件已经保存到HDFS");
			}else{
				File temp = new File(startAt.replace(":", "-"));
				OutputStream outputStream = new FileOutputStream(temp);
				OutputStreamWriter writer = new OutputStreamWriter(outputStream,"GBk");
				writer.write(result);//保存读取到的数据到文件
				writer.close();
				logger.logTextArea("成功上传："+temp.getName());
				logger.log("成功上传："+temp.getName()+"到"+strChannal);
				hdfsUtil.put(temp.getPath(), strChannal);//上传到HDFS上
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
			 logger.logTextArea(historyURL.getChannal()+"上传结束！");
			 logger.log(historyURL.getChannal()+"获取历史数据结束！");
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
			logger.logTextArea("等待读取历史数据线程开始执行......");
			logger.log("FinnishJobRunable在等待所有的读历史数据线程执行完毕！");
			try {
				this.downLatch.await();
				logger.logTextArea("任务执行完毕！");
				logger.log("FinnishJobRunable释放连接资源！");
//				connection.disconnect();
				hdfsUtil.destroy();
				propertiesHelper.destroy();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
		
	public void executeJob(String filePath) throws SAXException, IOException, InterruptedException{
		ExecutorService service = Executors.newCachedThreadPool(); //创建一个线程池
		Exchanger<Integer> exchanger = new Exchanger<Integer>();//子线程与主线程交换数据
		XmlService xmlService = XmlService.getInstance();
		InputStream inputStream;
			//读配置文件
			inputStream = new FileInputStream(new File(filePath));
			List<Gate> gateList = xmlService.readXML(inputStream);
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
					QueryRunnable queryRunnable = new QueryRunnable(downLatch, exchanger,historyURL);//多线程执行--是否有公共资源
					 service.execute(queryRunnable);//为线程池添加任务
				}
			}
			 FinnishJobRunable writeToXMLRunable = new FinnishJobRunable(downLatch,xmlService,gateList);
			 service.execute(writeToXMLRunable);//为线程池添加任务
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
	}

	public static void main(String[] args) {
		RestHDFS rest = new RestHDFS("hdfs://192.168.1.30:9000");
		try {
			rest.executeJob("sensors.xml");
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
