package controller.overview.vms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.overview.Device;
import model.overview.Port;
import model.overview.Switch;
import model.overview.VmData;
import controller.floodlightprovider.FloodlightProvider;
import controller.overview.json.DevicesJSON;
import controller.util.JSONException;

public class VmDataGetter {
	private static List<VmData> vms = new ArrayList<VmData>();

	public static List<VmData> getVmDatas() {
		updateVMDatas();
		return vms;
	}

	public static void updateVMDatas() {
		getDataFromController();
		getDataFromXenServer();
	}

	public static VmData getVmData(String sw, String port) {
		// the switch must be xenserver switch
		Iterator<VmData> it = vms.iterator();
		VmData vm = null;
		while (it.hasNext()) {
			vm = it.next();
			if (vm.getVmSwitch().equals(sw)
					&& vm.getVmSwitchPort().equals(port))
				return vm;
		}
		return null;
	}

	private static void getDataFromController() {
		// TODO Auto-generated method stub
		List<Device> devices = null;
		try {
			devices = DevicesJSON.getDeviceSummaries();
			List<Port> ports = null;
			Device device = null;
			Switch sw = null;
			Port port = null;
			VmData vm = new VmData();
			Iterator<Device> itd = devices.iterator();
			while (itd.hasNext()) {
				device = itd.next();
				if (device.getIpv4() != null) {
					vm.setVmIpAddr(device.getIpv4());
				}
				if (device.getMacAddress() != null) {
					vm.setVmMacAddr(device.getMacAddress());
				}
				if (device.getAttachedSwitch() != null) {
					vm.setVmSwitch(device.getAttachedSwitch());
				}
				if (device.getSwitchPort() != 0) {
					vm.setVmSwitchPort(String.valueOf(device.getSwitchPort()));
				}
				if (device.getLastSeen() != null) {
					vm.setLastSeen(device.getLastSeen());
				}
				sw = FloodlightProvider.getSwitch(device.getAttachedSwitch(), false);
				if (sw != null) {
					ports = sw.getPorts();
					Iterator<Port> itp = ports.iterator();
					while (itp.hasNext()) {
						port = itp.next();
						if (port.getPortNumber().equals(vm.getVmSwitchPort())) {
							vm.setVmVifNumber(port.getName());
							break;
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getDataFromXenServer() {
		// TODO Auto-generated method stub
		String command = "xe vif-list | grep uuid";
		String command1 = "xe vif-param-list uuid=" + " | grep MAC";
		String command2 = "xe vm-param-list uuid="
				+ " | grep -E \'dom-id|network\'";
	}
}
