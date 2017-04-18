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
	public void leaveTheTrain(Station station) {
		this.Passengers = false;	
	}

	/**
	 * Vissyaadja, hogy vannak-e utasok a vagonban
	 * @return Vannak-e utasok
	 */
	public boolean getPassengers() {
		return Passengers;
	}
	
	/**
	 * Felszallitja az utasokat a vagonra.
	 */
	public void setPassengers() {
		this.Passengers = true;
	}
	
	/**
	 * Visszaadja a kocsi szinet.
	 * @return A kocsi szine
	 */
	public Color getColor(){
		return this.color;
	}
	
	/**
	 * Ellenorzi, hogy a leszallasi feltetelek teljesulnek-e.
	 * @param station Az allomos, ahol a leszallas tortenik
	 * @return A leszallasi feltetelek megvannak-e
	 */
	public boolean colorCheck(Station station) {
		Color c = station.getColor(this);
		if(c.equals(Color.HORROR))
		{
			if(Pulls != null) 
			{
				Pulls.colorCheck(station);
			}
			return false;
		}
		if(c.equals(Color.FELSZALLTAK))
		{
			Application.sendMessage("Az mcn kocsira felszalltak az utasok!", this);
			return true;
		}
		if(c.equals(this.color))
		{
			if(Passengers)
			{
				this.leaveTheTrain(station);
				Application.sendMessage("Az mcn kocsibol leszalltak az utasok!", this);
				if(Pulls != null) 
				{
					Pulls.colorCheck(station);
				}
				return true;
			}
			else
			{
				if(Pulls != null) 
				{
					Pulls.colorCheck(station);
				}
				return false;
			}
		}
		return false;
	}
	@Override
	public void ArrivedAtStation(Station station) 
	{	
		return;
	}
}
