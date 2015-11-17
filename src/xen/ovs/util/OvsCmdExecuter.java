package xen.ovs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import controller.util.SSHConnector;

/**
 * @author LZ
 * 
 */
public class OvsCmdExecuter {

	private static Logger log = Logger.getGlobal();

	/**
	 * add queue to qos
	 * 
	 * @param qosUuid
	 * @param queueId
	 * @param queueUuid
	 */
	public static void addQosQueue(String qosUuid, int queueId, String queueUuid) {
		String command = "ovs-vsctl add qos " + qosUuid + " queues " + queueId
				+ "=" + queueUuid;
		List<String> result = new SSHConnector().exec(command);
		if (result.size() != 0) {
			log.info("Error occured :" + result.get(0));
		}
	}

	/**
	 * create qos or queue record in table
	 * 
	 * @param table
	 * @param maxRate
	 * @param minRate
	 * @return uuid of the specific table
	 */
	public static String createRow(String table, long maxRate, long minRate) {
		String command = "ovs-vsctl";
		if (table.equals("qos")) {
			command = command + " create qos type=linux-htb ";
		} else if (table.equals("queue")) {
			command = command + "create queue";
		} else {
			log.info("Unrecognized table :" + table);
			return null;
		}

		if (maxRate > 0)
			command = command + "other-config:max-rate=" + maxRate;
		if (minRate > 0)
			command = command + "other-config:min-rate=" + minRate;

		List<String> result = new SSHConnector().exec(command);
		if (result.size() != 0) {
			log.info("Error occured :" + result.get(0));
			return null;
		}
		return result.get(0);
	}

	/**
	 * destroy the specific table row with the uuid
	 * 
	 * @param table
	 * @param uuid
	 */
	public static void destroyRecord(String table, String uuid) {
		String command = "ovs-vsctl destroy " + table + " " + uuid;
		List<String> result = new SSHConnector().exec(command);
		if (result.size() != 0) {
			log.info("Error occured :" + result.get(0));
		}
	}

	public static void removeQosQueue(String qosUuid, String queueId) {
		String command = "ovs-vsctl remove qos " + qosUuid + " queues "
				+ queueId;
		List<String> result = new SSHConnector().exec(command);
		if (result.size() != 0) {
			log.info("Error occured :" + result.get(0));
		}
	}

	public static void destroyAll(String table) {
		String command = "ovs-vsctl --all destroy ";
		if (table.equals("qos"))
			command = command + "qos";
		else if (table.equals("queue"))
			command = command + "queue";
		else {
			log.info("Unrecognized table :" + table);
			return;
		}

		List<String> result = new SSHConnector().exec(command);
		if (result.size() != 0) {
			log.info("Error occured :" + result.get(0));
		}
	}

	public static void setUploadRate(String portId, long maxRate, long burstRate) {
		String command1 = "";
		if (maxRate > 0) {
			command1 = "ovs-vsctl set interface" + portId
					+ " ingress_policing_rate=" + maxRate;
		} else {
			log.info("Max rate must be set.");
			return;
		}

		List<String> result = new SSHConnector().exec(command1);
		if (result.size() != 0) {
			log.info("Error occured :" + result.get(0));
			return;
		}

		String command2 = "";
		if (burstRate > 0) {
			command2 = "ovs-vsctl set interface" + portId
					+ " ingress_policing_burst=" + burstRate;
		}

		result = new SSHConnector().exec(command2);
		if (result.size() != 0) {
			log.info("Error occured :" + result.get(0));
		}

	}

	public static void setPortQos(String portId, String qosUuid,
			String[] queueUuids) {
		String command = "ovs-vsctl set port " + portId + " qos=" + qosUuid;
		List<String> result = new SSHConnector().exec(command);
		if (result.size() != 0) {
			log.info("Error occured :" + result.get(0));
		}
	}

	public static void setVlanTag(String portId, int vlanTag) {
		String command = "ovs-vsctl set interface " + portId + " tag="
				+ vlanTag;
		List<String> result = new SSHConnector().exec(command);
		if (result.size() != 0) {
			// log.info("");
		}
	}

	public static List<String> getUuids(String portId) {
		// String command =
		// "xe vm-vif-list | grep -v network | grep -E \'uuid|label\'";
		String command = "ovs-vsctl list interface " + portId
				+ " | grep external_ids";
		List<String> result = new SSHConnector().exec(command);
		if(result.size() == 0){
			return result;
		}
		String str = result.get(0);
		
		result = new ArrayList<String>();
		result.add(str.substring(str.indexOf("xs-vm-uuid") + 13,
				str.indexOf("}") - 1));
		result.add(str.substring(str.indexOf("xs-vif-uuid") + 14,
				str.indexOf("xs-vm-uuid") - 3));
		return result;
	}

	public static String getVmUuid(String vifUuid) {
		String command = "xe vif-list uuid=" + vifUuid + " | grep vm-uuid";
		List<String> result = new SSHConnector().exec(command);

		return result.get(0);
	}

	public static List<String> getVmUuids() {
		String command = "xe vif-list | grep vm-uuid";
		List<String> result = new SSHConnector().exec(command);

		return result;
	}
}
