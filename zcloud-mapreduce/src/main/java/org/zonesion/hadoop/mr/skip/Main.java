package org.zonesion.hadoop.mr.skip;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Main extends Configured implements Tool{

	public void configure(JobConf job) {
		
	}
	
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		JobConf conf = new JobConf(getConf(),Main.class);
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(),new Main(), args);
		System.exit(res);
	}

}
