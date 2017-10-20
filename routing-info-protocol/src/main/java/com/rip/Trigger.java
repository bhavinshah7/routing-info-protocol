package com.rip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.TimerTask;

public class Trigger extends TimerTask {

	private DatagramSocket socket;
	private List<Node> neighbors;
	private RoutingService rs = RoutingService.getInstance();
	
	
	public Trigger(DatagramSocket soc, List<Node> neighborList) throws SocketException {
		socket = new DatagramSocket();
		neighbors = neighborList;
	}

	@Override
	public void run() {
		// publish routing table here.
		for (Node n : neighbors) {
			sendObj(rs.getRoutingTable(n.getAddress()), n.getAddress(), n.getPort());				
		}		
	}

	private void sendObj(Object obj, InetAddress ip, int port) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			
			oos.writeObject(obj);
			byte[] data = baos.toByteArray();
			System.out.println("RT size: " + data.length + " bytes");
			
			DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
			socket.send(packet);
			
			System.out.println("RT published to [" + ip + " " + port + "] ");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
