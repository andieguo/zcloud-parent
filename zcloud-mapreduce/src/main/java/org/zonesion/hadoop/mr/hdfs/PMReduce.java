package org.zonesion.hadoop.mr.hdfs;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.JSONObject;

/**输入<key,value>为
 <"userid:channal:month:2015-05",
[
"{"number":90.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}",
"{"number":80.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"
"{"number":70.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"
]
}>

**/

public class PMReduce extends
		Reducer<Text, Text, Text, Text> {
	@Override
	protected void reduce(Text key, Iterable<Text> values,
		Context context) throws IOException, InterruptedException {
		Counter counterin = context.getCounter("Reduce", "输入个数：");
		counterin.increment(1);
		double minValue = Double.MAX_VALUE;
		double maxValue = Double.MIN_VALUE;
		double sumValue = 0;
		long numValue = 0;
		double avgValue = 0;
		JSONObject resultObj = new JSONObject();
		resultObj.put("0-50", 0);
		resultObj.put("50-100", 0);
		resultObj.put("100-150", 0);
		resultObj.put("150-200", 0);
		resultObj.put("200-250", 0);
		resultObj.put("250-300", 0);
		resultObj.put("300-!", 0);
		for(Text value :values){//"{"number":90.12,"classify":"{"0-50:0","50-100:1"}"
			JSONObject jsonObj = new JSONObject(value.toString());
			double numbervalue = jsonObj.getDouble("number");
			minValue = Math.min(minValue, numbervalue);//计算最小值
			maxValue = Math.max(maxValue, numbervalue);//计算最大值
			sumValue += numbervalue;//计算平均值
			numValue++;
			JSONObject classisyObj = jsonObj.getJSONObject("classify");//"{"0-50:0","50-100:1"}"
			@SuppressWarnings("unchecked")
			Iterator<String> keys = resultObj.keys();
			while(keys.hasNext()){
				String keyObj = keys.next();
				resultObj.put(keyObj, resultObj.getInt(keyObj)+classisyObj.getInt(keyObj));//计算分类
			}
		}
		avgValue = sumValue / numValue;//平均值
		JSONObject reduceObj = new JSONObject();
		reduceObj.put("min", minValue);
		reduceObj.put("max", maxValue);
		reduceObj.put("avg", avgValue);
		reduceObj.put("classify", resultObj);
		context.write(key, new Text(reduceObj.toString()));
	}
}
