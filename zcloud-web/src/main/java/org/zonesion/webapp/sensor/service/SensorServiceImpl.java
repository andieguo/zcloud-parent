package org.zonesion.webapp.sensor.service;

import java.util.List;

import org.zonesion.hadoop.base.bean.Gate;
import org.zonesion.hadoop.base.bean.Sensor;

public class SensorServiceImpl implements SensorService {
	
	GateService gateService = new GateServiceImpl();

	public List<Sensor> findSensors(String userid) {
		// TODO Auto-generated method stub
		List<Gate> gates = gateService.findGates();
		for(Gate gate : gates){
			if(gate.getUserid().equals(userid)){
				return gate.getSensors();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		SensorService sensorService = new SensorServiceImpl();
	   for(Sensor s :sensorService.findSensors("23710173")){
		   System.out.println(s);
	   }
	}
}
