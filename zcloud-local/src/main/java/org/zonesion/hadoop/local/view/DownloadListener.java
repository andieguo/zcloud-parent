package org.zonesion.hadoop.local.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.xml.sax.SAXException;
import org.zonesion.hadoop.base.util.LogListener;
import org.zonesion.hadoop.local.util.RestLocal;

public class DownloadListener extends LogListener implements ActionListener {
	DownloadView downloadView = null;
	String configText,dstText;
	static double Length = 0;
	static long startTime = 0;
	String line = "";

	public DownloadListener(DownloadView downloadView) {
		this.downloadView = downloadView;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("configSelect")) {
			configText = getSelectFileName(JFileChooser.FILES_AND_DIRECTORIES);
			System.out.println(configText);
			downloadView.configText.setText(configText);
		} else if (e.getActionCommand().equals("dstSelect")) {
			dstText = getSelectFileName(JFileChooser.FILES_AND_DIRECTORIES);
			System.out.println(dstText);
			downloadView.dstText.setText(dstText);
		} else if (e.getActionCommand().equals("reset")) {
			configText = null;dstText=null;line = "";
			downloadView.configText.setText("");
			downloadView.dstText.setText("");
			downloadView.logTextArea.setText("");
			downloadView.progressBar.setValue(0);
		} else if (e.getActionCommand().equals("OK")) {
			startTime = System.currentTimeMillis();
			if (configText != null && dstText!=null) {
				new Thread(new Runnable() {
					public void run() {
						try {
							File configTextfile = new File(configText);
							if(configTextfile.isFile()){//校验配置文件
								File dstTextfile = new File(dstText);
								if(dstTextfile.isDirectory()){//校验存储文件夹
									//执行下载
									RestLocal rest = new RestLocal(dstText);
									downloadView.registerRestListener(rest);
									rest.registerLogListener(DownloadListener.this);
									rest.executeJob(configText);
								}else{
									log("存储地址必须为文件夹!");
									JOptionPane.showMessageDialog(null, "存储地址必须为文件夹!");
								}
							}else{
								log("配置文件必须为文件!");
								JOptionPane.showMessageDialog(null, "配置文件必须为文件!");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							 if(e instanceof SAXException){
									log("配置文件格式错误!");
									JOptionPane.showMessageDialog(null, "配置文件格式错误!");
								}
							e.printStackTrace();
						}
					}
				}).start();
			}else{
				if(configText ==null) { 
					log("配置文件地址不为空!");
					JOptionPane.showMessageDialog(null, "配置文件地址不为空!");
				}
				else if(dstText ==null) { 
					log("存储地址不为空!");
					JOptionPane.showMessageDialog(null, "存储地址不为空!");
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
