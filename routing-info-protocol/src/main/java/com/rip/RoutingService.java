package com.rip;

import java.net.InetAddress;
import java.util.Iterator;

public class RoutingService {

	private RoutingTable routingTable = new RoutingTable();
	private RoutingTable neighbors = new RoutingTable();
	private static RoutingService instance;
	public static final boolean isPoisonReverse = true;

	private RoutingService() {
	}

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

	 public synchronized RoutingTable getRoutingTable(InetAddress destAddr) {
		
		RoutingTable rt = new RoutingTable(); 		
		Iterator<InetAddress> itr = routingTable.keySet().iterator();		
		while (itr.hasNext()) {
			InetAddress key = itr.next();
			RTEntry entry = routingTable.get(key);
			if (isPoisonReverse && entry.getNexthop().equals(destAddr)) {
				rt.put(key, new RTEntry(entry.getSubnet(), destAddr, Double.POSITIVE_INFINITY));
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

	public void updateRoute(InetAddress destNtwk, InetAddress subnet, InetAddress nxthopIp, double cost) {
		RTEntry re = new RTEntry(subnet, nxthopIp, cost);
		routingTable.put(destNtwk, re);
	}
	
	public void addNeighbor(InetAddress address, InetAddress subnet, double cost) {		
		RTEntry entry = new RTEntry(subnet, address, cost);
		neighbors.put(address, entry);
	}
	
	public double getNbrLinkCost(InetAddress nbr) {
		RTEntry entry = neighbors.get(nbr);
		return entry.getCost();
	}
	
	public InetAddress getNbrSubnet(InetAddress nbr) {
		RTEntry entry = neighbors.get(nbr);
		return entry.getSubnet();
	}

	public String printTable() {
		return routingTable.toString();
	}

}
