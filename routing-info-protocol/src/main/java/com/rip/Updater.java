package com.rip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

public class Updater implements Runnable {

	private RoutingTable nbrTable;
	private RoutingService service;
	private InetAddress nbrIp;
	private InetAddress nbrSubnet;
	private Double nbrCost = 1.0;

	public Updater(RoutingTable table, InetAddress nbr) {
		nbrTable = table;
		nbrIp = nbr;
		service = RoutingService.getInstance();
		nbrSubnet = service.getNbrSubnet(nbrIp);
		nbrCost = service.getNbrLinkCost(nbrIp);
	}

	@Override
	public void run() {
		try {
			InetAddress nbrNetwork = getNetworkAddr(nbrIp, nbrSubnet);

			// add neighbor in RT if not present
			if (service.getCost(nbrNetwork) > nbrCost) {
				service.updateRoute(nbrNetwork, nbrSubnet, nbrIp, nbrCost);
			}

			Iterator<InetAddress> itr = nbrTable.keySet().iterator();
			while (itr.hasNext()) {
				InetAddress destNtwkAddr = itr.next();
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
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		System.out.println(service.printTable());
	}

	public InetAddress getNetworkAddr(InetAddress ipAddr, InetAddress subnetMask) throws UnknownHostException {
		byte[] ipBytes = ipAddr.getAddress();
		byte[] subnetBytes = subnetMask.getAddress();

		byte[] networkBytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			networkBytes[i] = (byte) (ipBytes[i] & subnetBytes[i]);
		}
		return InetAddress.getByAddress(networkBytes);
	}

	private double getCostViaNbr(InetAddress destNtwkAddr) {
		if (nbrTable.getCost(destNtwkAddr) == Double.POSITIVE_INFINITY) {
			return Double.POSITIVE_INFINITY;
		}
		return nbrCost + nbrTable.getCost(destNtwkAddr);
	}

}
