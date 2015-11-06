package controller.overview.qos;

public class QosTools {
	/*
	 * Since once the application is disposed, policies stored will lose,
	 * and next time can be found except from xen, it should be stored, 
	 * and when enqueue, check if it exists.
	 * cmd:ovs-vsctl list <port> | grep qos == <queue.uuid>
	 */
	private void getQosPolicysFromDB(){
		
	}
}
