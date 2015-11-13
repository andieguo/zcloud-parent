package org.zonesion.hadoop.mr.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
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
		if (otherArgs.length != 4) {
			System.err.println("Usage: Main [min|max|avg|classify]  [month|day|hour] <in> <out>");
			System.exit(2);
		}
		conf.set("type", otherArgs[0]);
		conf.set("date", otherArgs[1]);
		Job job = new Job(conf, "MinPMByMonth");
		job.setJarByClass(Main.class);
		if (otherArgs[0].equals("min") || otherArgs[0].equals("max")) {
			job.setMapperClass(MinMaxPMMapper.class);
			job.setReducerClass(MinMaxPMReduce.class);
			job.setCombinerClass(MinMaxPMReduce.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(DoubleWritable.class);
		} else if (otherArgs[0].equals("avg")) {
			job.setMapperClass(MinMaxPMMapper.class);
			job.setReducerClass(AvgPMReduce.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(DoubleWritable.class);
		} else if (otherArgs[0].equals("classify")) {
			job.setMapperClass(ClassifyPMMapper.class);
			job.setReducerClass(ClassifyPMReduce.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
		}
		FileInputFormat.addInputPath(job, new Path(otherArgs[2]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[3]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
