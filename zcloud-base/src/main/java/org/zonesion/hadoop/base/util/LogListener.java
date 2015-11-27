package org.zonesion.hadoop.base.util;

//Util类方法回调View视图类方法
public abstract class LogListener {

	/**
	 * 打印日志信息
	 * @param info
	 */
	public abstract void log(String info);
	
	/**
	 * 记录进度信息
	 * @param i
	 * @param sum
	 */
	public abstract void progress(int i,int sum);
}
