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

	// This parses JSON from the restAPI to get all the match of a flow for the controller overview
	public static Match getMatch(JSONObject obj)
			throws JSONException, IOException {
		
		Match match = new Match();
		
		// Here we check the values, if they are default we set them empty strings.
		// This way they don't confuse the user into thinking they set something
		// they didn't
		/*if (!obj.getString("dataLayerDestination").equals("00:00:00:00:00:00"))
			match.setDataLayerDestination(obj.getString("dataLayerDestination"));
		if (!obj.getString("dataLayerSource").equals("00:00:00:00:00:00"))
			match.setDataLayerSource(obj.getString("dataLayerSource"));
		if (!obj.getString("dataLayerType").equals("0x0000"))
			match.setDataLayerType(obj.getString("dataLayerType"));
		if (obj.getInt("dataLayerVirtualLan") > 0)
			match.setDataLayerVLAN(String.valueOf(obj
					.getInt("dataLayerVirtualLan")));
		if (obj.getInt("dataLayerVirtualLanPriorityCodePoint") != 0)
			match.setDataLayerPCP(String.valueOf(obj
					.getInt("dataLayerVirtualLanPriorityCodePoint")));
		if (obj.getInt("inputPort") != 0)
			match.setInputPort(String.valueOf(obj.getInt("inputPort")));
		if (!obj.getString("networkDestination").equals("0.0.0.0"))
			match.setNetworkDestination(obj.getString("networkDestination"));
		// match.setNetworkDestinationMaskLength(String.valueOf(obj.getInt("networkDestinationMaskLen")));
		if (obj.getInt("networkProtocol") != 0)
			match.setNetworkProtocol(String.valueOf(obj
					.getInt("networkProtocol")));
		if (!obj.getString("networkSource").equals("0.0.0.0"))
			match.setNetworkSource(obj.getString("networkSource"));
		// match.setNetworkSourceMaskLength(String.valueOf(obj.getInt("networkSourceMaskLen")));
		if (obj.getInt("networkTypeOfService") != 0)
			match.setNetworkTypeOfService(String.valueOf(obj
					.getInt("networkTypeOfService")));
		if (obj.getInt("transportDestination") != 0)
			match.setTransportDestination(String.valueOf(obj
					.getInt("transportDestination")));
		if (obj.getInt("transportSource") != 0)
			match.setTransportSource(String.valueOf(obj
					.getInt("transportSource")));
		if(obj.getLong("wildcards") != 4194302)
			match.setWildcards(String.valueOf(obj.getLong("wildcards")));*/
		
		if(obj.has("eth_dst"))
			if (!obj.getString("eth_dst").equals("00:00:00:00:00:00"))
				match.setDataLayerDestination(obj.getString("eth_dst"));
		if(obj.has("eth_src"))
			if (!obj.getString("eth_src").equals("00:00:00:00:00:00"))
				match.setDataLayerSource(obj.getString("eth_src"));
		if(obj.has("eth_type"))
			if (!obj.getString("eth_type").equals("0x0000")){		
				String eth_type = obj.getString("eth_type");
				eth_type = eth_type.replaceAll("0x", "");
				eth_type = "0x" + eth_type;
				//match.setDataLayerType(obj.getString("eth_type"));
				match.setDataLayerType(eth_type);
			}
		if(obj.has("eth_vlan_vid"))
			if (obj.getInt("eth_vlan_vid") > 0)
				match.setDataLayerVLAN(String.valueOf(obj
						.getInt("eth_vlan_vid")));
		if(obj.has("eth_vlan_pcp"))
			if (obj.getInt("eth_vlan_pcp") != 0)
				match.setDataLayerPCP(String.valueOf(obj
						.getInt("eth_vlan_pcp")));
		if(obj.has("in_port"))//int or "local"
			/*if (obj.getInt("in_port") != 0)
				match.setInputPort(String.valueOf(obj.getInt("in_port")));*/
			if(!obj.getString("in_port").equals("0x00")){
				match.setInputPort(obj.getString("in_port"));
			}
		if(obj.has("ipv4_dst"))
			if (!obj.getString("ipv4_dst").equals("0.0.0.0"))
				match.setNetworkDestination(obj.getString("ipv4_dst"));
		// match.setNetworkDestinationMaskLength(String.valueOf(obj.getInt("networkDestinationMaskLen")));
		if(obj.has("ip_proto"))
			/*if (obj.getInt("ip_proto") != 0)
			match.setNetworkProtocol(String.valueOf(obj
					.getInt("ip_proto")));*/
			if (!obj.getString("ip_proto").equals(""))
				match.setNetworkProtocol(obj.getString("ip_proto"));
				
		if(obj.has("ipv4_src"))
			if (!obj.getString("ipv4_src").equals("0.0.0.0"))
				match.setNetworkSource(obj.getString("ipv4_src"));
		// match.setNetworkSourceMaskLength(String.valueOf(obj.getInt("networkSourceMaskLen")));
		if(obj.has("ip_tos"))
			if (obj.getInt("ip_tos") != 0)
				match.setNetworkTypeOfService(String.valueOf(obj
						.getInt("ip_tos")));
		if(obj.has("tp_dst"))
			if (obj.getInt("tp_dst") != 0)
				match.setTransportDestination(String.valueOf(obj
						.getInt("tp_dst")));
		if(obj.has("tp_src"))
			if (obj.getInt("tp_src") != 0)
				match.setTransportSource(String.valueOf(obj
						.getInt("tp_src")));

		return match;
	}
}
