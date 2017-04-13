package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Switch extends Railway {

	private Railway CurrentStanding;
	
	/**
	 * Konstruktor. A paraméterül kapott kimenetet állítja be a váltónak.
	 * Null-esetén a lehetõség közül az elsõt állítja be.
	 * @param defaultStanding A váltó kezdeti állása.
	 * @param previousRailway Elõzõ sín. A konstruáláshoz kell.
	 */
	public Switch(Railway defaultStanding, Railway previousRailway) {
		super(previousRailway);
		CurrentStanding = defaultStanding;
		ThisNeighbour.add(previousRailway);
	}
	
	/**
	 * Vált a paraméterül kapott sínre.
	 * @param nextStanding A kapott sín.
	 */
	public void switchTo(Railway nextStanding) {
				
		if (ThisNeighbour.contains(nextStanding) || ThatNeighbour.contains(nextStanding))
			CurrentStanding = nextStanding;
	}
	
	public Railway next(Railway previousRailway) {
		
		Railway result = null;
		
		if (ThatNeighbour.contains(previousRailway))
			result = ThisNeighbour.get(0);
		else if (ThisNeighbour.contains(previousRailway))
			result = CurrentStanding;

		return result;
	}
}
