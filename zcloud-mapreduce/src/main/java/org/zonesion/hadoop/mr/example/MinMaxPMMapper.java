package org.zonesion.hadoop.mr.example;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class MinMaxPMMapper extends
		Mapper<LongWritable, Text, Text, DoubleWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		Configuration conf = context. getConfiguration();
		String arg = conf.get("date");
		String source = value.toString();
		try {
			Counter counterin = context.getCounter("Map", "输入个数：");
			counterin.increment(1);
			Counter counterout = context.getCounter("Map", "输出个数：");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			JSONObject jsonObject = new JSONObject(source);
			JSONArray jsonArray = jsonObject.getJSONArray("datapoints");
			SimpleDateFormat fomateDate = null;
			if(arg.equals("month")){
				fomateDate  = new SimpleDateFormat("yyyy-MM");
			}else if(arg.equals("day")){
				fomateDate = new SimpleDateFormat("yyyy-MM-dd");
			}else if(arg.equals("hour")){
				fomateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				counterout.increment(1);
				JSONObject object = jsonArray.getJSONObject(i);
				String at = fomateDate.format(format.parse(object.getString("at")));
				String pmvalue = object.getString("value");
				context.write(new Text(at),
						new DoubleWritable(Double.valueOf(pmvalue)));// //输出<key,value>为<"2015-05",90.12>
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
