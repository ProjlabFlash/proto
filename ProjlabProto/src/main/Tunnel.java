package main;

import java.util.ArrayList;

public class Tunnel extends MetaData {
	
	private BuildingSpot bs1, bs2;

	/**
	 * Konstruktor. Nem hoz l�tre m�k�d� alagutat.
	 */
	public Tunnel() {
		
	}
	
	/**
	 * Meg�p�ti azalagutat a k�t param�ter�l kapott �p�t�si ter�let k�z�tt.
	 * @param buildingSpot1 Egyik �p�t�si ter�let
	 * @param buildingSpot2 M�sik �p�t�si ter�let
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
	 * Lebontja az alagutat a k�t kor�bban param�ter�l kapott �p�t�si ter�let k�z�tt.
	 */
	public void destroy() {
				
		bs1.setNewNeighbours(null, null);
		bs2.setNewNeighbours(null, null);
	}
}
