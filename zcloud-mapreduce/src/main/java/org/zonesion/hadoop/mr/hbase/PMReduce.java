package org.zonesion.hadoop.mr.hbase;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.JSONObject;
import org.apache.hadoop.mapreduce.Counter;
/**输入<key,value>为
 <"userid:channal:2015-05",
[
"{"number":90.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}",
"{"number":80.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"
"{"number":70.12,"classify":"{"0-50":0,"50-100":1,"100-150":0,"150-200":0,"200-250":0,"250-300":0,"300-!":0}"
]
}>

**/

public class PMReduce extends TableReducer<Text, Text, ImmutableBytesWritable> {

	@Override
	protected void reduce(Text key, Iterable<Text> values,
			Reducer<Text, Text, ImmutableBytesWritable, Writable>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
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
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String keyString = key.toString();//userid+":"+channalid+":"+arg+":"+at
		String userid = keyString.split(":")[0];//userid
		String channal = keyString.split(":")[1];
		String querytype = keyString.split(":")[2];
		String at = keyString.split(":")[3];
 		String keytail = keyString.substring(keyString.indexOf(":")+1,keyString.length());//channalid+":"+arg+":"+at
		byte[] keytailBytes =Bytes.toBytes(keytail);//channalid+":"+arg+":"+at
		byte[] useridHash = md.digest(Bytes.toBytes(userid));//MD5(userid)
		byte[] rowkey = new byte[useridHash.length+keytailBytes.length];
		int offset = 0;
		offset = Bytes.putBytes(rowkey, offset, useridHash, 0, useridHash.length);
		Bytes.putBytes(rowkey, offset, keytailBytes, 0, keytailBytes.length);//MD5(userid)+channalid+":"+arg+":"+at
		Put put = new Put(rowkey);//put实例化，每一个词存一行
   		 //列族为content,列修饰符为count，列值为数目
		put.add(Bytes.toBytes("content"),Bytes.toBytes("userid"),Bytes.toBytes(userid));
		put.add(Bytes.toBytes("content"),Bytes.toBytes("channal"),Bytes.toBytes(channal));
		put.add(Bytes.toBytes("content"),Bytes.toBytes("type"),Bytes.toBytes(querytype));
		put.add(Bytes.toBytes("content"),Bytes.toBytes("at"),Bytes.toBytes(at));
		put.add(Bytes.toBytes("content"),Bytes.toBytes("min"),Bytes.toBytes(String.valueOf(minValue)));
		put.add(Bytes.toBytes("content"),Bytes.toBytes("max"),Bytes.toBytes(String.valueOf(maxValue)));
		put.add(Bytes.toBytes("content"),Bytes.toBytes("avg"),Bytes.toBytes(String.valueOf(avgValue)));
		put.add(Bytes.toBytes("content"),Bytes.toBytes("classify"),Bytes.toBytes(resultObj.toString()));
		context.write(new ImmutableBytesWritable(Bytes.toBytes(key.toString())), put);// 输出求和后的<key,value>
	}
}
