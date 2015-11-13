package org.zonesion.hadoop.base.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sensor {
	
	private String title;
	private String unit;
	private String channal;
	
	public Sensor() {
		super();
	}
	public Sensor(String title, String unit, String channal) {
		super();
		this.title = title;
		this.unit = unit;
		this.channal = channal;
	}
	@XmlElement
	public String getChannal() {
		return channal;
	}
	public void setChannal(String channal) {
		this.channal = channal;
	}
	
	@XmlElement
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "Sensor [channal=" + channal + ", title=" + title + ", unit="
				+ unit  + "]";
	}
	
	

}
