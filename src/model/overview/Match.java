package model.overview;

public class Match {

	/*
	 *  in_port eth_type eth_src eth_dst eth_vlan_vid eth_vlan_pcp ip_proto ipv4_src ipv4_dst
	 *  ipv6_src ipv6_dst ipv6_label ip_tos ip_ecn ip_dscp tp_src tp_dst udp_src udp_dst 
	 *  tcp_src tcp_dst sctp_src sctp_dst icmpv4_type icmpv4_code icmpv6_type icmpv6_code 
	 *  ipv6_nd_ssl ipv6_nd_ttl ipv6_nd_target arp_opcode arp_sha arp_tha arp_spa arp_tpa 
	 *  mpls_label mpls_tc mpls_bos tunnel_id metadata
	 */
	String dataLayerDestination, dataLayerSource, dataLayerType, dataLayerVLAN,
			dataLayerPCP, inputPort, networkDestination,
			networkDestinationMaskLength, networkProtocol, networkSource,
			networkSourceMaskLength, networkTypeOfService,
			transportDestination, transportSource, wildcards;
	
	String ipProto, tcpSource, tcpDestination, udpSource, udpDestination, 
			stcpSource, stcpDestination, icmpv4Type, icmpv4Code, mplsLable, mplsTc,
			mplsBos, tunnelId, metadata;

	public Match() {

	}

	public String getDataLayerDestination() {
		return dataLayerDestination;
	}

	public void setDataLayerDestination(String dataLayerDestination) {
		this.dataLayerDestination = dataLayerDestination;
	}

	public String getDataLayerSource() {
		return dataLayerSource;
	}

	public void setDataLayerSource(String dataLayerSource) {
		this.dataLayerSource = dataLayerSource;
	}

	public String getDataLayerType() {
		return dataLayerType;
	}

	public void setDataLayerType(String dataLayerType) {
		this.dataLayerType = dataLayerType;
	}

	public String getDataLayerVLAN() {
		return dataLayerVLAN;
	}

	public void setDataLayerVLAN(String dataLayerVLAN) {
		this.dataLayerVLAN = dataLayerVLAN;
	}

	public String getDataLayerPCP() {
		return dataLayerPCP;
	}

	public void setDataLayerPCP(String dataLayerPCP) {
		this.dataLayerPCP = dataLayerPCP;
	}

	public String getInputPort() {
		return inputPort;
	}

	public void setInputPort(String inputPort) {
		this.inputPort = inputPort;
	}

	public String getNetworkDestination() {
		return networkDestination;
	}

	public void setNetworkDestination(String networkDestination) {
		this.networkDestination = networkDestination;
	}

	public String getNetworkDestinationMaskLength() {
		return networkDestinationMaskLength;
	}

	public void setNetworkDestinationMaskLength(
			String networkDestinationMaskLength) {
		this.networkDestinationMaskLength = networkDestinationMaskLength;
	}

	public String getNetworkProtocol() {
		return networkProtocol;
	}

	public void setNetworkProtocol(String networkProtocol) {
		this.networkProtocol = networkProtocol;
	}

	public String getNetworkSource() {
		return networkSource;
	}

	public void setNetworkSource(String networkSource) {
		this.networkSource = networkSource;
	}

	public String getNetworkSourceMaskLength() {
		return networkSourceMaskLength;
	}

	public void setNetworkSourceMaskLength(String networkSourceMaskLength) {
		this.networkSourceMaskLength = networkSourceMaskLength;
	}

	public String getNetworkTypeOfService() {
		return networkTypeOfService;
	}

	public void setNetworkTypeOfService(String networkTypeOfService) {
		this.networkTypeOfService = networkTypeOfService;
	}

	public String getTransportDestination() {
		return transportDestination;
	}

	public void setTransportDestination(String transportDestination) {
		this.transportDestination = transportDestination;
	}

	public String getTransportSource() {
		return transportSource;
	}

	public void setTransportSource(String transportSource) {
		this.transportSource = transportSource;
	}

	public String getWildcards() {
		return wildcards;
	}

	public void setWildcards(String wildcards) {
		this.wildcards = wildcards;
	}

	public String serialize() {
		String serial = "";
		if (this.dataLayerSource != null) {
			serial = serial.concat(",\"eth_src\":\"" + this.dataLayerSource
					+ "\"");
		}
		if (this.dataLayerDestination != null) {
			serial = serial.concat(",\"eth_dst\":\""
					+ this.dataLayerDestination + "\"");
		}
		if (this.dataLayerType != null) {
			serial = serial.concat(",\"eth_type\":\"" + this.dataLayerType
					+ "\"");
		}
		if (this.dataLayerVLAN != null) {
			serial = serial.concat(",\"eth_vlan_vid\":\"" + this.dataLayerVLAN
					+ "\"");
		}
		if (this.dataLayerPCP != null) {
			serial = serial.concat(",\"eth_vlan_pcp\":\"" + this.dataLayerPCP
					+ "\"");
		}
		if (this.inputPort != null) {
			serial = serial.concat(",\"in_port\":\"" + this.inputPort
					+ "\"");
		}
		if (this.networkDestination != null) {
			serial = serial.concat(",\"ipv4_dst\":\"" + this.networkDestination
					+ "\"");
		}
		if (this.networkSource != null) {
			serial = serial
					.concat(",\"ipv4_src\":\"" + this.networkSource + "\"");
		}
		if (this.networkTypeOfService != null) {
			serial = serial.concat(",\"ip_tos\":\""
					+ this.networkTypeOfService + "\"");
		}
		if (this.transportDestination != null) {
			serial = serial.concat(",\"tp_dst\":\""
					+ this.transportDestination + "\"");
		}
		if (this.transportSource != null) {
			serial = serial.concat(",\"tp_src\":\"" + this.transportSource
					+ "\"");
		}
		//if (this.wildcards != null) {
		//	serial = serial.concat(",\"wildcards\":\"" + this.wildcards + "\"");
		// This is not able to be set by the user yet
		// if(!this.networkDestinationMaskLength.isEmpty())
		// if(serial.length() > 5)
		// serial = serial.concat(",");
		// serial = serial.concat("\"ingress-port\":\"" +
		// this.networkDestinationMaskLength + "\"");
		// This is not able to be set by the user yet
		// if(!this.networkSourceMaskLength.isEmpty())
		// if(serial.length() > 5)
		// serial = serial.concat(",");
		// serial = serial.concat("\"ingress-port\":\"" +
		// this.networkSourceMaskLength + "\"");
		return serial;
	}
	
	@Override
	public String toString() {
		String serial = "";
		if (this.dataLayerSource != null) {
			if(serial.length() > 5)
			serial = serial.concat(", ");
			serial = serial.concat("src-mac:" + this.dataLayerSource);
		}
		if (this.dataLayerDestination != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("dst-mac:"
					+ this.dataLayerDestination);
		}
		if (this.dataLayerType != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("ether-type:" + this.dataLayerType);
		}
		if (this.dataLayerVLAN != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("vlan-id:" + this.dataLayerVLAN);
		}
		if (this.dataLayerPCP != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("vlan-priority:" + this.dataLayerPCP);
		}
		if (this.inputPort != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("port:" + this.inputPort);
		}
		if (this.networkDestination != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("dst-ip:" + this.networkDestination);
		}
		if (this.networkSource != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial
					.concat("src-ip:" + this.networkSource);
		}
		if (this.networkTypeOfService != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("tos-bits:"
					+ this.networkTypeOfService);
		}
		if (this.transportDestination != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("dst-port:"
					+ this.transportDestination);
		}
		if (this.transportSource != null) {
			if(serial.length() > 5)
				serial = serial.concat(", ");
			serial = serial.concat("src-port:" + this.transportSource);
		}
		// This is not able to be set by the user yet
		// if(!this.networkDestinationMaskLength.isEmpty())
		// if(serial.length() > 5)
		// serial = serial.concat(",");
		// serial = serial.concat("\"ingress-port\":\"" +
		// this.networkDestinationMaskLength + "\"");
		// This is not able to be set by the user yet
		// if(!this.networkSourceMaskLength.isEmpty())
		// if(serial.length() > 5)
		// serial = serial.concat(",");
		// serial = serial.concat("\"ingress-port\":\"" +
		// this.networkSourceMaskLength + "\"");
		return serial;
	}
}
