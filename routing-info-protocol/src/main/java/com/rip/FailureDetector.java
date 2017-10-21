package com.rip;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

/**
 * class FailureDetector
 *
 * @version 1.0
 * @author Bhavin Shah (bns8487)
 */
public class FailureDetector extends TimerTask {

	private RoutingService rs = RoutingService.getInstance();
	private final long timeLimit;
	private List<Node> neighbors;

	public FailureDetector(long timeLimit, List<Node> neighbors) {
		this.timeLimit = timeLimit;
		this.neighbors = neighbors;
	}

	/**
	 * This method checks the last update received from neighbor. If no update
	 * is received in given time limit, then it considers the node to be down
	 * and sets cost to all destinations via that node to Infinity.
	 */
	@Override
	public void run() {
		for (Node n : neighbors) {
			long prevTime = rs.getLastUpdateTime(n.getAddress());
			long curTime = System.currentTimeMillis();
			if (curTime - prevTime > timeLimit) {
				RoutingTable rt = rs.getRoutingTable(null);
				Iterator<InetAddress> iter = rt.keySet().iterator();
				while (iter.hasNext()) {
					InetAddress destNtwk = iter.next();
					InetAddress nxtHop = rs.getNxtHop(destNtwk);
					InetAddress subnet = rs.getSubnet(destNtwk);
					if (n.getAddress().equals(nxtHop)) {
						rs.updateRoute(destNtwk, subnet, nxtHop, Double.POSITIVE_INFINITY);
					}
				}
			}
		}
	}

}
