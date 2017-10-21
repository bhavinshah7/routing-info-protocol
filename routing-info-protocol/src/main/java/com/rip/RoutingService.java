package com.rip;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class RoutingService
 *
 * @version 1.0
 * @author Bhavin Shah (bns8487)
 */
public class RoutingService {

	private RoutingTable routingTable = new RoutingTable();
	private RoutingTable neighbors = new RoutingTable();
	private Map<InetAddress, Long> hmIpVsTime = new ConcurrentHashMap<>();
	private Set<InetAddress> hsNtwkInt = new HashSet<>();
	private static RoutingService instance;
	public static final boolean isPoisonReverse = true;

	private RoutingService() {
	}

	/**
	 * Returns singleton instance of RoutingService class
	 *
	 * @return
	 */
	public static RoutingService getInstance() {
		if (instance == null) {
			synchronized (RoutingService.class) {
				if (instance == null) {
					instance = new RoutingService();
				}
			}
		}
		return instance;
	}

	/**
	 * This method returns a copy of routing table. It also applies poison
	 * reverse if enabled.
	 *
	 * @param destAddr
	 * @return
	 */
	public RoutingTable getRoutingTable(InetAddress destAddr) {

		RoutingTable rt = new RoutingTable();
		Iterator<InetAddress> itr = routingTable.keySet().iterator();
		while (itr.hasNext()) {
			InetAddress key = itr.next();
			RTEntry entry = routingTable.get(key);
			if (isPoisonReverse && entry.getNexthop().equals(destAddr)) {
				// rt.put(key, new RTEntry(entry.getSubnet(), destAddr,
				// Double.POSITIVE_INFINITY));
			} else {
				rt.put(key, entry);
			}
		}
		return rt;
	}

	public double getCost(InetAddress networkAddr) {
		return routingTable.getCost(networkAddr);
	}

	public InetAddress getNxtHop(InetAddress networkAddr) {
		return routingTable.getNxtHop(networkAddr);
	}

	public InetAddress getSubnet(InetAddress networkAddr) {
		return routingTable.getSubnet(networkAddr);
	}

	/**
	 * this method updates cost to a given destination and its next hop
	 *
	 * @param destNtwk
	 * @param subnet
	 * @param nxthopIp
	 * @param cost
	 */
	public void updateRoute(InetAddress destNtwk, InetAddress subnet, InetAddress nxthopIp, double cost) {
		RTEntry re = new RTEntry(subnet, nxthopIp, cost);
		routingTable.put(destNtwk, re);
	}

	/**
	 * Adds given neighbor to a map against its address and initializes
	 * hmIpVsTime map to current time.
	 *
	 * @param address
	 * @param subnet
	 * @param cost
	 */
	public void addNeighbor(InetAddress address, InetAddress subnet, double cost) {
		RTEntry entry = new RTEntry(subnet, address, cost);
		neighbors.put(address, entry);
		hmIpVsTime.put(address, System.currentTimeMillis());
	}

	/**
	 * returns link cost to neighbor
	 *
	 * @param nbr
	 * @return
	 */
	public double getNbrLinkCost(InetAddress nbr) {
		RTEntry entry = neighbors.get(nbr);
		return entry.getCost();
	}

	/**
	 * returns subnet address of neighbor
	 *
	 * @param nbr
	 * @return
	 */
	public InetAddress getNbrSubnet(InetAddress nbr) {
		RTEntry entry = neighbors.get(nbr);
		return entry.getSubnet();
	}

	/**
	 * returns last time when update from neighbor was received
	 *
	 * @param address
	 * @return
	 */
	public long getLastUpdateTime(InetAddress address) {
		return hmIpVsTime.get(address);
	}

	/**
	 * Sets the time for update recived from neighbor to given time
	 *
	 * @param address
	 * @param time
	 */
	public void setUpdateTime(InetAddress address, long time) {
		hmIpVsTime.put(address, time);
	}

	/**
	 * Adds own network interface address to a set
	 *
	 * @param address
	 */
	public void addNtwkInterface(InetAddress address) {
		hsNtwkInt.add(address);
	}

	/**
	 * Checks if given address is contained in own network interface addresses
	 *
	 * @param address
	 * @return
	 */
	public boolean containsNtwkInt(InetAddress address) {
		return hsNtwkInt.contains(address);
	}

	/**
	 * prints routing table
	 *
	 * @return
	 */
	public String printTable() {
		return routingTable.toString();
	}

}
