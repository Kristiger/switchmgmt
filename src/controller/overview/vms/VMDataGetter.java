package controller.overview.vms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.overview.Device;
import model.overview.VMData;
import controller.overview.json.DevicesJSON;
import controller.util.JSONException;
import controller.util.SSHConnector;

public class VMDataGetter {
	private static List<VMData> vms = new ArrayList<VMData>();
	private static List<Device> devices;
	private static VMData vm = null;
	private static Device device = null;

	public static List<VMData> getVmDatas() {
		return vms;
	}

	public static VMData getVmData(String sw, String port) {
		// the switch must be xenserver switch
		Iterator<VMData> it = vms.iterator();
		while (it.hasNext()) {
			vm = it.next();
			if (vm.getVmSwPort().equals(port)) {
				return vm;
			}
		}
		return null;
	}

	public static void updateVMDatas() {
		vms = new ArrayList<VMData>();
		getVMUuids();
		getVMDetails();
		getVmSwPort();
		addPortOne();
	}
	
	private static void addPortOne(){
		vm = new VMData();
		vm.setVifUuid("1");
		vm.setVifUuid("1");
		vm.setVmMacAddr("");
		vm.setVmOvsPort("eth0");
		vm.setVmSwPort("1");
		vm.setVmIpAddr("");
		vms.add(vm);
	}

	private static void getVMUuids() {

		String command = "xe vif-list | grep uuid";
		String lines[] = SSHConnector.exec(command).split("\n");
		String vifuuid, vmuuid;

		for (int i = 0; i < lines.length; i += 3) {
			VMData vm = new VMData();
			vifuuid = lines[i].substring(lines[i].indexOf(":") + 2);
			vmuuid = lines[i + 1].substring(lines[i + 1].indexOf(":") + 2);
			vm.setVifUuid(vifuuid);
			vm.setVmUuid(vmuuid);
			vms.add(vm);
		}
	}

	private static void getVMDetails() {
		String command, vifuuid, vmuuid;
		String[] lines;
		Iterator<VMData> it = vms.iterator();
		String MAC, port, ip;
		while (it.hasNext()) {
			vm = it.next();

			// get MAC address
			vifuuid = vm.getVifUuid();
			command = "xe vif-param-list uuid=" + vifuuid + " | grep MAC";
			lines = SSHConnector.exec(command).split("\n");
			if(!lines[0].contains("MAC"))
				continue;
			MAC = lines[0].substring(lines[0].indexOf(":") + 2);
			vm.setVmMacAddr(MAC);

			// get IPv4 and vif port
			vmuuid = vm.getVmUuid();
			command = "xe vm-param-list uuid=" + vmuuid
					+ " | grep -E \'dom-id|network\'";
			lines = SSHConnector.exec(command).split("\n");
			if (lines.length < 2)
				continue;
			
			port = lines[0].substring(lines[0].indexOf(":") + 2);
			ip = lines[1].substring(lines[1].indexOf("ip") + 4,
					lines[1].indexOf(";"));

			// if the vm is not running, the port is -1
			if (!port.equals("-1"))
				vm.setVmOvsPort("vif" + port + ".0");
			else
				vm.setVmOvsPort(port);

			vm.setVmIpAddr(ip);
		}
	}

	private static void getVmSwPort() {
		try {
			devices = DevicesJSON.getDeviceSummaries();
			Iterator<VMData> it = vms.iterator();
			Iterator<Device> itd;

			while (it.hasNext()) {
				vm = it.next();
				itd = devices.iterator();
				while (itd.hasNext()) {
					device = itd.next();
					if (vm.getVmMacAddr().equals(device.getMacAddress())) {
						vm.setVmSwPort(String.valueOf(device.getSwitchPort()));
						break;
					}
				}
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
