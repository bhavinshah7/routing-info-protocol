package com.rip;

import java.io.Serializable;
import java.net.InetAddress;

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

	public InetAddress getSubnet() {
		return subnet;
	}

	public void setSubnet(InetAddress subnet) {
		this.subnet = subnet;
	}

	public InetAddress getNexthop() {
		return nexthop;
	}

	public void setNexthop(InetAddress nexthop) {
		this.nexthop = nexthop;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return subnet.toString() + "|" + nexthop.toString() + " | " + cost;
	}

}
