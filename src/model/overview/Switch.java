package model.overview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Switch {

	String manufacturerDescription, hardwareDescription, softwareDescription,
			serialNumber, datapathDescription, dpid, packetCount, byteCount,
			flowCount;

	List<Port> ports = new ArrayList<Port>();
	List<Flow> flows = new ArrayList<Flow>();

	public Switch(String dpid) {
		this.dpid = dpid;
	}

	public String getPacketCount() {
		return packetCount;
	}

	public void setPacketCount(String packetCount) {
		this.packetCount = packetCount;
	}

	public String getByteCount() {
		return byteCount;
	}

	public void setByteCount(String byteCount) {
		this.byteCount = byteCount;
	}

	public String getFlowCount() {
		return flowCount;
	}

	public void setFlowCount(String flowCount) {
		this.flowCount = flowCount;
	}

	public String getDpid() {
		return dpid;
	}

	public void setDpid(String dpid) {
		// this.dpid = manufacturerDescription;
		this.dpid = dpid;
	}

	public String getManufacturerDescription() {
		return manufacturerDescription;
	}

	public void setManufacturerDescription(String manufacturerDescription) {
		this.manufacturerDescription = manufacturerDescription;
	}

	public String getHardwareDescription() {
		return hardwareDescription;
	}

	public void setHardwareDescription(String hardwareDescription) {
		this.hardwareDescription = hardwareDescription;
	}

	public String getSoftwareDescription() {
		return softwareDescription;
	}

	public void setSoftwareDescription(String softwareDescription) {
		this.softwareDescription = softwareDescription;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDatapathDescription() {
		return datapathDescription;
	}

	public void setDatapathDescription(String datapathDescription) {
		this.datapathDescription = datapathDescription;
	}

	public List<Port> getPorts() {
		return ports;
	}

	public void setPorts(List<Port> ports) {
		this.ports = ports;
	}

	public List<Flow> getFlows(){
		//Collections.sort(flows);
		return flows;
	}

	public void setFlows(List<Flow> flows) {
		this.flows = flows;
	}
}
