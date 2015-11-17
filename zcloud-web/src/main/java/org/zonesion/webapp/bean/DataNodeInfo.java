package org.zonesion.webapp.bean;

public class DataNodeInfo {

	private long capacity;
	private long dfsused;
	private long nondfsused;
	private String name;
	private String state;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getCapacity() {
		return capacity;
	}
	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}
	public long getDfsused() {
		return dfsused;
	}
	public void setDfsused(long dfsused) {
		this.dfsused = dfsused;
	}
	public long getNondfsused() {
		return nondfsused;
	}
	public void setNondfsused(long nondfsused) {
		this.nondfsused = nondfsused;
	}
	
	public DataNodeInfo(long capacity, long dfsused, long nondfsused, String name, String state) {
		super();
		this.capacity = capacity;
		this.dfsused = dfsused;
		this.nondfsused = nondfsused;
		this.name = name;
		this.state = state;
	}
	
}
