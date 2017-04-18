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
	
	public Switch(Railway tbConnected) {
		super(tbConnected);
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
	
	/**Ellenorzi hogy ez a sin hozzakotheto-e a parametrben kapott sinhez
	 * 
	 * @param newNeighbour: Az uj sin amit hozza akarunk kotni
	 * @param where: A ket keresztezo sin kozul melyikre
	 * @return Hozzakotheto-e(true), vagy sem(false)
	 */
	public boolean checkInsertNeighbour(Railway newNeighbour, int where) {
		
		if (ThisNeighbour.contains(newNeighbour) || ThatNeighbour.contains(newNeighbour))
			return false;
		
		boolean result = true;
		
		if (where == 1) {
			if (ThisNeighbour.size() == 0);
			else result = false;
		}
		return result;
	}
	
	/**
	 * 
	 * @param newNeighbour: Az uj sin amit hozza akarunk kotni
	 * @param where: A valto melyik oldalara
	 */
	public void insertNeighbour(Railway newNeighbour, int where) {
		boolean result = true;
		switch (where) {
		case 1:
			if (ThisNeighbour.size() == 0) ThisNeighbour.add(newNeighbour);
			break;
		case 2:
			ThatNeighbour.add(newNeighbour);
			if (ThatNeighbour.size() == 1) CurrentStanding = newNeighbour;
			break;
		default:
		}
	}
	
	public Railway getCurrentStanding() { return CurrentStanding;}
}
