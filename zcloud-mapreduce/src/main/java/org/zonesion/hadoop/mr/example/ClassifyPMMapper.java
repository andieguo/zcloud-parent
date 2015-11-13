package org.zonesion.hadoop.mr.example;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class ClassifyPMMapper extends Mapper<LongWritable, Text, Text, Text> {

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		String source = value.toString();
		String arg = conf.get("date");
		try {
			Counter counterin = context.getCounter("Map", "输入个数：");
			counterin.increment(1);
			Counter counterout = context.getCounter("Map", "输出个数：");
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");
			JSONObject jsonObject = new JSONObject(source);
			JSONArray jsonArray = jsonObject.getJSONArray("datapoints");
			SimpleDateFormat fomateDate = null;
			if (arg.equals("month")) {
				fomateDate = new SimpleDateFormat("yyyy-MM");
			} else if (arg.equals("day")) {
				fomateDate = new SimpleDateFormat("yyyy-MM-dd");
			} else if (arg.equals("hour")) {
				fomateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				counterout.increment(1);
				JSONObject object = jsonArray.getJSONObject(i);
				String at = fomateDate.format(format.parse(object.getString("at")));
				Double pmvalue = Double.valueOf(object.getString("value"));
				JSONObject resultObj = new JSONObject();
				resultObj.put("0-50", 0);
				resultObj.put("50-100", 0);
				resultObj.put("100-150", 0);
				resultObj.put("150-200", 0);
				resultObj.put("200-250", 0);
				resultObj.put("250-300", 0);
				resultObj.put("300-!", 0);
				if (pmvalue < 50) {
					resultObj.put("0-50", 1);
				} else if (pmvalue < 100) {
					resultObj.put("50-100", 1);
				} else if (pmvalue < 150) {
					resultObj.put("100-150", 1);
				} else if (pmvalue < 200) {
					resultObj.put("150-200", 1);
				} else if (pmvalue < 250) {
					resultObj.put("200-250", 1);
				} else if (pmvalue < 300) {
					resultObj.put("250-300", 1);
				} else {
					resultObj.put("300-!", 1);
				}
				context.write(new Text(at), new Text(resultObj.toString()));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
