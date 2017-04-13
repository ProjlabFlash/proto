package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Switch extends Railway {

	private Railway CurrentStanding;
	
	/**
	 * Konstruktor. A param�ter�l kapott kimenetet �ll�tja be a v�lt�nak.
	 * Null-eset�n a lehet�s�g k�z�l az els�t �ll�tja be.
	 * @param defaultStanding A v�lt� kezdeti �ll�sa.
	 * @param previousRailway El�z� s�n. A konstru�l�shoz kell.
	 */
	public Switch(Railway defaultStanding, Railway previousRailway) {
		super(previousRailway);
		CurrentStanding = defaultStanding;
		ThisNeighbour.add(previousRailway);
	}
	
	/**
	 * V�lt a param�ter�l kapott s�nre.
	 * @param nextStanding A kapott s�n.
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
