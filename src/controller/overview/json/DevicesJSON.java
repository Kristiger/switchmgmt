package controller.overview.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;
import model.overview.Device;

public class DevicesJSON {

	private static String IP = FloodlightProvider.getIP();
	private static String PORT = FloodlightProvider.getPort();
	private static JSONObject obj;
	private static JSONArray jsonip;

	/**
	 * Get the string IDs of all the switches and create switch summary objects
	 * for each one
	 * 
	 * @return devices
	 * @throws JSONException
	 */
	public static List<Device> getDeviceSummaries() throws JSONException {

		List<Device> devicees = new ArrayList<Device>();

		try {
			Future<Object> devices = Deserializer
					.readJsonArrayFromURL("http://" + IP + ":" + PORT
							+ "/wm/device/");
			JSONArray json = (JSONArray) devices.get(5, TimeUnit.SECONDS);

			for (int i = 0; i < json.length(); i++) {
				obj = json.getJSONObject(i);
				Device temp = new Device(obj.getJSONArray("mac").getString(0));
				
				if (obj.has("ipv4")) {
					if (!obj.getJSONArray("ipv4").isNull(0)) {
						jsonip = obj.getJSONArray("ipv4");
						String ip = jsonip.getString(0);

						for (int j = 1; j < jsonip.length(); j++) {
							ip = ip + "," + jsonip.getString(j);
						}
						temp.setIpv4(ip);
					}
				}
				
				if (obj.has("attachmentPoint")) {
					if (!obj.getJSONArray("attachmentPoint").isNull(0)) {
						temp.setAttachedSwitch(obj
								.getJSONArray("attachmentPoint")
								.getJSONObject(0).getString("switchDPID"));
						temp.setSwitchPort(obj.getJSONArray("attachmentPoint")
								.getJSONObject(0).getInt("port"));
					}
				}
				
				if (obj.has("lastSeen")) {
					Date d = new Date(obj.getLong("lastSeen"));
					temp.setLastSeen(d);
				}
				
				if(obj.has("vlan") && !obj.getJSONArray("vlan").isNull(0)){
					temp.setVlan(obj.getJSONArray("vlan").getInt(0));
				}
				
				devicees.add(temp);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return devicees;
	}
}
