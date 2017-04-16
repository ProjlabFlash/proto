package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Railway extends MetaData{

	protected ArrayList<Railway> ThisNeighbour = new ArrayList<>();
	protected ArrayList<Railway> ThatNeighbour = new ArrayList<>();
	protected MovingObject OnMe;
	private Station station;
	
	/**
	 * Konstruktor. Beallitja a parameterul kapott Railway-t a szomszedjanak,
	 * es beallitja magat annak a szomszedjakent.
	 * @param previousRailway A kapott railway
	 */
	public Railway(Railway previousRailway) {
		
		if (previousRailway != null)
			ThisNeighbour.add(previousRailway);
	}
	
	/**
	 * Visszaadja azt a sint, amire a mozdony tovabblephet
	 * a parameterul kapott elozo sin alapjan.
	 * @param previousRailway
	 * @return A sin, amire a mozdony kovetkezonek lephet.
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
	 * A parameter szerint beallitja, hogy melyik vonatelem van eppen a sinen.
	 * @param OnMe A sinre beallitando vonatelem.
	 */
	public void setOnMe(MovingObject OnMe) {
		
		if (OnMe != null && this.OnMe != null)
			OnMe.crash();
		this.OnMe = OnMe;
		
	}
	
	/**
	 * Getter az egyik szomszedra
	 * @return Az egyik szomszed
	 */
	public ArrayList<Railway> getThisNeighbour() {
		
		
		return ThisNeighbour;
	}
	
	/**
	 * Getter a masik szomszedra
	 * @return A masik szomszed
	 */
	public ArrayList<Railway> getThatNeighbour() {
		
		return ThatNeighbour;
	}
	
	/**
	 * Elhelyezi a szomszedjai kozt a parameterul kapott Railway-t.
	 * @param newNeighbour A parameterul kapott Railway
	 */
	public void insertNeighbour(Railway newNeighbour) {
						
		
		ThatNeighbour.add(newNeighbour);
	}
	
	/**
	 * Beallitja a sinhez tartozo megallot.
	 * @param station A sinhez tartozo megallo.
	 */
	public void setStation(Station station) {
				
		this.station = station;
		
	}
}
