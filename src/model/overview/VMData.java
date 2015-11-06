package model.overview;

public class VMData {
	private String vmUuid;
	private String vifUuid;
	private String vmNameLabel;
	private String vmOvsPort;
	private String vmIpAddr;
	private String vmMacAddr;
	private String vmSwPort;
	
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
	public String getVmOvsPort() {
		return vmOvsPort;
	}
	public void setVmOvsPort(String vmOvsPort) {
		this.vmOvsPort = vmOvsPort;
	}
	public String getVmSwPort() {
		return vmSwPort;
	}
	public void setVmSwPort(String vmSwPort) {
		this.vmSwPort = vmSwPort;
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
	
	@Override
	public String toString() {
		return "VMData [vmUuid=" + vmUuid + ", vifUuid=" + vifUuid
				+ ", vmNameLabel=" + vmNameLabel + ", vmOvsPort=" + vmOvsPort
				+ ", vmIpAddr=" + vmIpAddr + ", vmMacAddr=" + vmMacAddr + "]";
	}
	
}
