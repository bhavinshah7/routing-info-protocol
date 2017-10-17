package com.rip;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;


public class RoutingTable extends ConcurrentHashMap<InetAddress, RTEntry> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7510002186289397765L;


	public double getCost(InetAddress dest) {
		RTEntry re = get(dest);
		if (re != null) {
			return re.getCost();
		}
		return Double.POSITIVE_INFINITY;
	}
		
	public void updateRoute(InetAddress dest, InetAddress nxthop, double cost) {
		RTEntry re = new RTEntry(nxthop, cost);
		put(dest, re);
	}

	
}
