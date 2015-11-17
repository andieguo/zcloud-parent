package org.zonesion.webapp.bean;

public class NameNodeInfo {

	private String ConfiguredCapacity;
	private String DFSUsed;
	private String NonDFSUsed;
	private String DFSRemaining;
	private String DFSUsedPercent;
	private String DFSRemainingPercent;
	public String getConfiguredCapacity() {
		return ConfiguredCapacity;
	}
	public void setConfiguredCapacity(String configuredCapacity) {
		ConfiguredCapacity = configuredCapacity;
	}
	public String getDFSUsed() {
		return DFSUsed;
	}
	public void setDFSUsed(String dFSUsed) {
		DFSUsed = dFSUsed;
	}
	public String getNonDFSUsed() {
		return NonDFSUsed;
	}
	public void setNonDFSUsed(String nonDFSUsed) {
		NonDFSUsed = nonDFSUsed;
	}
	public String getDFSRemaining() {
		return DFSRemaining;
	}
	public void setDFSRemaining(String dFSRemaining) {
		DFSRemaining = dFSRemaining;
	}
	public String getDFSUsedPercent() {
		return DFSUsedPercent;
	}
	public void setDFSUsedPercent(String dFSUsedPercent) {
		DFSUsedPercent = dFSUsedPercent;
	}
	public String getDFSRemainingPercent() {
		return DFSRemainingPercent;
	}
	public void setDFSRemainingPercent(String dFSRemainingPercent) {
		DFSRemainingPercent = dFSRemainingPercent;
	}
	public NameNodeInfo(String configuredCapacity, String dFSUsed, String nonDFSUsed, String dFSRemaining,
			String dFSUsedPercent, String dFSRemainingPercent) {
		super();
		ConfiguredCapacity = configuredCapacity;
		DFSUsed = dFSUsed;
		NonDFSUsed = nonDFSUsed;
		DFSRemaining = dFSRemaining;
		DFSUsedPercent = dFSUsedPercent;
		DFSRemainingPercent = dFSRemainingPercent;
	}
	public NameNodeInfo() {
		super();
	}
	
}
