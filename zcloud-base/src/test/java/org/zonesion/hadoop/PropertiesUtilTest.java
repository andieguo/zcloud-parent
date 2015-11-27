package org.zonesion.hadoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.zonesion.hadoop.base.util.PropertiesUtil;

import junit.framework.TestCase;

public class PropertiesUtilTest extends TestCase {

	public void testGetProperties() {
		System.out.println(this.getClass().getResource("/config.properties").getPath());
		System.out.println(ClassLoader.getSystemResource("config.properties").getPath());
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties");
		Properties pro = PropertiesUtil.loadFromInputStream(input);
		System.out.println(pro.get("zcloud.download.local.home"));
	}
	
	public void testGetProperties1() {
		System.out.println(this.getClass().getResource("/config.properties").getPath());
		System.out.println(ClassLoader.getSystemResource("config.properties").getPath());
		InputStream input = this.getClass().getResourceAsStream("/config.properties");
		Properties pro = PropertiesUtil.loadFromInputStream(input);
		System.out.println(pro.get("name")+","+pro.getProperty("age"));
	}
	
	public void testSetProperties(){
		InputStream input = this.getClass().getResourceAsStream("/config.properties");
		Properties pro = PropertiesUtil.loadFromInputStream(input);
		pro.setProperty("name", "jack");
		String path = this.getClass().getResource("/config.properties").getPath();
		OutputStream out;
		try {
			out = new FileOutputStream(new File(path));
			pro.store(out, "update");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testIntegr(){
		System.out.println(Integer.valueOf("2000")==2000);
		System.out.println(Integer.valueOf("2000").equals(2000));
	}
}
