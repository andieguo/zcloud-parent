package org.zonesion.hadoop.upload.view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UploadView extends JFrame {

	private static final long serialVersionUID = 1L;
	public JTextField srcText = null;
	public JTextField hdfsText = null;
	private JButton srcselectBtn = null;
	public  JProgressBar progressBar = null;
	public JTextArea logTextArea = null;
	private JScrollPane scroll = null;
	private JButton okBtn = null;
	private JButton resetBtn=null;
	private JPanel contentPanel = null;
	private JPanel panel = null;
	private JPanel panel_1 = null;
	private JPanel panel_2 = null;
	private JPanel panel_3 = null;
	private JPanel panel_4 = null;
	
	public static void main(String[] args) {
		new UploadView();
	}
	
	public UploadView() {
		this.setBounds(300, 200, 425, 400);
		this.setTitle("Upload To HDFS");
		this.setResizable(false);
		init();
		addComponent();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void init() {
		contentPanel = new JPanel();
		panel = new JPanel();
		panel_1 = new JPanel();
		panel_2 = new JPanel();
		panel_3 = new JPanel();
		panel_4 = new JPanel();
		srcText = new JTextField(22);
		hdfsText = new JTextField(29);
		hdfsText.setText("hdfs://master.zonesion:9000");
		srcText.setEnabled(false);
		srcselectBtn = new JButton("浏览");
		// 创建进度条
		progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
		// 创建进度条当前值
		progressBar.setValue(0);
		// 设置进度条上是否显示%比
		progressBar.setStringPainted(true);
		// 设置进度条默认大小
		progressBar.setPreferredSize(new Dimension(290, 20));
//		progressBar.setBackground(Color.white);
//		progressBar.setForeground(Color.green);
		logTextArea = new JTextArea(12,35); 
		logTextArea.setEditable(false);
//		logTextArea.setLineWrap(true);
		 scroll = new JScrollPane(logTextArea); 
		//分别设置水平和垂直滚动条自动出现 
		scroll.setHorizontalScrollBarPolicy( 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		scroll.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		okBtn = new JButton("拷贝");
		resetBtn=new JButton("重置");
	}

	private void addComponent() {
		this.add(contentPanel);
		// 创建监听器
		 UploadListener listener = new UploadListener(this);
		this.addWindowListener(listener);
		 //面板0
		panel.add(new JLabel("本地数据源："));
		panel.add(srcText);
		panel.add(srcselectBtn);
		srcselectBtn.setActionCommand("srcSelect");
		srcselectBtn.addActionListener(listener);
		//面板1
		panel_1.add(new JLabel("HDFS服务器:"));
		panel_1.add(hdfsText);
		//面板2
		panel_2.add(new JLabel("拷贝进度条："));
		panel_2.add(progressBar);
		//面板3
		panel_3.setSize(20, 100);
		panel_3.add(scroll);
		//面板4
		okBtn.setActionCommand("OK");
		okBtn.addActionListener(listener);
		panel_4.add(okBtn);
		resetBtn.setActionCommand("reset");
		resetBtn.addActionListener(listener);
		panel_4.add(resetBtn);
		//面板contentPanel
		contentPanel.setLayout(new FlowLayout());
		contentPanel.add(panel);
		contentPanel.add(panel_1);
		contentPanel.add(panel_2);
		contentPanel.add(panel_3);
		contentPanel.add(panel_4);
	}
}
