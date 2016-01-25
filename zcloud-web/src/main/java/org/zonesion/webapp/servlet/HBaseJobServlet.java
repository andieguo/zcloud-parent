package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.zonesion.hadoop.base.util.Constants;
import org.zonesion.hadoop.base.util.PropertiesUtil;
import org.zonesion.hadoop.mr.hbase.PMMapper;
import org.zonesion.hadoop.mr.hbase.PMReduce;


public class HBaseJobServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4778838109456206077L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	
	// 在MapReduce中，由Job对象负责管理和运行一个计算任务，并通过Job的一些方法对任>务的参数进行相关的设置。
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tablename = "zcloud";
		Configuration conf = HBaseConfiguration.create();
		ServletContext servletContext = getServletContext();
		conf.set("fs.default.name", (String)servletContext.getAttribute("fs.default.name"));
		conf.set("mapred.job.tracker", (String)servletContext.getAttribute("mapred.job.tracker"));
		conf.set("mapred.jar", Constants.JAR_HOME);
		Properties properties = PropertiesUtil.loadFromInputStream(HBaseJobServlet.class.getResourceAsStream("/hbase-config.properties"));
		conf.set("hbase.zookeeper.quorum",properties.getProperty("hbase.zookeeper.quorum"));
		Job job = new Job(conf, "PMHBaseMain");
		job.setJarByClass(HBaseJobServlet.class);
		job.setMapperClass(PMMapper.class);
		TableMapReduceUtil.initTableReducerJob(tablename, PMReduce.class, job);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		/**** 准备输出 *****/
		prepareOutput(conf, tablename);
		/**** 准备输入 *****/
		FileSystem fs = FileSystem.get(conf);
		for (String str : listFile(fs, "/user/hadoop/zcloud")) {// 遍历/user/hadoop/zcloud下的输入
			for (String str1 : listFile(fs, str)) {
				FileInputFormat.addInputPath(job, new Path(str1));
			}
		}
		try {
			job.waitForCompletion(true);
		} catch (ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void prepareOutput(Configuration conf, String tablename) {
		HBaseAdmin admin = null;
		try {
			admin = new HBaseAdmin(conf);
			if (!admin.tableExists(tablename)) {
				System.out.println("table not exists!creating.......");
				HTableDescriptor htd = new HTableDescriptor(tablename);
				HColumnDescriptor tcd = new HColumnDescriptor("content");
				htd.addFamily(tcd);// 创建列族
				admin.createTable(htd);// 创建表
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (admin != null)
				try {
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static List<String> listFile(FileSystem fs, String filePath) throws IOException {
		List<String> listDir = new ArrayList<String>();
		Path path = new Path(filePath);
		FileStatus[] fileStatus = fs.listStatus(path);
		for (FileStatus file : fileStatus) {
			listDir.add(file.getPath().toString());
		}
		return listDir;
	}
}
