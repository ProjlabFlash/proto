package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Railway extends MetaData{

	protected ArrayList<Railway> ThisNeighbour = new ArrayList<>();
	protected ArrayList<Railway> ThatNeighbour = new ArrayList<>();
	protected MovingObject OnMe;
	private Station station;
	
	/**
	 * Konstruktor. Be�ll�tja a param�ter�l kapott Railway-t a szomsz�dj�nak,
	 * �s be�ll�tja mag�t annak a szomsz�djak�nt.
	 * @param previousRailway A kapott railway
	 */
	public Railway(Railway previousRailway) {
		
		if (previousRailway != null)
			ThisNeighbour.add(previousRailway);
	}
	
	/**
	 * Visszaadja azt a s�nt, amire a mozdony tov�bbl�phet
	 * a param�ter�l kapott el�z� s�n alapj�n.
	 * @param previousRailway
	 * @return A s�n, amire a mozdony k�vetkez�nek l�phet.
	 */
	public Railway next(Railway previousRailway) {
		
		if (previousRailway == null) {
			
			if (ThisNeighbour.size() != 0)
				{
					return ThisNeighbour.get(0);
				}
			if (ThatNeighbour.size() != 0)
				{
					return ThatNeighbour.get(0);
				}
			return null;
		}
		
		if (ThisNeighbour.contains(previousRailway)) {
			
			if (ThatNeighbour.size() == 0)
				{
					return null;
				}
			return ThatNeighbour.get(0);
		}
		
		if (ThatNeighbour.contains(previousRailway)) {
			
			if (ThisNeighbour.size() == 0)
				{
					return null;
				}
			return ThisNeighbour.get(0);
		}
		
		return null;
	}
	
	/**
	 * A param�ter szerint be�ll�tja, hogy ki melyik vonatelem van �ppen a s�nen.
	 * @param OnMe A s�nre be�ll�tand� vonatelem.
	 */
	public void setOnMe(MovingObject OnMe) {
		
		if (OnMe != null && this.OnMe != null)
			OnMe.crash();
		this.OnMe = OnMe;
		
	}
	
	/**
	 * Getter az egyik szomsz�dra
	 * @return Az egyik szomsz�d
	 */
	public ArrayList<Railway> getThisNeighbour() {
		
		
		return ThisNeighbour;
	}
	
	/**
	 * Getter a m�sik szomsz�dra
	 * @return A m�sik szomsz�d
	 */
	public ArrayList<Railway> getThatNeighbour() {
		
		return ThatNeighbour;
	}
	
	/**
	 * Elhelyezi a szomsz�djai k�zt a param�ter�l kapott Railway-t.
	 * @param newNeighbour A param�ter�l kapott Railway
	 */
	public void insertNeighbour(Railway newNeighbour) {
						
		
		ThatNeighbour.add(newNeighbour);
	}
	
	/**
	 * Be�ll�tja a s�nhez tartoz� meg�ll�t.
	 * @param station A s�nhez tartoz� meg�ll�.
	 */
	public void setStation(Station station) {
				
		this.station = station;
		
	}
}
