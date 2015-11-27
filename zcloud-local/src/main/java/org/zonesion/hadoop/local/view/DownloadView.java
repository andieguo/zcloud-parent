package org.zonesion.hadoop.local.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.zonesion.hadoop.base.util.RestListener;


public class DownloadView extends JFrame {

	private static final long serialVersionUID = 1L;
	public JTextField configText = null;
	public JTextField dstText = null;
	private JButton configselectBtn = null;
	private JButton dstselectBtn = null;
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
	private DownloadListener listener = null;
	private RestListener restListener = null;
	
	public static void main(String[] args) {
		new DownloadView();
	}
	
	public DownloadView() {
		this.setBounds(300, 200,425, 430);
		this.setTitle("Download To Local");
		this.setResizable(false);
		init();
		addComponent();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.addWindowListener(new WindowAdapter()  
        {  
            @Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
    			System.out.println("触发windowClosing事件");  
    			if(restListener!=null)restListener.close();
			}
        });  
	}
	
	private void init() {
		contentPanel = new JPanel();
		panel = new JPanel();
		panel_1 = new JPanel();
		panel_2 = new JPanel();
		panel_3 = new JPanel();
		panel_4 = new JPanel();
		configText = new JTextField(22);
		dstText = new JTextField(22);
		configText.setEnabled(false);
		dstText.setEnabled(false);
		configselectBtn = new JButton("浏览");
		dstselectBtn = new JButton("浏览");
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
		okBtn = new JButton("下载");
		resetBtn=new JButton("重置");
	}

	private void addComponent() {
		this.add(contentPanel);
		// 创建监听器
		 listener=new DownloadListener(this);
		 //面板0
		panel.add(new JLabel("配置文件:"));
		panel.add(configText);
		panel.add(configselectBtn);
		configselectBtn.setActionCommand("configSelect");
		configselectBtn.addActionListener(listener);
		//面板1
		panel_1.add(new JLabel("存储地址:"));
		panel_1.add(dstText);
		panel_1.add(dstselectBtn);
		dstselectBtn.setActionCommand("dstSelect");
		dstselectBtn.addActionListener(listener);
		//面板2
		panel_2.add(new JLabel("进度条："));
		panel_2.add(progressBar);
		//面板3
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
	
	public void registerRestListener(RestListener restListener){
		this.restListener = restListener;
	}
}
