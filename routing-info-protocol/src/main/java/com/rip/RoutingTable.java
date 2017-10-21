package com.rip;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class RoutingTable
 *
 * @version 1.0
 * @author Bhavin Shah (bns8487)
 */
public class RoutingTable extends ConcurrentHashMap<InetAddress, RTEntry> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7510002186289397765L;

	/**
	 * Returns cost to send packet to given destination
	 *
	 * @param networkAddr
	 * @return
	 */
	public double getCost(InetAddress networkAddr) {
		RTEntry re = get(networkAddr);
		if (re != null) {
			return re.getCost();
		}
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * Returns the next hop for a given destination network
	 *
	 * @param networkAddr
	 * @return
	 */
	public InetAddress getNxtHop(InetAddress networkAddr) {
		RTEntry re = get(networkAddr);
		if (re != null) {
			return re.getNexthop();
		}
		return null;
	}

	/**
	 * returns the subnet mask for a given network
	 *
	 * @param networkAddr
	 * @return
	 */
	public InetAddress getSubnet(InetAddress networkAddr) {
		RTEntry re = get(networkAddr);
		if (re != null) {
			return re.getSubnet();
		}
		return null;
	}

	/**
	 * Returns string implementation of the routing table.
	 */
	@Override
	public String toString() {
		synchronized (this) {
			StringBuilder sb = new StringBuilder();
			Iterator<InetAddress> itr = keySet().iterator();
			sb.append("Dest Network | Subnet Mask | Nxt Hop IP | Cost" + "\n");
			sb.append("-------------------------------------------------------" + "\n");
			while (itr.hasNext()) {
				InetAddress key = itr.next();
				sb.append(key + " | " + get(key) + "\n");
			}
			return sb.toString();
		}
	}
}
