package model.tools.Qos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Qos {
	
	
	
	private String queuePort;
	private String qosName;
	
	private long maxRate;
	private long minRate;
	
	private List<QosQueue> queues;

	public Qos(){
		queues = new ArrayList<QosQueue>();
	}
	
	public Qos(String queuePort, String qosName, long maxRate, long minRate, int queueNum){
		this();
		this.maxRate = maxRate;
		this.minRate = minRate;
		this.qosName = qosName;
		this.queuePort = queuePort;
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
		
		Qos other = (Qos) obj;
		if (this.toString().equals(other.toString())){
			return true;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "Qos [queuePort=" + queuePort + ", qosName=" + qosName
				+ ", maxRate=" + maxRate + ", minRate=" + minRate + ", queues="
				+ queues + "]";
	}
	
	public String serialize(){
		StringBuilder seri = new StringBuilder();
		seri.append("ovs-vsctl -- set Port ");
		seri.append(queuePort);
		seri.append(" " + qosName + "=@newqos");
		seri.append(" -- --id=newqos");
		seri.append(" create QoS type=linux-htb");
		
		if (maxRate != 0)
			seri.append(" other-config:max-rate=" + maxRate);
		if (minRate != 0)
			seri.append(" other-config:in-rate=" + minRate);		
		seri.append(" queues=");
		
		Iterator<QosQueue> it = queues.iterator();
		QosQueue qu;
		int i = 0;
		qu = it.next();
		seri.append(qu.getQueueID() + "=q"+ i);
		
		while(it.hasNext()){
			seri.append(",");
			qu = it.next();
			seri.append(qu.getQueueID() + "=q"+ qu.getQueueID());
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
	
	/*ovs−vsctl −− set Port eth0 qos=@newqos \
	−− set Port eth1 qos=@newqos \
	−− −−id=@newqos create QoS type=linux−htb other−config:max−rate=1000000000
	queues=0=@q0,1=@q1 \
	−− −−id=@q0 create Queue other−config:min−rate=100000000 other−config:max−rate=100000000 \
	−− −−id=@q1 create Queue other−config:min−rate=500000000*/
		
}
