package org.zonesion.hadoop.mr.hbase.combiner;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.JSONObject;
/**输入<key,value>为
<"userid:channal:month:2015-05",
[
"{"number":90,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}",
"{"number":80,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"
"{"number":70,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"
]
}>
<"userid:channal:month:2015-05",
[
"{"number":60,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}",
"{"number":40,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"
]
}>
求平均值：
(90+80+70)/3+(60+40)/2=80+50=130  （错误）
(90+80+70+60+40)/5 = 68  正确

Cominber处理,并输出：
<"userid:channal:month:2015-05","{"max":90,"min":70,"sum":240,"num":3,"classify":"{"0-50":0,"50-100":3,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"}>
<"userid:channal:month:2015-05","{"max":60,"min":40,"sum":100,"num":2,"classify":"{"0-50":0,"50-100":2,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"}>

Reduce的输入：
<"userid:channal:month:2015-05",
[
"{"max":90,"min":70,"sum":240,"num":3,"classify":"{"0-50":0,"50-100":3,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"}
"{"max":60,"min":40,"sum":100,"num":2,"classify":"{"0-50":0,"50-100":3,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"}
]>

**/
public class PMCombiner extends Reducer<Text, Text, Text, Text>{

	@Override
	protected void reduce(Text key, Iterable<Text> values,
			Reducer<Text, Text, Text, Text>.Context context) throws IOException,
			InterruptedException {
			Counter counterin = context.getCounter("Cominer", "输入个数：");
			counterin.increment(1);
			double minValue = Double.MAX_VALUE;
			double maxValue = Double.MIN_VALUE;
			double sumValue = 0;
			long numValue = 0;
			JSONObject classifyObj = new JSONObject();
			classifyObj.put("0-50", 0);
			classifyObj.put("50-100", 0);
			classifyObj.put("100-150", 0);
			classifyObj.put("150-200", 0);
			classifyObj.put("200-250", 0);
			classifyObj.put("250-300", 0);
			classifyObj.put("300-!", 0);
			//"{"number":90.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"
			for(Text value :values){
				JSONObject jsonObj = new JSONObject(value.toString());
				double pmvalue = jsonObj.getDouble("number");
				minValue = Math.min(minValue, pmvalue);//计算最小值
				maxValue = Math.max(maxValue, pmvalue);//计算最大值
				sumValue += pmvalue;//计算总和
				numValue++;//计算个数
				JSONObject classisyObj = jsonObj.getJSONObject("classify");//"{"0-50:0","50-100:1"}"
				@SuppressWarnings("unchecked")
				Iterator<String> keys = classifyObj.keys();
				while(keys.hasNext()){
					String keyObj = keys.next();
					classifyObj.put(keyObj, classifyObj.getInt(keyObj)+classisyObj.getInt(keyObj));//计算分类
				}
			}
			JSONObject resultObj = new JSONObject();//输出JSON
			resultObj.put("min", minValue);
			resultObj.put("max", maxValue);
			resultObj.put("sum", sumValue);
			resultObj.put("num", numValue);
			resultObj.put("classify", classifyObj);
			context.write(key, new Text(resultObj.toString()));
	}
}
