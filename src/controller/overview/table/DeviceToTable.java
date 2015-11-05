package controller.overview.table;

import java.util.ArrayList;
import java.util.List;

import model.overview.Device;
import controller.overview.json.DevicesJSON;
import controller.util.JSONException;

public class DeviceToTable {

	public static String[][] deviceSummariesToTable() {

		List<Device> devices = null;
		try {
			devices = DevicesJSON.getDeviceSummaries();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String[][] tableArr = new String[devices.size()][6];
		int count = 0;

		for (Device sum : devices) {
			List<String> stringList = new ArrayList<String>();
			stringList.add(String.valueOf(count + 1));
			stringList.add(sum.getMacAddress());
			if (sum.getIpv4() != null)
				stringList.add(sum.getIpv4());
			else
				stringList.add("None");
			
			if (sum.getAttachedSwitch() != null)
				stringList.add(sum.getAttachedSwitch());
			else
				stringList.add("None");
			
			if (sum.getSwitchPort() != 0)
				stringList.add(String.valueOf(sum.getSwitchPort()));
			else
				stringList.add("None");
			
			stringList.add(String.valueOf(sum.getLastSeen()));

			tableArr[count] = stringList.toArray(new String[stringList.size()]);
			count++;
		}
		return tableArr;
	}
}
