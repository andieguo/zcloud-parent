package org.zonesion.hadoop.mr.hbase;

import org.apache.hadoop.util.ProgramDriver;

public class ExampleDriver {

	public static void main(String[] args) {
		ProgramDriver pgd = new ProgramDriver();
		try {
		      pgd.addClass("main", Main.class, "A map/reduce program that data analysis by [year|month|day|hour] .");
		      pgd.driver(args);
		    }
		    catch(Throwable e){
		      e.printStackTrace();
		    }
	}
}
