package org.zonesion.hadoop.base.bean;

public class HistoryURL {

	public String serverAddr, id, key,channal,starttime = "1990-01-01T00:00:00Z";

	public HistoryURL(String serverAddr, String id, String key,String channal) {
		this.serverAddr = serverAddr;
		this.id = id;
		this.key = key;
		this.channal = channal;
	}
	
	public HistoryURL(String serverAddr, String id,String key, String channal,
			String starttime) {
		this.serverAddr = serverAddr;
		this.id = id;
		this.key = key;
		this.channal = channal;
		if(starttime != null) this.starttime = starttime;
	}

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannal() {
		return channal;
	}

	public void setChannal(String channal) {
		this.channal = channal;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		String urlsrc  = "http://%s:8080/v2/feeds/%s/datastreams/%s?start=%s&interval=0&limit=2000";
		String url =String.format(urlsrc,serverAddr,id,channal,starttime);
		return url;
	}
	
	

}
