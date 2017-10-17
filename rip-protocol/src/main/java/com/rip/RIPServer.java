package com.rip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RIPServer implements Runnable {

	private DatagramSocket ds;
	private int port;
	private byte[] buffer = new byte[1024];

	public RIPServer(int udpPort) throws SocketException {
		port = udpPort;
		ds = new DatagramSocket(port);
	}

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
