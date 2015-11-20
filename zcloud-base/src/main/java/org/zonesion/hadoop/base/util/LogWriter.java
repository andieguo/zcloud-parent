package org.zonesion.hadoop.base.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * 日志工具类 使用了单例模式，保证只有一个实例。 为了更方便的配置日志文件名，使用属性文件配置。 也可以在程序中指定日志文件名。
 */
public class LogWriter {

	// 该类的唯一的实例
	private static LogWriter logWriter;
	// 文件输出流
	private PrintWriter writer;
	// 日志文件名
	private String logFileName;
	
	public synchronized static LogWriter getLogWriter(String logFileName)
			throws Exception {
		if (logWriter == null) {
			logWriter = new LogWriter(logFileName);
		}
		return logWriter;
	}

	private LogWriter(String fileName) throws Exception {
		this.logFileName = fileName;
		File logFile = new File(this.getClass().getResource("/log").getPath()+File.separator+this.logFileName);
		try {
			// 其中的FileWriter()中的第二个参数的含义是:是否在文件中追加内容
			// PrintWriter()中的第二个参数的含义是：自动将数据flush到文件中
			writer = new PrintWriter(new FileWriter(logFile, true), true);
			System.out.println("日志文件的位置：" + logFile.getAbsolutePath());
		} catch (IOException ex) {
			String errmsg = "无法打开日志文件:" + logFile.getAbsolutePath();
			throw new Exception(errmsg, ex);
		}
	}
	
	/**
	 * 往日志文件中写一条日志信息 为了防止多线程同时操作(写)日志文件，造成文件”死锁”。使用synchronized关键字
	 * 
	 * @param logMsg
	 *            日志消息
	 */
	public synchronized void log(String logMsg) {
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String info = format.format(new java.util.Date()) + ": " + logMsg;
		System.out.println(info);
		this.writer.println(info);
	}
	
	/**
	 * 往日志文件中写一条异常信息 使用synchronized关键字。
	 * 
	 * @param ex
	 *            待写入的异常
	 */
	public synchronized void log(Exception ex) {
		writer.println(new java.util.Date() + ": ");
		ex.printStackTrace(writer);
	}

	/**
	 * 关闭LogWriter
	 */
	public void close() {
		logWriter = null;
		if (writer != null) {
			writer.close();
		}
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	
	
}
