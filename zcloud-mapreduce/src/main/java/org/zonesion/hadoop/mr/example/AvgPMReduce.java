package org.zonesion.hadoop.mr.example;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Reducer;

//输入：<"2015-05-05",[90,98,90]>
//返回：<2015-05-05,90>
public class AvgPMReduce extends
		Reducer<Text, DoubleWritable, Text, DoubleWritable> {

	@Override
	protected void reduce(Text key, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {
		Counter counterin = context.getCounter("Reduce", "输入个数：");
		counterin.increment(1);
		double sumValue = 0;
		long numValue = 0;
		double avgValue = 0;
		for (DoubleWritable value : values) {
			sumValue += Double.valueOf(value.toString());
			numValue++;
		}
		avgValue = sumValue / numValue;
		context.write(key, new DoubleWritable(avgValue));
	}

}