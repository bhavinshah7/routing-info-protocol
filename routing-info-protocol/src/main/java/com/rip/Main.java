package com.rip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * class Main
 *
 * @version 1.0
 * @author Bhavin Shah (bns8487)
 */
public class Main {

	public static int port = 10002;
	public static final long timePeriod = 1000L;
	public static RoutingService service = RoutingService.getInstance();

	public static void main(String[] args) {

		try {

			if (args.length < 1) {
				System.out.println("USAGE: java com.rip.Main <PROPERTIES-FILE>");
				System.exit(1);
			}

			// Exit if properties file not found
			File f = new File(args[0]);
			if (!f.exists()) {
				System.out.println("Links File not found!");
				System.exit(1);
			}

			if (args.length > 1) {
				port = Integer.parseInt(args[1]);
			}

			// Start RIP server
			RIPServer server = new RIPServer(port);
			Thread th = new Thread(server);
			th.start();

			// Store Neighbors
			List<Node> links = parseNeighbors(f);

			// Store address from network interfaces
			// updateNtwkInterfaces();

			// Schedule trigger updates
			Trigger timerTask = new Trigger(new DatagramSocket(), links);
			Timer trigTimer = new Timer(true);
			trigTimer.scheduleAtFixedRate(timerTask, 0L, 1 * timePeriod);

			// Schedule failure detection
			FailureDetector fdTask = new FailureDetector(7 * timePeriod, links);
			Timer fdTimer = new Timer(true);
			fdTimer.scheduleAtFixedRate(fdTask, 0L, 3 * timePeriod);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void updateNtwkInterfaces() { try {
	 * System.out.println("Network Interfaces: "); Enumeration<NetworkInterface>
	 * n = NetworkInterface.getNetworkInterfaces(); while (n.hasMoreElements())
	 * { NetworkInterface e = n.nextElement(); Enumeration<InetAddress> a =
	 * e.getInetAddresses();
	 *
	 * for (InterfaceAddress address : e.getInterfaceAddresses()) { Node self =
	 * new Node(address.getAddress(), address.getNetworkPrefixLength(), 0,
	 * port); service.addNtwkInterface(self.getNetworkAddr());
	 * System.out.println("  " + address.getAddress() + "/" +
	 * address.getNetworkPrefixLength()); } } } catch (SocketException e1) {
	 * e1.printStackTrace(); } catch (UnknownHostException e1) {
	 * e1.printStackTrace(); }
	 *
	 * }
	 */

	/**
	 * This method parses information from properties file and extracts info
	 * regarding neighbors and link costs.
	 *
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static List<Node> parseNeighbors(File f) throws IOException {
		List<Node> links = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = br.readLine();
		while (line != null) {
			if (!line.startsWith("#") && !line.trim().equals("")) {

				String[] link = line.split(",");

				String[] parts = link[0].split("/");

				InetAddress address = InetAddress.getByName(parts[0]);
				Node n = new Node(address, port);
				if (parts.length > 1) {
					n.setMaskLength(Short.parseShort(parts[1]));
				}

				if (link.length > 1 && !"".equals(link[1])) {
					n.setCost(Double.parseDouble(link[1]));
				}

				if (link.length > 2 && !"".equals(link[2])) {
					n.setPort(Integer.parseInt(link[2]));
				}

				if (n.getCost() == 0) {
					service.addNtwkInterface(n.getNetworkAddr());
				} else {
					links.add(n);
					service.addNeighbor(address, n.getSubnet(), n.getCost());
				}
			}
			line = br.readLine();
		}
		br.close();
		return links;
	}
}
