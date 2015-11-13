package controller.overview.switches;

import java.io.IOException;

import model.overview.Match;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class MatchJSON {

	String dataLayerDestination, dataLayerSource, dataLayerType, dataLayerVLAN,
			dataLayerPCP, inputPort, networkDestination,
			networkDestinationMaskLength, networkProtocol, networkSource,
			networkSourceMaskLength, networkTypeOfService,
			transportDestination, transportSource, wildcards;

	static JSONObject obj;
	static JSONArray json;

	// This parses JSON from the restAPI to get all the match of a flow for the
	// controller overview
	public static Match getMatch(JSONObject obj) throws JSONException,
			IOException {

		Match match = new Match();

		// Here we check the values, if they are default we set them empty
		// strings.
		// This way they don't confuse the user into thinking they set something
		// they didn't

		if (obj.has("dataLayerSource"))
			if (!obj.get("dataLayerSource").equals("00:00:00:00:00:00"))
				match.setDataLayerSource(obj.getString("dataLayerSource"));

		if (obj.has("dataLayerDestination"))
			if (!obj.get("dataLayerDestination").equals("00:00:00:00:00:00"))
				match.setDataLayerDestination(obj
						.getString("dataLayerDestination"));

		if (obj.has("dataLayerType"))
			if (!obj.get("dataLayerType").equals("0x0000"))
				match.setDataLayerType(obj.getString("dataLayerType"));
		if (obj.has("dataLayerVirtualLan"))
			if (!obj.get("dataLayerVirtualLan").equals("-1"))
				match.setDataLayerVLAN(String.valueOf(obj
						.getInt("dataLayerVirtualLan")));
		if (obj.has("dataLayerVirtualLanPriorityCodePoint"))
			if (!(obj.getInt("dataLayerVirtualLanPriorityCodePoint") != 0))
				match.setDataLayerPCP(String.valueOf(obj
						.getInt("dataLayerVirtualLanPriorityCodePoint")));
		if (obj.has("inputPort"))
			if (!obj.get("inputPort").equals("-1"))
				match.setInputPort(String.valueOf(obj.getInt("inputPort")));
		if (obj.has("networkDestination"))
			if (!obj.getString("networkDestination").equals("0.0.0.0"))
				match.setNetworkDestination(obj.getString("networkDestination"));
		if (obj.has("networkDestinationMaskLen"))
			if (obj.getInt("networkDestinationMaskLen") != 0)
				match.setNetworkDestination(String.valueOf(obj
						.getInt("networkDestinationMaskLen")));
		if (obj.has("networkProtocol"))
			if (obj.getInt("networkProtocol") != 0)
				match.setNetworkProtocol(String.valueOf(obj
						.getInt("networkProtocol")));
		if (obj.has("networkSource"))
			if (!obj.getString("networkSource").equals("0.0.0.0"))
				match.setNetworkSource(obj.getString("networkSource"));
		if (obj.has("networkSourceMaskLen"))
			if (obj.getInt("networkSourceMaskLen") != 0)
				match.setTransportSource(String.valueOf(obj
						.getInt("networkSourceMaskLen")));
		if (obj.has("networkTypeOfService"))
			if (obj.getInt("networkTypeOfService") != 0)
				match.setNetworkTypeOfService(String.valueOf(obj
						.getInt("networkTypeOfService")));
		if (obj.has("transportDestination"))
			if (obj.getInt("transportDestination") != 0)
				match.setTransportDestination(String.valueOf(obj
						.getInt("transportDestination")));
		if (obj.has("transportSource"))
			if (obj.getInt("transportSource") != 0)
				match.setTransportSource(String.valueOf(obj
						.getInt("transportSource")));
		if (obj.has("wildcards"))
			match.setWildcards(String.valueOf(obj.getLong("wildcards")));

		if (obj.has("tcpSource"))
			if (obj.getInt("tcpSource") != 0)
				match.setDataLayerSource(String.valueOf(obj.getInt("tcpSource")));
		if (obj.has("tcpDestination"))
			if (obj.getInt("tcpDestination") != 0)
				match.setDataLayerSource(String.valueOf(obj
						.getInt("tcpDestination")));
		if (obj.has("udpSource"))
			if (obj.getInt("udpSource") != 0)
				match.setDataLayerSource(String.valueOf(obj.getInt("udpSource")));
		if (obj.has("udpDestination"))
			if (obj.getInt("udpDestination") != 0)
				match.setDataLayerSource(String.valueOf(obj
						.getInt("udpDestination")));

		return match;
	}

	// This parses JSON from the restAPI to get all the match of a flow for the
	// controller overview
	public static Match getMatch(JSONObject obj, boolean withoutqos)
			throws JSONException, IOException {

		Match match = new Match();

		// Here we check the values, if they are default we set them empty
		// strings.
		// This way they don't confuse the user into thinking they set something
		// they didn't

		if (obj.has("eth_dst"))
			if (!obj.getString("eth_dst").equals("00:00:00:00:00:00"))
				match.setDataLayerDestination(obj.getString("eth_dst"));
		if (obj.has("eth_src"))
			if (!obj.getString("eth_src").equals("00:00:00:00:00:00"))
				match.setDataLayerSource(obj.getString("eth_src"));
		if (obj.has("eth_type"))
			if (!obj.getString("eth_type").equals("0x0000")) {
				String eth_type = obj.getString("eth_type");
				eth_type = eth_type.replaceAll("0x", "");
				eth_type = "0x" + eth_type;
				match.setDataLayerType(eth_type);
			}
		if (obj.has("eth_vlan_vid"))
			if (obj.getInt("eth_vlan_vid") > 0)
				match.setDataLayerVLAN(String.valueOf(obj
						.getInt("eth_vlan_vid")));
		if (obj.has("eth_vlan_pcp"))
			if (obj.getInt("eth_vlan_pcp") != 0)
				match.setDataLayerPCP(String.valueOf(obj.getInt("eth_vlan_pcp")));
		if (obj.has("in_port"))// int or "local"
			if (!obj.getString("in_port").equals("0x00")) {
				match.setInputPort(obj.getString("in_port"));
			}
		if (obj.has("ipv4_dst"))
			if (!obj.getString("ipv4_dst").equals("0.0.0.0"))
				match.setNetworkDestination(obj.getString("ipv4_dst"));
		if (obj.has("ip_proto"))
			if (!obj.getString("ip_proto").equals(""))
				match.setNetworkProtocol(obj.getString("ip_proto"));
		if (obj.has("ipv4_src"))
			if (!obj.getString("ipv4_src").equals("0.0.0.0"))
				match.setNetworkSource(obj.getString("ipv4_src"));
		if (obj.has("ip_tos"))
			if (obj.getInt("ip_tos") != 0)
				match.setNetworkTypeOfService(String.valueOf(obj
						.getInt("ip_tos")));
		if (obj.has("tp_dst"))
			if (obj.getInt("tp_dst") != 0)
				match.setTransportDestination(String.valueOf(obj
						.getInt("tp_dst")));
		if (obj.has("tp_src"))
			if (obj.getInt("tp_src") != 0)
				match.setTransportSource(String.valueOf(obj.getInt("tp_src")));
		if (obj.has("tcp_src"))
			if (obj.getInt("tcp_src") != 0)
				match.setDataLayerSource(String.valueOf(obj.getInt("tcp_src")));
		if (obj.has("tcp_dst"))
			if (obj.getInt("tcp_dst") != 0)
				match.setDataLayerSource(String.valueOf(obj.getInt("tcp_dst")));
		if (obj.has("udp_src"))
			if (obj.getInt("udp_src") != 0)
				match.setDataLayerSource(String.valueOf(obj.getInt("udp_src")));
		if (obj.has("udp_dst"))
			if (obj.getInt("udp_dst") != 0)
				match.setDataLayerSource(String.valueOf(obj.getInt("udp_dst")));

		return match;
	}

}
