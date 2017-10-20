package com.rip;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;


public class RoutingTable extends ConcurrentHashMap<InetAddress, RTEntry> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7510002186289397765L;
	
	
	public double getCost(InetAddress networkAddr) {
		RTEntry re = get(networkAddr);
		if (re != null) {
			return re.getCost();
		}
		return Double.POSITIVE_INFINITY;
	}
	
	public InetAddress getNxtHop(InetAddress networkAddr) {
		RTEntry re = get(networkAddr);
		if (re != null) {
			return re.getNexthop();
		}
		return null;
	}
	
	public InetAddress getSubnet(InetAddress networkAddr) {
		RTEntry re = get(networkAddr);
		if (re != null) {
			return re.getSubnet();
		}
		return null;
	}

	@Override
	public String toString() {		
		synchronized (this) {
			StringBuilder sb = new StringBuilder();
			Iterator<InetAddress> itr = keySet().iterator();
			sb.append("Dest Ntwk | Subnet Mask | Nxt Hop IP | Cost" + "\n");
			while (itr.hasNext()) {
				InetAddress key = itr.next();
				sb.append(key + " | " + "255.255.255.0" + " | " + get(key) + "\n");
			}
			return sb.toString();
		}	
	}
}
