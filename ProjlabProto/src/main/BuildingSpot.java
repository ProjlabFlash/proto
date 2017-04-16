package main;

import java.util.ArrayList;

public class BuildingSpot extends Railway {
	
	private Railway OldThisNeighbour;
	private Railway OldThatNeighbour;
	
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
	public void setNewNeighbours(ArrayList<Railway> thisNewNeighbour, ArrayList<Railway> thatNewNeighbour) {
		ArrayList<Object> paramlist=new ArrayList<Object>();	
		paramlist.add(thisNewNeighbour);
		paramlist.add(thatNewNeighbour);
		
		if (thisNewNeighbour != null) {

			if (ThisNeighbour.size() != 0)
				OldThisNeighbour = ThisNeighbour.get(0);
			else if (ThatNeighbour.size() > 1)
				OldThisNeighbour = ThatNeighbour.get(1);
			ThisNeighbour = thisNewNeighbour;
		} else {
			ThisNeighbour = new ArrayList<Railway>();
			ThisNeighbour.add(OldThisNeighbour);
		}
		
		if (thisNewNeighbour != null) {
			
			OldThatNeighbour = ThatNeighbour.get(0);
			this.ThatNeighbour = thatNewNeighbour;
		} else {
			
			ThatNeighbour = new ArrayList<Railway>();
			ThisNeighbour.add(OldThisNeighbour);
		}
	}
}
