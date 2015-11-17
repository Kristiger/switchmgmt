package controller.floodlightprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.overview.FirewallRule;
import model.overview.Flow;
import model.overview.QosPolicy;
import model.overview.Switch;
import model.overview.VmData;
import controller.overview.switches.FlowJSON;
import controller.overview.switches.SwitchesJSON;
import controller.overview.vms.VmDataGetter;
import controller.tools.firewall.json.RuleJSON;
import controller.tools.flowmanager.json.StaticFlowManagerJSON;
import controller.util.JSONException;

public class FloodlightProvider {

	private static List<Switch> switches = new ArrayList<Switch>();
	private static List<Flow> staticFlows = new ArrayList<Flow>();
	private static List<Flow> realFlows = new ArrayList<Flow>();
	private static List<QosPolicy> qospolicies = new ArrayList<QosPolicy>();
	private static List<VmData> vms = new ArrayList<VmData>();
	
	private static String IP, PORT = "8080";

	/**
	 * @return The list of switches
	 */
	public static List<Switch> getSwitches(boolean update) {
		if (update) {
			try {
				switches = SwitchesJSON.getSwitches();
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
			return switches;
		} else {
			return switches;
		}
	}

	public static List<String> getSwitchDpids() {
		List<String> dpids = new ArrayList<String>();
		for (Switch sw : switches) {
			dpids.add(sw.getDpid());
		}
		return dpids;
	}

	/**
	 * 
	 * @return The specified switch detailes
	 */
	public static Switch getSwitch(String dpid, boolean update) {
		for (Switch sw : switches) {
			if (sw.getDpid().equals(dpid)) {
				if (update) {
					try {
						SwitchesJSON.updateSwitch(sw);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return sw;
			}
		}
		return null;
	}

	public static List<Flow> getStaticFlows(String dpid, boolean update) {
		if (update) {
			try {
				staticFlows = StaticFlowManagerJSON.getFlows(dpid);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return staticFlows;
		} else {
			return staticFlows;
		}
	}

	public static List<Flow> getRealFlows(String dpid, boolean update) {
		if (update) {
			try {
				realFlows = FlowJSON.getFlows(dpid);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return realFlows;
		} else {
			return realFlows;
		}
	}

	/**
	 * @return policys for the ports attached to switches
	 */
	public static List<QosPolicy> getQospolicys() {
		return qospolicies;
	}

	public static QosPolicy getQospolicy(String sw, String port) {
		for (QosPolicy qp : qospolicies) {
			if (qp.getSwitchdpid().equals(sw) && qp.getQueuePort().equals(port)) {
				return qp;
			}
		}
		return null;
	}

	public static void addQospolicy(QosPolicy qospolicy) {
		qospolicies.add(qospolicy);
	}

	/**
	 * @return Firewall rules
	 */
	public static List<FirewallRule> getRules() {
		try {
			return RuleJSON.getRules();
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return controller IP
	 */
	public static String getIP() {
		return IP;
	}

	public static void setIP(String IPAddress) {
		IP = IPAddress;
	}

	/**
	 * @return the port controller listened to 
	 */
	public static String getPort() {
		return PORT;
	}

	public static void setPort(String ControllerPort) {
		PORT = ControllerPort;
	}
}