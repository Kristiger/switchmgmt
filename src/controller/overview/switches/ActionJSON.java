package controller.overview.switches;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import model.overview.Action;
import controller.floodlightprovider.FloodlightProvider;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class ActionJSON {

	static String IP = FloodlightProvider.getIP();
	static JSONObject obj;
	static JSONArray json;

	// This parses JSON from the restAPI to get all the actions
	// from a JSON array, meant for the switch overview
	public static List<Action> getActions(JSONArray json) throws JSONException,
			IOException {

		List<Action> actions = new ArrayList<Action>();
		
		for (int i = 0; i < json.length(); i++) {
			obj = json.getJSONObject(i);
			String objActionType = obj.getString("type");
			try {
				if (objActionType.equals("OUTPUT")) {
					actions.add(new Action("output", String.valueOf(obj.getInt("port")), "Port"));
				} else if (objActionType.equals("OPAQUE_ENQUEUE")) {
					actions.add(new Action("enqueue", String.valueOf(obj
							.getInt("port") + ":" + obj.getInt("queueId")),
							"Port:Queue ID"));
				} else if (objActionType.equals("STRIP_VLAN")) {
					actions.add(new Action("strip-vlan", ""));
				} else if (objActionType.equals("SET_VLAN_ID")) {
					actions.add(new Action("set-vlan-id", String.valueOf(obj
							.getInt("virtualLanIdentifier")), "VLAN ID"));
				} else if (objActionType.equals("SET_VLAN_PCP")) {
					actions.add(new Action("set-vlan-priority",
							String.valueOf(obj
									.getInt("virtualLanPriorityCodePoint")),
							"VLAN PCP"));
				} else if (objActionType.equals("SET_DL_SRC")) {
					String dl = obj.getString("dataLayerAddress");
					actions.add(new Action("set-src-mac", dl,
							"Data Layer Address"));
				} else if (objActionType.equals("SET_DL_DST")) {
					String dl = obj.getString("dataLayerAddress");
					actions.add(new Action("set-dst-mac", dl,
							"Data Layer Address"));
				} else if (objActionType.equals("SET_NW_TOS")) {
					actions.add(new Action("set-tos-bits", String.valueOf(obj
							.getInt("networkTypeOfService")),
							"Network Type Of Service"));
				} else if (objActionType.equals("SET_NW_SRC")) {
					long ip = obj.getLong("networkAddress");
					byte[] bytes = BigInteger.valueOf(ip).toByteArray();
					InetAddress address = null;

					try {
						address = InetAddress.getByAddress(bytes);
					} catch (UnknownHostException e) {
						System.out.println("Getting address failed.");
						e.printStackTrace();
					}

					actions.add(new Action("set-src-ip", address.toString()
							.replaceAll("/", ""), "Network Address"));
				} else if (objActionType.equals("SET_NW_DST")) {
					long ip = obj.getLong("networkAddress");
					byte[] bytes = BigInteger.valueOf(ip).toByteArray();
					InetAddress address = null;

					try {
						address = InetAddress.getByAddress(bytes);
					} catch (UnknownHostException e) {
						System.out.println("Getting address failed.");
						e.printStackTrace();
					}

					actions.add(new Action("set-dst-ip", address.toString()
							.replaceAll("/", ""), "Network Address"));
				} else if (objActionType.equals("SET_TP_SRC")) {
					actions.add(new Action("set-src-port", String.valueOf(obj
							.getInt("transportPort")), "Transport Port"));
				} else if (objActionType.equals("SET_TP_DST")) {
					actions.add(new Action("set-dst-port", String.valueOf(obj
							.getInt("transportPort")), "Transport Port"));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return actions;
	}

	public static List<Action> getActions(JSONObject obj) throws JSONException,
			IOException {
		List<Action> actions = new ArrayList<Action>();

		if (obj.has("output")) {
			actions.add(new Action("output", obj.getString("output")));
		}
		if (obj.has("strip_vlan")) {
			actions.add(new Action("strip_vlan", obj.getString("strip_vlan")));
		}
		if (obj.has("set_vlan_vid")) {
			actions.add(new Action("set_vlan_vid", obj
					.getString("set_vlan_vid")));
		}
		if (obj.has("set_vlan_pcp")) {
			actions.add(new Action("set_vlan_pcp", obj
					.getString("set_vlan_pcp")));
		}
		if (obj.has("set_eth_src")) {
			actions.add(new Action("set_eth_src", obj.getString("set_eth_src")));
		}
		if (obj.has("set_eth_dst_")) {
			actions.add(new Action("set_eth_dst", obj.getString("set_eth_dst")));
		}
		if (obj.has("set_ip_tos")) {
			actions.add(new Action("set_ip_tos", obj.getString("set_ip_tos")));
		}
		if (obj.has("set_ipv4_src")) {
			actions.add(new Action("set_ipv4_src", obj
					.getString("set_ipv4_src")));
		}
		if (obj.has("set_ipv4_dst")) {
			actions.add(new Action("set_ipv4_dst", obj
					.getString("set_ipv4_dst")));
		}
		if (obj.has("set_tp_src")) {
			actions.add(new Action("set_tp_src", obj.getString("set_tp_src")));
		}
		if (obj.has("set_tp_dst")) {
			actions.add(new Action("set_tp_dst", obj.getString("set_tp_dst")));
		}
		if (obj.has("enqueue")) {
			actions.add(new Action("enqueue", obj.getString("enqueue")));
		}

		return actions;
	}

	public static List<Action> getOF13Actions(JSONObject jsonObject)
			throws JSONException, IOException {
		// TODO Auto-generated method stub
		jsonObject = jsonObject.getJSONObject("instruction_apply_actions");
		return getActions(jsonObject);
	}

}
