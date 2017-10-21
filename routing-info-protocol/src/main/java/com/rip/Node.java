package com.rip;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * class Node
 *
 * @version 1.0
 * @author Bhavin Shah (bns8487)
 */
public class Node {

	private InetAddress address;
	private short maskLength = 0;
	private double cost = 1.0;
	private int port = 10002;

	/**
	 * Constructor
	 * 
	 * @param ipAddr
	 * @param nPort
	 */
	public Node(InetAddress ipAddr, int nPort) {
		address = ipAddr;
		port = nPort;
	}

	/**
	 * Constructor
	 *
	 * @param ipAddr
	 * @param NtwkPrefixLen
	 * @param linkCost
	 * @param portNo
	 */
	public Node(InetAddress ipAddr, short NtwkPrefixLen, double linkCost, int portNo) {
		address = ipAddr;
		cost = linkCost;
		port = portNo;
		maskLength = NtwkPrefixLen;
	}

	/**
	 * This method returns subnet mask on based on the prefix length
	 *
	 * @return
	 */
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

	/**
	 * This method returns the network prefix address of after applying subnet
	 * mask to the given ip address
	 *
	 * @return
	 * @throws UnknownHostException
	 */
	public InetAddress getNetworkAddr() throws UnknownHostException {
		byte[] ipBytes = address.getAddress();
		byte[] subnetBytes = getSubnet().getAddress();

		byte[] networkBytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			networkBytes[i] = (byte) (ipBytes[i] & subnetBytes[i]);
		}
		return InetAddress.getByAddress(networkBytes);
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
