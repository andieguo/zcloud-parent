package org.zonesion.hadoop.mr.hbase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class PMMapper extends
		Mapper<LongWritable, Text, Text, Text> {

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String[] args = new String[]{"year","month","day","hour"};
		String source = value.toString();
		String userid="",channalid = "";
		try {
			Counter counterin = context.getCounter("Map", "输入个数：");
			counterin.increment(1);
			Counter counterout = context.getCounter("Map", "输出个数：");
			//输入路径：hdfs://master.zonesion:9000/user/hadoop/zcloud/userid/channalid/file2.txt=1
            String path = ((FileSplit) context.getInputSplit()).getPath().toString();//读取输入PATH
            //从输入路径中获取userid和channalid
            String [] paths =  path.substring(path.indexOf("zcloud/"), path.length()).split("/");  
            if(paths.length==4){
            	 userid = paths[1];
            	 channalid = paths[2];
            }
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			JSONObject jsonObject = new JSONObject(source);
			JSONArray jsonArray = jsonObject.getJSONArray("datapoints");
			SimpleDateFormat fomateDate = null;
			for(String arg : args){
				if(arg.equals("year")){
					fomateDate  = new SimpleDateFormat("yyyy");
				} else if(arg.equals("month")){
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
					double pmvalue = Double.valueOf(object.getString("value"));
					JSONObject resultObj = new JSONObject();//返回的结果字符串
					JSONObject classifyObj = new JSONObject();
					classifyObj.put("0-50", 0);
					classifyObj.put("50-100", 0);
					classifyObj.put("100-150", 0);
					classifyObj.put("150-200", 0);
					classifyObj.put("200-250", 0);
					classifyObj.put("250-300", 0);
					classifyObj.put("300-!", 0);
					if (pmvalue < 50) {
						classifyObj.put("0-50", 1);
					} else if (pmvalue < 100) {
						classifyObj.put("50-100", 1);
					} else if (pmvalue < 150) {
						classifyObj.put("100-150", 1);
					} else if (pmvalue < 200) {
						classifyObj.put("150-200", 1);
					} else if (pmvalue < 250) {
						classifyObj.put("200-250", 1);
					} else if (pmvalue < 300) {
						classifyObj.put("250-300", 1);
					} else {
						classifyObj.put("300-!", 1);
					}
					resultObj.put("number", pmvalue);
					resultObj.put("classify", classifyObj);
					context.write(new Text(userid+":"+channalid+":"+arg+":"+at),new Text(resultObj.toString()));
					//输出<key,value>为<"2015-05",{"number":90.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"}>
					//输出<key,value>为<"userid:channal:2015-05",{"number":90.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"}>
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
