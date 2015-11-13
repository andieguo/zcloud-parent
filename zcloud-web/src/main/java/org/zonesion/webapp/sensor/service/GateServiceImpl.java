package org.zonesion.webapp.sensor.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xml.sax.SAXException;
import org.zonesion.hadoop.base.bean.Gate;
import org.zonesion.hadoop.base.util.XmlService;

public class GateServiceImpl implements GateService {
	private XmlService xmlService  = XmlService.getInstance();

	public List<Gate> findGates() {
		// TODO Auto-generated method stub

		InputStream input = null;
		List<Gate> gates;
		try {
			input = this.getClass().getResourceAsStream("/sensors.xml");
			gates = xmlService.readXML(input);
			return gates;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(input !=null){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(GateServiceImpl.class.getResource("/sensors.xml"));
		//file:/home/hadoop/workspace/zcloud-parent/zcloud-web/target/classes/sensors.xml
		System.out.println(ClassLoader.getSystemResource("sensors.xml"));
		GateService gateService = new GateServiceImpl();
		   for(Gate g : gateService.findGates()){
			   System.out.println(g);
		   }
	}
}
