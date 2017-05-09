package main;

import java.util.ArrayList;

public class BuildingSpot extends Railway {
	
	private ArrayList<Railway> OldThisNeighbour;
	private ArrayList<Railway> OldThatNeighbour;
	private BuildingSpot otherbs;
	
	/**
	 * Konstruktor. Beallitja a parameterul kapott Railway-t a szomszedjanak,
	 * es beallitja magat annak a szomszedjakent.
	 * @param previousRailway A kapott railway
	 */
	public BuildingSpot(Railway previousRailway) {
		super(previousRailway);
	}
	
	/**
	 * A parameterul kapott Railway-eket beellitja a sajat szomszedjanak.
	 * @param thisNewNeighbour Egyik szomszed
	 * @param thatNewNeighbour Masik szomszed
	 */
	public void setNewNeighbours(ArrayList<Railway> thisNewNeighbour, ArrayList<Railway> thatNewNeighbour, BuildingSpot otherbs) {
		
		if (thisNewNeighbour != null && thatNewNeighbour != null) {
			OldThisNeighbour = ThisNeighbour;
			ThisNeighbour = thisNewNeighbour;
		
			OldThatNeighbour = ThatNeighbour;
			ThatNeighbour = thatNewNeighbour;
			
			if (OldThatNeighbour.size() != 0) {
				OldThatNeighbour.get(0).deleteNeighbour(this);
				OldThatNeighbour.get(0).insertNeighbour(otherbs);
			}
		} else if (thisNewNeighbour == null && thatNewNeighbour == null){
			
			if (ThatNeighbour.size() != 0) {
				ThatNeighbour.get(0).deleteNeighbour(otherbs);
				OldThatNeighbour.get(0).insertNeighbour(this);
			}
			
			ThisNeighbour = OldThisNeighbour;
			ThatNeighbour = OldThatNeighbour;
		}
		this.otherbs = otherbs;
	}
	
	public void setOnMe(MovingObject OnMe) {
		super.setOnMe(OnMe);
		if (otherbs != null && otherbs.OnMe != null) OnMe.crash();
	}
}
