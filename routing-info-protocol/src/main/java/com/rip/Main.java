package com.rip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Main {

	public static int port = 10002;
	public static RoutingService service = RoutingService.getInstance();
	
	
	public static void main(String[] args) {
		

		try {
			
			if (args.length < 1) {
				System.out.println("USAGE: java com.rip.Main <PROPERTIES-FILE>");
				System.exit(1);
			}
						
			if (args.length > 1) {
				port = Integer.parseInt(args[1]);
			}			
			
			//Start Server
			RIPServer server = new RIPServer(port);
			Thread th = new Thread(server);
			th.start();
			
			//Read Links File
			File f = new File(args[0]);
			if (!f.exists()) {
				System.out.println("Links File not found!");
				System.exit(1);
			}
			
			List<Node> links = parseNeighbors(f);
			
			//Schedule Trigger
			Trigger timerTask = new Trigger(new DatagramSocket(), links);
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(timerTask, 0, 10000);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static List<Node> parseNeighbors(File f) throws IOException {
		List<Node> links = new ArrayList<>();		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = br.readLine();
	    while (line != null) {
	    	if (!line.startsWith("#")) {
	    		
	    		String[] link = line.split(" ");	    		
	    		
	    		String[] parts = link[0].split("/");
	    		InetAddress address = InetAddress.getByName(parts[0]);
	    		Node n = new Node(address, port);	    		
	    		
	    		if (parts.length > 1) {
	    			n.setMaskLength(Short.parseShort(parts[1]));
	    		}
	    		if (link.length > 1) {
	    			n.setCost(Double.parseDouble(link[1]));
	    		}
	    		if (link.length > 2) {
	    			n.setPort(Integer.parseInt(link[2]));
	    		}	    			    	    			    		
	    		links.add(n);
	    		service.addNeighbor(address, n.getSubnet(), n.getCost());
	    	}	    	
	        line = br.readLine();
	    }
	    br.close();
		return links; 
	}
}
