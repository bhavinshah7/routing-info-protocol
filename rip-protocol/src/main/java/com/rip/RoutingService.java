package com.rip;

public class RoutingService {
	
	private RoutingTable routingTable = new RoutingTable(); 
	private static RoutingService instance;
	
	private RoutingService() {
	}
		
	public static RoutingService getInstance() {
		if (instance == null) {
			synchronized (RoutingService.class) {
				if (instance == null) {
					instance = new RoutingService();
				}				
			}
		}
		return instance;
	}

	public RoutingTable getRoutingTable() {
		return routingTable;
	}
}
