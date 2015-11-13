package org.zonesion.webapp.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.zonesion.hadoop.base.bean.Gate;
import org.zonesion.webapp.sensor.service.GateService;
import org.zonesion.webapp.sensor.service.GateServiceImpl;

@Path("/gate")
public class GateResource {
	
	private GateService gateService = new GateServiceImpl();

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Gate> findGates(){
		return gateService.findGates();
	}
	
}
