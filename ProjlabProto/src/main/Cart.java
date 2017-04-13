package main;

public class Cart extends MovingObject {

	private Color color;
	private boolean Passengers;
	
	/**
	 * Konstruktor. A paraméterül kapott értékeket állítja be rendre:
	 * a sínt ahol a vagon tartózkodik, az elõzõ tartózkodási helyét a vagonnak,
	 * a vagont amit húz, a színét, és hogy vannak-e rajta utasok.
	 * @param railwaySegment Sín, ahol a vagon tartózkodik
	 * @param previousRailwaySegment Elõzõ tartózkodási hely
	 * @param nextCart Vagont amit húz
	 * @param color A vagon színe
	 * @param passengers Vannak-e rajta utasok
	 */
	public Cart(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart, Color color, boolean passengers) {
		super(railwaySegment, previousRailwaySegment, nextCart);
		this.color = color;
		this.Passengers = passengers;
	}
	
	/**
	 * Leszállítja az utasokat a kocsiról.
	 * @param station Az állomás színe, aminél leszállnak.
	 */
	private void leaveTheTrain(Station station) {
		
		Passengers = false;
		
	}
	
	/**
	 * Ellenõrzi, hogy a leszállási feltételek teljesülnek-e.
	 * @param station Az állomás, ahol a leszállás történik
	 * @return A leszállási feltételek megvannak-e
	 */
	public boolean colorCheck(Station station) {
				
		if(station.getColor().equals(color)) leaveTheTrain(station);
		if(Passengers == false && Pulls != null) {
			
			boolean result = Pulls.colorCheck(station);
			
			return result;
		}
		if(Passengers == false && Pulls == null) {
			
			return true;
		}
		
		return false;
	}
}
