package controller.overview.table;

import java.util.ArrayList;
import java.util.List;

import controller.util.FormatLong;
import model.overview.Port;

public class PortToTable {

	// This returns a table representation of a list of ports on a switch
	public static String[][] getPortTableFormat(List<Port> ports) {

		if (!ports.isEmpty()) {
			int count = 0;
			String[][] arrData = new String[ports.size()][8];

			for (Port port : ports) {
				List<String> stringList = new ArrayList<String>();
				stringList.add(port.getPortNumber());
				// stringList.add(port.getStatus());
				stringList.add(FormatLong.formatData(port.getTransmitBytes()));
				stringList.add(FormatLong.formatData(port.getReceiveBytes()));
				stringList.add(FormatLong.formatData(port.getTransmitPackets()));
				stringList.add(FormatLong.formatData(port.getReceivePackets()));
				stringList.add(String.valueOf(Integer.valueOf(port
						.getTransmitDropped())
						+ Integer.valueOf(port.getReceiveDropped())));
				
				stringList.add(port.getErrors());
				stringList.add(port.getName());
				arrData[count] = stringList.toArray(new String[stringList
						.size()]);
				count++;
			}
			return arrData;
		} else
			return new String[0][0];
	}
}