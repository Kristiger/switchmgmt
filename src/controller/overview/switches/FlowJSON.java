package controller.overview.switches;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import model.overview.Flow;
import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;
import controller.util.FormatLong;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class FlowJSON {

	static String IP = FloodlightProvider.getIP();
	static JSONObject obj;
	static JSONArray json;
	static Future<Object> future;

	// This parses JSON from the restAPI to get all the flows from a specified
	// switch, meant for the controller overview
	public static List<Flow> getFlows(String dpid) throws IOException,
			JSONException {

		List<Flow> flows = new ArrayList<Flow>();
		// If JSONObject is not supplied, get it.
		try {
			obj = (JSONObject) Deserializer.readJsonObjectFromURL(
					"http://" + IP + ":8080/wm/core/switch/" + dpid
							+ "/flow/json").get(5, TimeUnit.SECONDS);
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
		if (!obj.has(dpid))
			return flows;

		json = obj.getJSONArray(dpid);
		if (json.length() != 0) {
			for (int i = 0; i < json.length(); i++) {
				obj = (JSONObject) json.get(i);
				Flow flow = new Flow(dpid);

				if (obj.has("actions")) {
					JSONArray action = obj.getJSONArray("actions");
					flow.setActions(ActionJSON.getActions(action));
				}

				// OF13 has 256 tables and, through this can exclude those has
				// no flows.
				if (obj.has("match")
						&& obj.getJSONObject("match").length() != 0)
					flow.setMatch(MatchJSON.getMatch(obj.getJSONObject("match")));

				if (obj.has("priority"))
					flow.setPriority(String.valueOf(obj.getInt("priority")));
				if (obj.has("idleTimeout") && obj.getInt("idleTimeout") != 0)
					flow.setIdleTimeOut(String.valueOf(obj
							.getInt("idleTimeout")));
				if (obj.has("hardTimeout") && obj.getInt("hardTimeout") != 0)
					flow.setHardTimeOut(String.valueOf(obj
							.getInt("hardTimeout")));
				if (obj.has("durationSeconds"))
					flow.setDurationSeconds(String.valueOf(obj
							.getInt("durationSeconds")));
				if (obj.has("packetCount"))
					flow.setPacketCount(obj.getLong("packetCount"));
				if (obj.has("byteCount"))
					flow.setByteCount(obj.getLong("byteCount"));
				if (obj.has("durationNanoseconds"))
					flow.setDurationNanoseconds(String.valueOf(obj
							.getLong("durationNanoseconds")));
				if (obj.has("cookie"))
					flow.setCookie(String.valueOf(obj.getLong("cookie")));
				if (obj.has("tableId"))
					flow.setTable(String.valueOf(obj.getInt("tableId")));

				flows.add(flow);
			}
		}
		return flows;
	}
}
