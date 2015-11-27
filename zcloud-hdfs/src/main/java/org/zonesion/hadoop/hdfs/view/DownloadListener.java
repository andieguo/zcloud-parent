package org.zonesion.hadoop.hdfs.view;

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
import org.zonesion.hadoop.hdfs.util.RestHDFS;


public class DownloadListener extends LogListener implements ActionListener {
	DownloadView downloadView = null;
	String src = null;
	static double Length = 0;
	static long startTime = 0;
	String line="";

	public DownloadListener(DownloadView downloadView) {
		this.downloadView = downloadView;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("srcSelect")) {
			src = getSelectFileName(JFileChooser.FILES_AND_DIRECTORIES);
			 System.out.println(src);
			downloadView.srcText.setText(src);
		} else if (e.getActionCommand().equals("reset")) {
			src = null;line = "";
			downloadView.srcText.setText("");
			downloadView.logTextArea.setText("");
			downloadView.progressBar.setValue(0);
		} else if (e.getActionCommand().equals("OK")) {
			startTime = System.currentTimeMillis();
			final String hdfsName = downloadView.hdfsText.getText();
			if (src != null && !hdfsName.equals("")) {
				// 获得拷贝文件或文件夹的总个数
				Length = getFileNumber(src);
				new Thread(new Runnable() {
					public void run() {
						try {
							File file = new File(src);
							if(file.isFile()){//校验配置文件
								log("正在连接HDFS服务器........");
							    Configuration conf = new Configuration();
						        conf.set("fs.default.name", hdfsName);
						        FileSystem fs = FileSystem.get(conf);
								if(fs != null){//校验HDFS
									fs.close();
									log("HDFS服务器连接成功!");
									RestHDFS rest = new RestHDFS(hdfsName);
									downloadView.registerRestListener(rest);
									rest.registerLogListener(DownloadListener.this);
									rest.executeJob(src);
								}else{
									log("HDFS服务器连接失败!");
									JOptionPane.showMessageDialog(null, "HDFS服务器连接失败!");
								}
							}else{
								log("配置文件必须为文件!");
								JOptionPane.showMessageDialog(null, "配置文件必须为文件!");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
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
					log("配置文件地址不为空!");
					JOptionPane.showMessageDialog(null, "配置文件地址不为空!");
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

	@Override
	public void log(String info) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder builder = new StringBuilder(line);
		line = builder.append(format.format(new java.util.Date())).append(":").append(info).append("\r\n").toString();
		downloadView.logTextArea.setText(line);
	}

	@Override
	public void progress(int i, int sum) {
		// TODO Auto-generated method stub
		int precent=(int)( ((float)i/(float)sum)*100); 
		downloadView.progressBar.setValue(precent); 
	}

}
