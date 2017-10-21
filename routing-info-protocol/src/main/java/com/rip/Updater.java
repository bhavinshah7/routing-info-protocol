package com.rip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 * class Updater
 *
 * @version 1.0
 * @author Bhavin Shah (bns8487)
 */
public class Updater implements Runnable {

	private RoutingTable nbrTable;
	private RoutingService service;
	private InetAddress nbrIp;
	private InetAddress nbrSubnet;
	private Double nbrCost = 1.0;

	/**
	 * Constructor for Updater class
	 *
	 * @param table
	 * @param nbr
	 */
	public Updater(RoutingTable table, InetAddress nbr) {
		nbrTable = table;
		nbrIp = nbr;
		service = RoutingService.getInstance();
		nbrSubnet = service.getNbrSubnet(nbrIp);
		nbrCost = service.getNbrLinkCost(nbrIp);
	}

	/**
	 * This method updates router's routing table based on routing table
	 * received from the neighbor
	 */
	@Override
	public void run() {
		try {

			// Set Time of update received from neighbor
			service.setUpdateTime(nbrIp, System.currentTimeMillis());

			InetAddress nbrNetwork = getNetworkAddr(nbrIp, nbrSubnet);

			// add neighbor in RT if not present
			if (service.getCost(nbrNetwork) > nbrCost) {
				service.updateRoute(nbrNetwork, nbrSubnet, nbrIp, nbrCost);
			}

			Iterator<InetAddress> itr = nbrTable.keySet().iterator();
			while (itr.hasNext()) {
				InetAddress destNtwkAddr = itr.next();

				if (!service.containsNtwkInt(destNtwkAddr)) {
					double costViaNbr = getCostViaNbr(destNtwkAddr);
					double curCost = service.getCost(destNtwkAddr);

					InetAddress curNxtHop = service.getNxtHop(destNtwkAddr);

					if (nbrIp.equals(curNxtHop) && (costViaNbr > curCost)) {
						// link cost updated
						InetAddress destSubnet = nbrTable.getSubnet(destNtwkAddr);
						service.updateRoute(destNtwkAddr, destSubnet, nbrIp, costViaNbr);
					} else {
						// Nbr has better path
						if (costViaNbr < curCost) {
							InetAddress destSubnet = nbrTable.getSubnet(destNtwkAddr);
							service.updateRoute(destNtwkAddr, destSubnet, nbrIp, costViaNbr);
						}
					}
				}

			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		System.out.println(service.printTable());
	}

	/**
	 * This method returns the network prefix address of after applying subnet
	 * mask to the given ip address
	 *
	 * @param ipAddr
	 * @param subnetMask
	 * @return
	 * @throws UnknownHostException
	 */
	public InetAddress getNetworkAddr(InetAddress ipAddr, InetAddress subnetMask) throws UnknownHostException {
		byte[] ipBytes = ipAddr.getAddress();
		byte[] subnetBytes = subnetMask.getAddress();

		byte[] networkBytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			networkBytes[i] = (byte) (ipBytes[i] & subnetBytes[i]);
		}
		return InetAddress.getByAddress(networkBytes);
	}

	/**
	 * This method returns cost to send a packet to given destination via the
	 * given neighbor
	 * 
	 * @param destNtwkAddr
	 * @return
	 */
	private double getCostViaNbr(InetAddress destNtwkAddr) {
		if (nbrTable.getCost(destNtwkAddr) == Double.POSITIVE_INFINITY) {
			return Double.POSITIVE_INFINITY;
		}
		return nbrCost + nbrTable.getCost(destNtwkAddr);
	}

}
