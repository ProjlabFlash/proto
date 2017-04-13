package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Railway extends MetaData{

	protected ArrayList<Railway> ThisNeighbour = new ArrayList<>();
	protected ArrayList<Railway> ThatNeighbour = new ArrayList<>();
	protected MovingObject OnMe;
	private Station station;
	
	/**
	 * Konstruktor. Beállítja a paraméterül kapott Railway-t a szomszédjának,
	 * és beállítja magát annak a szomszédjaként.
	 * @param previousRailway A kapott railway
	 */
	public Railway(Railway previousRailway) {
		
		if (previousRailway != null)
			ThisNeighbour.add(previousRailway);
	}
	
	/**
	 * Visszaadja azt a sínt, amire a mozdony továbbléphet
	 * a paraméterül kapott elõzõ sín alapján.
	 * @param previousRailway
	 * @return A sín, amire a mozdony következõnek léphet.
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
	 * A paraméter szerint beállítja, hogy ki melyik vonatelem van éppen a sínen.
	 * @param OnMe A sínre beállítandó vonatelem.
	 */
	public void setOnMe(MovingObject OnMe) {
		
		if (OnMe != null && this.OnMe != null)
			OnMe.crash();
		this.OnMe = OnMe;
		
	}
	
	/**
	 * Getter az egyik szomszédra
	 * @return Az egyik szomszéd
	 */
	public ArrayList<Railway> getThisNeighbour() {
		
		
		return ThisNeighbour;
	}
	
	/**
	 * Getter a másik szomszédra
	 * @return A másik szomszéd
	 */
	public ArrayList<Railway> getThatNeighbour() {
		
		return ThatNeighbour;
	}
	
	/**
	 * Elhelyezi a szomszédjai közt a paraméterül kapott Railway-t.
	 * @param newNeighbour A paraméterül kapott Railway
	 */
	public void insertNeighbour(Railway newNeighbour) {
						
		
		ThatNeighbour.add(newNeighbour);
	}
	
	/**
	 * Beállítja a sínhez tartozó megállót.
	 * @param station A sínhez tartozó megálló.
	 */
	public void setStation(Station station) {
				
		this.station = station;
		
	}
}
