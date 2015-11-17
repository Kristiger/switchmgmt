package model.overview;

public class Action {

	
	/*
	 * output table group enqueue set_queue strip_vlan set_vlan_vid set_vlan_pcp 
	 * push_vlan pop_vlan set_eth_src set_eth_dst set_ip_tos set_ip_ecn 
	 * set_ipv4_src set_ipv4_dst set_ip_ttl dec_ip_ttl copy_ip_ttl_in copy_ip_ttl_out
	 * set_mpls_label set_mpls_tc set_mpls_ttl dec_mpls_ttl push_mpls pop_mpls 
	 * push_pbb pop_pbb set_tp_src set_tp_dst set_field
	 */
	String type, param, value;

	public Action(String t){
	    
	    this.type = t;
	    
		if (t.equals("output"))
			this.param = "Port";
		else if (t.equals("enqueue"))
			this.param = "Port:Queue ID";
		else if (t.equals("strip_vlan"))
		    this.param = "STRIP_VLAN";
		else if (t.equals("set_vlan_vid"))
			this.param = "VLAN ID";
		else if (t.equals("set_vlan_pcp"))
			this.param = "VLAN PCP";
		else if (t.equals("set_eth_src"))
			this.param = "Data Layer Address";
		else if (t.equals("set_eth_dst_"))
			this.param = "Data Layer Address";
		else if (t.equals("set_ip_tos"))
			this.param = "Network Type Of Service";
		else if (t.equals("set_ipv4_src"))
			this.param = "Network Address";
		else if (t.equals("set_ipv4_dst"))
			this.param = "Network Address";
		else if (t.equals("set_tp_src"))
			this.param = "Transport Port";
		else if (t.equals("set_tp_dst"))
			this.param = "Transport Port";
		
		//还有许多参数未添加进去。
	}

	public Action(String t, String v){
		
		this.type = t;
		this.value = v;
		
		if (t.equals("output"))
			this.param = "Port";
		else if (t.equals("enqueue"))
			this.param = "Port:Queue ID";
		else if (t.equals("strip_vlan"))
            this.param = "STRIP_VLAN";
		else if (t.equals("set_vlan_vid"))
			this.param = "VLAN ID";
		else if (t.equals("set_vlan_pcp"))
			this.param = "VLAN PCP";
		else if (t.equals("set_eth_src"))
			this.param = "Data Layer Address";
		else if (t.equals("set_eth_dst"))
			this.param = "Data Layer Address";
		else if (t.equals("set_ip_tos"))
			this.param = "Network Type Of Service";
		else if (t.equals("set_ipv4_src"))
			this.param = "Network Address";
		else if (t.equals("set_ipv4_dst"))
			this.param = "Network Address";
		else if (t.equals("set_tp_src"))
			this.param = "Transport Port";
		else if (t.equals("set_tp_dst"))
			this.param = "Transport Port";
	}

	public Action(String t, String v, String p) {
		this.type = t;
		this.value = v;
		this.param = p;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String serialize() {
		if (type.equals("strip_vlan")) {
			return type;
		}
		return type + "=" + value;
	}
}
