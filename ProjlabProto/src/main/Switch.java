package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Switch extends Railway {

	private Railway CurrentStanding;
	
	/**
	 * Konstruktor. A parameterul kapott kimenetet allitja be a valtonak.
	 * Null-eseten a lehetosegek kozul az elsot allitja be.
	 * @param defaultStanding A valto kezdeti allasa.
	 * @param previousRailway Elozo sin. A konstrualashoz kell.
	 */
	public Switch(Railway defaultStanding, Railway previousRailway) {
		super(previousRailway);
		CurrentStanding = defaultStanding;
		ThisNeighbour.add(previousRailway);
	}
	
	/**
	 * Valt a parameterul kapott sinre.
	 * @param nextStanding A kapott sin.
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
