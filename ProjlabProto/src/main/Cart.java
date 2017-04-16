package main;

public class Cart extends MovingObject {

	private Color color;
	private boolean Passengers;
	
	/**
	 * Konstruktor. A parameterul kapott ertekeket allitja be rendre:
	 * a sint ahol a vagon tartozkodik, az elozo tartozkodasi helyet a vagonnak,
	 * a vagont amit huz, a szinet, es hogy vannak-e rajta utasok.
	 * @param railwaySegment Sin, ahol a vagon tartozkodik
	 * @param previousRailwaySegment Elozo tartozkodasi hely
	 * @param nextCart Vagont amit huz
	 * @param color A vagon szine
	 * @param passengers Vannak-e rajta utasok
	 */
	public Cart(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart, Color color, boolean passengers) {
		super(railwaySegment, previousRailwaySegment, nextCart);
		this.color = color;
		this.Passengers = passengers;
	}
	
	/**
	 * Leszallitja az utasokat a kocsirol.
	 * @param station Az allomos szine, aminal leszallnak.
	 */
	private void leaveTheTrain(Station station) {
		
		Passengers = false;
		
	}
	
	/**
	 * Ellenorzi, hogy a leszallasi feltetelek teljesulnek-e.
	 * @param station Az allomos, ahol a leszallas tortenik
	 * @return A leszallasi feltetelek megvannak-e
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
