package controller.overview.switches;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import sun.rmi.runtime.Log;
import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;
import controller.util.FormatLong;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;
import model.overview.Port;
import model.overview.Switch;

public class SwitchesJSON {

	static String IP = FloodlightProvider.getIP();
	static List<Switch> switches, oldSwitches;
	static JSONObject obj;
	static JSONArray json;
	static Logger log = Logger.getGlobal();

	// This parses JSON from the restAPI to get all the switches connected to
	// the controller
	@SuppressWarnings("unchecked")
	public static List<Switch> getSwitches() throws JSONException, IOException {

		List<String> switchDpids = new ArrayList<String>();
		Map<String, Future<Object>> futureStats = new HashMap<String, Future<Object>>();
		switches = new ArrayList<Switch>();
		oldSwitches = FloodlightProvider.getSwitches(false);

		switchDpids = getSwitchDpids();

		for (String dpid : switchDpids)
			futureStats.put(dpid, SwitchJSON.startSwitchRestCalls(dpid, false));

		for (String dpid : futureStats.keySet()) {
			Switch sw = null;
			boolean updateSwitch = false;

			// Check to see if this switch already exists, if it does just
			// update it
			if (!oldSwitches.isEmpty()) {
				for (Switch oldSwitch : oldSwitches) {
					if (oldSwitch.getDpid().equals(dpid)) {
						sw = oldSwitch;
						updateSwitch = true;
					}
				}
			}
			// If it doesn't exist we make a new Switch object, if it exist,
			// then update
			if (!updateSwitch)
				sw = new Switch(dpid);

			List<Port> ports = new ArrayList<Port>();
			Map<String, Future<Object>> stats;
			JSONObject descriptionObj = null, aggregateObj = null, portObj = null, featuresObj = null;
			try {
				stats = (Map<String, Future<Object>>) futureStats.get(dpid)
						.get(5L, TimeUnit.SECONDS);
				// Don't bother if we are updating this switch, since
				// description is static
				if (!updateSwitch)
					descriptionObj = (JSONObject) stats.get("description").get(
							5L, TimeUnit.SECONDS);
				aggregateObj = (JSONObject) stats.get("aggregate").get(5L,
						TimeUnit.SECONDS);
				portObj = (JSONObject) stats.get("port").get(5L,
						TimeUnit.SECONDS);
				featuresObj = (JSONObject) stats.get("features").get(5L,
						TimeUnit.SECONDS);
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

			// Description stats
			if (!updateSwitch && descriptionObj != null) {
				descriptionObj = descriptionObj.getJSONArray(dpid)
						.getJSONObject(0);
				sw.setManufacturerDescription(descriptionObj
						.getString("manufacturerDescription"));
				sw.setHardwareDescription(descriptionObj
						.getString("hardwareDescription"));
				sw.setSoftwareDescription(descriptionObj
						.getString("softwareDescription"));
				sw.setSerialNumber(descriptionObj.getString("serialNumber"));
				sw.setDatapathDescription(descriptionObj
						.getString("datapathDescription"));
			}

			// Aggregate stats, ignore
			if (aggregateObj != null) {
				aggregateObj = aggregateObj.getJSONArray(dpid).getJSONObject(0);
				sw.setPacketCount(String.valueOf(aggregateObj
						.getInt("packetCount")));
				sw.setByteCount(FormatLong.formatData(aggregateObj
						.getLong("byteCount")));
				sw.setFlowCount(FormatLong.formatData(aggregateObj
						.getLong("flowCount")));
			}

			// Flow Stats
			sw.setFlows(FlowJSON.getFlows(dpid));

			// Port and Features stats
			if (portObj == null || !portObj.has(dpid)) {
				System.out.println("no ports find.");
				return switches;
			}
			JSONArray json = portObj.getJSONArray(dpid);

			JSONArray jsontwo = new JSONArray();
			if (featuresObj != null && featuresObj.has(dpid)) {
				if (featuresObj.getJSONObject(dpid).has("ports"))
					jsontwo = featuresObj.getJSONObject(dpid).getJSONArray(
							"ports");
				else
					log.info("no ports find in feature");
			}
			if (jsontwo.length() != json.length()) {
				log.info("features number dos not corresponse to port number");
				return switches;
			}

			for (int i = 0; i < json.length(); i++) {
				obj = (JSONObject) json.get(i);
				if (!obj.has("portNumber")) {
					continue;
				}
				Port port = new Port(String.valueOf(obj.getInt("portNumber")));
				// byte counts
				if (obj.has("receivePackets"))
					port.setReceivePackets(obj.getLong("receivePackets"));
				if (obj.has("transmitPackets"))
					port.setTransmitPackets(obj.getLong("transmitPackets"));
				if (obj.has("receiveBytes"))
					port.setReceiveBytes(obj.getLong("receiveBytes"));
				if (obj.has("transmitBytes"))
					port.setTransmitBytes(obj.getLong("transmitBytes"));

				// droped
				if (obj.has("receiveDropped"))
					port.setReceiveDropped(String.valueOf(obj
							.getLong("receiveDropped")));
				if (obj.has("transmitDropped"))
					port.setTransmitDropped(String.valueOf(obj
							.getLong("transmitDropped")));
				// errors
				if (obj.has("receiveErrors"))
					port.setReceiveErrors(String.valueOf(obj
							.getLong("receiveErrors")));
				if (obj.has("transmitErrors"))
					port.setTransmitErrors(String.valueOf(obj
							.getLong("transmitErrors")));
				if (obj.has("receiveFrameErrors"))
					port.setReceieveFrameErrors(String.valueOf(obj
							.getInt("receiveFrameErrors")));
				if (obj.has("receiveOverrunErrors"))
					port.setReceieveOverrunErrors(String.valueOf(obj
							.getInt("receiveOverrunErrors")));
				if (obj.has("receiveCRCErrors"))
					port.setReceiveCRCErrors(String.valueOf(obj
							.getInt("receiveCRCErrors")));
				// conflicts
				if (obj.has("collisions"))
					port.setCollisions(String.valueOf(obj.getInt("collisions")));

				if (!jsontwo.isNull(i)) {
					obj = (JSONObject) jsontwo.get(i);
					if (!port.getPortNumber().equals(
							String.valueOf(obj.getInt("portNumber")))) {
						log.info("port number is not concurrent.");
						continue;
					}
					port.setName(obj.getString("name"));
					port.setHardwareAddress(obj.getString("hardwareAddress"));
					port.setConfig(String.valueOf(obj.getInt("config")));
					port.setState(String.valueOf(obj.getInt("state")));
					// features
					port.setCurrentFeatures(String.valueOf(obj
							.getInt("currentFeatures")));
					port.setAdvertisedFeatures(String.valueOf(obj
							.getInt("advertisedFeatures")));
					port.setSupportedFeatures(String.valueOf(obj
							.getInt("supportedFeatures")));
					port.setPeerFeatures(String.valueOf(obj
							.getInt("peerFeatures")));
				}
				ports.add(port);
			}

			sw.setPorts(ports);
			switches.add(sw);
		}
		return switches;
	}

	@SuppressWarnings("unchecked")
	public static void updateSwitch(Switch sw) throws JSONException {

		String dpid = sw.getDpid();
		List<Port> ports = new ArrayList<Port>();
		JSONObject obj, portObj = null, featuresObj = null;
		Map<String, Future<Object>> stats;
		// Start the rest calls, true is passed since we are updating and don't
		// care about the description
		Future<Object> futureStat = SwitchJSON.startSwitchRestCalls(dpid, true);

		try {
			stats = (Map<String, Future<Object>>) futureStat.get(5L,
					TimeUnit.SECONDS);
			portObj = (JSONObject) stats.get("port").get(5L, TimeUnit.SECONDS);
			featuresObj = (JSONObject) stats.get("features").get(5L,
					TimeUnit.SECONDS);
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

		// Flow Stats
		try {
			sw.setFlows(FlowJSON.getFlows(dpid));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Port and Features stats
		if (portObj == null || !portObj.has(dpid)) {
			System.out.println("no ports find.");
			return;
		}
		JSONArray json = portObj.getJSONArray(dpid);

		JSONArray jsontwo = new JSONArray();
		if (featuresObj != null && featuresObj.has(dpid)) {
			if (featuresObj.getJSONObject(dpid).has("ports"))
				jsontwo = featuresObj.getJSONObject(dpid).getJSONArray("ports");
			else
				log.info("no ports find in feature");
		}
		if (jsontwo.length() != json.length()) {
			log.info("features number dos not corresponse to port number");
			return;
		}

		for (int i = 0; i < json.length(); i++) {
			obj = (JSONObject) json.get(i);
			if (!obj.has("portNumber")) {
				return;
			}
			Port port = new Port(String.valueOf(obj.getInt("portNumber")));

			port.setReceivePackets(obj.getLong("receivePackets"));
			port.setTransmitPackets(obj.getLong("transmitPackets"));
			port.setReceiveBytes(obj.getLong("receiveBytes"));
			port.setTransmitBytes(obj.getLong("transmitBytes"));

			port.setReceiveDropped(String.valueOf(obj.getLong("receiveDropped")));
			port.setTransmitDropped(String.valueOf(obj
					.getLong("transmitDropped")));

			port.setReceiveErrors(String.valueOf(obj.getLong("receiveErrors")));
			port.setTransmitErrors(String.valueOf(obj.getLong("transmitErrors")));
			port.setReceieveFrameErrors(String.valueOf(obj
					.getInt("receiveFrameErrors")));
			port.setReceieveOverrunErrors(String.valueOf(obj
					.getInt("receiveOverrunErrors")));
			port.setReceiveCRCErrors(String.valueOf(obj
					.getInt("receiveCRCErrors")));

			port.setCollisions(String.valueOf(obj.getInt("collisions")));

			if (!jsontwo.isNull(i)) {
				obj = (JSONObject) jsontwo.get(i);
				if (!port.getPortNumber().equals(
						String.valueOf(obj.getInt("portNumber")))) {
					log.info("port number is not concurrent.");
					continue;
				}
				port.setName(obj.getString("name"));
				port.setHardwareAddress(obj.getString("hardwareAddress"));
				port.setConfig(String.valueOf(obj.getInt("config")));
				port.setState(String.valueOf(obj.getInt("state")));

				port.setAdvertisedFeatures(String.valueOf(obj
						.getInt("advertisedFeatures")));
				port.setCurrentFeatures(String.valueOf(obj
						.getInt("currentFeatures")));
				port.setPeerFeatures(String.valueOf(obj.getInt("peerFeatures")));
				port.setSupportedFeatures(String.valueOf(obj
						.getInt("supportedFeatures")));
			}
			ports.add(port);
		}
		sw.setPorts(ports);
	}

	public static List<String> getSwitchDpids() throws JSONException {
		List<String> switchDpids = new ArrayList<String>();

		Future<Object> futureSwDpids = Deserializer
				.readJsonArrayFromURL("http://" + IP
						+ ":8080/wm/core/controller/switches/json");
		try {
			json = (JSONArray) futureSwDpids.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ExecutionException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (TimeoutException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (int i = 0; i < json.length(); i++) {
			obj = json.getJSONObject(i);
			if (obj.has("dpid")) {
				String dpid = obj.getString("dpid");
				switchDpids.add(dpid);
			}
		}
		return switchDpids;
	}
}
