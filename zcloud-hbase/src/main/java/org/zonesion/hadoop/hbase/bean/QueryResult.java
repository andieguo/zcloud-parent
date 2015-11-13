package org.zonesion.hadoop.hbase.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QueryResult {

	private String userid;
	private String channal;
	private String type;
	private String at;
	private float avg;
	private float min;
	private float max;
	private String classify;
	
	@Override
	public String toString() {
		return "QueryResult [userid=" + userid + ", channal=" + channal
				+ ", type=" + type + ", at=" + at + ", avg=" + avg + ", min="
				+ min + ", max=" + max + ", classify=" + classify + "]";
	}

	public QueryResult(){
		
	}
	
	public QueryResult(String userid, String channal, String type, String at,
			float avg, float min, float max, String classify) {
		super();
		this.userid = userid;
		this.channal = channal;
		this.type = type;
		this.at = at;
		this.avg = avg;
		this.min = min;
		this.max = max;
		this.classify = classify;
	}
	@XmlElement
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@XmlElement
	public String getChannal() {
		return channal;
	}
	public void setChannal(String channal) {
		this.channal = channal;
	}
	@XmlElement
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@XmlElement
	public String getAt() {
		return at;
	}
	public void setAt(String at) {
		this.at = at;
	}
	@XmlElement
	public float getAvg() {
		return avg;
	}
	public void setAvg(float avg) {
		this.avg = avg;
	}
	@XmlElement
	public float getMin() {
		return min;
	}
	public void setMin(float min) {
		this.min = min;
	}
	@XmlElement
	public float getMax() {
		return max;
	}
	public void setMax(float max) {
		this.max = max;
	}
	@XmlElement
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	
	
}
