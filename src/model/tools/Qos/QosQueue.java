package model.tools.Qos;

public class QosQueue {
	
	private int queueID;
	private long minRate;
	private long maxRate;
	
	public int getQueueID() {
		return queueID;
	}
	public void setQueueID(int queueID) {
		this.queueID = queueID;
	}
	public long getMinRate() {
		return minRate;
	}
	public void setMinRate(long minRate) {
		this.minRate = minRate;
	}
	public long getMaxRate() {
		return maxRate;
	}
	public void setMaxRate(long maxRate) {
		this.maxRate = maxRate;
	}
	
	public Object serialize() {
		// TODO Auto-generated method stub
		if(!check()){
			return "error, see log";
		}
		
		StringBuilder seri = new StringBuilder();
		seri.append(" -- --id=@" + queueID + " create queue");
		if (maxRate != 0)
			seri.append(" other-config:max-rate=" + String.valueOf(maxRate));		
		if (minRate != 0)
			seri.append(" other-config:min-rate=" + String.valueOf(minRate));
				
		return seri.toString();
	}
	
	public boolean check(){
		if (maxRate == 0 && minRate == 0){
			System.out.println("Rate can't be both set 0.");
			return false;
		}
		
		return true;
	}
}
