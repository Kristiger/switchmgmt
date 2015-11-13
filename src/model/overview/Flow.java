package model.overview;

import java.util.ArrayList;
import java.util.List;

public class Flow implements Comparable<Flow> {

	// priority, max is 32767,default is 32767.
	String name, priority, cookie, idleTimeOut, hardTimeOut, outPort, sw,
			durationSeconds, packetCount, byteCount;
	String table, cookieMask;

	List<Action> actions;
	Match match;

	public Flow() {
		match = new Match();
		actions = new ArrayList<Action>();
	}

	public Flow(String selectedSwitch) {
		sw = selectedSwitch;
		actions = new ArrayList<Action>();
		match = new Match();
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getSw() {
		return sw;
	}

	public void setSw(String sw) {
		this.sw = sw;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPacketCount() {
		return packetCount;
	}

	public void setPacketCount(String packetCount) {
		this.packetCount = packetCount;
	}

	public String getByteCount() {
		return byteCount;
	}

	public void setByteCount(String byteCount) {
		this.byteCount = byteCount;
	}

	public String getDurationSeconds() {
		return durationSeconds;
	}

	public void setDurationSeconds(String durationSeconds) {
		this.durationSeconds = durationSeconds;
	}

	public String getSwitch() {
		return sw;
	}

	public void setSwitch(String sw) {
		this.sw = sw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		/*
		 * if(priority.equals("32767")) this.priority = ""; else this.priority =
		 * priority;
		 */
		// if priority is num and between 1-32767 ?
		this.priority = priority;
	}

	public String getIdleTimeOut() {
		return idleTimeOut;
	}

	public void setIdleTimeOut(String idleTimeOut) {
		this.idleTimeOut = idleTimeOut;
	}

	public String getHardTimeOut() {
		return hardTimeOut;
	}

	public void setHardTimeOut(String hardTimeOut) {
		this.hardTimeOut = hardTimeOut;
	}

	public List<Action> getActions() {
		return this.actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public String actionsToString() {
		String serial = "";
		if (!actions.isEmpty()) {
			int len = serial.length();
			for (Action act : actions) {
				if (serial.length() > len + 1)
					serial = serial.concat(", ");
				serial = serial.concat(act.serialize());
			}
			return serial;
		}
		return serial;
	}

	public String serialize() {
		String serial = "{";

		if (sw != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"switch\":\"" + sw + "\"");
		}
		if (name != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"name\":\"" + name + "\"");
		}
		serial = serial.concat(", \"active\":\"true\"");
		if (priority != null && !priority.equals("")) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"priority\":\"" + priority + "\"");
		}
		if (!actions.isEmpty()) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"actions\":\"");
			int len = serial.length();
			for (Action act : actions) {
				if (serial.length() > len + 1)
					serial = serial.concat(",");
				serial = serial.concat(act.serialize());
			}
			serial = serial.concat("\"");
		}
		if (match != null) {
			serial = serial.concat(match.serialize());
		}

		serial = serial.concat("}");
		return serial;
	}

	public String deleteString() {
		return "{\"name\":\"" + name + "\"}";
	}

	public boolean equals(Flow otherFlow) {
		if (this.match.serialize().equals(otherFlow.match.serialize())
				&& this.actionsToString().equals(otherFlow.actionsToString())
				&& this.priority.equals(otherFlow.priority))
			return true;

		return false;
	}

	@Override
	public int compareTo(Flow o) {
		// TODO Auto-generated method stub
		return o.byteCount.length() - this.byteCount.length();
	}
}
