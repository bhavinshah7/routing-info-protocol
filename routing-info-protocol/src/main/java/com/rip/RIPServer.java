package com.rip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * class RIPServer
 *
 * @version 1.0
 * @author Bhavin Shah (bns8487)
 */
public class RIPServer implements Runnable {

	private DatagramSocket ds;
	private int port;
	private byte[] buffer = new byte[2048];

	/**
	 * Constructor
	 *
	 * @param udpPort
	 * @throws SocketException
	 */
	public RIPServer(int udpPort) throws SocketException {
		port = udpPort;
		ds = new DatagramSocket(port);
	}

	/**
	 * This method listens on given port for incoming routing table information
	 * from neighbors.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				InetAddress nbr = dp.getAddress();
				RoutingTable nbrTable = recvObj();
				if (nbrTable != null) {
					Updater updater = new Updater(nbrTable, nbr);
					Thread th = new Thread(updater);
					th.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method converts the packet received into Routing Table object.
	 * 
	 * @return
	 */
	public RoutingTable recvObj() {

		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (RoutingTable) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
