package main;

public class Cart extends MovingObject {

	private Color color;
	private boolean Passengers;
	
	/**
<<<<<<< HEAD
	 * Konstruktor. A param�ter�l kapott �rt�keket �ll�tja be rendre:
	 * a s�nt ahol a vagon tart�zkodik, az el�z� tart�zkod�si hely�t a vagonnak,
	 * a vagont amit h�z, a sz�n�t, �s hogy vannak-e rajta utasok.
	 * @param railwaySegment S�n, ahol a vagon tart�zkodik
	 * @param previousRailwaySegment El�z� tart�zkod�si hely
	 * @param nextCart Vagont amit h�z
	 * @param color A vagon sz�ne
=======
	 * Konstruktor. A parameterul kapott ertekeket allitja be rendre:
	 * a sint ahol a vagon tartozkodik, az elozo tartozkodasi helyet a vagonnak,
	 * a vagont amit huz, a szinet, es hogy vannak-e rajta utasok.
	 * @param railwaySegment Sin, ahol a vagon tartozkodik
	 * @param previousRailwaySegment Elozo tartozkodasi hely
	 * @param nextCart Vagont amit huz
	 * @param color A vagon szine
>>>>>>> branch 'master' of https://github.com/ProjlabFlash/proto.git
	 * @param passengers Vannak-e rajta utasok
	 */
	public Cart(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart, Color color, boolean passengers) {
		super(railwaySegment, previousRailwaySegment, nextCart);
		this.color = color;
		this.Passengers = passengers;
	}
	
	/**
<<<<<<< HEAD
	 * Lesz�ll�tja az utasokat a kocsir�l.
	 * @param station Az �llom�s sz�ne, amin�l lesz�llnak.
=======
	 * Leszallitja az utasokat a kocsirol.
	 * @param station Az allomos szine, aminal leszallnak.
>>>>>>> branch 'master' of https://github.com/ProjlabFlash/proto.git
	 */
	private void leaveTheTrain(Station station) {
		
		Passengers = false;
		
	}
	
	/**
<<<<<<< HEAD
	 * Ellen�rzi, hogy a lesz�ll�si felt�telek teljes�lnek-e.
	 * @param station Az �llom�s, ahol a lesz�ll�s t�rt�nik
	 * @return A lesz�ll�si felt�telek megvannak-e
=======
	 * Ellenorzi, hogy a leszallasi feltetelek teljesulnek-e.
	 * @param station Az allomos, ahol a leszallas tortenik
	 * @return A leszallasi feltetelek megvannak-e
>>>>>>> branch 'master' of https://github.com/ProjlabFlash/proto.git
	 */
	public boolean colorCheck(Station station) {
		Color c = station.getColor();
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
			return true;
		}
		if(c.equals(this.color))
		{
			if(Passengers)
			{
				this.leaveTheTrain(station);
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
		
}
