package com.rip;

import java.net.InetAddress;

public class Node {

	private InetAddress address;
	private short maskLength = 0;
	private double cost = 1.0;
	private int port = 10002;

	public Node(InetAddress ipAddr, int nPort) {
		address = ipAddr;
		port = nPort;
	}

	public Node(InetAddress ipAddr, short NtwkPrefixLen, double linkCost, int portNo) {
		address = ipAddr;
		cost = linkCost;
		port = portNo;
		maskLength = NtwkPrefixLen;
	}

	public InetAddress getSubnet() {
		int value = 0xffffffff << (32 - maskLength);
		byte[] subnetBytes = new byte[] { (byte) (value >>> 24), (byte) (value >> 16 & 0xff),
				(byte) (value >> 8 & 0xff), (byte) (value & 0xff) };
		InetAddress subnet = null;

		try {
			subnet = InetAddress.getByAddress(subnetBytes);
		} catch (Exception e) {
		}
		return subnet;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress ipAddr) {
		address = ipAddr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public short getNetworkPrefixLength() {
		return maskLength;
	}

	public short getMaskLength() {
		return maskLength;
	}

	public void setMaskLength(short maskLength) {
		this.maskLength = maskLength;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

}
