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

	public static void main(String[] args) {

		try {
			
			if (args.length < 2) {
				System.out.println("USAGE: java com.rip.Main <PORT> <PROPERTIES-FILE>");
				System.exit(1);
			}
			
			//Start Server
			int port = Integer.parseInt(args[0]);
			RIPServer server = new RIPServer(port);
			Thread th = new Thread(server);
			th.start();
			
			//Read Links File
			File f = new File(args[1]);
			if (!f.exists()) {
				System.out.println("Links File not found!");
				System.exit(1);
			}
			
			List<Node> links = parseNeighbors(f);
			
			//Schedule Trigger
			Trigger timerTask = new Trigger(new DatagramSocket(), links);
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(timerTask, 0, 1000);
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
	    		String[] link = line.split(":");
	    		Node n = new Node(InetAddress.getByName(link[0]), Integer.parseInt(link[1]));
	    		links.add(n);
	    	}	    	
	        line = br.readLine();
	    }
	    br.close();
		return links; 
	}

}
