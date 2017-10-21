package com.rip;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * class RTEntry
 *
 * @version 1.0
 * @author Bhavin Shah (bns8487)
 */
public class RTEntry implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7404168374212697721L;
	private InetAddress subnet;
	private InetAddress nexthop;
	private double cost;

	public RTEntry(InetAddress subnet, InetAddress nexthop, double cost) {
		this.subnet = subnet;
		this.nexthop = nexthop;
		this.cost = cost;
	}

	/**
	 * gets the subnet address
	 * 
	 * @return
	 */
	public InetAddress getSubnet() {
		return subnet;
	}

	/**
	 * sets the subnet address
	 * 
	 * @param subnet
	 */
	public void setSubnet(InetAddress subnet) {
		this.subnet = subnet;
	}

	/**
	 * gets the next hop address
	 * 
	 * @return
	 */
	public InetAddress getNexthop() {
		return nexthop;
	}

	/**
	 * sets the next hop address
	 * 
	 * @param nexthop
	 */
	public void setNexthop(InetAddress nexthop) {
		this.nexthop = nexthop;
	}

	/**
	 * gets cost
	 * 
	 * @return
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * sets cost
	 * 
	 * @param cost
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * returns string of RTEntry
	 */
	@Override
	public String toString() {
		return subnet.toString() + "|" + nexthop.toString() + " | " + cost;
	}

}
