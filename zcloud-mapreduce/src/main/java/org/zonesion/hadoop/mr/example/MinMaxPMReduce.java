package org.zonesion.hadoop.mr.example;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Reducer;

public class MinMaxPMReduce extends
		Reducer<Text, DoubleWritable, Text, DoubleWritable> {

	@Override
	protected void reduce(Text key, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {
		Counter counterin = context.getCounter("Reduce", "输入个数：");
		counterin.increment(1);
		Configuration conf = context. getConfiguration();
		String arg = conf.get("type");
		if(arg.equals("min")){
			double minValue = Double.MAX_VALUE;
			for (DoubleWritable value : values) {
				minValue = Math.min(minValue, value.get());
			}
			context.write(key, new DoubleWritable(minValue));
		}else if(arg.equals("max")){
			double maxValue = Double.MIN_VALUE;
			for (DoubleWritable value : values) {
				maxValue = Math.max(maxValue, value.get());
			}
			context.write(key, new DoubleWritable(maxValue));
		}
	}
}
