package main;

import java.util.ArrayList;

public class Tunnel extends MetaData {
	
	private BuildingSpot bs1, bs2;

	/**
	 * Konstruktor. Nem hoz létre mûködõ alagutat.
	 */
	public Tunnel() {
		
	}
	
	/**
	 * Megépíti azalagutat a két paraméterül kapott építési terület között.
	 * @param buildingSpot1 Egyik építési terület
	 * @param buildingSpot2 Másik építési terület
	 */
	public void build(BuildingSpot buildingSpot1, BuildingSpot buildingSpot2) {
				
		bs1 = buildingSpot1; bs2 = buildingSpot2;
		
		ArrayList<Railway> bs1thisNeighbour = buildingSpot1.getThisNeighbour();
		ArrayList<Railway> bs1thatNeighbour = buildingSpot1.getThatNeighbour();
		ArrayList<Railway> bs2thisNeighbour = buildingSpot2.getThisNeighbour();
		ArrayList<Railway> bs2thatNeighbour = buildingSpot2.getThatNeighbour();
		
		bs1.setNewNeighbours(bs2thisNeighbour, bs2thatNeighbour);
		bs2.setNewNeighbours(bs1thisNeighbour, bs1thatNeighbour);
		
	}
	
	/**
	 * Lebontja az alagutat a két korábban paraméterül kapott építési terület között.
	 */
	public void destroy() {
				
		bs1.setNewNeighbours(null, null);
		bs2.setNewNeighbours(null, null);
	}
}
