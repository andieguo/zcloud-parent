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
import org.zonesion.hadoop.base.util.LogWriter;
import org.zonesion.hadoop.local.util.RestLocal;

public class DownloadListener extends LogListener implements ActionListener {
	DownloadView downloadView = null;
	String configText,dstText;
	static double Length = 0;
	static long startTime = 0;
	String line = "";
	LogWriter logWriter ;

	public DownloadListener(DownloadView uploadView) {
		this.downloadView = uploadView;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			logWriter = LogWriter.getLogWriter(File.separator+format.format(new java.util.Date())+".log");//日志文件存放在类路径log文件夹下
			logWriter.registerLogListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			DownloadView.progressBar.setValue(0);
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
									rest.executeJob(configText);
								}else{
									logWriter.logTextArea("存储地址必须为文件夹!");
									JOptionPane.showMessageDialog(null, "存储地址必须为文件夹!");
								}
							}else{
								logWriter.logTextArea("配置文件必须为文件!");
								JOptionPane.showMessageDialog(null, "配置文件必须为文件!");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							 if(e instanceof SAXException){
									logWriter.logTextArea("配置文件格式错误!");
									JOptionPane.showMessageDialog(null, "配置文件格式错误!");
								}
							e.printStackTrace();
						}
					}
				}).start();
			}else{
				if(configText ==null) { 
					logWriter.logTextArea("配置文件地址不为空!");
					JOptionPane.showMessageDialog(null, "配置文件地址不为空!");
				}
				else if(dstText ==null) { 
					logWriter.logTextArea("存储地址不为空!");
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
		StringBuilder builder = new StringBuilder(line);
		line = builder.append(info).append("\r\n").toString();
		downloadView.logTextArea.setText(line);
	}

	
}
