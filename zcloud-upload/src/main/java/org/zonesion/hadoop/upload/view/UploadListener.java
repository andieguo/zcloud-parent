package org.zonesion.hadoop.upload.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.text.SimpleDateFormat;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.ipc.RemoteException;
import org.xml.sax.SAXException;
import org.zonesion.hadoop.base.util.LogListener;
import org.zonesion.hadoop.base.util.LogWriter;

public class UploadListener extends LogListener implements ActionListener{
	UploadView uploadView = null;
	String src = null;
	static int Length = 0;
	static long startTime = 0;
	LogWriter logWriter;
	String line = "";
	UploadTool uploadTool;

	public UploadListener(UploadView uploadView) {
		this.uploadView = uploadView;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("srcSelect")) {
			src = getSelectFileName(JFileChooser.FILES_AND_DIRECTORIES);
			 System.out.println(src);
			uploadView.srcText.setText(src);
		} else if (e.getActionCommand().equals("reset")) {
			line = "";src = null;
			uploadView.srcText.setText("");
			uploadView.logTextArea.setText("");
			uploadView.progressBar.setValue(0);
		} else if (e.getActionCommand().equals("OK")) {
			startTime = System.currentTimeMillis();
			final String hdfsName = uploadView.hdfsText.getText();
			if (src != null && !hdfsName.equals("")) {
				// 获得拷贝文件或文件夹的总个数
				Length = getFileNumber(src);
				new Thread(new Runnable() {
					public void run() {
						try {
							File file = new File(src);
							if(file.isDirectory()){//校验src
								if(src.endsWith("zcloud")){//校验src
									log("正在连接HDFS服务器........");
									Configuration conf = new Configuration();
									conf.set("fs.default.name",hdfsName);
									FileSystem fs = FileSystem.get(conf);
									if(fs != null){//校验服务器是否能连接上
										log("HDFS服务器连接成功！");
										File srcFile = new File(src);
										uploadTool = new UploadTool();
										uploadTool.registerLogListener(UploadListener.this);
										if (srcFile.isDirectory()) {
											uploadTool.copyDirectory(src,"/user/hadoop/zcloud", conf);
										} else {
											uploadTool.copyFile(src, "/user/hadoop/zcloud",conf);
										}
										log("上传任务完成！");
									}else{
										log("HDFS服务器连接失败！");
										JOptionPane.showMessageDialog(null, "HDFS服务器连接失败！");
									}
								}else{
									log("错误格式的本地数据源！");
									JOptionPane.showMessageDialog(null, "错误格式的本地数据源！");
								}
							}else{
								log("本地数据源必须是文件夹！");
								JOptionPane.showMessageDialog(null, "本地数据源必须是文件夹！");
							}
						} catch (Exception e) {
							System.out.println("发生异常");
							if(e instanceof IOException || e instanceof ConnectException || e instanceof RemoteException || e instanceof NoRouteToHostException){
								log("HDFS服务器连接失败!");
								JOptionPane.showMessageDialog(null, "HDFS服务器连接失败!");
							}else if(e instanceof SAXException){
								log("配置文件格式错误!");
								JOptionPane.showMessageDialog(null, "配置文件格式错误!");
							}
							e.printStackTrace();
						}
					}
				}).start();
			}else{
				if(src ==null) { 
					log("本地数据源不为空!");
					JOptionPane.showMessageDialog(null, "本地数据源不为空!");
				}
				else if(hdfsName.equals("")) {
					log("HDFS服务器地址不为空!");
					JOptionPane.showMessageDialog(null, "HDFS服务器地址不为空!");
				}
			}
		}
	}

	private String getSelectFileName(int mode) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(mode);
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		if (file == null)
			return null;
		return file.getAbsolutePath();
	}

	@SuppressWarnings("unused")
	private double getLength(String src) {
		double length = 0;
		File file = new File(src);
		if (file.isFile())
			return file.length();
		File fs[] = file.listFiles();
		for (File f : fs) {
			if (f.isDirectory()) {
				length += getLength(f.getAbsolutePath());
			} else {
				length += f.length();
			}
		}
		return length;
	}
	
	private int getFileNumber(String src) {
		int length = 0;
		File file = new File(src);
		if (file.isFile())
			return 1;
		File fs[] = file.listFiles();
		for (File f : fs) {
			if (f.isDirectory()) {
				length += getFileNumber(f.getAbsolutePath());
			} else {
				length += 1;
			}
		}
		return length;
	}

	public static void main(String[] args) {
		UploadListener listener = new UploadListener(null);
		System.out.println(listener.getFileNumber("/home/hadoop/workspace_maven/HistorySqoop/zcloud/"));
		System.out.println(listener.getFileNumber("/home/hadoop/workspace_maven/HistorySqoop/zcloud/1674047509"));
		System.out.println(listener.getFileNumber("/home/hadoop/workspace_maven/HistorySqoop/zcloud/367953136"));
		System.out.println(listener.getFileNumber("/home/hadoop/workspace_maven/HistorySqoop/zcloud/1155223953"));
	}

	@Override
	public void log(String info) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder builder = new StringBuilder(line);
		line = builder.append(format.format(new java.util.Date())).append(":").append(info).append("\r\n").toString();
		uploadView.logTextArea.setText(line);
	}

	@Override
	public void progress(int i, int sum) {
		// TODO Auto-generated method stub
		int precent=(int)( ((float)i/(float)sum)*100); 
		uploadView.progressBar.setValue(precent); 
	}
}
