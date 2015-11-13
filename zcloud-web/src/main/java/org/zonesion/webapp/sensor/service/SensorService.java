package org.zonesion.webapp.sensor.service;

import java.util.List;

import org.zonesion.hadoop.base.bean.Sensor;

public interface SensorService {

	public List<Sensor> findSensors(String userid);
}
