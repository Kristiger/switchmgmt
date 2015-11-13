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
		//sometimes there is no flow syntax and may cause an Exception
		if (obj.has("flows"))
			json = obj.getJSONArray("flows");
		else {
			return flows;
		}

		if (json.length() != 0) {
			for (int i = 0; i < json.length(); i++) {
				obj = (JSONObject) json.get(i);
				Flow flow = new Flow(dpid);

				if (obj.has("actions"))
					flow.setActions(ActionJSON.getActions(obj.getJSONObject("actions")));

				//OF13 has 256 tables and, through this can exclude those has no flows.
				if (obj.has("match") && obj.getJSONObject("match").length() != 0)
					flow.setMatch(MatchJSON.getMatch(obj.getJSONObject("match")));
				
				if (obj.has("priority"))
					flow.setPriority(String.valueOf(obj.getInt("priority")));
				if (obj.has("idleTimeoutSec") && obj.getInt("idleTimeoutSec") != 0)
					flow.setIdleTimeOut(String.valueOf(obj.getInt("idleTimeoutSec")));
				if (obj.has("hardTimeoutSec") && obj.getInt("hardTimeoutSec") != 0)
					flow.setHardTimeOut(String.valueOf(obj.getInt("hardTimeoutSec")));
				if (obj.has("durationSeconds"))
					flow.setDurationSeconds(String.valueOf(obj.getInt("durationSeconds")));
				
				if (obj.has("packetCount"))
					flow.setPacketCount(String.valueOf(obj.getInt("packetCount")));
				if(obj.has("byteCount"))
					flow.setByteCount(String.valueOf(obj.getLong("byteCount")));
				
				if(obj.has("tableId"))
					flow.setTable(obj.getString("tableId"));
				if(obj.has("cookie"))
					flow.setCookie(obj.getString("cookie"));

				flows.add(flow);
			}
		}
		return flows;
	}
}
