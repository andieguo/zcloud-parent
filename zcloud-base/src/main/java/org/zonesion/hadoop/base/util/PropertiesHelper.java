package org.zonesion.hadoop.base.util;

//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesHelper {

	private  Properties prop;
	private  OutputStream output;
	private InputStream input;
	private String filename;
	private static  PropertiesHelper instance;
	
	public synchronized static PropertiesHelper getInstance(String filename){//单例模式
		if(instance ==null){
			instance = new PropertiesHelper(filename);//调用构造方法
		}
		return instance;
	}
	
	private PropertiesHelper(String filename) {
		super();
		this.filename = filename;
		this.init(filename);//调用初始化方法，只执行一次（打开输入流只打开一次）
	}
	
	//初始化连接----只执行一次
	public void init(String filename) {
		// TODO Auto-generated method stub
		prop = new Properties();
		try {
			System.out.println("update读路径："+ClassLoader.getSystemResource(filename).getPath());
			input = ClassLoader.getSystemResourceAsStream(filename);//从类路径下加载
			//input = new FileInputStream(filename);//从项目的根路径下获取：zcloud-local/[filename]
			//input = PropertiesHelper.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}
			prop.load(input);//加载资源
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取属性值
	 * @param key 属性名
	 * @return 属性值
	 */
	public  String getProperty(String key) {
		return prop.getProperty(key);
	}

	/**
	 * 更新properties文件的键值对  
     * 如果该主键已经存在，更新该主键的值；  
     * 如果该主键不存在，则插件一对键值。
	 * @param key 属性名
	 * @param value	属性值
	 */
	public  void setProperty(String key, String value) {
		try {
			prop.setProperty(key, value);
			System.out.println("update写路径："+ClassLoader.getSystemResource(filename).getPath());
			output = new FileOutputStream(ClassLoader.getSystemResource(filename).getPath());
			if (output == null) {
				System.out.println("Sorry, unable to find " + filename);
				return ;
			}
			prop.store(output,"update");//已追加的方式更新
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public Set<Entry<Object, Object>> entrySet(){
			return prop.entrySet();
	}
	
	/**
	 * 打印属性文件
	 */
	public void printProperties() {
		//执行操作
		Set<Entry<Object, Object>> props = prop.entrySet();
		for (Entry<Object, Object> entrys : props) {
			System.out.println(entrys.getKey() + ":" + entrys.getValue());
		}
	}
	
	//关闭资源
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}