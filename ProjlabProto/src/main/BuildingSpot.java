package main;

import java.util.ArrayList;

public class BuildingSpot extends Railway {
	
	private Railway OldThisNeighbour;
	private Railway OldThatNeighbour;
	
	/**
	 * Konstruktor. Be�ll�tja a param�ter�l kapott Railway-t a szomsz�dj�nak,
	 * �s be�ll�tja mag�t annak a szomsz�djak�nt.
	 * @param previousRailway A kapott railway
	 */
	public BuildingSpot(Railway previousRailway) {
		super(previousRailway);
	}
	
	/**
	 * A param�ter�l kapott Railway-eket be�ll�tja a saj�t szomsz�dj�nak.
	 * @param thisNewNeighbour Egyik szomsz�d
	 * @param thatNewNeighbour M�sik szomsz�d
	 */
	public void setNewNeighbours(ArrayList<Railway> thisNewNeighbour, ArrayList<Railway> thatNewNeighbour) {
		//logger enter
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
		
		//logger exit
	}
}
