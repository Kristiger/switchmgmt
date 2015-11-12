package controller.tools.flowmanager.json;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import model.overview.Action;
import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;
import controller.util.HexString;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class ActionManagerJSON {

	private static String IP = FloodlightProvider.getIP();
	private static String PORT = FloodlightProvider.getPort();
	private static JSONObject obj, jsonobj;
	private static JSONArray json;
	private static Future<Object> future;

	// This parses JSON from the restAPI to get all the actions and values for
	// that action by it's flow name
	public static List<Action> getActions(String dpid, String flowName)
			throws JSONException, IOException {

		List<Action> actions = new ArrayList<Action>();
		// Get the array of actions
		future = Deserializer.readJsonObjectFromURL("http://" + IP + ":" + PORT
				+ "/wm/staticflowpusher/list/" + dpid + "/json");
		try {
			obj = (JSONObject) future.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		json = obj.getJSONArray(dpid);

		for (int i = 0; i < json.length(); i++) {

			if (!json.getJSONObject(i).has(flowName))
				continue;

			if (!json.getJSONObject(i).getJSONObject(flowName)
					.isNull("actions")) {
				jsonobj = json.getJSONObject(i).getJSONObject(flowName)
						.getJSONObject("actions");

				//jsonobj = (JSONObject) jsonobj.get("actions");
				if (jsonobj.has("none"))
					actions.add(new Action("none", jsonobj.getString("none")));
				if (jsonobj.has("output"))
					actions.add(new Action("output", jsonobj
							.getString("output")));
				if (jsonobj.has("enqueue"))
					actions.add(new Action("enqueue", jsonobj
							.getString("enqueue")));
				if (jsonobj.has("strip_vlan"))
					actions.add(new Action("strip_vlan", jsonobj
							.getString("strip_vlan")));
				if (jsonobj.has("set_vlan_vid"))
					actions.add(new Action("set_vlan_vid", jsonobj
							.getString("set_vlan_vid")));
				if (jsonobj.has("set_vlan_pcp"))
					actions.add(new Action("set_vlan_pcp", jsonobj
							.getString("set_vlan_pcp")));
				if (jsonobj.has("set_eth_src"))
					actions.add(new Action("set_eth_src", jsonobj
							.getString("set_eth_src")));
				if (jsonobj.has("set_eth_dst"))
					actions.add(new Action("set_eth_dst", jsonobj
							.getString("set_eth_dst")));
				if (jsonobj.has("set_ip_tos"))
					actions.add(new Action("set_ip_tos", jsonobj
							.getString("set_ip_tos")));
				if (jsonobj.has("set_ipv4_src"))
					actions.add(new Action("set_ipv4_src", jsonobj
							.getString("set_ipv4_src")));
				if (jsonobj.has("set_ipv4_dst"))
					actions.add(new Action("set_ipv4_dst", jsonobj
							.getString("set_ipv4_dst")));
				if (jsonobj.has("set_tp_src"))
					actions.add(new Action("set_tp_src", jsonobj
							.getString("set_tp_src")));
				if (jsonobj.has("set_tp_dst"))
					actions.add(new Action("set_tp_dst", jsonobj
							.getString("set_tp_dst")));

				/*String act = jsonobj.getString("actions");
				String action[] = act.split(",");
				String s[];

				for (int j = 0; j < action.length; j++) {
					s = action[j].split("=");
					if (s[0].equals("none"))
						actions.add(new Action("none", s[1]));

					else if (s[0].equals("output"))
						actions.add(new Action("output", s[1]));

					else if (s.equals("enqueue")) {
						// String[] param = s.split("q");
						// String queue = param[0] + ":" + param[1];
						// actions.add(new Action("enqueue", queue));
						actions.add(new Action("enqueue", s[1]));
					} else if (s.equals("strip_vlan"))
						actions.add(new Action("strip_vlan", s[1]));

					else if (s.equals("set_vlan_vid"))
						actions.add(new Action("set_vlan_vid", s[1]));

					else if (s.equals("set_vlan_pcp"))
						actions.add(new Action("set_vlan_pcp", s[1]));

					else if (s.equals("set_eth_src"))
						actions.add(new Action("set-eth_src", s[1]));

					else if (s.equals("set_eth_dst"))
						actions.add(new Action("set_eth_dst", s[1]));

					else if (s.equals("set_ip_tos"))
						actions.add(new Action("set_ip_tos", s[1]));

					else if (s.equals("set_ipv4_src"))
						actions.add(new Action("set_ipv4_src", s[1]));

					else if (s.equals("set_ipv4_dst"))
						actions.add(new Action("set_ipv4_dst", s[1]));

					else if (s.equals("set_tp_src"))
						actions.add(new Action("set_tp_src", s[1]));

					else if (s.equals("set_tp_dst"))
						actions.add(new Action("set_tp_dst", s[1]));

					else
						System.out.println("unrecognized value");
				}*/
			}
		}
		return actions;
	}

}
