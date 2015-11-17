package controller.overview.vms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xen.ovs.util.OvsCmdExecuter;
import model.overview.Device;
import model.overview.Port;
import model.overview.Switch;
import model.overview.VmData;
import controller.floodlightprovider.FloodlightProvider;
import controller.overview.json.DevicesJSON;
import controller.util.JSONException;

public class VmDataGetter {
	private static List<VmData> vms = new ArrayList<VmData>();

	public VmDataGetter() {
		updating();
	}

	public static List<VmData> getVmDatas(boolean update) {
		if (update)
			getDataFromController();
		return vms;
	}

	private static void updating() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						getDataFromController();
						Thread.sleep(3000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
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
			Iterator<Device> itd = devices.iterator();
			while (itd.hasNext()) {
				VmData vm = null;
				device = itd.next();
				if (device.getIpv4() != null) {
					if ((vm = hasVm(device.getIpv4())) != null) {
						if (vm.getVmUuid() == null) {
							getDataFromXenServer(vm);
						}
					} else {
						vm = new VmData();
						vm.setVmIpAddr(device.getIpv4());
						if (device.getSwitchPort() > 0
								&& device.getSwitchPort() != 1) {
							vm.setVmSwitchPort(String.valueOf(device
									.getSwitchPort()));
						} else {
							continue;
						}
						vms.add(vm);
					}
					if (vm.getVmMacAddr() == null)
						if (device.getMacAddress() != null) {
							vm.setVmMacAddr(device.getMacAddress());
						}
					if (vm.getVmSwitch() == null)
						vm.setVmSwitch(device.getAttachedSwitch());

					if (vm.getLastSeen() == null)
						if (device.getLastSeen() != null) {
							vm.setLastSeen(device.getLastSeen());
						}
					if (vm.getVmVifNumber() == null) {
						sw = FloodlightProvider.getSwitch(
								device.getAttachedSwitch(), false);
						if (sw != null) {
							ports = sw.getPorts();
							Iterator<Port> itp = ports.iterator();
							while (itp.hasNext()) {
								port = itp.next();
								if (port.getPortNumber().equals(
										vm.getVmSwitchPort())) {
									vm.setVmVifNumber(port.getName());
									break;
								}
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getDataFromXenServer(VmData vm) {
		// TODO Auto-generated method stub

		List<String> result = OvsCmdExecuter.getUuids(vm.getVmVifNumber());
		if (result.size() > 0) {
			vm.setVmUuid(result.get(0));
			vm.setVifUuid(result.get(1));
		} else {
			System.out.println("No data got.");
		}
	}

	private static VmData hasVm(String ipv4) {
		Iterator<VmData> it = vms.iterator();
		while (it.hasNext()) {
			VmData vm = it.next();
			if (vm.getVmIpAddr() != null && vm.getVmIpAddr().equals(ipv4)) {
				return vm;
			}
		}
		return null;
	}
}
