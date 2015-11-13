package org.zonesion.hadoop.mr.hdfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Main {

	// 在MapReduce中，由Job对象负责管理和运行一个计算任务，并通过Job的一些方法对任>务的参数进行相关的设置。
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.addResource("classpath:/hadoop/core-site.xml");
		conf.addResource("classpath:/hadoop/hdfs-site.xml");
		conf.addResource("classpath:/hadoop/mapred-site.xml");
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 1) {
			System.err.println("Usage: main <out>");
			System.exit(2);
		}
		conf.set("date", otherArgs[0]);
		Job job = new Job(conf, "PMMain");
		job.setJarByClass(Main.class);
		job.setMapperClass(PMMapper.class);
		job.setReducerClass(PMReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileSystem fs = FileSystem.get(conf);
		for (String str : listFile(fs, "/user/hadoop/zcloud")) {// 遍历/user/hadoop/zcloud下的输入
			for (String str1 : listFile(fs, str)) {
				FileInputFormat.addInputPath(job, new Path(str1));
			}
		}
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[0]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
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
