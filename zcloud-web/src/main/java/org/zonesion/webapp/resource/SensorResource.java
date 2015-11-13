package org.zonesion.webapp.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.zonesion.hadoop.base.bean.Sensor;
import org.zonesion.webapp.sensor.service.SensorService;
import org.zonesion.webapp.sensor.service.SensorServiceImpl;

@Path("/sensor")
public class SensorResource {
	
	private SensorService sensorService = new SensorServiceImpl();

	@GET
	@Path("/userid/{userid}/")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Sensor> findSensors(@PathParam("userid") String userid){
		return sensorService.findSensors(userid);
	}
}
