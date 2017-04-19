package main;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Railway extends MetaData{

	protected ArrayList<Railway> ThisNeighbour = new ArrayList<>();
	protected ArrayList<Railway> ThatNeighbour = new ArrayList<>();
	protected MovingObject OnMe;
	protected Station station;
	
	/**
	 * Konstruktor. Beallitja a parameterul kapott Railway-t a szomszedjanak,
	 * es beallitja magat annak a szomszedjakent.
	 * @param previousRailway A kapott railway
	 */
	public Railway(Railway previousRailway) {
		
		if (previousRailway != null) {
			ThisNeighbour.add(previousRailway);
			previousRailway.insertNeighbour(this);
		}
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
		if (station != null && OnMe != null)
			OnMe.ArrivedAtStation(station);
		
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
		if (ThisNeighbour.size() != 0)ThatNeighbour.add(newNeighbour);
		else ThisNeighbour.add(newNeighbour);
	}
	
	/**Helyfoglalo a valto es keresztezodesnek ugyanilyen funkcioju fuggvenyere
	 * 
	 * @param newNeighbour: A parameterul kapott Railway
	 * @param where: A beszurassal kapcsolatos informacio(melyik oldalara szurja a valtonak/keresztezodesnek)
	 */
	public void insertNeighbour(Railway newNeighbour, int where) {
		this.insertNeighbour(newNeighbour);
	}
	
	/**Ellenorzi hogy a ket sin osszekotheto-e. A ket parameter megfeleltetheto az insertNeighbour fuggveny parametereivel.
	 * 
	 * @param newNeighbour: A parameterul kapott Railway
	 * @param where: A beszurassal kapcsolatos informacio(melyik oldalara szurja a valtonak/keresztezodesnek)
	 * @return
	 */
	public boolean checkInsertNeighbour(Railway newNeighbour, int where) {
		boolean result = true;
		if (ThisNeighbour.size() != 0 && ThatNeighbour.size() != 0) result = false;
		return result;
	}
	/**
	 * Beallitja a sinhez tartozo megallot.
	 * @param station A sinhez tartozo megallo.
	 */
	public void setStation(Station station) {
				
		this.station = station;
		
	}
	
	public List<Railway> getNeighbours () {
		List<Railway> result = new ArrayList<Railway>();
		result.addAll(ThisNeighbour);
		result.addAll(ThatNeighbour);
		return result;
	}
	
	public boolean deleteNeighbour (Railway tbDeleted) {
		boolean result = ThisNeighbour.remove(tbDeleted);
		if (result == false) ThatNeighbour.remove(tbDeleted);
		return result;
	}
	
}