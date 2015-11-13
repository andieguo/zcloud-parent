package org.zonesion.hadoop.base.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Gate{
	private String serverAddr;
	private String userid;
	private String userkey;
	private List<Sensor> sensors;
	
	@XmlElement
	public List<Sensor> getSensors() {
		return sensors;
	}
	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}
	
	@XmlElement
	public String getServerAddr() {
		return serverAddr;
	}
	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	
	@XmlElement
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@XmlElement
	public String getUserkey() {
		return userkey;
	}
	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}
	
	public Gate(){
		super();
	}

	public Gate(String serverAddr, String userid, String userkey,List<Sensor> sensors) {
		super();
		this.serverAddr = serverAddr;
		this.userid = userid;
		this.userkey = userkey;
		this.sensors = sensors;
	}
	public Gate(String serverAddr, String userid, String userkey) {
		super();
		this.serverAddr = serverAddr;
		this.userid = userid;
		this.userkey = userkey;
	}
	
	@Override
	public String toString() {
		return "Gate [serverAddr=" + serverAddr + ", userid=" + userid
				+ ", userkey=" + userkey + ", sensors=" + sensors + "]";
	}
	
	public int getSensorSize(){
		return this.sensors.size();
	}
	
}
