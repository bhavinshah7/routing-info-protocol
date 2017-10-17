package com.rip;

import java.net.InetAddress;
import java.util.Iterator;

public class Updater implements Runnable {

	private RoutingTable nbrTable;
	private RoutingService service;
	private InetAddress nbr;

	public Updater(RoutingTable nbrTable, InetAddress nbr) {
		this.nbrTable = nbrTable;
		this.nbr = nbr;
		service = RoutingService.getInstance();
	}

	@Override
	public void run() {
		RoutingTable rt = service.getRoutingTable();
		Iterator<InetAddress> itr = nbrTable.keySet().iterator();
		while (itr.hasNext()) {
			InetAddress dest = itr.next();
			double costViaNbr = rt.getCost(nbr) + nbrTable.getCost(dest);
			double curCost = rt.getCost(dest);
			if (costViaNbr < curCost) {
				rt.updateRoute(dest, nbr, costViaNbr);
			}
		}

	}

}
