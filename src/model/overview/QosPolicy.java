package model.overview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QosPolicy {
	private String queuePort;
	private String qosName;
	private String switchdpid;
	private String uuid;
	
	private long maxRate;
	private long minRate;
	
	private List<QosQueue> queues;

	public QosPolicy(){
		queues = new ArrayList<QosQueue>();
	}
	
	public QosPolicy(String switchdpid, String qosName, String queuePort, long maxRate, long minRate){
		this();
		this.qosName = qosName;
		this.queuePort = queuePort;
		this.switchdpid = switchdpid;
		
		this.maxRate = maxRate;
		this.minRate = minRate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getQueuePort() {
		return queuePort;
	}

	public void setQueuePort(String queuePort) {
		this.queuePort = queuePort;
	}

	public String getQosName() {
		return qosName;
	}

	public void setQosName(String qosName) {
		this.qosName = qosName;
	}

	public String getSwitchdpid() {
		return switchdpid;
	}

	public void setSwitchdpid(String switchdpid) {
		this.switchdpid = switchdpid;
	}

	public long getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(long maxRate) {
		this.maxRate = maxRate;
	}

	public long getMinRate() {
		return minRate;
	}

	public void setMinRate(long minRate) {
		this.minRate = minRate;
	}

	public void addQueue(QosQueue queue){
		queues.add(queue);
	}
	
	public QosQueue getQueue(String queueid){
		Iterator<QosQueue> it = queues.iterator();
		QosQueue queue = null;
		while(it.hasNext()){
			queue = it.next();
			if(queue.getQueueID() == Integer.valueOf(queueid))
				return queue;
		}
		return null;
	}
	
	public List<QosQueue> getQueues() {
		return queues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (maxRate ^ (maxRate >>> 32));
		result = prime * result + (int) (minRate ^ (minRate >>> 32));
		result = prime * result + ((qosName == null) ? 0 : qosName.hashCode());
		result = prime * result
				+ ((queuePort == null) ? 0 : queuePort.hashCode());
		result = prime * result + ((queues == null) ? 0 : queues.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		QosPolicy other = (QosPolicy) obj;
		if (this.toString().equals(other.toString())){
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "QosPolicy [queuePort=" + queuePort + ", qosName=" + qosName
				+ ", switchdpid=" + switchdpid + ", maxRate=" + maxRate
				+ ", minRate=" + minRate + ", queues=" + queues + "]";
	}

	public String serialize(){
		if (!check()){
			return "error, see log";
		}
		StringBuilder seri = new StringBuilder();
		seri.append("ovs-vsctl -- set Port ");
		seri.append(queuePort);
		seri.append(" " + qosName + "=@newqos");
		seri.append(" -- --id=@newqos");
		seri.append(" create QoS type=linux-htb");
		
		if (maxRate != 0)
			seri.append(" other-config:max-rate=" + maxRate);
		
		if (minRate != 0)
			seri.append(" other-config:min-rate=" + minRate);
		
		seri.append(" queues=");
		
		Iterator<QosQueue> it = queues.iterator();
		QosQueue qu;
		qu = it.next();
		seri.append(qu.getQueueID() + "=@q"+ qu.getQueueID());
		
		while(it.hasNext()){
			seri.append(",");
			qu = it.next();
			seri.append(qu.getQueueID() + "=@q"+ qu.getQueueID());
		}
		
		it = null;
		it = queues.iterator();
		while(it.hasNext()){
			qu = it.next();
			seri.append(qu.serialize());
		}
		
		return seri.toString();		
	}
	
	public boolean check(){
		
		if (queuePort == null){
			System.out.println("port is null.");
			return false;
		}			
		if (qosName == null){
			System.out.println("qosName is null.");
			return false;
		}
		if (maxRate == 0 && minRate == 0){
			System.out.println("Rate can't be both set 0.");
			return false;
		}
		if (queues.size() == 0){
			System.out.println("No queue set.");
			return false;
		}
		
		return true;
	}		
}
