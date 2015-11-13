package org.zonesion.hadoop.mr.example;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.JSONObject;

//输入：["2015-05-05",["{"0-50:0","50-100:1"}","{"0-50:0","50-100:1"}".........]]
public class ClassifyPMReduce extends
		Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {
		Counter counterin = context.getCounter("Reduce", "输入个数：");
		counterin.increment(1);
		JSONObject resultObj = new JSONObject();
		resultObj.put("0-50", 0);
		resultObj.put("50-100", 0);
		resultObj.put("100-150", 0);
		resultObj.put("150-200", 0);
		resultObj.put("200-250", 0);
		resultObj.put("250-300", 0);
		resultObj.put("300-!", 0);
		for (Text val : values) {// 遍历求和
			JSONObject valObj = new JSONObject(val.toString());
			@SuppressWarnings("unchecked")
			Iterator<String> keys = resultObj.keys();
			while(keys.hasNext()){
				String keyObj = keys.next();
				resultObj.put(keyObj, resultObj.getInt(keyObj)+valObj.getInt(keyObj));
			}
		}
		context.write(key,new Text( resultObj.toString()));// 输出求和后的<key,value>
	}

}
