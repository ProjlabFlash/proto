package main;

public class Cart extends MovingObject {

	private Color color;
	private boolean Passengers;
	
	/**
	 * Konstruktor. A param�ter�l kapott �rt�keket �ll�tja be rendre:
	 * a s�nt ahol a vagon tart�zkodik, az el�z� tart�zkod�si hely�t a vagonnak,
	 * a vagont amit h�z, a sz�n�t, �s hogy vannak-e rajta utasok.
	 * @param railwaySegment S�n, ahol a vagon tart�zkodik
	 * @param previousRailwaySegment El�z� tart�zkod�si hely
	 * @param nextCart Vagont amit h�z
	 * @param color A vagon sz�ne
	 * @param passengers Vannak-e rajta utasok
	 */
	public Cart(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart, Color color, boolean passengers) {
		super(railwaySegment, previousRailwaySegment, nextCart);
		this.color = color;
		this.Passengers = passengers;
	}
	
	/**
	 * Lesz�ll�tja az utasokat a kocsir�l.
	 * @param station Az �llom�s sz�ne, amin�l lesz�llnak.
	 */
	private void leaveTheTrain(Station station) {
		
		Passengers = false;
		
	}
	
	/**
	 * Ellen�rzi, hogy a lesz�ll�si felt�telek teljes�lnek-e.
	 * @param station Az �llom�s, ahol a lesz�ll�s t�rt�nik
	 * @return A lesz�ll�si felt�telek megvannak-e
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
