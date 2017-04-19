package main;

import java.util.ArrayList;

public class Tunnel extends MetaData {
	
	private BuildingSpot bs1, bs2;

	/**
	 * Konstruktor. Nem hoz letre mukodo alagutat.
	 */
	public Tunnel() {
		
	}
	
	/**
	 * Megepiti az alagutat a ket parameterul kapott epitesi terulet kozott.
	 * @param buildingSpot1 Egyik epitesi terulet
	 * @param buildingSpot2 Masik epitesi terulet
	 */
	public void build(BuildingSpot buildingSpot1, BuildingSpot buildingSpot2) {
				
		bs1 = buildingSpot1; bs2 = buildingSpot2;
		
		ArrayList<Railway> bs1thisNeighbour = buildingSpot1.getThisNeighbour();
		ArrayList<Railway> bs1thatNeighbour = buildingSpot1.getThatNeighbour();
		ArrayList<Railway> bs2thisNeighbour = buildingSpot2.getThisNeighbour();
		ArrayList<Railway> bs2thatNeighbour = buildingSpot2.getThatNeighbour();
		
		bs1.setNewNeighbours(bs1thisNeighbour, bs2thatNeighbour, bs2);
		bs2.setNewNeighbours(bs2thisNeighbour, bs1thatNeighbour, bs1);
		
	}
	
	/**
	 * Lebontja az alagutat a ket korobban parameterul kapott epitesi terulet kozott.
	 */
	public void destroy() {
				
		bs1.setNewNeighbours(null, null, null);
		bs2.setNewNeighbours(null, null, null);
	}
	
	public boolean isTunnel(Railway railway) {
		return railway != null && (railway == bs2 || railway == bs1);
	}
}
