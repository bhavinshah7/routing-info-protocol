package com.rip;

import java.net.InetAddress;


public class RTEntry {

	private transient InetAddress nexthop;
	private double cost;
	
	
		
	public RTEntry(InetAddress nexthop, double cost) {
		this.nexthop = nexthop;
		this.cost = cost;
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
	
		
}
