package model.overview;

import java.util.Date;

public class VmData {
	
	private String vmUuid, vifUuid;
	private String vmNameLabel;
	private String vmVifNumber;
	private String vmIpAddr;
	private String vmMacAddr;
	private String vmSwitchPort;
	private String vmSwitch;
	private Date LastSeen;
	
	public String getVmUuid() {
		return vmUuid;
	}
	public void setVmUuid(String vmuuid) {
		this.vmUuid = vmuuid;
	}
	public String getVifUuid() {
		return vifUuid;
	}
	public void setVifUuid(String vifUuid) {
		this.vifUuid = vifUuid;
	}
	public String getVmNameLabel() {
		return vmNameLabel;
	}
	public void setVmNameLabel(String vmNameLabel) {
		this.vmNameLabel = vmNameLabel;
	}
	public String getVmVifNumber() {
		return vmVifNumber;
	}
	public void setVmVifNumber(String vmVifNumber) {
		this.vmVifNumber = vmVifNumber;
	}
	public String getVmSwitch() {
		return vmSwitch;
	}
	public void setVmSwitch(String vmSwitch) {
		this.vmSwitch = vmSwitch;
	}
	public String getVmSwitchPort() {
		return vmSwitchPort;
	}
	public void setVmSwitchPort(String vmSwitchPort) {
		this.vmSwitchPort = vmSwitchPort;
	}
	public String getVmIpAddr() {
		return vmIpAddr;
	}
	public void setVmIpAddr(String vmIpAddr) {
		this.vmIpAddr = vmIpAddr;
	}
	public String getVmMacAddr() {
		return vmMacAddr;
	}
	public void setVmMacAddr(String vmMacAddr) {
		this.vmMacAddr = vmMacAddr;
	}
	
	public Date getLastSeen() {
		return LastSeen;
	}
	public void setLastSeen(Date lastSeen) {
		LastSeen = lastSeen;
	}
	@Override
	public String toString() {
		return "VMData [vmUuid=" + vmUuid + ", vifUuid=" + vifUuid
				+ ", vmNameLabel=" + vmNameLabel + ", vmVifNumber="
				+ vmVifNumber + ", vmIpAddr=" + vmIpAddr + ", vmMacAddr="
				+ vmMacAddr + ", vmSwitchPort=" + vmSwitchPort + "]";
	}
}
