package org.zonesion.hadoop.mr.main;

import org.apache.hadoop.util.ProgramDriver;

public class ExampleDriver {

	public static void main(String[] args) {
		ProgramDriver pgd = new ProgramDriver();
		try {
		      pgd.addClass("hdfs", org.zonesion.hadoop.mr.hdfs.Main.class, "run mapreduce and export data to HDFS");
		      pgd.addClass("hbase", org.zonesion.hadoop.mr.hbase.Main.class, "run mapreduce and export data to HBase");
		      pgd.addClass("hbase-combiner", org.zonesion.hadoop.mr.hbase.combiner.Main.class, "run mapreduce and export data to HBase");
		      pgd.driver(args);
		    }
		    catch(Throwable e){
		      e.printStackTrace();
		    }
	}
}
