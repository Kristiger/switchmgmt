package model.tools.ACL;

public class ACLRule {
	private String ruleID;		//get from controller

	private String nw_proto; // TCP or UDP or ICMP
	private String src_ip; // must be specified
	private String dst_ip; // must be specified
	private String action; // DENY or ALLOW ,DENY default

	private int tp_dst; // valid when nw_proto=TCP or UDP

	public ACLRule() {
		action = "DENY";
		tp_dst = -1;
	}

	public String getRuleID() {
		return ruleID;
	}

	public void setRuleID(String ruleID) {
		this.ruleID = ruleID;
	}

	public String getNw_proto() {
		return nw_proto;
	}

	public void setNw_proto(String nw_proto) {
		if(nw_proto.equals("TCP") || nw_proto.equals("UDP") || nw_proto.equals("ICMP"))
			this.nw_proto = nw_proto;
	}

	public String getSrc_ip() {
		return src_ip;
	}

	public void setSrc_ip(String src_ip) {
		this.src_ip = src_ip;
	}

	public String getDst_ip() {
		return dst_ip;
	}

	public void setDst_ip(String dst_ip) {
		this.dst_ip = dst_ip;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if (action.equals("DENY") || action.equals("ALLOW"))
			this.action = action;
	}

	public int getTp_dst() {
		return tp_dst;
	}

	public void setTp_dst(int tp_dst) {
		if (tp_dst > 0 && tp_dst < 65535)
			this.tp_dst = tp_dst;
	}

	@Override
	public String toString() {
		return "ACLRule ["
				+ (nw_proto != null ? "nw_proto=" + nw_proto + ", " : "")
				+ (src_ip != null ? "src_ip=" + src_ip + ", " : "")
				+ (dst_ip != null ? "dst_ip=" + dst_ip + ", " : "") + "tp_dst="
				+ tp_dst + ", " + (action != null ? "action=" + action : "")
				+ "]";
	}

	public String serialize() {
		String serial = "{";
		if (nw_proto != null) {
			if (serial.length() > 10)
				serial.concat(",");
			serial.concat("\"nw-proto\":\"" + nw_proto + "\"");
		}
		if (src_ip != null) {
			if (serial.length() > 10)
				serial.concat(",");
			serial.concat("\"src-ip\":\"" + src_ip + "\"");
		}
		if (dst_ip != null) {
			if (serial.length() > 10)
				serial.concat(",");
			serial.concat("\"dst-ip\":\"" + dst_ip + "\"");
		}
		if (!nw_proto.equals("ICMP") && !nw_proto.equals("")) {
			if(tp_dst != -1){
				if (serial.length() > 10)
					serial.concat(",");
				serial.concat("\"tp-dst\":\"" + Integer.toString(tp_dst) + "\"");
			}
		}
		if (action != null){
			if(serial.length() > 10)
				serial.concat(",");
			serial.concat("\"action\":" + action + "\"");
		}
		
		serial.concat("}");
		return serial;
	}

	/**
	 * @param other
	 * @return if their serialize is totally equal. 
	 */
	public boolean equals(ACLRule other) {		
		if (this.serialize().equals(other.serialize()))
			return true;
		
		return false;
	}
		
}
